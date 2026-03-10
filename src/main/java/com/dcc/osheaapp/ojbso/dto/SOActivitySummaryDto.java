package com.dcc.osheaapp.ojbso.dto;

import java.util.List;

//@Entity
public class SOActivitySummaryDto {
	
//	@Id
//    private Long id;
    private Long SC;
    private Integer TC;
    private Integer PC;
    private Integer OVC;
    private Double orderNetValue;    
    private Double orderNetQty;
    private List<SOActivitySummaryDtlDto> primaryCat;
    private List<SOActivitySummaryDtlDto> secondaryCat;
    private List<SOActivitySummaryDtlDto> sku;
	public Long getSC() {
		return SC;
	}
	public void setSC(Long sC) {
		SC = sC;
	}
	public Integer getTC() {
		return TC;
	}
	public void setTC(Integer tC) {
		TC = tC;
	}
	public Integer getPC() {
		return PC;
	}
	public void setPC(Integer pC) {
		PC = pC;
	}
	public Integer getOVC() {
		return OVC;
	}
	public void setOVC(Integer oVC) {
		OVC = oVC;
	}
	public Double getOrderNetValue() {
		return orderNetValue;
	}
	public void setOrderNetValue(Double orderNetValue) {
		this.orderNetValue = orderNetValue;
	}
	public Double getOrderNetQty() {
		return orderNetQty;
	}
	public void setOrderNetQty(Double orderNetQty) {
		this.orderNetQty = orderNetQty;
	}
	public List<SOActivitySummaryDtlDto> getPrimaryCat() {
		return primaryCat;
	}
	public List<SOActivitySummaryDtlDto> getSecondaryCat() {
		return secondaryCat;
	}
	public void setSecondaryCat(List<SOActivitySummaryDtlDto> secondaryCat) {
		this.secondaryCat = secondaryCat;
	}
	public List<SOActivitySummaryDtlDto> getSku() {
		return sku;
	}
	public void setSku(List<SOActivitySummaryDtlDto> sku) {
		this.sku = sku;
	}
	public void setPrimaryCat(List<SOActivitySummaryDtlDto> primaryCat) {
		this.primaryCat = primaryCat;
	}
	@Override
	public String toString() {
		return "SOActivitySummaryDto [SC=" + SC + ", TC=" + TC + ", PC=" + PC + ", OVC=" + OVC + ", orderNetValue="
				+ orderNetValue + ", orderNetQty=" + orderNetQty + ", primaryCat=" + primaryCat + ", secondaryCat="
				+ secondaryCat + ", sku=" + sku + "]";
	}
}
