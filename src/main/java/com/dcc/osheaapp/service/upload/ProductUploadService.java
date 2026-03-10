package com.dcc.osheaapp.service.upload;

import com.amazonaws.services.securityhub.model.Product;
import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.service.exporter.excel.ExcelExporter;
import com.dcc.osheaapp.repository.IProductViewRepository;
import com.dcc.osheaapp.service.ProductService;
import com.dcc.osheaapp.vo.ProductCategoryVo;
import com.dcc.osheaapp.vo.ProductVo;
import com.dcc.osheaapp.vo.views.ProductView;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductUploadService {

  /**
   * Contains a mapping from Excel column names to product view fields. Also, setter has been given
   * for setting view fields with appropriate column values. Enum value contains relevant Product
   * View field value.
   */
  enum ColFieldEnum {
    Category_ID(e -> e.getCategoryId()),
    Category_Name(ProductView::getCategoryName),
    Subcategory_ID(e -> e.getSubcategoryId()),
    Subcategory_Name(ProductView::getSubcategoryName),
    Product_Name(ProductView::getProductName),
    Product_Code(ProductView::getProductCode),
    Description(ProductView::getProductDesc),
    MRP(ProductView::getProductMRP),
    PTR(ProductView::getProductPTR),
    PTD(ProductView::getProductPTD),
    Size(e -> e.getSize()),
    Unit(ProductView::getUnit),
    Packaging_Type(e -> e.getPackagingType()),
    Min_Batch_Qty(e -> e.getMinBatchQty()),
    Division_ID(e -> e.getDivisionId()),
    Division_Name(ProductView::getDivisionName);

    private final Function<ProductView, Object> product;

    ColFieldEnum(Function<ProductView, Object> product) {
      this.product = product;
    }

    public Object getValue(ProductView view) {
      return this.product.apply(view);
    }

    public void setValue(ProductView view, Object value) {
      String val = (String) value;
      switch (this) {
        case Category_ID:
          view.setCategoryId(Long.parseLong(val));
          break;
        case Category_Name:
          view.setCategoryName((String) value);
          break;
        case Subcategory_ID:
          view.setSubcategoryId(val == null || val.isEmpty() ? null : Long.parseLong(val));
          break;
        case Subcategory_Name:
          view.setSubcategoryName(val);
          break;
        case Product_Name:
          view.setProductName(val);
          break;
        case Product_Code:
          view.setProductCode(val);
          break;
        case Description:
          view.setProductDesc(val);
          break;
        case MRP:
          view.setProductMRP(val);
          break;
        case PTR:
          view.setProductPTR(val);
          break;
        case PTD:
          view.setProductPTD(val);
          break;
        case Size:
          view.setSize(Long.valueOf(val));
          break;
        case Unit:
          view.setUnit(val);
          break;
        case Packaging_Type:
          view.setPackagingType(Long.valueOf(val));
          break;
        case Min_Batch_Qty:
          view.setMinBatchQty(Long.valueOf(val));
          break;
        case Division_ID:
          view.setDivisionId(Long.valueOf(val));
          break;
        case Division_Name:
          view.setDivisionName(val);
          break;
      }
    }
  }

  private static final Logger LOGGER = LogManager.getLogger(ProductUploadService.class);

  String[] HEADER_COLS = {
    "Category ID",
    "Category Name",
    "Subcategory ID",
    "Subcategory Name",
    "Product Name",
    "Product Code",
    "Description",
    "MRP",
    "PTR",
    "PTD",
    "Size",
    "Unit",
    "Packaging Type",
    "Min Batch Qty",
    "Division ID",
    "Division Name"
  };
  @Autowired IProductViewRepository productViewRepository;

  @Autowired ProductService productService;

  public Object export(HttpServletResponse response, Long divisionId) {
    LOGGER.info("UploadService :: Entering export products method");
    String msgResponse = "Products Exported";

    List<ProductView> products = productViewRepository.findByIsActiveAndDivision(true, divisionId);
    BiFunction<String, ProductView, Object> getter = this::getFieldValue;
    ExcelExporter<ProductView> exporter = new ExcelExporter(Arrays.asList(HEADER_COLS), "products", getter);
    try {
    exporter.export(products, "products.xlsx", response.getOutputStream());

    } catch (IOException e) {
      throw new OjbException(e, ErrorCode.FILE_NOT_FOUND, new Object[] {});
    }
    return msgResponse;
  }

  /**
   * get the enum value from the header string. e.g., if header is "header name" the respective enum
   * value returned header_name.
   *
   * @param header
   * @return Optional<ColFieldEnum>
   */
  private Optional<ColFieldEnum> getEnumFromHeader(String header) {
    String headerEnumName = header.replace(" ", "_");
    return Arrays.stream(ColFieldEnum.values())
        .filter(e -> e.name().equals(headerEnumName))
        .findAny();
  }

  private Object getFieldValue(String header, ProductView view) {
    return getEnumFromHeader(header).map(e -> e.getValue(view)).orElse(null);
  }

  public void upload(MultipartFile excelInput) {
    XSSFWorkbook workbook = null;
    XSSFSheet workSheet = null;
    try {
      workbook = new XSSFWorkbook(excelInput.getInputStream());
    } catch (Exception e) {
      throw new OjbException(e, ErrorCode.INVALID_EXCEL_FILE, new Object[] {});
    }
    workSheet = workbook.getSheetAt(0);
    ExcelUploadService<ProductView> excelUploadService =
        new ExcelUploadService<>(workbook, workSheet, HEADER_COLS);
    for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i++) {
      Row row = workSheet.getRow(i);
      ProductView view = constructProduct(row, excelUploadService.getColMapInverted());
      persistProductTree(view);
    }
  }

  private ProductView constructProduct(Row row, Map<Integer, String> colMap) {
    DataFormatter formatter = new DataFormatter();
    ProductView view = new ProductView();
    colMap.forEach(
        (key, value) -> {
          Cell cell = row.getCell(key);
          String cellValue = formatter.formatCellValue(cell);
          getEnumFromHeader(value).ifPresent(e -> e.setValue(view, cellValue));
        });
    return view;
  }

  private void persistProductTree(ProductView view) {
    BiFunction<ProductCategoryVo, ProductCategoryVo, Boolean> updateCriteria =
        ProductUploadService::getCategoryDiff;
    ProductCategoryVo category =
        productService.saveOrUpdateCategoryByDiff(
            new ProductCategoryVo(
                view.getCategoryId() == null ? null : view.getCategoryId(),
                view.getCategoryName(),
                0L,
                true),
            updateCriteria);
    ProductCategoryVo subCategory =
        productService.saveOrUpdateCategoryByDiff(
            new ProductCategoryVo(
                view.getSubcategoryId() == null ? null : view.getSubcategoryId(),
                view.getSubcategoryName(),
                category.getParentId(),
                true),
            updateCriteria);
    view.setCategoryId(category.getId());
    view.setSubcategoryId(subCategory.getId());
    List<String> fieldNames =
        new ArrayList<>(
            List.of(
                new String[] {
                  "categoryId",
                  "productName",
                  "productMRP",
                  "productCode",
                  "productPTD",
                  "productPTR",
                  "minBatchQty",
                  "packagingType"
                }));
    BiFunction<ProductVo, ProductVo, Boolean> diffFunction =
        (vo, dto) -> getProductDiff(vo, dto, fieldNames);
    productService.saveByDiff(view.toEntity(), diffFunction);
  }

  private static Boolean getCategoryDiff(ProductCategoryVo vo, ProductCategoryVo dto) {
    return !vo.getCategoryName().equals(dto.getCategoryName())
        || !vo.getIsActive().equals(dto.getIsActive());
  }

  /**
   * Checks either two products are similar or not by getting the product from DB using product code
   * and iteratively checking each field!
   *
   * @param vo
   * @param dto
   * @param fields
   * @return boolean indicating if there's a difference exists or not
   */
  private Boolean getProductDiff(ProductVo vo, ProductVo dto, List<String> fields) {
    for (String fieldName : fields) {

      Field voField = null;
      Field dtoField = null;
      try {
        voField = vo.getClass().getDeclaredField(fieldName);
        dtoField = dto.getClass().getDeclaredField(fieldName);
        voField.setAccessible(true);
        dtoField.setAccessible(true);

      } catch (NoSuchFieldException e) {
        throw new OjbException(e, ErrorCode.GENERAL, new Object[] {});
      }

      Object voData = null;
      Object dtoData = null;
      try {
        voData = voField.get(vo);
        dtoData = dtoField.get(dto);
        if (!voData.equals(dtoData)) return true;
      } catch (IllegalAccessException e) {
        throw new IllegalArgumentException(e);
      }
    }
    return false;
  }
}
