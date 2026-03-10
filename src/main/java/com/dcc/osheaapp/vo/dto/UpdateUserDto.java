package com.dcc.osheaapp.vo.dto;

import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.UserTypeVo;
import java.util.Date;
import java.util.List;

public class UpdateUserDto {
  private Long id;

  private String fullName;
  private String userCode;
  private String contactNumber;
  private String aadhar;
  private String pan;
  private String bankName;
  private String bankBranchName;
  private String bankAccountNo;
  private String ifscCode;
  private UserTypeVo userType;
  private DropdownMasterVo employeeType;
  private DropdownMasterVo companyZone;
  private DropdownMasterVo productDivision;
  private UserDetailsVo reportingTo;
  private String salary;
  private String state;
  private Date dateOfJoining;
  private List<OutletVo> assotiateOutlet;

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

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public UserTypeVo getUserType() {
    return userType;
  }

  public void setUserType(UserTypeVo userType) {
    this.userType = userType;
  }

  public DropdownMasterVo getEmployeeType() {
    return employeeType;
  }

  public void setEmployeeType(DropdownMasterVo employeeType) {
    this.employeeType = employeeType;
  }

  public DropdownMasterVo getCompanyZone() {
    return companyZone;
  }

  public void setCompanyZone(DropdownMasterVo companyZone) {
    this.companyZone = companyZone;
  }

  public String getUserCode() {
    return userCode;
  }

  public UpdateUserDto setUserCode(String userCode) {
    this.userCode = userCode;
    return this;
  }

  public DropdownMasterVo getProductDivision() {
    return productDivision;
  }

  public void setProductDivision(DropdownMasterVo productDivision) {
    this.productDivision = productDivision;
  }

  public UserDetailsVo getReportingTo() {
    return reportingTo;
  }

  public void setReportingTo(UserDetailsVo reportingTo) {
    this.reportingTo = reportingTo;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Date getDateOfJoining() {
    return dateOfJoining;
  }

  public void setDateOfJoining(Date dateOfJoining) {
    this.dateOfJoining = dateOfJoining;
  }

  public List<OutletVo> getAssotiateOutlet() {
    return assotiateOutlet;
  }

  public void setAssotiateOutlet(List<OutletVo> assotiateOutlet) {
    this.assotiateOutlet = assotiateOutlet;
  }

  public String getAadhar() {
    return aadhar;
  }

  public void setAadhar(String aadhar) {
    this.aadhar = aadhar;
  }

  public String getPan() {
    return pan;
  }

  public void setPan(String pan) {
    this.pan = pan;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankBranchName() {
    return bankBranchName;
  }

  public void setBankBranchName(String bankBranchName) {
    this.bankBranchName = bankBranchName;
  }

  public String getBankAccountNo() {
    return bankAccountNo;
  }

  public void setBankAccountNo(String bankAccountNo) {
    this.bankAccountNo = bankAccountNo;
  }

  public String getIfscCode() {
    return ifscCode;
  }

  public void setIfscCode(String ifscCode) {
    this.ifscCode = ifscCode;
  }
}
