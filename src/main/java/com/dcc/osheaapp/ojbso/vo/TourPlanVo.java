package com.dcc.osheaapp.ojbso.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dcc.osheaapp.vo.UserDetailsVo;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "so_tour_plan")
@EntityListeners(AuditingEntityListener.class)
public class TourPlanVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "month_yr")
    private String monthYr;

    //@Column(name = "so_id")
    @OneToOne // (cascade = CascadeType.MERGE )
	@JoinColumn(name = "so_id")
    private UserDetailsVo soId;

    @Column(name = "created_on", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;
    
    @Column(name = "created_by", nullable = false, updatable = false)
	private Long createdBy;

    @Column(name = "plan_status")
    private String planStatus;				// approved/ cancelled/ rejected

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "plan_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date planUpdatedOn;
    
    @Column(name = "updated_by")
	private Long updatedBy;

    @Column(name = "tour_plan_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tourPlanStartDate;

    @Column(name = "tour_plan_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tourPlanEndDate;
    
    @Column(name = "tour_days")
    private Long tourDays;

    @Transient
    private String approvedBy;
    @Transient
    private String createdByUser;
    @Transient
    private String zone;
    
    @JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tourPlanVo")
	private List<TourPlanDtlVo> tourPlanDtlVo = new ArrayList<>();



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

	public UserDetailsVo getSoId() {
		return soId;
	}

	public void setSoId(UserDetailsVo soId) {
		this.soId = soId;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public Date getPlanUpdatedOn() {
		return planUpdatedOn;
	}

	public void setPlanUpdatedOn(Date planUpdatedOn) {
		this.planUpdatedOn = planUpdatedOn;
	}

	public Long getTourDays() {
		return tourDays;
	}

	public void setTourDays(Long tourDays) {
		this.tourDays = tourDays;
	}

	public List<TourPlanDtlVo> getTourPlanDtlVo() {
		return tourPlanDtlVo;
	}

	public void setTourPlanDtlVo(List<TourPlanDtlVo> tourPlanDtlVo) {
		this.tourPlanDtlVo = tourPlanDtlVo;
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

    @Override
    public String toString() {
        return "TourPlanVo{" +
                "id=" + id +
                ", monthYr='" + monthYr + '\'' +
                ", soId=" + soId +
                ", createdOn=" + createdOn +
                ", createdBy=" + createdBy +
                ", planStatus='" + planStatus + '\'' +
                ", remarks='" + remarks + '\'' +
                ", planUpdatedOn=" + planUpdatedOn +
                ", updatedBy=" + updatedBy +
                ", tourPlanStartDate=" + tourPlanStartDate +
                ", tourPlanEndDate=" + tourPlanEndDate +
                ", tourDays=" + tourDays +
                '}';
    }
}
