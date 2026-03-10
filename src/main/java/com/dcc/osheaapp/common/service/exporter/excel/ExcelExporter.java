package com.dcc.osheaapp.common.service.exporter.excel;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.dcc.osheaapp.common.service.exporter.Exporter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter<T> implements Exporter<T> {

  private final List<String> header;
  private final BiFunction<String, T, Object> valueGetter;
  private BiFunction<String, XSSFWorkbook, CellStyle> styleGetter = null;
  private final XSSFWorkbook workbook;
  private final XSSFSheet sheet;
  private final Map<String, Integer> colMap;



  public ExcelExporter(
      List<String> header, String sheetName, BiFunction<String, T, Object> valueGetter) {
    this.header = header;
    this.workbook = new XSSFWorkbook();
    this.valueGetter = valueGetter;
    this.sheet = workbook.createSheet(sheetName);
    this.colMap = new HashMap<>();
  }


  public ExcelExporter(
          List<String> header, String sheetName, BiFunction<String, T, Object> valueGetter, BiFunction<String, XSSFWorkbook , CellStyle> styleGetter) {
    this.header = header;
    this.workbook = new XSSFWorkbook();
    this.valueGetter = valueGetter;
    this.sheet = workbook.createSheet(sheetName);
    this.colMap = new HashMap<>();
    this.styleGetter = styleGetter;
  }
  @Override
  public void export(List<T> data, String fileName, OutputStream os) {
    createHeader(0);
    populateRows(data);
    try {
      workbook.write(os);
      workbook.close();

    } catch (IOException e) {
      throw new OjbException(e, ErrorCode.FILE_NOT_FOUND, new Object[] {});
    }
  }

  void populateRows(List<T> data) {
    int startRowIdx = 1;
    for (T product : data) {
      Row row = sheet.createRow(startRowIdx);
      for (String col : header) {
        Integer colIdx = buildColMap().get(col);
        Cell cell = row.createCell(colIdx);
        Object val = valueGetter.apply(col, product);
        if(val instanceof  String)  {
          cell.setCellValue( (String) val);
        }
        if(val instanceof  Long)
          cell.setCellValue((Long) val);
        if(val instanceof Date)
          cell.setCellValue((Date) val);

        if(val instanceof  Double)
          cell.setCellValue((Double) val);

        if(val instanceof  Integer)
          cell.setCellValue((Integer) val);

        if(styleGetter != null) {
          cell.setCellStyle(styleGetter.apply(col, workbook));
        }
      }
      startRowIdx++;
    }

    for(int i = 0; i < header.size(); i++) {
      sheet.autoSizeColumn(i);
    }
  }


  void createHeader(int headerRowIdx) {
    Row headerRow = sheet.createRow(headerRowIdx);
      headerRow.setHeight((short) (headerRow.getHeight() * 2));
    for (int colIdx = 0; colIdx < header.size(); colIdx++) {
      Cell cell = headerRow.createCell(colIdx);
      cell.setCellValue(header.get(colIdx));
      cell.setCellStyle(getHeaderCellStyle());
      CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
      sheet.autoSizeColumn(colIdx);
    }
  }

  public Map<String, Integer> buildColMap() {
    for (int idx = 0; idx < header.size(); idx++) this.colMap.put(header.get(idx), idx);
    return this.colMap;
  }

  CellStyle getHeaderCellStyle() {
    Font headerFont = workbook.createFont();
    headerFont.setColor(IndexedColors.BLACK.index);
    headerFont.setFontName("Arial");
    CellStyle my_style = sheet.getWorkbook().createCellStyle();
    my_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
    my_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerFont.setFontHeightInPoints((short) 10);
    headerFont.setBold(true);
    my_style.setFont(headerFont);
    my_style.setBorderBottom(BorderStyle.THIN);
    my_style.setBorderTop(BorderStyle.THIN);
    my_style.setBorderLeft(BorderStyle.THIN);
    my_style.setBorderRight(BorderStyle.THIN);
    return my_style;
  }

  public Map<Integer, String> getColMapInverted() {
    Map<Integer, String> invertedColMap = new HashMap<>();
    this.colMap.forEach((key, value) -> invertedColMap.put(value, key));
    return invertedColMap;
  }

  public XSSFWorkbook getWorkbook() {
    return workbook;
  }
}
