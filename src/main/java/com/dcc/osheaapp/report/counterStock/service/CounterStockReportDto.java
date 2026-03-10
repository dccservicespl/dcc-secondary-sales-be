package com.dcc.osheaapp.report.counterStock.service;

import java.math.BigDecimal;
import java.time.Month;

import com.dcc.osheaapp.report.common.model.UserChainFlat;

public class CounterStockReportDto {
    private UserChainFlat userChain;
    private Long companyZone;
	private String zone;
	private String state;
	private Long beat;
	private String beatName;
	private String distributorCode;
	private String distributorName;
	private Long outletId;
	private String outletErpId;
	private String outletCode;
	private String outletName;
	private String outletCreatedOn;
	private String outletCategory;
	private Month month;
	private String categoryName;
	private String subCategoryName;
	private Long productId;
	private String productErp;
	private String productName;
	private String productMrp;
	private BigDecimal openingStock = BigDecimal.ZERO;
	private String openingStockAmount;
	private Long purchase;
	private String purchaseAmount;
	private Long purchaseReturn;
	private String purchaseReturnAmount;
	private Long sale;
	private String saleAmount;
	private Long saleReturn;
	private String saleReturnAmount;
	private Long damage;
	private String damageAmount;
	private Long closingStock;
	private String closingStockAmount;	
	private BigDecimal lastMonthClosingStock = BigDecimal.ZERO;
	private BigDecimal percentStockMovement;
	private String productMovementStatus;
	
	private Long userId;


    public String getZone() {
        return zone;
    }

    public CounterStockReportDto setZone(String zone) {
        this.zone = zone;
        return this;
    }

    public UserChainFlat getUserChain() {
        return userChain;
    }

    public CounterStockReportDto setUserChain(UserChainFlat userChain) {
        this.userChain = userChain;
        return this;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public CounterStockReportDto setDistributorName(String distributorName) {
        this.distributorName = distributorName;
        return this;
    }

    public String getDistributorCode() {
        return distributorCode;
    }

    public CounterStockReportDto setDistributorCode(String distributorCode) {
        this.distributorCode = distributorCode;
        return this;
    }

	public Long getCompanyZone() {
		return companyZone;
	}

	public void setCompanyZone(Long companyZone) {
		this.companyZone = companyZone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public Long getOutletId() {
		return outletId;
	}

	public void setOutletId(Long outletId) {
		this.outletId = outletId;
	}

	public String getOutletErpId() {
		return outletErpId;
	}

	public void setOutletErpId(String outletErpId) {
		this.outletErpId = outletErpId;
	}

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}

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

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
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

	public String getProductMrp() {
		return productMrp;
	}

	public void setProductMrp(String productMrp) {
		this.productMrp = productMrp;
	}

	public BigDecimal getOpeningStock() {
		return openingStock;
	}

	public void setOpeningStock(BigDecimal openingStock) {
		this.openingStock = openingStock;
	}

	public String getOpeningStockAmount() {
		return openingStockAmount;
	}

	public void setOpeningStockAmount(String openingStockAmount) {
		this.openingStockAmount = openingStockAmount;
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

	public Long getPurchaseReturn() {
		return purchaseReturn;
	}

	public void setPurchaseReturn(Long purchaseReturn) {
		this.purchaseReturn = purchaseReturn;
	}

	public String getPurchaseReturnAmount() {
		return purchaseReturnAmount;
	}

	public void setPurchaseReturnAmount(String purchaseReturnAmount) {
		this.purchaseReturnAmount = purchaseReturnAmount;
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

	public Long getSaleReturn() {
		return saleReturn;
	}

	public void setSaleReturn(Long saleReturn) {
		this.saleReturn = saleReturn;
	}

	public String getSaleReturnAmount() {
		return saleReturnAmount;
	}

	public void setSaleReturnAmount(String saleReturnAmount) {
		this.saleReturnAmount = saleReturnAmount;
	}

	public Long getDamage() {
		return damage;
	}

	public void setDamage(Long damage) {
		this.damage = damage;
	}

	public String getDamageAmount() {
		return damageAmount;
	}

	public void setDamageAmount(String damageAmount) {
		this.damageAmount = damageAmount;
	}

	public Long getClosingStock() {
		return closingStock;
	}

	public void setClosingStock(Long closingStock) {
		this.closingStock = closingStock;
	}

	public String getClosingStockAmount() {
		return closingStockAmount;
	}

	public void setClosingStockAmount(String closingStockAmount) {
		this.closingStockAmount = closingStockAmount;
	}

	public BigDecimal getLastMonthClosingStock() {
		return lastMonthClosingStock;
	}

	public void setLastMonthClosingStock(BigDecimal lastMonthClosingStock) {
		this.lastMonthClosingStock = lastMonthClosingStock;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getPercentStockMovement() {
		return percentStockMovement;
	}

	public void setPercentStockMovement(BigDecimal percentStockMovement) {
		this.percentStockMovement = percentStockMovement;
	}

	public String getProductMovementStatus() {
		return productMovementStatus;
	}

	public void setProductMovementStatus(String productMovementStatus) {
		this.productMovementStatus = productMovementStatus;
	}
}
