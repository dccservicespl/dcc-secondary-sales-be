package com.dcc.osheaapp.ojbso.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dcc.osheaapp.vo.UserDetailsVo;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "so_tour_plan_dtl")
@EntityListeners(AuditingEntityListener.class)
public class TourPlanDtlVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tour_plan_id", nullable = false)
	@JsonBackReference
	private TourPlanVo tourPlanVo;

	@OneToOne // (cascade = CascadeType.MERGE ) //fetch = FetchType.EAGER,
	@JoinColumn(name = "tour_plan_activity_mst_id")
	private TourPlanActivityMstVo tourPlanActivityMstId;
	
//	@OneToOne // (cascade = CascadeType.MERGE ) //fetch = FetchType.EAGER,
//	@JoinColumn(name = "beat_id", nullable = true)
//	private BeatName beatId;
//
//	@OneToOne
//	@JoinColumn(name = "beat_type_id",nullable = true)
//	private DropdownMasterVo beatTypeId;
	
	@OneToOne
	@JoinColumn(name = "beat_mapping_id", nullable = true)
	private SoBeatNTypeMappingVo soBeatTypeMapping;
	
	@Column(name = "plan_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @CreatedDate
    private Date planDate;
	
	@OneToOne // (cascade = CascadeType.MERGE )
	@JoinColumn(name = "join_work_user_id")
    private UserDetailsVo joinWorkUserId;
	
	@Column(name = "user_reason")
	private String userReason;
	
	@Column(name = "plan_details")
	private String planDetails;
	@Column(name = "update_details")
	private String updateDetails;
	@OneToOne
	@JoinColumn(name = "update_beat_mapping_id", nullable = true)
	private SoBeatNTypeMappingVo updateBeatTypeMapping;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TourPlanVo getTourPlanVo() {
		return tourPlanVo;
	}

	public void setTourPlanVo(TourPlanVo tourPlanVo) {
		this.tourPlanVo = tourPlanVo;
	}

	public TourPlanActivityMstVo getTourPlanActivityMstId() {
		return tourPlanActivityMstId;
	}

	public void setTourPlanActivityMstId(TourPlanActivityMstVo tourPlanActivityMstId) {
		this.tourPlanActivityMstId = tourPlanActivityMstId;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public UserDetailsVo getJoinWorkUserId() {
		return joinWorkUserId;
	}

	public void setJoinWorkUserId(UserDetailsVo joinWorkUserId) {
		this.joinWorkUserId = joinWorkUserId;
	}

	public String getUserReason() {
		return userReason;
	}

	public void setUserReason(String userReason) {
		this.userReason = userReason;
	}

	public String getPlanDetails() {
		return planDetails;
	}

	public void setPlanDetails(String planDetails) {
		this.planDetails = planDetails;
	}

	public SoBeatNTypeMappingVo getSoBeatTypeMapping() {
		return soBeatTypeMapping;
	}

	public void setSoBeatTypeMapping(SoBeatNTypeMappingVo soBeatTypeMapping) {
		this.soBeatTypeMapping = soBeatTypeMapping;
	}

	public String getUpdateDetails() {
		return updateDetails;
	}

	public void setUpdateDetails(String updateDetails) {
		this.updateDetails = updateDetails;
	}

	public SoBeatNTypeMappingVo getUpdateBeatTypeMapping() {
		return updateBeatTypeMapping;
	}

	public void setUpdateBEatTypeMapping(SoBeatNTypeMappingVo updateBeatTypeMapping) {
		this.updateBeatTypeMapping = updateBeatTypeMapping;
	}

	@Override
	public String toString() {
		return "TourPlanDtlVo{" +
				"id=" + id +
				", tourPlanVo=" + tourPlanVo +
				", tourPlanActivityMstId=" + tourPlanActivityMstId +
				", soBeatTypeMapping=" + soBeatTypeMapping +
				", planDate=" + planDate +
				", joinWorkUserId=" + joinWorkUserId +
				", userReason='" + userReason + '\'' +
				", planDetails='" + planDetails + '\'' +
				", updateDetails='" + updateDetails + '\'' +
				", updateBeatTypeMapping=" + updateBeatTypeMapping +
				'}';
	}
}
