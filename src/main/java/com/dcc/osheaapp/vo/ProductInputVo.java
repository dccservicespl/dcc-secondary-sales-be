package com.dcc.osheaapp.vo;

public class ProductInputVo {

	private Long id;
	private Long category;
	private String productName;
	private String productCode;
	private Boolean status;
	private Long outletId;
	private Long stockId;
	private Long baId;
	private Long divisionId;
	private Integer page;
	private Integer size;
	private String monYr;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getPage() {
		return page;
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

	public Long getOutletId() {
		return outletId;
	}

	public void setOutletId(Long outletId) {
		this.outletId = outletId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Long getBaId() {
		return baId;
	}

	public void setBaId(Long baId) {
		this.baId = baId;
	}

	public Long getDivisionId() {
		return this.divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getMonYr() {
		return monYr;
	}

	public void setMonYr(String monYr) {
		this.monYr = monYr;
	}
}
