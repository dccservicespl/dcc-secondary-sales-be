package com.dcc.osheaapp.ojbso.vo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.swing.text.StyledEditorKit;
import java.util.Date;

@Entity
@Table(name = "tour_plan_days")
@EntityListeners(AuditingEntityListener.class)
public class TourPlanDaysMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name="no_of_days")
    private Long noOfDays;
    @Column(name="description")
    private String description;
    @Column(name="created_on",nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;
    @Column(name="created_by",nullable = false,updatable = false)
    private Long createdBy;

    @Column(name = "updated_on",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date updatedOn;
    @Column(name="updated_by",nullable = false,updatable = false)
    private Long updatedBy;

    @Column(name="is_active")
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Long noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString(){
        return "TourPlanDaysMst{" +
                "id=" + id +
                ", noOfDays='" + noOfDays + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy=" + createdBy +
                ", description='" + description + '\'' +
                ", updatedOn=" + updatedOn +
                ", updatedBy=" + updatedBy +
                ", isActive=" + isActive +
                '}';

    }
}
