package com.dcc.osheaapp.report.common.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUploadService<T> extends ExcelService {
  public ExcelUploadService(XSSFWorkbook workbook, XSSFSheet sheet, String[] HEADER_COLS) {
    super(workbook, sheet, HEADER_COLS);
    this.colMap = new HashMap<String, Integer>();
    this.buildColMap();
  }

  public Map<String, Integer> buildColMap() {
    Row headerRow = sheet.getRow(0);
    DataFormatter formatter = new DataFormatter();
    for (int col = 0; col < headerRow.getPhysicalNumberOfCells(); col++) {
      Cell cell = headerRow.getCell(col);
      String cellValue = formatter.formatCellValue(cell);
      Optional<String> headerContains =
          Arrays.stream(HEADER_COLS).filter(e -> e.equals(cellValue)).findAny();
      if (headerContains.isPresent()) colMap.put(cellValue, col);
    }
    return colMap;
  }
}
