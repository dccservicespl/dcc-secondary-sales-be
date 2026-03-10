package com.dcc.osheaapp.vo.views;

import com.dcc.osheaapp.vo.UserActivityRegisterVo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BaListOfABdeOutputVo {

    @Id
    private Long id;
    
    private Long actual_id;
    private String fullName;
    private String baCode;
    private Long userType;
    private Long assotiatedUserId;

    private boolean isActive;
    private Long outletId;
    private String outletCode;
    private String outletName;

    private String dateOfJoining;
    private String releaseDate;
    private String leftOn;

    @Transient
    private List<UserActivityRegisterVo> activities = new ArrayList<>();


    public List<UserActivityRegisterVo> getActivities() {
        return activities;
    }

    public BaListOfABdeOutputVo setActivities(List<UserActivityRegisterVo> activities) {
        this.activities = activities;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBaCode() {
        return baCode;
    }

    public void setBaCode(String baCode) {
        this.baCode = baCode;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public Long getAssotiatedUserId() {
        return assotiatedUserId;
    }

    public void setAssotiatedUserId(Long assotiatedUserId) {
        this.assotiatedUserId = assotiatedUserId;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        isActive = isActive;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLeftOn() {
        return leftOn;
    }

    public void setLeftOn(String leftOn) {
        this.leftOn = leftOn;
    }

	public Long getActual_id() {
		return actual_id;
	}

	public void setActual_id(Long actual_id) {
		this.actual_id = actual_id;
	}
    
    
}
