package com.dcc.osheaapp.common.service.exporter.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.function.Function;

public class ExcelColumn {
 private String colName;
 private Object colValue;
 private CellStyle cellStyle;

 public ExcelColumn(String colName, Object colValue,  CellStyle cellStyle) {
  this.colName = colName;
  this.colValue = colValue;
  this.cellStyle = cellStyle;
 }

 public String getColName() {
  return colName;
 }

 public Object getColValue() {
  return colValue;
 }

 public  CellStyle getCellStyle() {
  return cellStyle;
 }
}
