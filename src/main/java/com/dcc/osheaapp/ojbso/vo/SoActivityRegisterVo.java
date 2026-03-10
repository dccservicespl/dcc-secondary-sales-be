package com.dcc.osheaapp.ojbso.vo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dcc.osheaapp.vo.UserDetailsVo;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "so_activity_register")
@EntityListeners(AuditingEntityListener.class)
public class SoActivityRegisterVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "activity_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date activityDate;
    
    @OneToOne 
	@JoinColumn(name = "so_id")
    private UserDetailsVo soId;

    @Column(name = "start_time_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date startTimeDate;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date endTimeDate;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "working_hour")
    private String workingHour;

    //@Column(name = "tour_plan_dtl_id", nullable = true)
    @OneToOne // (cascade = CascadeType.MERGE ) //fetch = FetchType.EAGER,
	@JoinColumn(name = "tour_plan_dtl_id", nullable = true)
    private TourPlanDtlVo tourPlanDtlId;

    //@Column(name = "daily_activity_id", nullable = true)
    @OneToOne // (cascade = CascadeType.MERGE ) //fetch = FetchType.EAGER,
	@JoinColumn(name = "daily_activity_id", nullable = true)
    private DailyActivityMstVo dailyActivityId;

    @Column(name = "order_id", nullable = true)
    private Long orderId;

    @Column(name = "order_value", nullable = true)
    private String orderValue;

    @Column(name = "system_remarks")
    private String systemRemarks;

    @Column(name = "user_remarks", nullable = true)
    private String userRemarks;

    //@Column(name = "no_order_reason_id", nullable = true)
    @OneToOne // (cascade = CascadeType.MERGE ) //fetch = FetchType.EAGER,
	@JoinColumn(name = "no_order_reason_id", nullable = true)
    private NoOrderReasonMstVo noOrderReasonId;

    @Column(name = "beat_id", nullable = true)
    private Long beatId;

    @Column(name = "outlet_id", nullable = true)
    private Long outletId;

    @Column(name = "image_path", nullable = true)
    private String imagePath;

    @Transient
    private MultipartFile image;

    @Transient
    private Long createdBy;

    @Transient
    private Long soIdtr;

    @Transient
    private Long dailyActivityIdtr;

    public SoActivityRegisterVo(){

    }
    public SoActivityRegisterVo(Long id, Date activityDate, UserDetailsVo soId, Date startTimeDate, String startTime, Date endTimeDate,
			String endTime, String workingHour, TourPlanDtlVo tourPlanDtlId, DailyActivityMstVo dailyActivityId,
			String systemRemarks, String userRemarks, Long beatId, Long outletId) {
		this.id = id;
		this.activityDate = activityDate;
		this.soId = soId;
		this.startTimeDate = startTimeDate;
		this.startTime = startTime;
		this.endTimeDate = endTimeDate;
		this.endTime = endTime;
		this.workingHour = workingHour;
		this.tourPlanDtlId = tourPlanDtlId;
		this.dailyActivityId = dailyActivityId;
		this.systemRemarks = systemRemarks;
		this.userRemarks = userRemarks;
		this.beatId = beatId;
		this.outletId = outletId;
	}

	public SoActivityRegisterVo(Long id, Date activityDate, UserDetailsVo soId, Date startTimeDate, String startTime, Date endTimeDate,
			String endTime, String workingHour, TourPlanDtlVo tourPlanDtlId, DailyActivityMstVo dailyActivityId,
			Long orderId, String orderValue, String systemRemarks, String userRemarks,
			NoOrderReasonMstVo noOrderReasonId, Long beatId, Long outletId) {
		this.id = id;
		this.activityDate = activityDate;
		this.soId = soId;
		this.startTimeDate = startTimeDate;
		this.startTime = startTime;
		this.endTimeDate = endTimeDate;
		this.endTime = endTime;
		this.workingHour = workingHour;
		this.tourPlanDtlId = tourPlanDtlId;
		this.dailyActivityId = dailyActivityId;
		this.orderId = orderId;
		this.orderValue = orderValue;
		this.systemRemarks = systemRemarks;
		this.userRemarks = userRemarks;
		this.noOrderReasonId = noOrderReasonId;
		this.beatId = beatId;
		this.outletId = outletId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDetailsVo getSoId() {
		return soId;
	}

	public void setSoId(UserDetailsVo soId) {
		this.soId = soId;
	}

	public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Date getStartTimeDate() {
        return startTimeDate;
    }

    public void setStartTimeDate(Date startTimeDate) {
        this.startTimeDate = startTimeDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Date getEndTimeDate() {
        return endTimeDate;
    }

    public void setEndTimeDate(Date endTimeDate) {
        this.endTimeDate = endTimeDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(String workingHour) {
        this.workingHour = workingHour;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }

    public String getSystemRemarks() {
        return systemRemarks;
    }

    public void setSystemRemarks(String systemRemarks) {
        this.systemRemarks = systemRemarks;
    }

    public String getUserRemarks() {
        return userRemarks;
    }

    public void setUserRemarks(String userRemarks) {
        this.userRemarks = userRemarks;
    }

    public Long getBeatId() {
        return beatId;
    }

    public void setBeatId(Long beatId) {
        this.beatId = beatId;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

	public TourPlanDtlVo getTourPlanDtlId() {
		return tourPlanDtlId;
	}

	public void setTourPlanDtlId(TourPlanDtlVo tourPlanDtlId) {
		this.tourPlanDtlId = tourPlanDtlId;
	}

	public DailyActivityMstVo getDailyActivityId() {
		return dailyActivityId;
	}

	public void setDailyActivityId(DailyActivityMstVo dailyActivityId) {
		this.dailyActivityId = dailyActivityId;
	}

	public NoOrderReasonMstVo getNoOrderReasonId() {
		return noOrderReasonId;
	}

	public void setNoOrderReasonId(NoOrderReasonMstVo noOrderReasonId) {
		this.noOrderReasonId = noOrderReasonId;
	}

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Long getSoIdtr() {
        return soIdtr;
    }

    public void setSoIdtr(Long soIdtr) {
        this.soIdtr = soIdtr;
    }

    public Long getDailyActivityIdtr() {
        return dailyActivityIdtr;
    }

    public void setDailyActivityIdtr(Long dailyActivityIdtr) {
        this.dailyActivityIdtr = dailyActivityIdtr;
    }

    @Override
	public String toString() {
		return "SoActivityRegisterVo [id=" + id + ", activityDate=" + activityDate + ", startTimeDate=" + startTimeDate
				+ ", startTime=" + startTime + ", endTimeDate=" + endTimeDate + ", endTime=" + endTime
				+ ", workingHour=" + workingHour + ", tourPlanDtlId=" + tourPlanDtlId + ", dailyActivityId="
				+ dailyActivityId + ", orderId=" + orderId + ", orderValue=" + orderValue + ", systemRemarks="
				+ systemRemarks + ", userRemarks=" + userRemarks + ", noOrderReasonId=" + noOrderReasonId + ", beatId="
				+ beatId + ", outletId=" + outletId + "]";
	}
}
