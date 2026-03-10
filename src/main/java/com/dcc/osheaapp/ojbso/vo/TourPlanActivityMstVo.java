package com.dcc.osheaapp.ojbso.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "so_tour_plan_activity_mst")
@EntityListeners(AuditingEntityListener.class)
public class TourPlanActivityMstVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "tour_plan_activity_name")
	private String tourPlanActivityName;

	@Column(name = "is_active")
	private Boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTourPlanActivityName() {
		return tourPlanActivityName;
	}

	public void setTourPlanActivityName(String tourPlanActivityName) {
		this.tourPlanActivityName = tourPlanActivityName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "TourPlanActivityMstVo [id=" + id + ", tourPlanActivityName=" + tourPlanActivityName + ", isActive="
				+ isActive + "]";
	}
}
