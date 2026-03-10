package com.dcc.osheaapp.vo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ListOfBaActivityOutputVo {

    @Id
    private Long id;
    private String  fullName;
    private String baCode;
    private boolean isActive;
    private String activityType;
    private String  outletCode;

    private Long bdeId;
    private String activityDate;

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public Long getBdeId() {
        return bdeId;
    }

    public void setBdeId(Long bdeId) {
        this.bdeId = bdeId;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }
}
