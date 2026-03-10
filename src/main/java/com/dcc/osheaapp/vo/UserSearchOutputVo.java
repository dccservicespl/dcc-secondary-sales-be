package com.dcc.osheaapp.vo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_search_output")
public class UserSearchOutputVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  private String fullName;
  private String username;
  private Long outletId;
  private String outletName;
  private String outletCode;
  private String reportingTo;
  private String companyZone;
  private Long userType;
  private Boolean isActive;
  private String reportingToName;
  private String companyZoneName;
  private String userTypeName;
  private String contactNumber;
  private String productDivision;

  public UserSearchOutputVo() {}

  public Boolean isActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Long getOutletId() {
    return outletId;
  }

  public void setOutletId(Long outletId) {
    this.outletId = outletId;
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

  public String getCompanyZone() {
    return companyZone;
  }

  public void setCompanyZone(String companyZone) {
    this.companyZone = companyZone;
  }

  public Long getUserType() {
    return userType;
  }

  public void setUserType(Long userType) {
    this.userType = userType;
  }

  public String getReportingToName() {
    return reportingToName;
  }

  public void setReportingToName(String reportingToName) {
    this.reportingToName = reportingToName;
  }

  public String getCompanyZoneName() {
    return companyZoneName;
  }

  public void setCompanyZoneName(String companyZoneName) {
    this.companyZoneName = companyZoneName;
  }

  public String getUserTypeName() {
    return userTypeName;
  }

  public void setUserTypeName(String userTypeName) {
    this.userTypeName = userTypeName;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public String getProductDivision() {
    return productDivision;
  }

  public void setProductDivision(String productDivision) {
    this.productDivision = productDivision;
  }

  @Override
  public String toString() {
    return "UserSearchOutputVo [id="
        + id
        + ", fullName="
        + fullName
        + ", username="
        + username
        + ", outletId="
        + outletId
        + ", outletName="
        + outletName
        + ", outletCode="
        + outletCode
        + ", reportingTo="
        + reportingTo
        + ", companyZone="
        + companyZone
        + "]";
  }
}
