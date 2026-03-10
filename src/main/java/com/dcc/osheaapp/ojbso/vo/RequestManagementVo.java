package com.dcc.osheaapp.ojbso.vo;

import com.dcc.osheaapp.vo.BeatName;
import com.dcc.osheaapp.vo.UserDetailsVo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="so_request_management")
@EntityListeners(AuditingEntityListener.class)
public class RequestManagementVo implements Serializable {

    private static  final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID",unique = true,nullable = false)
    private Long id;


    @OneToOne
    @JoinColumn(name="original_tour_plan_id")
    private TourPlanDtlVo originalTourPlanId;


    @OneToOne
    @JoinColumn(name="plan_beat_id")
    private  BeatName planBeatId;


    @OneToOne
    @JoinColumn(name="plan_activity_id")
    private TourPlanActivityMstVo planActivityId;


    @Column(name="actual_beat_id")
    private Long actualBeatId;

    @OneToOne
    @JoinColumn(name="actual_activity_id")
    private DailyActivityMstVo actualActivityId;

    @Column(name="user_remarks",length = 100)
    private String userRemarks;
    @Column(name="system_remarks",length = 100)
    private String systemRemarks;

    @Column(name="approval_status")
    private String approvalStatus;


    @OneToOne
    @JoinColumn(name = "so_id")
    private UserDetailsVo soId;

    @Column(name = "created_on", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "updated_by")
    private Long updatedBy;

    @OneToOne // (cascade = CascadeType.ALL )
    @JoinColumn(name = "assign_to") // Admin id + SO id
    private UserDetailsVo assignTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TourPlanDtlVo getOriginalTourPlanId() {
        return originalTourPlanId;
    }

    public void setOriginalTourPlanId(TourPlanDtlVo originalTourPlanId) {
        this.originalTourPlanId = originalTourPlanId;
    }

    public String getUserRemarks() {
        return userRemarks;
    }

    public void setUserRemarks(String userRemarks) {
        this.userRemarks = userRemarks;
    }

    public String getSystemRemarks() {
        return systemRemarks;
    }

    public void setSystemRemarks(String systemRemarks) {
        this.systemRemarks = systemRemarks;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public UserDetailsVo getSoId() {
        return soId;
    }

    public void setSoId(UserDetailsVo soId) {
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

    public UserDetailsVo getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(UserDetailsVo assignTo) {
        this.assignTo = assignTo;
    }

    public BeatName getPlanBeatId() {
        return planBeatId;
    }

    public void setPlanBeatId(BeatName planBeatId) {
        this.planBeatId = planBeatId;
    }

    public TourPlanActivityMstVo getPlanActivityId() {
        return planActivityId;
    }

    public void setPlanActivityId(TourPlanActivityMstVo planActivityId) {
        this.planActivityId = planActivityId;
    }

    public Long getActualBeatId() {
        return actualBeatId;
    }

    public void setActualBeatId(Long actualBeatId) {
        this.actualBeatId = actualBeatId;
    }

    public DailyActivityMstVo getActualActivityId() {
        return actualActivityId;
    }

    public void setActualActivityId(DailyActivityMstVo actualActivityId) {
        this.actualActivityId = actualActivityId;
    }


}
