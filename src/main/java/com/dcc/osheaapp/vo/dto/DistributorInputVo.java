package com.dcc.osheaapp.vo.dto;

import com.dcc.osheaapp.vo.type.ActivationStatus;

public class DistributorInputVo {

    private String distributorName;
    // private String zone;
    private Long companyZone;
    private String region;
    private String address;
    private String contactNumber;
    private  String pan;
private ActivationStatus active;
    private String gstin;
    private Long creditDu;
    private  String stockist;
    private  Long userType ;
    private Long userId;
    private String userFullName;
    private Integer page;
    private Integer size;

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

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public Long getCreditDu() {
        return creditDu;
    }

    public void setCreditDu(Long creditDu) {
        this.creditDu = creditDu;
    }

    public String getStockist() {
        return stockist;
    }

    public void setStockist(String stockist) {
        this.stockist = stockist;
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

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
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

    // public Long getCompanyZone() {
    //     return companyZone;
    // }

    // public void setCompanyZone(Long companyZone) {
    //     this.companyZone = companyZone;
    // }

    

   
    
}
