package com.dcc.osheaapp.vo.views;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import com.dcc.osheaapp.report.common.model.UserChainFlat;

@Entity(name = "counter_stock_report_view")
@Immutable
public class CounterStockReportView {
	@Id
	private String id;
	private String companyZone;
	private String zone;
	private String state;
	private String beat;
	private String beatName;
	private String distributorCode;
	private String distributorName;
	private String outletId;
	private String outletErpId;
//	private String outletCode;
	private String outletName;

	private String outletCreatedOn;
	private String outletCategory;
	private String month;
	private String categoryName;
	private String subCategoryName;
	private String productId;
	private String productErp;
	private String productName;
	private String pUnit;
	private String pSize;
	private Double productMrp;
	private Long openingStock;
	private Double openingStockAmount;
	private Long purchase;
	private Double purchaseAmount;
	private Long purchaseReturn;
	private Double purchaseReturnAmount;
	private Long sale;
	private Double saleAmount;
	private Long saleReturn;
	private Double saleReturnAmount;
	private Long damage;
	private Double damageAmount;
	private Long closingStock;
	private Double closingStockAmount;
	private Long lastMonthClosingStock;
	private Long percentStockMovement;
	private Long productMovementStatus;
	private Long userId;
	private  Boolean status;//??

	@Transient
	private UserChainFlat userChain;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCompanyZone() {
		return companyZone;
	}


	public void setCompanyZone(String companyZone) {
		this.companyZone = companyZone;
	}


	public String getZone() {
		return zone;
	}


	public void setZone(String zone) {
		this.zone = zone;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getBeat() {
		return beat;
	}


	public void setBeat(String beat) {
		this.beat = beat;
	}


	public String getBeatName() {
		return beatName;
	}


	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}


	public String getDistributorCode() {
		return distributorCode;
	}


	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}


	public String getDistributorName() {
		return distributorName;
	}


	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}


	public String getOutletId() {
		return outletId;
	}


	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}


	public String getOutletErpId() {
		return outletErpId;
	}


	public void setOutletErpId(String outletErpId) {
		this.outletErpId = outletErpId;
	}


//	public String getOutletCode() {
//		return outletCode;
//	}
//
//
//	public void setOutletCode(String outletCode) {
//		this.outletCode = outletCode;
//	}


	public String getOutletName() {
		return outletName;
	}


	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}


	public String getOutletCreatedOn() {
		return outletCreatedOn;
	}


	public void setOutletCreatedOn(String outletCreatedOn) {
		this.outletCreatedOn = outletCreatedOn;
	}


	public String getOutletCategory() {
		return outletCategory;
	}


	public void setOutletCategory(String outletCategory) {
		this.outletCategory = outletCategory;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
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


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getProductErp() {
		return productErp;
	}


	public void setProductErp(String productErp) {
		this.productErp = productErp;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public Double getProductMrp() {
		return productMrp;
	}


	public void setProductMrp(Double productMrp) {
		this.productMrp = productMrp;
	}


	public Long getOpeningStock() {
		return openingStock;
	}


	public void setOpeningStock(Long openingStock) {
		this.openingStock = openingStock;
	}


	public Double getOpeningStockAmount() {
		return openingStockAmount;
	}


	public void setOpeningStockAmount(Double openingStockAmount) {
		this.openingStockAmount = openingStockAmount;
	}


	public Long getPurchase() {
		return purchase;
	}


	public void setPurchase(Long purchase) {
		this.purchase = purchase;
	}


	public Double getPurchaseAmount() {
		return purchaseAmount;
	}


	public void setPurchaseAmount(Double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}


	public Long getPurchaseReturn() {
		return purchaseReturn;
	}


	public void setPurchaseReturn(Long purchaseReturn) {
		this.purchaseReturn = purchaseReturn;
	}


	public Double getPurchaseReturnAmount() {
		return purchaseReturnAmount;
	}


	public void setPurchaseReturnAmount(Double purchaseReturnAmount) {
		this.purchaseReturnAmount = purchaseReturnAmount;
	}


	public Long getSale() {
		return sale;
	}


	public void setSale(Long sale) {
		this.sale = sale;
	}


	public Double getSaleAmount() {
		return saleAmount;
	}


	public void setSaleAmount(Double saleAmount) {
		this.saleAmount = saleAmount;
	}


	public Long getSaleReturn() {
		return saleReturn;
	}


	public void setSaleReturn(Long saleReturn) {
		this.saleReturn = saleReturn;
	}


	public Double getSaleReturnAmount() {
		return saleReturnAmount;
	}


	public void setSaleReturnAmount(Double saleReturnAmount) {
		this.saleReturnAmount = saleReturnAmount;
	}


	public Long getDamage() {
		return damage;
	}


	public void setDamage(Long damage) {
		this.damage = damage;
	}


	public Double getDamageAmount() {
		return damageAmount;
	}


	public void setDamageAmount(Double damageAmount) {
		this.damageAmount = damageAmount;
	}


	public Long getClosingStock() {
		return closingStock;
	}


	public void setClosingStock(Long closingStock) {
		this.closingStock = closingStock;
	}


	public Double getClosingStockAmount() {
		return closingStockAmount;
	}


	public void setClosingStockAmount(Double closingStockAmount) {
		this.closingStockAmount = closingStockAmount;
	}


	public Long getLastMonthClosingStock() {
		return lastMonthClosingStock;
	}


	public void setLastMonthClosingStock(Long lastMonthClosingStock) {
		this.lastMonthClosingStock = lastMonthClosingStock;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public UserChainFlat getUserChain() {
		return userChain;
	}


	public void setUserChain(UserChainFlat userChain) {
		this.userChain = userChain;
	}


	public Long getPercentStockMovement() {
		return percentStockMovement;
	}


	public void setPercentStockMovement(Long percentStockMovement) {
		this.percentStockMovement = percentStockMovement;
	}


	public Long getProductMovementStatus() {
		return productMovementStatus;
	}


	public void setProductMovementStatus(Long productMovementStatus) {
		this.productMovementStatus = productMovementStatus;
	}


	public String getpUnit() {
		return pUnit;
	}


	public void setpUnit(String pUnit) {
		this.pUnit = pUnit;
	}


	public String getpSize() {
		return pSize;
	}


	public void setpSize(String pSize) {
		this.pSize = pSize;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
