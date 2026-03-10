package com.dcc.osheaapp.ojbso.vo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TimeLineInputVo {

    @Id
    private Long soId;
    private String activityDate;

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    @Override
    public String toString() {
        return "TimeLineInputVo{" +
                "soId=" + soId +
                ", activityDate='" + activityDate + '\'' +
                '}';
    }
}
