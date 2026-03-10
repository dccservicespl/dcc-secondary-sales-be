package com.dcc.osheaapp.report.purchaseSale.controller;

import java.util.Date;
import java.util.List;

public class PurchaseRecordExcelExportInputDto {
  Date fromDate;
  Date toDate;
  Long outlet;
  String activity;
  List<String> stockStatus;
  Long zone;

  public Long getZone() {
    return zone;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public Long getOutlet() {
    return outlet;
  }

  public List<String> getStockStatus() {
    return stockStatus;
  }

  public String getActivity() {
    return activity;
  }
}
