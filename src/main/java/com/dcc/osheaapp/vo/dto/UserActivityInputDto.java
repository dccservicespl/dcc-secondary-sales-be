package com.dcc.osheaapp.vo.dto;

import java.util.List;

public class UserActivityInputDto {
  public Long id;
  //	public String activityType;
  public String fromDate;
  public String toDate;
  public List<Long> userID;
  public List<Long> outletID;
  public List<String> activityType;
  private Integer page;
  private Integer size;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public List<Long> getOutletID() {
    return outletID;
  }

  public void setOutletID(List<Long> outletID) {
    this.outletID = outletID;
  }

  public List<String> getActivityType() {
    return activityType;
  }

  public void setActivityType(List<String> activityType) {
    this.activityType = activityType;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  @Override
  public String toString() {
    return "UserActivityInputDto [id="
        + id
        + ", fromDate="
        + fromDate
        + ", toDate="
        + toDate
        + ", userID="
        + userID
        + ", outletID="
        + outletID
        + ", activityType="
        + activityType
        + ", page="
        + page
        + ", size="
        + size
        + "]";
  }

  public List<Long> getUserID() {
    return userID;
  }

  public void setUserID(List<Long> userID) {
    this.userID = userID;
  }
}
