package com.dcc.osheaapp.vo;

import java.util.List;

public class UserActivityOPVo {

  private Long totalStoreVisit;
  private Long totalLeave;
  private Long totalWeekOff;
  private Long totalCompOff;
  private Long totalOfficeWork;
  private Long totalHoliday;
  private List<UserActivityListVo> activitySummary;

  public Long getTotalStoreVisit() {
    return totalStoreVisit;
  }

  public void setTotalStoreVisit(Long totalStoreVisit) {
    this.totalStoreVisit = totalStoreVisit;
  }

  public Long getTotalLeave() {
    return totalLeave;
  }

  public void setTotalLeave(Long totalLeave) {
    this.totalLeave = totalLeave;
  }

  public Long getTotalWeekOff() {
    return totalWeekOff;
  }

  public void setTotalWeekOff(Long totalWeekOff) {
    this.totalWeekOff = totalWeekOff;
  }

  public Long getTotalCompOff() {
    return totalCompOff;
  }

  public void setTotalCompOff(Long totalCompOff) {
    this.totalCompOff = totalCompOff;
  }

  public Long getTotalOfficeWork() {
    return totalOfficeWork;
  }

  public void setTotalOfficeWork(Long totalOfficeWork) {
    this.totalOfficeWork = totalOfficeWork;
  }

  public Long getTotalHoliday() {
    return totalHoliday;
  }

  public void setTotalHoliday(Long totalHoliday) {
    this.totalHoliday = totalHoliday;
  }

  public List<UserActivityListVo> getActivitySummary() {
    return activitySummary;
  }

  public void setActivitySummary(List<UserActivityListVo> activitySummary) {
    this.activitySummary = activitySummary;
  }
}
