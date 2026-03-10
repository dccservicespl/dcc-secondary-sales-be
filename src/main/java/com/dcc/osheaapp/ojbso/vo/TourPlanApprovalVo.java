package com.dcc.osheaapp.ojbso.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.dcc.osheaapp.vo.StockEntryVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tour_plan_approval")
@EntityListeners(AuditingEntityListener.class)
public class TourPlanApprovalVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_plan_id") // Tour plan entry id
    private TourPlanVo tourPlanVo;

    @Column(name = "approval_status") // approved/ sent for approval/ rejected
    private String approvalStatus;

    @Column(name = "remarks") // remarks while status update
    private String remarks;

    @Column(name = "created_on", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Column(name = "updated_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedOn;

    @Column(name = "updated_by")
    private Long updatedBy;

    @OneToOne // (cascade = CascadeType.ALL )
    @JoinColumn(name = "assign_to") // Admin id + SO id
    private UserDetailsVo assignTo;

    @Transient
    private String planStatus;

    @Transient
    private String userType;

    public TourPlanApprovalVo(){

    }
    public TourPlanApprovalVo(
            TourPlanVo tourPlanVo,
            String approvalStatus,
            Date createdOn,
            Long createdBy,
            Date updatedOn,
            Long updatedBy,
            UserDetailsVo assignTo) {
        this.tourPlanVo = tourPlanVo;
        this.approvalStatus = approvalStatus;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
        this.assignTo = assignTo;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public UserDetailsVo getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(UserDetailsVo assignTo) {
        this.assignTo = assignTo;
    }

    public Date getCreatedOn() {
        return createdOn;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



    public TourPlanVo getTourPlanVo() {
        return tourPlanVo;
    }

    public void setTourPlanVo(TourPlanVo tourPlanVo) {
        this.tourPlanVo = tourPlanVo;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "TourPlanApprovalVo{" +
                "id=" + id +
                ", tourPlanVo=" + tourPlanVo +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy=" + createdBy +
                ", updatedOn=" + updatedOn +
                ", updatedBy=" + updatedBy +
                ", assignTo=" + assignTo +
                '}';
    }
}

