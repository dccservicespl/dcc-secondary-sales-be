package com.dcc.osheaapp.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "distributor_search_output")
public class DistributorOutputVo  implements Serializable{

    private static final long serialVersionUID = 1L;
    

     @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  private String distributorName;
  private String region;
  private String stockistType;
    private Boolean isActive;
  private String address;
  private String contactNumber;
  private Long creditDuration;
  private String gstin;
  private String panNo;
  private Long companyZone;
  private Long userType;
  private String userId;
  private String companyZoneName;
  private String userTypeName;
  private String userFullName;

    public DistributorOutputVo(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStockistType() {
        return stockistType;
    }

    public void setStockistType(String stockistType) {
        this.stockistType = stockistType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getCreditDuration() {
        return creditDuration;
    }

    public void setCreditDuration(Long creditDuration) {
        this.creditDuration = creditDuration;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public Long getCompanyZone() {
        return companyZone;
    }

    public void setCompanyZone(Long companyZone) {
        this.companyZone = companyZone;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }


    
}
