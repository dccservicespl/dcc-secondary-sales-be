package com.dcc.osheaapp.vo.dto;

import java.util.List;

public class ProductVideoLinksDto {
  Long transactionId;
  String tabName;
  List<String> links;

  public Long getTransactionId() {
    return transactionId;
  }

  public String getTabName() {
    return tabName;
  }

  public List<String> getLinks() {
    return links;
  }
}
