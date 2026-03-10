package com.dcc.osheaapp.report.purchaseSale.service;

import com.dcc.osheaapp.common.service.exporter.excel.ExcelExporter;
import com.dcc.osheaapp.common.service.exporter.ExportManager;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * MD
 * NSM
 * ZSM
 * ASM/BDM
 * ASE/SO
 * BDE NAME
 * BA
 */
@Service
public class ExportService {

  private static final Logger LOGGER = LogManager.getLogger(ExportService.class);
  private final String[] HEADER_COLS_PURCHASE = {
    "Zone",
    "MD",
    "NSM",
    "ZSM",
    "ASM/BDM",
    "ASE/SO",
    "BDE",
    "BA Name",
    "BA Code",
    "Outlet ErpId",
    "Outlet Name",
          "Outlet Type",
    "Market Name",
    "City",
    "State",
    "Month",
    "Inv No",
    "Stock Inward Date",
    "Stock Inward Time",
    "Primary Category",
    "Secondary Category",
    "Product Erp Id",
    "Product",
    "Qty",
    "MRP",
    "Value",
          "Return Quantity",
          "Return Value",
          "Return Date"
  };

  private final String[] HEADER_COLS_SALE= {
          "Zone",
          "MD",
          "NSM",
          "ZSM",
          "ASM/BDM",
          "ASE/SO",
          "BDE",
          "BA Name",
          "BA Code",
          "Outlet ErpId",
          "Outlet Name",
          "Outlet Type",
          "Market Name",
          "City",
          "State",
          "Month",
          "Date",
          "Time",
          "Primary Category",
          "Secondary Category",
          "Product Erp Id",
          "Product",
          "Qty",
          "MRP",
          "Value",
          "Return Quantity",
          "Return Value",
          "Return Date"
  };
  public OutputStream exportPurchases(List<PurchaseRecordDto> records, OutputStream outputStream) {

    final long startTime = System.currentTimeMillis();
    BiFunction<String, PurchaseRecordDto, Object> getter = this::getFieldValue;
    ExportManager<PurchaseRecordDto> exportManager =
        new ExportManager<>(records, "purchase_records.xlsx", outputStream);
    ExcelExporter<PurchaseRecordDto> exporter =
        new ExcelExporter<>(List.of(HEADER_COLS_PURCHASE), "purchases", getter);
    exportManager.export(exporter);
    final long elapsedTimeMillis1 = System.currentTimeMillis() - startTime;
    LOGGER.info("Elapsed::Generation:: " + elapsedTimeMillis1);
    return outputStream;
  }

  public OutputStream exportSales(List<PurchaseRecordDto> records, OutputStream outputStream) {

    final long startTime = System.currentTimeMillis();
    BiFunction<String, PurchaseRecordDto, Object> getter = this::getFieldValue;
    ExportManager<PurchaseRecordDto> exportManager =
            new ExportManager<>(records, "sales_record.xlsx", outputStream);
    ExcelExporter<PurchaseRecordDto> exporter =
            new ExcelExporter<>(List.of(HEADER_COLS_SALE), "sales", getter);
    exportManager.export(exporter);
    final long elapsedTimeMillis1 = System.currentTimeMillis() - startTime;
    LOGGER.info("Elapsed::Generation:: " + elapsedTimeMillis1);
    return outputStream;
  }
  private Object getFieldValue(String header, PurchaseRecordDto view) {
    String headerEnumName = header.replace(" ", "_").replace("/", "_");
    return Arrays.stream(ColFieldEnum.values())
        .filter(e1 -> e1.name().equals(headerEnumName))
        .findAny()
        .map(e ->  e.getValue(view))
        .orElse(null);
  }

  enum ColFieldEnum {
    Zone(PurchaseRecordDto::getZone),
    MD(PurchaseRecordDto::getMd),
    NSM(PurchaseRecordDto::getNsm),
    ZSM(PurchaseRecordDto::getZsm),
    ASM_BDM(e -> (e.getAsm() == null ? "" : e.getAsm()) + (e.getBdm() == null ? "" : e.getBdm())),
    BDE(PurchaseRecordDto::getBde),
    ASE_SO(e -> (e.getAse() == null ? "" : e.getAse()) + (e.getSo() == null ? "" : e.getSo())),
    BA_Name(PurchaseRecordDto::getBaName),
    BA_Code(PurchaseRecordDto::getBaCode),
    Outlet_ErpId(PurchaseRecordDto::getOutletErpId),
    Outlet_Name(PurchaseRecordDto::getOutletName),
    Outlet_Type(PurchaseRecordDto::getOutletType),
    Market_Name(PurchaseRecordDto::getMarketName),
    City(PurchaseRecordDto::getCity),
    State(PurchaseRecordDto::getState),
    Month(PurchaseRecordDto::getMonth),
    Inv_No(PurchaseRecordDto::getInvoice),
    Stock_Inward_Date(PurchaseRecordDto::getStockInwardDate),
    Date(PurchaseRecordDto::getStockInwardDate),
    Stock_Inward_Time(PurchaseRecordDto::getStockInwardTime),
    Time(PurchaseRecordDto::getStockInwardTime),
    Primary_Category(PurchaseRecordDto::getPrimaryCategory),
    Secondary_Category(PurchaseRecordDto::getSecondaryCategory),
    Product_Erp_Id(PurchaseRecordDto::getProuctErpId),
    Product(PurchaseRecordDto::getProductName),
    Qty(PurchaseRecordDto::getQty),
    MRP(e -> e.getMrp() != null && !e.getMrp().isEmpty() ? Double.valueOf(e.getMrp()) : e.getMrp()),
    Value( e -> e.getValue() != null && !e.getValue().isEmpty() ? Double.valueOf(e.getValue()) : e.getValue()),
    Return_Quantity(PurchaseRecordDto::getReturnQty),
    Return_Value(PurchaseRecordDto::getReturnValue),
    Return_Date(PurchaseRecordDto::getReturnDate);

    private final Function<PurchaseRecordDto, Object> view;

    ColFieldEnum(Function<PurchaseRecordDto, Object> view) {
      this.view = view;
    }

    public Object getValue(PurchaseRecordDto view) {
      return this.view.apply(view);
    }
  }
}
