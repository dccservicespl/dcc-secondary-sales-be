package com.dcc.osheaapp.service.upload;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

abstract class ExcelService {

  protected XSSFWorkbook workbook;
  protected XSSFSheet sheet;
  protected String[] HEADER_COLS;
  protected Map<String, Integer> colMap;

  public ExcelService(XSSFWorkbook workbook, XSSFSheet sheet, String[] HEADER_COLS) {
    this.workbook = workbook;
    this.sheet = sheet;
    this.HEADER_COLS = HEADER_COLS;
    this.colMap = new HashMap<>();
  }

  public CellStyle getHeaderCellStyle() {
    Font headerFont = workbook.createFont();
    headerFont.setColor(IndexedColors.BLACK.index);
    headerFont.setFontName("Arial");
    CellStyle my_style = sheet.getWorkbook().createCellStyle();
    my_style.setFillForegroundColor(IndexedColors.YELLOW.index);
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

  public abstract Map<String, Integer> buildColMap();
}
