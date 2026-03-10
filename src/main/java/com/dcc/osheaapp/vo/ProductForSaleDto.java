package com.dcc.osheaapp.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product_for_sale")
public class ProductForSaleDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productName;
	private String productCode;
	private String productDesc;
	private Long division;
	private Long categoryId;
	private Long subcategoryId;
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
	private String availableStock;
	private String categoryName;
	private String subCategoryName;
	private Date transactionDate;

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

	public String getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(String availableStock) {
		this.availableStock = availableStock;
	}

	public Long getDivision() {
		return division;
	}

	public void setDivision(Long division) {
		this.division = division;
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

	public Long getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(Long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "ProductForSaleDto [id=" + id + ", productName=" + productName + ", productCode=" + productCode
				+ ", productDesc=" + productDesc + ", division=" + division + ", categoryId=" + categoryId
				+ ", productPTR=" + productPTR + ", productPTD=" + productPTD + ", productMRP=" + productMRP + ", unit="
				+ unit + ", size=" + size + ", packagingType=" + packagingType + ", minBatchQty=" + minBatchQty
				+ ", productVdoLink=" + productVdoLink + ", isActive=" + isActive + ", createdOn=" + createdOn
				+ ", createdBy=" + createdBy + ", updatedOn=" + updatedOn + ", updatedBy=" + updatedBy
				+ ", availableStock=" + availableStock + "]";
	}
}
