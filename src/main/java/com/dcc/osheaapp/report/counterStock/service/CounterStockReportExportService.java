package com.dcc.osheaapp.report.counterStock.service;

import com.dcc.osheaapp.common.service.exporter.excel.ExcelColumn;
import com.dcc.osheaapp.common.service.exporter.excel.ExcelExporter;
import com.dcc.osheaapp.common.service.exporter.ExportManager;
import com.dcc.osheaapp.common.service.exporter.excel.ExcelExporterAlt;
import com.dcc.osheaapp.report.attendance.service.AttendanceDetailsDto;
import com.dcc.osheaapp.report.attendance.service.AttendanceExportService;
import com.dcc.osheaapp.vo.views.CounterStockReportView;

import com.mysema.commons.lang.Pair;
import org.apache.poi.ss.formula.functions.Count;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCreationHelper;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CounterStockReportExportService {

  private final String[] HEADER_COLS = {};
  Function<SXSSFWorkbook, Pair<SXSSFWorkbook, CellStyle>> baseStyle =
      CounterStockReportExportService::getBaseStyle;
  Function<SXSSFWorkbook, Pair<SXSSFWorkbook, CellStyle>> headerStyle =
      baseStyle.andThen(CounterStockReportExportService::getLightBlueBackgroundCell);

  static Pair<SXSSFWorkbook, CellStyle> getBaseStyle(SXSSFWorkbook wb) {
    CellStyle cellStyle = wb.createCellStyle();
    Font font = wb.createFont();
    SXSSFCreationHelper creationHelper = new SXSSFCreationHelper(wb);
    cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("General"));
    font.setFontName("Calibri");

    font.setBold(true);
    font.setFontHeightInPoints((short) 10);

    cellStyle.setFont(font);
    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);

    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);

    cellStyle.setBottomBorderColor(IndexedColors.GREY_80_PERCENT.index);
    cellStyle.setTopBorderColor(IndexedColors.GREY_80_PERCENT.index);
    cellStyle.setLeftBorderColor(IndexedColors.GREY_80_PERCENT.index);
    cellStyle.setRightBorderColor(IndexedColors.GREY_80_PERCENT.index);
    return new Pair<SXSSFWorkbook, CellStyle>(wb, cellStyle);
  }

  static Pair<SXSSFWorkbook, CellStyle> getLightBlueBackgroundCell(
      Pair<SXSSFWorkbook, CellStyle> stylePair) {
    CellStyle cellStyle = stylePair.getFirst().createCellStyle();
    cellStyle.cloneStyleFrom(stylePair.getSecond());
    cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
    cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
    return new Pair<SXSSFWorkbook, CellStyle>(stylePair.getFirst(), cellStyle);
  }

  public OutputStream export(List<CounterStockReportView> records, OutputStream outputStream) {
    SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
    ExcelExporterAlt<Void> exporter =
        new ExcelExporterAlt<>(
            getHeaders(workbook), getData(records, workbook), workbook, "Stocks");
    exporter.export(new ArrayList<>(), null, outputStream);
    return outputStream;
  }

  private List<ExcelColumn> getHeaders(SXSSFWorkbook wb) {
    String[] staticHeaders = {
      "MD",
      "ZSM",
      "ASM/BDM",
      "BDE",
      "SO",
      "ZONE",
      "STATE",
      "BEAT NAME",
      "DISTRIBUTOR ID",
      "DISTRIBUTOR NAME",
      "OUTLET ErpId",
      "OUTLET NAME",
            "OUTLET TYPE",
      "STATUS",
      "OUTLET CREATED ON",
      "OUTLET CATEGORY",
      "MONTH",
      "PRIMARY CATEGORY",
      "SECONDARY CATEGORY",
      "PRODUCT ERP ID",
      "PRODUCT",
      "MRP",
      "LAST MONTH CL. STOCK",
      "OPENING STOCK(QTY)",
      "OPENING STOCK(VALUE)",
      "PURCHASE(QTY)",
      "PURCHASE(VALUE)",
      "PURCHASE RETURN(QTY)",
      "PURCHASE RETURN(VALUE)",
      "SALE(QTY)",
      "SALE(VALUE)",
      "SALE RETURN(QTY)",
      "SALE RETURN(VALUE)",
      "DAMAGE(QTY)",
      "DAMAGE(VALUE)",
      "CLOSING STOCK(QTY)",
      "CLOSING STOCK(VALUE)",
      "PERCENT STOCK MOVEMENT",
      "PRODUCT MOVEMENT STATUS"
    };
    CellStyle headerStyles = headerStyle.apply(wb).getSecond();


    return Arrays.stream(staticHeaders)
        .map(e -> new ExcelColumn(e, null, headerStyles))
        .collect(Collectors.toList());
  }

  public List<List<ExcelColumn>> getData(List<CounterStockReportView> dtos, SXSSFWorkbook wb) {
    CellStyle generalStyle = baseStyle.apply(wb).getSecond();
    return dtos.stream()
        .map(
            e -> {
              List<ExcelColumn> columns = new ArrayList<>();
              columns.add(new ExcelColumn("ZONE", e.getZone(), generalStyle));
              columns.add(new ExcelColumn("MD", e.getUserChain().getMd(), generalStyle));
              columns.add(new ExcelColumn("ZSM", e.getUserChain().getZsm(), generalStyle));
              columns.add(
                  new ExcelColumn(
                      "ASM/BDM",
                      (e.getUserChain().getAsm() == null ? "" : e.getUserChain().getAsm())
                          + (e.getUserChain().getBdm() == null ? "" : e.getUserChain().getBdm()),
                      generalStyle));
              columns.add(new ExcelColumn("BDE", e.getUserChain().getBde(), generalStyle));
              columns.add(new ExcelColumn("SO", e.getUserChain().getSo(), generalStyle));
              columns.add(new ExcelColumn("STATE", e.getState(), generalStyle));
              columns.add(new ExcelColumn("BEAT NAME", e.getBeatName(), generalStyle));
              columns.add(new ExcelColumn("BEAT NAME", e.getBeatName(), generalStyle));
              columns.add(new ExcelColumn("DISTRIBUTOR ID", e.getDistributorCode(), generalStyle));
              columns.add(
                  new ExcelColumn("DISTRIBUTOR NAME", e.getDistributorName(), generalStyle));
              columns.add(new ExcelColumn("OUTLET ErpId", e.getOutletErpId(), generalStyle));
              columns.add(new ExcelColumn("OUTLET NAME", e.getOutletName(), generalStyle));
                columns.add(new ExcelColumn("OUTLET TYPE", e.getOutletCategory(), generalStyle));

                columns.add(new ExcelColumn("STATUS", e.getStatus()  ? "ACTIVE" : "IN-ACTIVE", generalStyle));
              columns.add(
                  new ExcelColumn("OUTLET CREATED ON", e.getOutletCreatedOn(), generalStyle));
              columns.add(new ExcelColumn("OUTLET CATEGORY", e.getOutletCategory(), generalStyle));
              columns.add(new ExcelColumn("MONTH", e.getMonth(), generalStyle));
              columns.add(new ExcelColumn("PRIMARY CATEGORY", e.getCategoryName(), generalStyle));
              columns.add(
                  new ExcelColumn("SECONDARY CATEGORY", e.getSubCategoryName(), generalStyle));
              columns.add(new ExcelColumn("PRODUCT ERP ID", e.getProductErp(), generalStyle));
              columns.add(
                  new ExcelColumn(
                      "PRODUCT",
                      e.getProductName() + " - " + e.getpSize() + "/" + e.getpUnit(),
                      generalStyle));
              columns.add(new ExcelColumn("MRP", e.getProductMrp(), generalStyle));
              columns.add(
                  new ExcelColumn(
                      "LAST MONTH CL. STOCK", e.getLastMonthClosingStock(), generalStyle));
              columns.add(new ExcelColumn("OPENING STOCK(QTY)", e.getOpeningStock(), generalStyle));
              columns.add(
                  new ExcelColumn("OPENING STOCK(VALUE)", e.getOpeningStockAmount(), generalStyle));
              columns.add(new ExcelColumn("PURCHASE(QTY)", e.getPurchase(), generalStyle));
              columns.add(new ExcelColumn("PURCHASE(VALUE)", e.getPurchaseAmount(), generalStyle));
              columns.add(
                  new ExcelColumn("PURCHASE RETURN(QTY)", e.getPurchaseReturn(), generalStyle));
              columns.add(
                  new ExcelColumn(
                      "PURCHASE RETURN(VALUE)", e.getPurchaseReturnAmount(), generalStyle));
              columns.add(new ExcelColumn("SALE(QTY)", e.getSale(), generalStyle));
              columns.add(new ExcelColumn("SALE(VALUE)", e.getSaleAmount(), generalStyle));
              columns.add(new ExcelColumn("SALE RETURN(QTY)", e.getSaleReturn(), generalStyle));
              columns.add(
                  new ExcelColumn("SALE RETURN(VALUE)", e.getSaleReturnAmount(), generalStyle));
              columns.add(new ExcelColumn("DAMAGE(QTY)", e.getDamage(), generalStyle));
              columns.add(new ExcelColumn("DAMAGE(VALUE)", e.getDamageAmount(), generalStyle));
              columns.add(new ExcelColumn("CLOSING STOCK(QTY)", e.getClosingStock(), generalStyle));
              columns.add(
                  new ExcelColumn("CLOSING STOCK(VALUE)", e.getClosingStockAmount(), generalStyle));
              columns.add(
                  new ExcelColumn(
                      "PERCENT STOCK MOVEMENT", e.getPercentStockMovement(), generalStyle));
              columns.add(
                  new ExcelColumn(
                      "PRODUCT MOVEMENT STATUS", e.getProductMovementStatus(), generalStyle));
              return columns;
            })
        .collect(Collectors.toList());
  }
}
