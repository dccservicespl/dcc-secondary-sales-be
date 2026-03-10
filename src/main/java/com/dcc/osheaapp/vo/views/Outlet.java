package com.dcc.osheaapp.vo.views;

import java.util.Date;
import javax.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "outlet_view")
@EntityListeners(AuditingEntityListener.class)
public class Outlet {
  @Id private Long id;

  private String outletName;
  private Long userId;
  private String baName;
  private String soName;

  private String outletCode;

  private String latitude;

  private String longitude;

  private Long outletChannel;
  private String outletChannelName;

  private Long outletType;
  private String outletTypeName;

  private String market;

  private Long beat;
  private String beatName;

  private Long companyZone;
  private String companyZoneName;

  private Long distributor;
  private String distributorName;

  private Long productDivision;

  private String ownerFullName;

  private String ownerContactNumber;

  private String regionName;

  private String address;

  private String city;

  private Long state; // different master table

  private Long country; // different master table

  private Long pin;

  private Boolean isActive;

  private Date createdOn;

  private Long createdBy;

  private Date updatedOn;

  private Long updatedBy;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getSoName() {
    return soName;
  }

  public void setSoName(String soName) {
    this.soName = soName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Long getOutletChannel() {
    return outletChannel;
  }

  public void setOutletChannel(Long outletChannel) {
    this.outletChannel = outletChannel;
  }

  public String getOutletChannelName() {
    return outletChannelName;
  }

  public void setOutletChannelName(String outletChannelName) {
    this.outletChannelName = outletChannelName;
  }

  public Long getOutletType() {
    return outletType;
  }

  public void setOutletType(Long outletType) {
    this.outletType = outletType;
  }

  public String getOutletTypeName() {
    return outletTypeName;
  }

  public void setOutletTypeName(String outletTypeName) {
    this.outletTypeName = outletTypeName;
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

  public String getBeatName() {
    return beatName;
  }

  public void setBeatName(String beatName) {
    this.beatName = beatName;
  }

  public Long getCompanyZone() {
    return companyZone;
  }

  public void setCompanyZone(Long companyZone) {
    this.companyZone = companyZone;
  }

  public String getCompanyZoneName() {
    return companyZoneName;
  }

  public void setCompanyZoneName(String companyZoneName) {
    this.companyZoneName = companyZoneName;
  }

  public Long getDistributor() {
    return distributor;
  }

  public void setDistributor(Long distributor) {
    this.distributor = distributor;
  }

  public String getDistributorName() {
    return distributorName;
  }

  public void setDistributorName(String distributorName) {
    this.distributorName = distributorName;
  }

  public Long getProductDivision() {
    return productDivision;
  }

  public void setProductDivision(Long productDivision) {
    this.productDivision = productDivision;
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

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
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

  public Long getState() {
    return state;
  }

  public void setState(Long state) {
    this.state = state;
  }

  public Long getCountry() {
    return country;
  }

  public void setCountry(Long country) {
    this.country = country;
  }

  public Long getPin() {
    return pin;
  }

  public void setPin(Long pin) {
    this.pin = pin;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }

  public Long getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Long updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getBaName() {
    return baName;
  }

  public void setBaName(String baName) {
    this.baName = baName;
  }
}
