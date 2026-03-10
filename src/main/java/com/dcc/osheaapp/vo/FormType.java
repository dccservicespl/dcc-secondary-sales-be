package com.dcc.osheaapp.vo;

public enum FormType {
  BA("BA"),
  ST("ST");

  private final String formTypeName;

  FormType(String formTypeName) {
    this.formTypeName = formTypeName;
  }

  public String getFormTypeName() {
    return formTypeName;
  }
}
