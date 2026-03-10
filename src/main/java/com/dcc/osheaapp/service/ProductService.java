package com.dcc.osheaapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.common.service.storage.UploadStrategy;
import com.dcc.osheaapp.repository.IFormMediaMappingRepository;
import com.dcc.osheaapp.repository.IProductCategoryRepository;
import com.dcc.osheaapp.repository.IProductForSaleRepository;
import com.dcc.osheaapp.repository.IProductRepository;
import com.dcc.osheaapp.repository.IProductViewRepository;
import com.dcc.osheaapp.vo.*;
import com.dcc.osheaapp.vo.dto.ProductVideoLinksDto;
import com.dcc.osheaapp.vo.dto.UpdateProductDto;
import com.dcc.osheaapp.vo.views.NestedProductView;
import com.dcc.osheaapp.vo.views.ProductView;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

  private static final Logger LOGGER = LogManager.getLogger(ProductService.class);
  private final UserService userService;
  private final IProductCategoryRepository iProductCategoryRepository;
  private final IProductRepository iProductRepository;
  private final IFormMediaMappingRepository iFormMediaMappingRepository;
  private final IProductForSaleRepository iProductForSaleRepository;
  private final IProductViewRepository iProductViewRepository;

  private final ProductViewMapper mapper;

  @Autowired
  UploadService uploadService;

  @Autowired
  AmazonS3 client;

  @Value("${s3.bucket}")
  String bucketName;

  @Value("${file.productUploadFolder}")
  private String productUploadFolder;

  @Autowired
  public ProductService(IProductCategoryRepository iProductCategoryRepository, UserService userService,
      IProductRepository iProductRepository, IFormMediaMappingRepository iFormMediaMappingRepository,
      IProductForSaleRepository iProductForSaleRepository, IProductViewRepository iProductViewRepository,
      ProductViewMapper mapper) {
    this.userService = userService;
    this.iProductCategoryRepository = iProductCategoryRepository;
    this.iProductRepository = iProductRepository;
    this.iFormMediaMappingRepository = iFormMediaMappingRepository;
    this.iProductForSaleRepository = iProductForSaleRepository;
    this.iProductViewRepository = iProductViewRepository;
    this.mapper = mapper;
  }

  public ResponseEntity<ApiResponse> fetchCategory(HttpServletRequest request) {
    LOGGER.info("ProductService :: fetchCategory() called...");

    final String requestTokenHeader = request.getHeader("Authorization");

    String jwtToken = null;

    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);
    }

    try {
      UserCredVo userVo = userService.getUserDetails(request, jwtToken);
      if (null == userVo) {
        LOGGER.info("ProductService :: fetchLocations :: Invalid User...");
        return new ResponseEntity<>(new ApiResponse(401, "ERROR", "Invalid User", null),
            HttpStatus.UNAUTHORIZED);
      }
      List<ProductCategoryVo> dataList = iProductCategoryRepository.getDataByParentId(0L);
      List<CategoryTreeOPDto> root = new ArrayList<CategoryTreeOPDto>();
      for (ProductCategoryVo vo : dataList) {
        List<ProductCategoryVo> child = iProductCategoryRepository.getDataByParentId(vo.getId());
        List<ProductOPDto> subcategories = new ArrayList<ProductOPDto>();
        for (ProductCategoryVo ch : child) {
          ProductOPDto pro = new ProductOPDto();
          pro.setId(ch.getId());
          pro.setName(ch.getCategoryName());
          // List<ProductVo> child1 = iProductRepository.getDataByCategoryId(ch.getId());
          List<ProductView> child1 = iProductViewRepository.getDataByCategoryId(ch.getId());
          pro.setProducts(child1);
          subcategories.add(pro);
        }

        CategoryTreeOPDto pro = new CategoryTreeOPDto();
        pro.setId(vo.getId());
        pro.setName(vo.getCategoryName());
        pro.setSubcategories(subcategories);
        root.add(pro);
      }

      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", root), HttpStatus.OK);
    } catch (ExpiredJwtException exp) {
      LOGGER.info("QuoteService :: ExpiredJwtException :: Token expired..." + exp.getMessage());
      exp.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(401, "ERROR", "Token expired", null), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(500, "Internal Server Err", "Location fetched Failed.", null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<ApiResponse> save(ProductVo input) {
    LOGGER.info("ProductService :: save() called...");

    ProductVo savedData = iProductRepository.save(input);
    if (savedData != null) {
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
          HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "ProductView" });
    }
  }

  public ResponseEntity<ApiResponse> updateStatus(ProductInputVo input) {
    LOGGER.info("ProductService :: updateStatus() called...");
    int savedData = iProductRepository.updateStatus(input.getId(), input.getStatus());
    if (savedData != 0) {
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", null), HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "ProductView" });
    }
  }

  // public ResponseEntity<ApiResponse> searchByInput(ProductInputVo inputVo) {
  // LOGGER.info("ProductService::searchByInput::Entering...");
  // ApiResponse apiResponse = null;
  // List<ProductVo> getDataList = null;
  // String whereClause = " and is_active = true ";
  // String limitStr = "";
  // if (null == inputVo.getPage() && null == inputVo.getSize()) {
  // // do nothing
  // } else {
  // Integer size = (null != inputVo.getSize()) ? inputVo.getSize()
  // : Constants.DEFAULT_DATA_SIZE_PAGINATION;
  // Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ?
  // (inputVo.getPage() - 1)
  // * size : 0;
  // limitStr = " limit " + size + " offset " + page;
  // LOGGER.info("limitStr..." + limitStr);
  // }
  //
  // if (null != inputVo.getCategory()) {
  // whereClause += " and category_id = " + inputVo.getCategory();
  // }
  //
  // if (null != inputVo.getProductName() &&
  // !inputVo.getProductName().trim().isEmpty()) {
  // LOGGER.info("vo.getProductName() ------------ " + inputVo.getProductName());
  // whereClause += " and product_name like '%" + inputVo.getProductName().trim()
  // + "%' ";
  // }
  //
  // LOGGER.info("Whereclause..." + whereClause);
  // getDataList = iProductRepository.searchByInput(whereClause, "product",
  // limitStr);
  //
  // // To fetch total no of data which satisfy this where clause
  // int totalNo = iProductRepository.getTotalCountByInput(whereClause,
  // "product").intValue();
  // LOGGER.info("UserService::searchByInput::Exiting...");
  // if (getDataList != null) {
  // apiResponse = new ApiResponse(200, "success", "success", getDataList,
  // totalNo);
  // return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
  // } else {
  // throw new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Users"});
  // }
  // }

  public ResponseEntity<ApiResponse> searchByInput(ProductInputVo inputVo) {
    LOGGER.info("ProductService::searchByInput::Entering...");
    ApiResponse apiResponse = null;
    List<ProductView> getDataList = null;
    String whereClause = " ";
    String limitStr = "";
    if (null == inputVo.getPage() && null == inputVo.getSize()) {
      // do nothing
    } else {
      Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
      Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
      limitStr = " limit " + size + " offset " + page;
      LOGGER.info("limitStr..." + limitStr);
    }

    if (null != inputVo.getCategory()) {
      whereClause += " and (categoryId = " + inputVo.getCategory() + " or subcategoryId = "
          + inputVo.getCategory() + ") ";
    }

    if (null != inputVo.getDivisionId())
      whereClause += " and divisionId = " + inputVo.getDivisionId();

    if (null != inputVo.getProductName() && !inputVo.getProductName().trim().isEmpty()) {
      LOGGER.info("vo.getProductName() ------------ " + inputVo.getProductName());
      whereClause += " and productName like '%" + inputVo.getProductName().trim() + "%' ";
    }

    if (null != inputVo.getProductCode() && !inputVo.getProductCode().isEmpty())
      whereClause += " and productCode = '" + inputVo.getProductCode() + "'";

    LOGGER.info("Whereclause..." + whereClause);
    getDataList = iProductViewRepository.searchByInput(whereClause, "product_view", limitStr);

    // To fetch total no of data which satisfy this where clause
    int totalNo = iProductViewRepository.getTotalCountByInput(whereClause, "product_view").intValue();
    LOGGER.info("UserService::searchByInput::Exiting...");
    if (getDataList != null) {
      apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
      return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Users" });
    }
  }

  public ResponseEntity<ApiResponse> uploadProductFiles(Long transactionId, String tabName, MultipartFile[] files)
      throws IOException {
    LOGGER.info("ProductService :: uploadProductFiles() called...");
    ResponseEntity<ApiResponse> apiResponse = null;
    List<FormMediaMappingVo> mediaDataList = new ArrayList<>();
    int count = 0;
    for (MultipartFile file : files) {
      if (!file.isEmpty()) {
        count++;
        LOGGER.info("File Count --------------------------" + count);

        String UPLOAD_FOLDER = productUploadFolder;

        // byte[] bytes = file.getBytes();
        // Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
        // Files.write(path, bytes);
        Path path = Paths.get(UPLOAD_FOLDER);
        if (!Files.exists(path)) {
          Files.createDirectories(path);
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Files.copy(file.getInputStream(), path.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        String tempFileName = filename;
        Path filePath = Paths.get(productUploadFolder + tempFileName);
        String ext = FilenameUtils.getExtension(tempFileName);
        // Long transactionId = Long.parseLong("3"); //ID of respective ProductView
        String formType = "ProductView image";
        String tempFilePath = filePath.toString();

        FormMediaMappingVo savedMediaData = new FormMediaMappingVo();

        savedMediaData.setTransactionId(transactionId);
        savedMediaData.setFormType(formType);
        savedMediaData.setTabName(tabName);
        savedMediaData.setFilePath(tempFilePath);
        savedMediaData.setImageName(filename);
        savedMediaData.setCreatedOn(new Date());

        savedMediaData = iFormMediaMappingRepository.save(savedMediaData);
        if (savedMediaData != null) {
          LOGGER.info("ProductView Media Data saved in DB -----------" + savedMediaData);
          mediaDataList.add(savedMediaData);
        } else {
          LOGGER.info("ProductView Media Data NOT saved in DB -----------" + savedMediaData);
          throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "ProductMediaData" });
        }
      }
    }
    apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS",
        "Total " + count + " Media Data Saved Successfully.", mediaDataList, count), HttpStatus.OK);

    LOGGER.info("ProductService :: uploadProductFiles() :: Exiting...");
    return apiResponse;
  }

  public ResponseEntity<ApiResponse> saveCategory(ProductCategoryVo input) {
    LOGGER.info("ProductService :: saveCategory() called...");
    ProductCategoryVo savedData = iProductCategoryRepository.save(input);
    if (savedData != null) {
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
          HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "ProductCategory" });
    }
  }

  public ResponseEntity<ApiResponse> productForSale(ProductInputVo input) {
    LOGGER.info("ProductService :: fetchCategory() called...");

    if (null == input.getMonYr() || input.getMonYr().isEmpty()) {
      SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM");
      String monYr = obj.format(new Date());
      input.setMonYr(monYr);
      LOGGER.info(" productForSale ====== monYr ==== >> " + input.getMonYr());
    }
    try {
      List<ProductCategoryVo> dataList = iProductCategoryRepository.getDataByParentId(0L);
      List<CategoryTreeOPDto> root = new ArrayList<CategoryTreeOPDto>();
      for (ProductCategoryVo vo : dataList) {
        List<ProductCategoryVo> child = iProductCategoryRepository.getProductCategoryInOutletMonYr(vo.getId(),
            input.getOutletId(), input.getMonYr());
        List<ProductOPDto> subcategories = new ArrayList<ProductOPDto>();
        if (child.size() > 0) {
          for (ProductCategoryVo ch : child) {
            ProductOPDto pro = new ProductOPDto();
            pro.setId(ch.getId());
            pro.setName(ch.getCategoryName());
            List<ProductForSaleDto> child1 = iProductForSaleRepository.getProductForSale(ch.getId(),
                input.getOutletId(), input.getMonYr());
            pro.setProductsForSale(child1);
            subcategories.add(pro);
          }

          CategoryTreeOPDto pro = new CategoryTreeOPDto();
          pro.setId(vo.getId());
          pro.setName(vo.getCategoryName());
          pro.setSubcategories(subcategories);
          root.add(pro);
        }
      }

      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", root), HttpStatus.OK);
    } catch (ExpiredJwtException exp) {
      LOGGER.info("QuoteService :: ExpiredJwtException :: Token expired..." + exp.getMessage());
      exp.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(401, "ERROR", "Token expired", null), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(500, "Internal Server Err", "Location fetched Failed.", null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<ApiResponse> productForPurchaseReturn(ProductInputVo input) {
    LOGGER.info("ProductService :: productForPurchaseReturn() called...");

    try {
      List<ProductCategoryVo> dataList = iProductCategoryRepository.getDataByParentId(0L);
      List<CategoryTreeOPDto> root = new ArrayList<CategoryTreeOPDto>();
      for (ProductCategoryVo vo : dataList) {
        List<ProductCategoryVo> child = iProductCategoryRepository.getProductCategoryInOutlet(vo.getId(),
            input.getOutletId());
        List<ProductOPDto> subcategories = new ArrayList<ProductOPDto>();
        if (child.size() > 0) {
          for (ProductCategoryVo ch : child) {
            ProductOPDto pro = new ProductOPDto();
            pro.setId(ch.getId());
            pro.setName(ch.getCategoryName());
            List<ProductForSaleDto> child1 = iProductForSaleRepository
                .getProductForPurchaseReturn(ch.getId(), input.getOutletId(), input.getStockId());
            pro.setProductsForSale(child1);
            subcategories.add(pro);
          }

          CategoryTreeOPDto pro = new CategoryTreeOPDto();
          pro.setId(vo.getId());
          pro.setName(vo.getCategoryName());
          pro.setSubcategories(subcategories);
          root.add(pro);
        }
      }

      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", root), HttpStatus.OK);
    } catch (ExpiredJwtException exp) {
      LOGGER.info("QuoteService :: ExpiredJwtException :: Token expired..." + exp.getMessage());
      exp.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(401, "ERROR", "Token expired", null), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(500, "Internal Server Err", "Location fetched Failed.", null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<ApiResponse> productForSaleReturn(ProductInputVo input) {
    LOGGER.info("ProductService :: productForSaleReturn() called...");

    if (null == input.getMonYr() || input.getMonYr().isEmpty()) {
      SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM");
      String monYr = obj.format(new Date());
      input.setMonYr(monYr);
      LOGGER.info(" productForSale ====== monYr ==== >> " + input.getMonYr());
    }

    try {
      List<ProductCategoryVo> dataList = iProductCategoryRepository.getDataByParentId(0L);
      List<CategoryTreeOPDto> root = new ArrayList<CategoryTreeOPDto>();
      for (ProductCategoryVo vo : dataList) {
        List<ProductCategoryVo> child = iProductCategoryRepository.getProductCategoryInOutlet(vo.getId(),
            input.getOutletId());
        List<ProductOPDto> subcategories = new ArrayList<ProductOPDto>();
        if (child.size() > 0) {
          for (ProductCategoryVo ch : child) {
            ProductOPDto pro = new ProductOPDto();
            pro.setId(ch.getId());
            pro.setName(ch.getCategoryName());
            List<ProductForSaleDto> child1 = iProductForSaleRepository.getProductForSaleReturn(ch.getId(),
                input.getOutletId(), input.getMonYr()); // , input.getBaId()
            pro.setProductsForSale(child1);
            subcategories.add(pro);
          }

          CategoryTreeOPDto pro = new CategoryTreeOPDto();
          pro.setId(vo.getId());
          pro.setName(vo.getCategoryName());
          pro.setSubcategories(subcategories);
          root.add(pro);
        }
      }

      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", root), HttpStatus.OK);
    } catch (ExpiredJwtException exp) {
      LOGGER.info("QuoteService :: ExpiredJwtException :: Token expired..." + exp.getMessage());
      exp.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(401, "ERROR", "Token expired", null), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(new ApiResponse(500, "Internal Server Err", "Location fetched Failed.", null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ProductCategoryVo saveOrUpdateCategoryByDiff(ProductCategoryVo vo,
      BiFunction<ProductCategoryVo, ProductCategoryVo, Boolean> diff) {
    if (null == vo.getId())
      return iProductCategoryRepository.save(vo);
    Optional<ProductCategoryVo> existingCategory = iProductCategoryRepository.findById(vo.getId());

    return existingCategory.map(e -> {
      if (diff.apply(e, vo)) {
        vo.setId(e.getId());
        vo.setUpdatedOn(new Date());
        return iProductCategoryRepository.save(vo);
      }
      return e;
    }).orElseGet(() -> iProductCategoryRepository.save(vo));
  }

  public ProductVo saveByDiff(ProductVo vo, BiFunction<ProductVo, ProductVo, Boolean> diff) {
    String productCode = vo.getProductCode();
    Optional<ProductVo> existingProduct = iProductRepository.findByProductCode(productCode);

    return existingProduct.map(existing -> {
      if (diff.apply(vo, existing)) {
        vo.setId(null);
        vo.setUpdatedOn(new Date());
        vo.setIsActive(true);
        return iProductRepository.save(vo);
      }
      return existing;
    }).orElseGet(() -> iProductRepository.save(vo));
  }

  public ProductVo fetchById(Long id) {
    ProductVo data = iProductRepository.findById(id)
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Product" }));
    List<FormMediaMappingVo> media = iFormMediaMappingRepository.findByFormTypeAndTransactionId("product",
        data.getId());
    data.setMediaData(media);
    return data;
  }

  public NestedProductView fetchByIdAlt(Long id) {
    ProductVo data = iProductRepository.findById(id)
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Product" }));
    return mapper.toNestedView(data);
  }

  public List<FormMediaMappingVo> uploadProductMedia(Long transactionId, String tabName, MultipartFile[] files)
      throws IOException {
    return uploadService.uploadMedia("product", transactionId, tabName, files, UploadStrategy.S3, null);
  }

  public void uploadProductLinks(ProductVideoLinksDto dto) {
    dto.getLinks().parallelStream().forEach(e -> iFormMediaMappingRepository.save(new FormMediaMappingVo(null,
        dto.getTransactionId(), "product", dto.getTabName(), null, e, new Date())));
  }

  public ProductVo update(UpdateProductDto input){
    LOGGER.info("ProductService :: update() called...");
    ProductVo product = iProductRepository.findById(input.getId())
            .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Product" }));
    BeanUtils.copyProperties(input, product);
    product.setUpdatedOn(new Date());
    product.setUpdatedBy(1L);
    ProductVo updateProduct = iProductRepository.save(product);
    return iProductRepository.findById(updateProduct.getId()).orElse(new ProductVo());
  }
}
