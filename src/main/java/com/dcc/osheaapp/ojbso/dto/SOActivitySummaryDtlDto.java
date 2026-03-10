package com.dcc.osheaapp.ojbso.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SOActivitySummaryDtlDto {
	
	@Id
    private Long id;
    private String desciption;
    private Double productMRP;   
    private Double orderNetValue;    
    private Double orderNetQty;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDesciption() {
		return desciption;
	}
	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}
	public Double getProductMRP() {
		return productMRP;
	}
	public void setProductMRP(Double productMRP) {
		this.productMRP = productMRP;
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
}
