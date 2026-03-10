package com.dcc.osheaapp.vo;

public class UserActivitySearchInputVo {

  private String activityType; // Emun
  private Long userId;
  private Long outlet; // counter id
  private String activityDate;
  private String fromDate;
  private String toDate;
  private String monYr;
  private Integer page;
  private Integer size;

  public String getActivityType() {
    return activityType;
  }

  public UserActivitySearchInputVo setActivityType(String activityType) {
    this.activityType = activityType;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserActivitySearchInputVo setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getOutlet() {
    return outlet;
  }

  public UserActivitySearchInputVo setOutlet(Long outlet) {
    this.outlet = outlet;
    return this;
  }

  public String getActivityDate() {
    return activityDate;
  }

  public UserActivitySearchInputVo setActivityDate(String activityDate) {
    this.activityDate = activityDate;
    return this;
  }

  public String getFromDate() {
    return fromDate;
  }

  public UserActivitySearchInputVo setFromDate(String fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public String getToDate() {
    return toDate;
  }

  public UserActivitySearchInputVo setToDate(String toDate) {
    this.toDate = toDate;
    return this;
  }

  public String getMonYr() {
    return monYr;
  }

  public UserActivitySearchInputVo setMonYr(String monYr) {
    this.monYr = monYr;
    return this;
  }

  public Integer getPage() {
    return page;
  }

  public UserActivitySearchInputVo setPage(Integer page) {
    this.page = page;
    return this;
  }

  public Integer getSize() {
    return size;
  }

  public UserActivitySearchInputVo setSize(Integer size) {
    this.size = size;
    return this;
  }
}
