package com.dcc.osheaapp.vo.dto;

import com.dcc.osheaapp.vo.DropdownMasterVo;

public class UpdateOutletDto {

  private Long id;
  private String outletName;
  private String outletCode;
  private Long outletType;
  private Long outletChannel;
  private String market;
  private Long productDivision;
  private Long companyZone;
  private String ownerFullName;
  private String ownerContactNumber;
  private String address;
  private String city;
  private String regionName;
  private Long pin;
  private Long beat;

  private String latitude;
  private String longitude;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMarket() {
    return market;
  }

  public void setMarket(String market) {
    this.market = market;
  }

  public Long getBeat() {
    return beat;
  }

  public void setBeat(Long beat) {
    this.beat = beat;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
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

  public Long getOutletType() {
    return outletType;
  }

  public void setOutletType(Long outletType) {
    this.outletType = outletType;
  }

  public Long getOutletChannel() {
    return outletChannel;
  }

  public void setOutletChannel(Long outletChannel) {
    this.outletChannel = outletChannel;
  }

  public Long getProductDivision() {
    return productDivision;
  }

  public void setProductDivision(Long productDivision) {
    this.productDivision = productDivision;
  }

  public Long getCompanyZone() {
    return companyZone;
  }

  public void setCompanyZone(Long companyZone) {
    this.companyZone = companyZone;
  }

  public String getOwnerFullName() {
    return ownerFullName;
  }

  public void setOwnerFullName(String ownerFullName) {
    this.ownerFullName = ownerFullName;
  }

  public String getOwnerContactNumber() {
    return ownerContactNumber;
  }

  public void setOwnerContactNumber(String ownerContactNumber) {
    this.ownerContactNumber = ownerContactNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public Long getPin() {
    return pin;
  }

  public void setPin(Long pin) {
    this.pin = pin;
  }
}

