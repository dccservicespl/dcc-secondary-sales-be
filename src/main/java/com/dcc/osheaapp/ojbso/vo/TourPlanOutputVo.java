package com.dcc.osheaapp.ojbso.vo;

import com.dcc.osheaapp.vo.UserDetailsVo;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class TourPlanOutputVo {
    @Id
    private Long id;
    private String monthYr;
    private Long soId;
    private Date createdOn;
    private Long createdBy;
    private String planStatus;
    private String remarks;
    private Date planUpdatedOn;
    private Long updatedBy;
    private Date tourPlanStartDate;
    private Date tourPlanEndDate;
    private Long tourDays;
    private String approvedBy;
    private String createdByUser;
    private String zone;
    private Long zoneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonthYr() {
        return monthYr;
    }

    public void setMonthYr(String monthYr) {
        this.monthYr = monthYr;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getPlanUpdatedOn() {
        return planUpdatedOn;
    }

    public void setPlanUpdatedOn(Date planUpdatedOn) {
        this.planUpdatedOn = planUpdatedOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getTourPlanStartDate() {
        return tourPlanStartDate;
    }

    public void setTourPlanStartDate(Date tourPlanStartDate) {
        this.tourPlanStartDate = tourPlanStartDate;
    }

    public Date getTourPlanEndDate() {
        return tourPlanEndDate;
    }

    public void setTourPlanEndDate(Date tourPlanEndDate) {
        this.tourPlanEndDate = tourPlanEndDate;
    }

    public Long getTourDays() {
        return tourDays;
    }

    public void setTourDays(Long tourDays) {
        this.tourDays = tourDays;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }
}
