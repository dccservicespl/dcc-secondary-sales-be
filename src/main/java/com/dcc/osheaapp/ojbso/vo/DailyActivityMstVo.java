package com.dcc.osheaapp.ojbso.vo;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "so_daily_activity_mst")
@EntityListeners(AuditingEntityListener.class)
public class DailyActivityMstVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "daily_activity", nullable = false, length = 100)
	private String dailyActivity;
    
    @Column(name = "daily_activity_sub", nullable = false, length = 100)
	private String dailyActivitySub;

	@Column(name = "is_active")
	private Boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDailyActivity() {
		return dailyActivity;
	}

	public void setDailyActivity(String dailyActivity) {
		this.dailyActivity = dailyActivity;
	}

	public String getDailyActivitySub() {
		return dailyActivitySub;
	}

	public void setDailyActivitySub(String dailyActivitySub) {
		this.dailyActivitySub = dailyActivitySub;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "DailyActivityMstVo [id=" + id + ", dailyActivity=" + dailyActivity + ", isActive=" + isActive + "]";
	}
}
