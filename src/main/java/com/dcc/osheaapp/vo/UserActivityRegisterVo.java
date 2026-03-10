package com.dcc.osheaapp.vo;

import com.dcc.osheaapp.common.model.Constants.BA_Activity_Enum;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "user_activity_register")
@EntityListeners(AuditingEntityListener.class)
public class UserActivityRegisterVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "activity_type") // from BA_Activity_Enum
	@Enumerated(EnumType.STRING)
	private BA_Activity_Enum activityType;

	@Column(name = "activity_time", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date activityTime;

	@Column(name = "selfie_image_path")
	private String selfieImagePath;

	@OneToOne // (cascade = CascadeType.ALL )
	@JoinColumn(name = "outlet_id")
	private OutletVo outlet;

	@Column(name = "sale_purchase_amount")
	private String salePurchaseAmount;

	@Column(name = "created_by", updatable = false)
	private Long createdBy;

	@Column(name = "leave_type")
	private String leaveType;

	@Column(name = "leave_reason")
	private String leaveReason;

	@Column(name = "working_hours")
	private Long workingHours;

	@Column(name = "stock_reference_id")
	private Long stockReferenceId;

	@Transient
	private MultipartFile selfie;

	@Transient
	private String userActivityType;

	@Transient
	private Double lat;

	@Transient
	private Double lon;

	public UserActivityRegisterVo() {
		super();
	}

	public UserActivityRegisterVo(BA_Activity_Enum activityType, Date activityTime, OutletVo outlet,
			String salePurchaseAmount, Long createdBy) {
		this.activityType = activityType;
		this.activityTime = activityTime;
		this.outlet = outlet;
		this.salePurchaseAmount = salePurchaseAmount;
		this.createdBy = createdBy;
	}

	public Double getLat() {
		return lat;
	}

	public UserActivityRegisterVo setLat(Double lat) {
		this.lat = lat;
		return this;
	}

	public Double getLon() {
		return lon;
	}

	public UserActivityRegisterVo setLon(Double lon) {
		this.lon = lon;
		return this;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BA_Activity_Enum getActivityType() {
		return activityType;
	}

	public void setActivityType(BA_Activity_Enum activityType) {
		this.activityType = activityType;
	}

	public Date getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(Date activityTime) {
		this.activityTime = new Date();
	}

	public String getSelfieImagePath() {
		return selfieImagePath;
	}

	public void setSelfieImagePath(String selfieImagePath) {
		this.selfieImagePath = selfieImagePath;
	}

	public OutletVo getOutlet() {
		return outlet;
	}

	public void setOutlet(OutletVo outlet) {
		this.outlet = outlet;
	}

	public String getSalePurchaseAmount() {
		return salePurchaseAmount;
	}

	public void setSalePurchaseAmount(String salePurchaseAmount) {
		this.salePurchaseAmount = salePurchaseAmount;
	}

	public MultipartFile getSelfie() {
		return selfie;
	}

	public void setSelfie(MultipartFile selfie) {
		this.selfie = selfie;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public Long getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(Long workingHours) {
		this.workingHours = workingHours;
	}

	public Long getStockReferenceId() {
		return stockReferenceId;
	}

	public void setStockReferenceId(Long stockReferenceId) {
		this.stockReferenceId = stockReferenceId;
	}

	public String getUserActivityType() {
		return userActivityType;
	}

	public void setUserActivityType(String userActivityType) {
		this.userActivityType = userActivityType;
	}

	@Override
	public String toString() {
		return "UserActivityRegisterVo [id=" + id + ", activityType=" + activityType + ", activityTime=" + activityTime
				+ ", selfieImagePath=" + selfieImagePath + ", outlet=" + outlet + ", salePurchaseAmount="
				+ salePurchaseAmount + ", createdBy=" + createdBy + ", leaveType=" + leaveType + ", leaveReason="
				+ leaveReason + ", workingHours=" + workingHours + ", stockReferenceId=" + stockReferenceId
				+ ", userActivityType=" + userActivityType + ", lat=" + lat + ", lon=" + lon + "]";
	}
}
