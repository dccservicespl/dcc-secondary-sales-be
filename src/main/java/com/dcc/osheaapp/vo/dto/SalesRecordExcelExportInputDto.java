package com.dcc.osheaapp.vo.dto;

import java.util.Date;
import java.util.List;

public class SalesRecordExcelExportInputDto {
  Date fromDate;
  Date toDate;
  Long outlet;
  String activity;
  List<String> stockStatus;

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  public Long getOutlet() {
    return outlet;
  }

  public void setOutlet(Long outlet) {
    this.outlet = outlet;
  }

  public List<String> getStockStatus() {
    return stockStatus;
  }

  public void setStockStatus(List<String> stockStatus) {
    this.stockStatus = stockStatus;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }
}
