package com.dcc.osheaapp.vo.views;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import com.dcc.osheaapp.report.common.model.UserChainFlat;

@Entity(name = "category_report_view")
@Immutable
public class CategoryReportView {
	@Id
	private String id;
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

	private Long baId;

	private Long bdeId;

	@Transient
	private String md;
	@Transient
	private String zsm;
	@Transient
	private UserChainFlat userChain;
	
	@Transient
	private String purchasePercentage;
	@Transient
	private String salePercentage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMd() {
		return md;
	}

	public void setMd(String md) {
		this.md = md;
	}

	public String getZsm() {
		return zsm;
	}

	public void setZsm(String zsm) {
		this.zsm = zsm;
	}

	public Long getBaId() {
		return baId;
	}

	public void setBaId(Long baId) {
		this.baId = baId;
	}

	public Long getBdeId() {
		return bdeId;
	}

	public void setBdeId(Long bdeId) {
		this.bdeId = bdeId;
	}

	public String getPurchasePercentage() {
//		DecimalFormat df = new DecimalFormat("#.##");
//		return df.format(purchasePercentage);
		return purchasePercentage;
	}

	public void setPurchasePercentage(String purchasePercentage) {
		this.purchasePercentage = purchasePercentage;
	}

	public String getSalePercentage() {
//		DecimalFormat df = new DecimalFormat("#.##");
//		return df.format(salePercentage);
		return salePercentage;
	}

	public void setSalePercentage(String salePercentage) {
		this.salePercentage = salePercentage;
	}

	public UserChainFlat getUserChain() {
		return userChain;
	}

	public void setUserChain(UserChainFlat userChain) {
		this.userChain = userChain;
	}
}
