package com.dcc.osheaapp.report.baPerformance.controller;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TopBAPerformanceDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leaderboard_id;
    private Long leaderboard_rank;
    private String ba_code;
    private String ba_name;
    private String zone_name;
    private String total_sale;
    
	
	public Long getLeaderboard_id() {
		return leaderboard_id;
	}
	public void setLeaderboard_id(Long leaderboard_id) {
		this.leaderboard_id = leaderboard_id;
	}
	public Long getLeaderboard_rank() {
		return leaderboard_rank;
	}
	public void setLeaderboard_rank(Long leaderboard_rank) {
		this.leaderboard_rank = leaderboard_rank;
	}
	public String getBa_code() {
		return ba_code;
	}
	public void setBa_code(String ba_code) {
		this.ba_code = ba_code;
	}
	public String getBa_name() {
		return ba_name;
	}
	public void setBa_name(String ba_name) {
		this.ba_name = ba_name;
	}
	public String getZone_name() {
		return zone_name;
	}
	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}
	public String getTotal_sale() {
		return total_sale;
	}
	public void setTotal_sale(String total_sale) {
		this.total_sale = total_sale;
	}
	
	public TopBAPerformanceDto() {
		super();
	}
	public TopBAPerformanceDto(Long leaderboard_id, Long leaderboard_rank, String ba_code, String ba_name, String zone_name,
			String total_sale) {
		super();
		this.leaderboard_id = leaderboard_id;
		this.leaderboard_rank = leaderboard_rank;
		this.ba_code = ba_code;
		this.ba_name = ba_name;
		this.zone_name = zone_name;
		this.total_sale = total_sale;
	}
	
	@Override
	public String toString() {
		return "TopBAPerformanceDto [leaderboard_id=" + leaderboard_id + ", leaderboard_rank=" + leaderboard_rank + ", ba_code=" + ba_code
				+ ", ba_name=" + ba_name + ", zone_name=" + zone_name + ", total_sale=" + total_sale + "]";
	}
}
