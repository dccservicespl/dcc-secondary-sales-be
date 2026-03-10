package com.dcc.osheaapp.ojbso.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OutletlistDto {
	
	@Id
    private Long id;
    private String outletName;
    private String outletCode;
    private Long beat;
    private Long companyZone;
    private Long productDivision;
    
    private String address;
    private String city;
    private Long pin;
    private String state;
    
    private String latitude;
    private String longitude;
    
    private String currentStatus;
    private Date currentDate = new Date();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	public String getOutletCode() {
		return outletCode;
	}
	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}
	public Long getBeat() {
		return beat;
	}
	public void setBeat(Long beat) {
		this.beat = beat;
	}
	public Long getCompanyZone() {
		return companyZone;
	}
	public void setCompanyZone(Long companyZone) {
		this.companyZone = companyZone;
	}
	public Long getProductDivision() {
		return productDivision;
	}
	public void setProductDivision(Long productDivision) {
		this.productDivision = productDivision;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getPin() {
		return pin;
	}
	public void setPin(Long pin) {
		this.pin = pin;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
    
    

}
