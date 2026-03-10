package com.dcc.osheaapp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.transaction.TransactionScoped;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "outlet")
@EntityListeners(AuditingEntityListener.class)
public class OutletVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "outlet_name", nullable = false, length = 700)
  private String outletName;

  @Column(name = "outlet_code", nullable = false, length = 50)
  private String outletCode;

  @Column(name = "outlet_latitude")
  private String latitude;

  @Column(name = "outlet_longitude")
  private String longitude;

  // @Column(name = "so_user_id")
  @Transient // different mapping table
  private Long soUserId;

  @Column(name = "outlet_channel") // different master table
  private Long outletChannel;

  @Column(name = "outlet_type") // Dropdown master table
  private Long outletType;

  @Column(name = "market") // different master table
  private String market;

  @Column(name = "beat") // different master table
  private Long beat;

  @Column(name = "company_zone") // different master table
  private Long companyZone;

  @Column(name = "distributor") // different master table
  private Long distributor;

  @Column(name = "product_division") // different master table
  private Long productDivision;

  @Column(name = "owner_full_name", nullable = true)
  private String ownerFullName;

  @Column(name = "owner_contact_number", nullable = true)
  private String ownerContactNumber;

  @Column(name = "region_name")
  private String regionName;

  @Column(name = "outlet_address")
  private String address;

  @Column(name = "outlet_city")
  private String city;

  @Column(name = "outlet_state")
  private Long state; // different master table

  @Column(name = "outlet_country")
  private Long country; // different master table

  @Column(name = "outlet_pin")
  private Long pin;

  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "created_on", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  private Date createdOn;

  @Column(name = "created_by", nullable = false, updatable = false)
  private Long createdBy;

  @Column(name = "updated_on", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  private Date updatedOn;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "outlet_type_so", nullable = true)
  private Long outletTypeSo;

  @Column(name = "outlet_category_so", nullable = true)
  private Long outletCategorySo;

  @Column(name = "outlet_image_link")
  private String outletImageLink;

  @Column(name = "gst_in_no")
  private String gstNumber;

  @Transient
  private MultipartFile outletImg;

  @Transient
  private DropdownMasterVo outletTypeObj;

  @Transient
  private DropdownMasterVo outletChanelObj;
  @Transient
  private DropdownMasterVo ProductDiv;

  @Transient
  private DropdownMasterVo companyZoneObj;
  
  @Transient
  private String soOutletvisitType = "Not Visited";

  @Transient
  private List<FormMediaMappingVo> mediaFiles = new ArrayList<FormMediaMappingVo>();

  public OutletVo() {}

  public OutletVo(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public OutletVo setId(Long id) {
    this.id = id;
    return this;
  }

  public String getOutletName() {
    return outletName;
  }

  public void setOutletName(String outletName) {
    this.outletName = outletName;
  }

  public Long getSoUserId() {
    return soUserId;
  }

  public void setSoUserId(Long soUserId) {
    this.soUserId = soUserId;
  }

  public Long getOutletChannel() {
    return outletChannel;
  }

  public void setOutletChannel(Long outletChannel) {
    this.outletChannel = outletChannel;
  }

  public Long getOutletType() {
    return outletType;
  }

  public void setOutletType(Long outletType) {
    this.outletType = outletType;
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

  public Long getCompanyZone() {
    return companyZone;
  }

  public void setCompanyZone(Long companyZone) {
    this.companyZone = companyZone;
  }

  public Long getDistributor() {
    return distributor;
  }

  public void setDistributor(Long distributor) {
    this.distributor = distributor;
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

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
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

  public Long getOutletTypeSo() {
    return outletTypeSo;
  }

  public void setOutletTypeSo(Long outletTypeSo) {
    this.outletTypeSo = outletTypeSo;
  }

  public Long getOutletCategorySo() {
    return outletCategorySo;
  }

  public void setOutletCategorySo(Long outletCategorySo) {
    this.outletCategorySo = outletCategorySo;
  }

  public String getOutletImageLink() {
    return outletImageLink;
  }

  public void setOutletImageLink(String outletImageLink) {
    this.outletImageLink = outletImageLink;
  }

  public MultipartFile getOutletImg() {
    return outletImg;
  }

  public void setOutletImg(MultipartFile outletImg) {
    this.outletImg = outletImg;
  }

  public List<FormMediaMappingVo> getMediaFiles() {
    return mediaFiles;
  }

  public void setMediaFiles(List<FormMediaMappingVo> mediaFiles) {
    this.mediaFiles = mediaFiles;
  }

  public String getSoOutletvisitType() {
	return soOutletvisitType;
}

public void setSoOutletvisitType(String soOutletvisitType) {
	this.soOutletvisitType = soOutletvisitType;
}

  public DropdownMasterVo getOutletTypeObj() {
    return outletTypeObj;
  }

  public void setOutletTypeObj(DropdownMasterVo outletTypeObj) {
    this.outletTypeObj = outletTypeObj;
  }

  public DropdownMasterVo getOutletChanelObj() {
    return outletChanelObj;
  }

  public void setOutletChanelObj(DropdownMasterVo outletChanelObj) {
    this.outletChanelObj = outletChanelObj;
  }

  public DropdownMasterVo getProductDiv() {
    return ProductDiv;
  }

  public void setProductDiv(DropdownMasterVo productDiv) {
    ProductDiv = productDiv;
  }

  public DropdownMasterVo getCompanyZoneObj() {
    return companyZoneObj;
  }

  public void setCompanyZoneObj(DropdownMasterVo companyZoneObj) {
    this.companyZoneObj = companyZoneObj;
  }

  @Override
  public String toString() {
    return "OutletVo [id="
        + id
        + ", outletName="
        + outletName
        + ", outletCode="
        + outletCode
        + ", latitude="
        + latitude
        + ", longitude="
        + longitude
        + ", soUserId="
        + soUserId
        + ", outletChannel="
        + outletChannel
        + ", outletType="
        + outletType
        + ", market="
        + market
        + ", beat="
        + beat
        + ", companyZone="
        + companyZone
        + ", distributor="
        + distributor
        + ", productDivision="
        + productDivision
        + ", ownerFullName="
        + ownerFullName
        + ", ownerContactNumber="
        + ownerContactNumber
        + ", regionName="
        + regionName
        + ", address="
        + address
        + ", city="
        + city
        + ", state="
        + state
        + ", country="
        + country
        + ", pin="
        + pin
        + ", isActive="
        + isActive
        + ", createdOn="
        + createdOn
        + ", createdBy="
        + createdBy
        + ", updatedOn="
        + updatedOn
        + ", updatedBy="
        + updatedBy
        + "]";
  }
}
