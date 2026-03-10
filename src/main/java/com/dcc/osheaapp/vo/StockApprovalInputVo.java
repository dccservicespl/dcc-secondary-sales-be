package com.dcc.osheaapp.vo;

public class StockApprovalInputVo {

	private StockEntryVo stockEntryVo;
	private String approvalStatus;
	private String remarks;
	private String userType;
	private Long outletId;
	private String stockStatus;

	public StockEntryVo getStockEntryVo() {
		return stockEntryVo;
	}

	public void setStockEntryVo(StockEntryVo stockEntryVo) {
		this.stockEntryVo = stockEntryVo;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getOutletId() {
		return outletId;
	}

	public void setOutletId(Long outletId) {
		this.outletId = outletId;
	}

	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

	@Override
	public String toString() {
		return "StockApprovalInputVo [stockEntryVo=" + stockEntryVo + ", approvalStatus=" + approvalStatus
				+ ", remarks=" + remarks + ", userType=" + userType + "]";
	}
}
