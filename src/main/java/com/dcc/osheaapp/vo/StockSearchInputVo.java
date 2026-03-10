package com.dcc.osheaapp.vo;

import java.util.List;

public class StockSearchInputVo {

	private List<String> activityType; // Sale / Purchase
	private String transactionType; // Auto / Self
	private Long userId; // counter id
	private Long outlet;
	private String invoiceNo;
	private Boolean isDamageReturn;
	private List<String> stockStatus;
	private String purchaseDate;
	private String fromDate;
	private String toDate;
	private Integer page;
	private Integer size;
	private String listingActivityType;
	private String callType = "web";

	public Integer getPage() {
		return page;
	}

	public List<String> getActivityType() {
		return activityType;
	}

	public void setActivityType(List<String> activityType) {
		this.activityType = activityType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOutlet() {
		return outlet;
	}

	public void setOutlet(Long outlet) {
		this.outlet = outlet;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Boolean getIsDamageReturn() {
		return isDamageReturn;
	}

	public void setIsDamageReturn(Boolean isDamageReturn) {
		this.isDamageReturn = isDamageReturn;
	}

	public List<String> getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(List<String> stockStatus) {
		this.stockStatus = stockStatus;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getListingActivityType() {
		return listingActivityType;
	}

	public void setListingActivityType(String listingActivityType) {
		this.listingActivityType = listingActivityType;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	@Override
	public String toString() {
		return "StockSearchInputVo [activityType=" + activityType + ", transactionType=" + transactionType + ", userId="
				+ userId + ", outlet=" + outlet + ", invoiceNo=" + invoiceNo + ", isDamageReturn=" + isDamageReturn
				+ ", stockStatus=" + stockStatus + ", purchaseDate=" + purchaseDate + ", fromDate=" + fromDate
				+ ", toDate=" + toDate + ", page=" + page + ", size=" + size + "]";
	}
}
