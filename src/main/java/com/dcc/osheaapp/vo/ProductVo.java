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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class ProductVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "product_name", nullable = false, length = 700)
  private String productName;

  @Column(name = "product_code", nullable = false, length = 50)
  private String productCode;

  @Column(name = "product_desc", nullable = false, length = 700)
  private String productDesc;

  @OneToOne // (cascade = CascadeType.ALL )
  @JoinColumn(name = "division")
  private DropdownMasterVo division;

  @Column(name = "category_id")
  private Long categoryId;

  @Column(name = "product_ptr")
  private String productPTR;

  @Column(name = "product_ptd")
  private String productPTD;

  @Column(name = "product_mrp")
  private String productMRP;

  @Column(name = "unit", length = 10)
  private String unit;

  @Column(name = "size")
  private Long size;

  @Column(name = "packaging_type")
  private Long packagingType;

  @Column(name = "min_batch_qty")
  private Long minBatchQty;

  @Column(name = "product_vdo_link")
  private String productVdoLink;

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

  @Transient List<FormMediaMappingVo> mediaData = new ArrayList<FormMediaMappingVo>();

  @Transient private String categoryName;
  @Transient private String subCategoryName;
  @Transient private String availableStock;

  public ProductVo() {}

  public ProductVo(
      Long id,
      String productName,
      String productCode,
      String productDesc,
      DropdownMasterVo division,
      Long categoryId,
      String productPTR,
      String productPTD,
      String productMRP,
      String unit,
      Long size,
      Long packagingType,
      Long minBatchQty,
      String productVdoLink,
      Boolean isActive,
      Long createdBy,
      Long updatedBy) {
    this.id = id;
    this.productName = productName;
    this.productCode = productCode;
    this.productDesc = productDesc;
    this.division = division;
    this.categoryId = categoryId;
    this.productPTR = productPTR;
    this.productPTD = productPTD;
    this.productMRP = productMRP;
    this.unit = unit;
    this.size = size;
    this.packagingType = packagingType;
    this.minBatchQty = minBatchQty;
    this.productVdoLink = productVdoLink;
    this.isActive = isActive;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getProductDesc() {
    return productDesc;
  }

  public void setProductDesc(String productDesc) {
    this.productDesc = productDesc;
  }

  public DropdownMasterVo getDivision() {
    return division;
  }

  public void setDivision(DropdownMasterVo division) {
    this.division = division;
  }

  public String getProductPTR() {
    return productPTR;
  }

  public void setProductPTR(String productPTR) {
    this.productPTR = productPTR;
  }

  public String getProductPTD() {
    return productPTD;
  }

  public void setProductPTD(String productPTD) {
    this.productPTD = productPTD;
  }

  public String getProductMRP() {
    return productMRP;
  }

  public void setProductMRP(String productMRP) {
    this.productMRP = productMRP;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public Long getPackagingType() {
    return packagingType;
  }

  public void setPackagingType(Long packagingType) {
    this.packagingType = packagingType;
  }

  public Long getMinBatchQty() {
    return minBatchQty;
  }

  public void setMinBatchQty(Long minBatchQty) {
    this.minBatchQty = minBatchQty;
  }

  public String getProductVdoLink() {
    return productVdoLink;
  }

  public void setProductVdoLink(String productVdoLink) {
    this.productVdoLink = productVdoLink;
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

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public List<FormMediaMappingVo> getMediaData() {
    return mediaData;
  }

  public void setMediaData(List<FormMediaMappingVo> mediaData) {
    this.mediaData = mediaData;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getSubCategoryName() {
    return subCategoryName;
  }

  public void setSubCategoryName(String subCategoryName) {
    this.subCategoryName = subCategoryName;
  }

  public String getAvailableStock() {
    return availableStock;
  }

  public void setAvailableStock(String availableStock) {
    this.availableStock = availableStock;
  }

  @Override
  public String toString() {
    return "ProductVo [id="
        + id
        + ", productName="
        + productName
        + ", productCode="
        + productCode
        + ", productDesc="
        + productDesc
        + ", division="
        + division
        + ", categoryId="
        + categoryId
        + ", productPTR="
        + productPTR
        + ", productPTD="
        + productPTD
        + ", productMRP="
        + productMRP
        + ", unit="
        + unit
        + ", size="
        + size
        + ", packagingType="
        + packagingType
        + ", minBatchQty="
        + minBatchQty
        + ", productVdoLink="
        + productVdoLink
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
        + ", mediaData="
        + mediaData
        + ", categoryName="
        + categoryName
        + ", subCategoryName="
        + subCategoryName
        + ", availableStock="
        + availableStock
        + "]";
  }
}
