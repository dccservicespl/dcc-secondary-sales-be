package com.dcc.osheaapp.common.service.exporter.excel;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.service.exporter.Exporter;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelExporterAlt<T> implements Exporter<T> {
  private final List<ExcelColumn> headers;
  private final List<List<ExcelColumn>> data;
  private final SXSSFWorkbook workbook;
  private final String sheetName;
  private final SXSSFSheet sheet;
  private final Map<String, Integer> colMap = new HashMap<>();

  public ExcelExporterAlt(
      List<ExcelColumn> headers,
      List<List<ExcelColumn>> data,
      SXSSFWorkbook workbook,
      String sheetName) {
    if (headers == null) throw new IllegalArgumentException("Invalid excel columns");
    if (data == null) throw new IllegalArgumentException("Invalid excel columns");
    this.data = data;
    this.headers = headers;
    this.workbook = workbook == null ? new SXSSFWorkbook() : workbook;
    this.sheetName = sheetName;
    this.sheet = this.workbook.createSheet(this.sheetName);
    this.buildColMap();
  }

  private static void setCellValue(Object val, Cell cell) {
    if (val == null) cell.setCellValue("");
    if (val instanceof String) {
      cell.setCellValue((String) val);
    }
    if (val instanceof Date) cell.setCellValue((Date) val);

    if (val instanceof Double) cell.setCellValue((Double) val);

    if (val instanceof Long) cell.setCellValue((Long) val);
  }

  @Override
  public void export(List<T> data, String fileName, OutputStream os) {
    populateHeader(0);

    long start1 = System.currentTimeMillis();
    populateRows(1);
    System.out.println("[Excel Row Gen] " + (System.currentTimeMillis() - start1));
    try {
      long start = System.currentTimeMillis();
      workbook.write(os);
      workbook.close();
      System.out.println("[Excel Write] " + (System.currentTimeMillis() - start));

    } catch (IOException e) {
      throw new OjbException(e, ErrorCode.FILE_NOT_FOUND, new Object[] {});
    }
  }

  void populateRows(int startRowIdx) {
    int rowIdx = startRowIdx;
    for (List<ExcelColumn> rowData : data) {
      Row row = sheet.createRow(rowIdx);
      row.setHeight((short) (row.getHeight() * 1.5));

      rowData.forEach(
          colData -> {
            int colIdx = colMap.get(colData.getColName());
            Cell cell = row.createCell(colIdx);
            Object val = colData.getColValue();
            setCellValue(val, cell);
            cell.setCellStyle(colData.getCellStyle());
          });
      rowIdx++;
    }

    for (int i = 0; i < headers.size(); i++) {
      sheet.autoSizeColumn(i);
    }
  }

  private void populateHeader(int rowIdx) {
    Row row = sheet.createRow(rowIdx);
    row.setHeight((short) (row.getHeight() * 1.5));
    for (int colIdx = 0; colIdx < headers.size(); colIdx++) {
      sheet.trackColumnForAutoSizing(colIdx);
      ExcelColumn col = headers.get(colIdx);
      Cell cell = row.createCell(colIdx);
      cell.setCellValue(col.getColName());
      cell.setCellStyle(col.getCellStyle());
      sheet.autoSizeColumn(colIdx);
    }
  }

  private void buildColMap() {
    for (int idx = 0; idx < headers.size(); idx++)
      this.colMap.put(headers.get(idx).getColName(), idx);
  }
}
