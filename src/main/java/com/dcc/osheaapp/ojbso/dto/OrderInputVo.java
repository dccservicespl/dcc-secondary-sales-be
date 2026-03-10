package com.dcc.osheaapp.ojbso.dto;

public class OrderInputVo {

	private Long beatId;
	private Long soId;
	private Long outletId;
	private String fromDate;
	private String toDate;
	private Integer page;
	private Integer size;

	public Long getBeatId() {
		return beatId;
	}

	public void setBeatId(Long beatId) {
		this.beatId = beatId;
	}

	public Long getSoId() {
		return soId;
	}

	public void setSoId(Long soId) {
		this.soId = soId;
	}

	public Long getOutletId() {
		return outletId;
	}

	public void setOutletId(Long outletId) {
		this.outletId = outletId;
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

	@Override
	public String toString() {
		return "OrderInputVo [beatId=" + beatId + ", soId=" + soId + ", outletId=" + outletId + ", fromDate=" + fromDate
				+ ", toDate=" + toDate + ", page=" + page + ", size=" + size + "]";
	}
}
