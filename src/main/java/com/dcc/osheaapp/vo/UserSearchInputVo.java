package com.dcc.osheaapp.vo;

import com.dcc.osheaapp.vo.type.ActivationStatus;

public class UserSearchInputVo {

  private Long userType;
  private ActivationStatus active;
  private String fullName;
  private FormType formType;
  private String username;
  private Long outletId;
  private String outletName;
  private String outletCode;
  private String reportingTo;
  private Long companyZone;
  private Long divisionId;
  private Integer page;
  private Integer size;

  public void setDivisionId(Long divisionId) {
    this.divisionId = divisionId;
  }

  public Long getDivisionId() {
    return divisionId;
  }

  public ActivationStatus getActive() {
    return active;
  }

  public void setActive(ActivationStatus active) {
    this.active = active;
  }

  public Long getCompanyZone() {
    return companyZone;
  }

  public void setCompanyZone(Long companyZone) {
    this.companyZone = companyZone;
  }

  public Long getOutletId() {
    return outletId;
  }

  public void setOutletId(Long outletId) {
    this.outletId = outletId;
  }

  public FormType getFormType() {
    return formType;
  }

  public void setFormType(FormType formType) {
    this.formType = formType;
  }

  public Long getUserType() {
    return userType;
  }

  public void setUserType(Long userType) {
    this.userType = userType;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getOutletName() {
    return outletName;
  }

  public void setOutletName(String outletName) {
    this.outletName = outletName;
  }

  public String getOutletCode() {
    return outletCode;
  }

  public void setOutletCode(String outletCode) {
    this.outletCode = outletCode;
  }

  public String getReportingTo() {
    return reportingTo;
  }

  public void setReportingTo(String reportingTo) {
    this.reportingTo = reportingTo;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }
}
