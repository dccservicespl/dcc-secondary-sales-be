package com.dcc.osheaapp.vo.views;

import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.ProductVo;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Immutable
@Subselect(
    "select  p.ID  ,\n"
        + "p.created_by as createdBy,\n"
        + "p.created_on as createdOn,\n"
        + "p.is_active as isActive,\n"
        + "p.min_batch_qty as  minBatchQty,\n"
        + "p.packaging_type as packagingType,\n"
        + "p.product_desc as productDesc,\n"
        + "p.product_mrp as productMRP,\n"
        + "p.product_name as productName,\n"
        + "p.product_ptr as productPTR,\n"
        + "p.product_ptd as productPTD, \n"
        + "p.product_vdo_link as productVdoLink,\n"
        + "p.unit as unit,\n"
        + "p.size as size,\n"
        + "p.updated_by as updatedBy ,\n"
        + "p.updated_on as updatedOn,\n"
        + "p.product_code as productCode,\n"
        + "pc2.ID as categoryId,\n"
        + "pc2.category_name as categoryName,\n"
        + "pc.ID  as subcategoryId, \n"
        + "pc.category_name as subcategoryName ,\n"
        + "pd.Id  as divisionId,\n"
        + "pd.field_name as divisionName\n"
        + "from product p \n"
        + "inner join product_category pc on pc.ID = p.category_id \n"
        + "left join product_category pc2 on pc.parent_id = pc2.ID \n"
        + "inner join dropdown_mst pd on pd.Id = p.division and field_type = 'division' \n")
@Table(name = "product_view")
@EntityListeners(AuditingEntityListener.class)
public class ProductView {
  @Id private Long ID;

  private String productName;

  private String productCode;

  private String productDesc;

  private Long divisionId;
  private String divisionName;

  private Long categoryId;
  private String categoryName;
  private Long subcategoryId;
  private String subcategoryName;

  private String productPTR;

  private String productPTD;

  private String productMRP;

  private String unit;

  private Long size;

  private Long packagingType;

  private Long minBatchQty;

  private String productVdoLink;

  private Boolean isActive;

  private Date createdOn;

  private Long createdBy;

  private Date updatedOn;

  private Long updatedBy;

  public ProductView() {}

  public Long getID() {
    return ID;
  }

  public String getProductName() {
    return productName;
  }

  public String getProductCode() {
    return productCode;
  }

  public String getProductDesc() {
    return productDesc;
  }

  public Long getDivisionId() {
    return divisionId;
  }

  public String getDivisionName() {
    return divisionName;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public Long getSubcategoryId() {
    return subcategoryId;
  }

  public String getSubcategoryName() {
    return subcategoryName;
  }

  public String getProductPTR() {
    return productPTR;
  }

  public String getProductPTD() {
    return productPTD;
  }

  public String getProductMRP() {
    return productMRP;
  }

  public String getUnit() {
    return unit;
  }

  public Long getSize() {
    return size;
  }

  public Long getPackagingType() {
    return packagingType;
  }

  public Long getMinBatchQty() {
    return minBatchQty;
  }

  public String getProductVdoLink() {
    return productVdoLink;
  }

  public Boolean getActive() {
    return isActive;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public Long getUpdatedBy() {
    return updatedBy;
  }

  public void setID(Long ID) {
    this.ID = ID;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public void setProductDesc(String productDesc) {
    this.productDesc = productDesc;
  }

  public void setDivisionId(Long divisionId) {
    this.divisionId = divisionId;
  }

  public void setDivisionName(String divisionName) {
    this.divisionName = divisionName;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public void setSubcategoryId(Long subcategoryId) {
    this.subcategoryId = subcategoryId;
  }

  public void setSubcategoryName(String subcategoryName) {
    this.subcategoryName = subcategoryName;
  }

  public void setProductPTR(String productPTR) {
    this.productPTR = productPTR;
  }

  public void setProductPTD(String productPTD) {
    this.productPTD = productPTD;
  }

  public void setProductMRP(String productMRP) {
    this.productMRP = productMRP;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public void setPackagingType(Long packagingType) {
    this.packagingType = packagingType;
  }

  public void setMinBatchQty(Long minBatchQty) {
    this.minBatchQty = minBatchQty;
  }

  public void setProductVdoLink(String productVdoLink) {
    this.productVdoLink = productVdoLink;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }

  public void setUpdatedBy(Long updatedBy) {
    this.updatedBy = updatedBy;
  }

  public ProductVo toEntity() {
    DropdownMasterVo division = new DropdownMasterVo();
    division.setId(this.divisionId);
    return new ProductVo(
        this.ID,
        this.productName,
        this.productCode,
        this.productDesc,
        division,
        this.subcategoryId,
        this.productPTR,
        this.productPTD,
        this.productMRP,
        this.unit,
        this.size,
        this.packagingType,
        this.minBatchQty,
        this.productVdoLink,
        this.isActive,
        this.createdBy,
        this.updatedBy);
  }
  //    create or replace view product_view as
  //    select  p.ID  ,
  //            p.created_by as createdBy,
  //            p.created_on as createdOn,
  //            p.is_active as isActive,
  //            p.min_batch_qty as  minBatchQty,
  //            p.packaging_type as packagingType,
  //            p.product_desc as productDesc,
  //            p.product_mrp as productMRP,
  //            p.product_name as productName,
  //            p.product_ptr as productPTR,
  //            p.product_ptd as productPTD,
  //            p.product_vdo_link as productVdoLink,
  //            p.unit as unit,
  //            p.size as size,
  //            p.updated_by as updatedBy ,
  //            p.updated_on as updatedOn,
  //            p.product_code as productCode,
  //            pc2.ID as categoryId,
  //            pc2.category_name as categoryName,
  //            pc.ID  as subcategoryId,
  //            pc.category_name as subcategoryName ,
  //            pd.Id  as divisionId,
  //            pd.field_name as divisionName
  //            from product p
  //            inner join product_category pc on pc.ID = p.category_id
  //            left join product_category pc2 on pc.parent_id = pc2.ID
  //            inner join dropdown_mst pd on pd.Id = p.division and field_type = 'division';
}
