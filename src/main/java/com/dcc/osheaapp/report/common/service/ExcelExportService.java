package com.dcc.osheaapp.report.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExportService<T> extends ExcelService {

  public ExcelExportService(XSSFWorkbook workbook, XSSFSheet sheet, String[] HEADER_COLS) {
    super(workbook, sheet, HEADER_COLS);
    this.colMap = new HashMap<>();
    this.buildColMap();
  }

  public void createHeader(int headerRowIdx) {
    Row headerRow = sheet.createRow(headerRowIdx);
    for (int colIdx = 0; colIdx < HEADER_COLS.length; colIdx++) {
      Cell cell = headerRow.createCell(colIdx);
      cell.setCellValue(HEADER_COLS[colIdx]);
      cell.setCellStyle(getHeaderCellStyle());
      sheet.autoSizeColumn(colIdx);
    }
  }

  public Map<String, Integer> buildColMap() {
    for (int idx = 0; idx < HEADER_COLS.length; idx++) this.colMap.put(HEADER_COLS[idx], idx);
    return this.colMap;
  }

  public void populateRows(
      List<T> products, int startRowIdx, BiFunction<String, T, String> valueGetter) {
    for (T product : products) {
      Row row = sheet.createRow(startRowIdx);
      for (String col : HEADER_COLS) {
        Integer colIdx = buildColMap().get(col);
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(valueGetter.apply(col, product));
        sheet.autoSizeColumn(colIdx);
      }
      startRowIdx++;
    }
  }
}
