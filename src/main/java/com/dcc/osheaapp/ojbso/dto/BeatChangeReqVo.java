package com.dcc.osheaapp.ojbso.dto;


import com.dcc.osheaapp.ojbso.vo.TourPlanVo;
import com.dcc.osheaapp.vo.BeatName;
import com.dcc.osheaapp.vo.UserDetailsVo;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.management.ConstructorParameters;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="beat_change_request")
@EntityListeners(AuditingEntityListener.class)
public class BeatChangeReqVo {
    private static  final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name="created_on",nullable = false, updatable = false)
    private Date createdOn;
    @Column(name="updated_on")
    private Date updatedOn;
    @Column(name= "created_by",nullable = false, updatable = false)
    private Long createdBy;
    @Column(name="updated_by")
    private Long updatedBy;
    @OneToOne
    @JoinColumn(name="tourPlan_id")
    private TourPlanVo tourPlanId;
    @Column(name = "plan_date")
    private Date tourPlanDate;
    @Column(name="remarks")
    private String remarks;

    @OneToOne
    @JoinColumn(name="last_beat")
    private BeatName lastBeat;
    @OneToOne
    @JoinColumn(name="update_beat")
    private BeatName updatedBeat;

    @Column(name="status")
    private String status;
    @Column(name = "so_id")
    private Long soId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public TourPlanVo getTourPlanId() {
        return tourPlanId;
    }

    public void setTourPlanId(TourPlanVo tourPlanId) {
        this.tourPlanId = tourPlanId;
    }

    public Date getTourPlanDate() {
        return tourPlanDate;
    }

    public void setTourPlanDate(Date tourPlanDate) {
        this.tourPlanDate = tourPlanDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BeatName getLastBeat() {
        return lastBeat;
    }

    public void setLastBeat(BeatName lastBeat) {
        this.lastBeat = lastBeat;
    }

    public BeatName getUpdatedBeat() {
        return updatedBeat;
    }

    public void setUpdatedBeat(BeatName updatedBeat) {
        this.updatedBeat = updatedBeat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }



    @Override
    public String toString() {
        return "BeatChangeReqVo{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", tourPlanId=" + tourPlanId +
                ", tourPlanDate=" + tourPlanDate +
                ", remarks='" + remarks + '\'' +
                ", lastBeat=" + lastBeat +
                ", updatedBeat=" + updatedBeat +
                ", status='" + status + '\'' +
                ", soId=" + soId + '\'' +
                '}';
    }
}
