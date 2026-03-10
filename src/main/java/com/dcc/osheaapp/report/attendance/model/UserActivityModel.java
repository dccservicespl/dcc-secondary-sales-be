package com.dcc.osheaapp.report.attendance.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dcc.osheaapp.report.common.model.UserChainFlat;

/*
 Class for user activity report generation
 */
public class UserActivityModel {
	Long userId;
	String fullName;
	String userCode;
	LocalDate joiningDate;
	LocalDate releaseDate;
	Boolean status;
	OutletMappingView outlet;

	Long zoneId;
	String zone;
	Long userTypeId;
	String userType;

	String contactNo;
	String createdOn;

	Map<String, List<ActivityModel>> activityMap;
	UserChainFlat userChain;

	public Long getUserId() {
		return userId;
	}

	public UserActivityModel setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public UserActivityModel setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public UserActivityModel setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
		return this;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public UserActivityModel setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
		return this;
	}

	public Boolean getStatus() {
		return status;
	}

	public UserActivityModel setStatus(Boolean status) {
		this.status = status;
		return this;
	}

	public OutletMappingView getOutlet() {
		return outlet;
	}

	public UserActivityModel setOutlet(OutletMappingView outlet) {
		this.outlet = outlet;
		return this;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public UserActivityModel setZoneId(Long zoneId) {
		this.zoneId = zoneId;
		return this;
	}

	public String getZone() {
		return zone;
	}

	public UserActivityModel setZone(String zone) {
		this.zone = zone;
		return this;
	}

	public Long getUserTypeId() {
		return userTypeId;
	}

	public UserActivityModel setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
		return this;
	}

	public String getUserType() {
		return userType;
	}

	public UserActivityModel setUserType(String userType) {
		this.userType = userType;
		return this;
	}

	public String getContactNo() {
		return contactNo;
	}

	public UserActivityModel setContactNo(String contactNo) {
		this.contactNo = contactNo;
		return this;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public UserActivityModel setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public Map<String, List<ActivityModel>> getActivityMap() {
		return activityMap;
	}

	public UserActivityModel setActivityMap(Map<String, List<ActivityModel>> activityMap) {
		this.activityMap = activityMap;
		return this;
	}

	public UserChainFlat getUserChain() {
		return userChain;
	}

	public UserActivityModel setUserChain(UserChainFlat userChain) {
		this.userChain = userChain;
		return this;
	}

	public String getUserCode() {
		return userCode;
	}

	public UserActivityModel setUserCode(String userCode) {
		if (this.userType.equals("BA"))
			this.userCode = userCode.split("@")[0];
		else
			this.userCode = userCode;
		return this;
	}
}
