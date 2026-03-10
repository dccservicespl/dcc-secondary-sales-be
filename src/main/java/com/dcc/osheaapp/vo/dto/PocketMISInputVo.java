package com.dcc.osheaapp.vo.dto;

import java.util.List;

public class PocketMISInputVo {

	private Long outletId;
	private List<Long> outlets;
	private String categoryName;
	private String subCategoryName;
	private String monthYr;
	private Long stockId;
	private List<String> stockStatus;

	public List<Long> getOutlets() {
		return outlets;
	}

	public PocketMISInputVo setOutlets(List<Long> outlets) {
		this.outlets = outlets;
		return this;
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

	public String getMonthYr() {
		return monthYr;
	}

	public void setMonthYr(String monthYr) {
		this.monthYr = monthYr;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public List<String> getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(List<String> stockStatus) {
		this.stockStatus = stockStatus;
	}

	@Override
	public String toString() {
		return "PocketMISInputVo [outletId=" + outletId + ", categoryName=" + categoryName + ", subCategoryName="
				+ subCategoryName + "]";
	}
}
