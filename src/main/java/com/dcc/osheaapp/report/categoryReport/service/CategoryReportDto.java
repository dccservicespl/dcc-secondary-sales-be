package com.dcc.osheaapp.report.categoryReport.service;

import com.dcc.osheaapp.report.common.model.UserChainFlat;

public class CategoryReportDto {
    private UserChainFlat userChain;
    private Long companyZone;
    private String companyZoneName;
	private Long outletId;
	private String categoryName;
	private String subCategoryName;
	private Long purchase;
	private String purchaseAmount;
	private Long sale;
	private String saleAmount;
	private String monYr;	
	private Long createdBy;
	public UserChainFlat getUserChain() {
		return userChain;
	}
	public void setUserChain(UserChainFlat userChain) {
		this.userChain = userChain;
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
	public Long getOutletId() {
		return outletId;
	}
	public void setOutletId(Long outletId) {
		this.outletId = outletId;
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
	public Long getPurchase() {
		return purchase;
	}
	public void setPurchase(Long purchase) {
		this.purchase = purchase;
	}
	public String getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(String purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public Long getSale() {
		return sale;
	}
	public void setSale(Long sale) {
		this.sale = sale;
	}
	public String getSaleAmount() {
		return saleAmount;
	}
	public void setSaleAmount(String saleAmount) {
		this.saleAmount = saleAmount;
	}
	public String getMonYr() {
		return monYr;
	}
	public void setMonYr(String monYr) {
		this.monYr = monYr;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
}
