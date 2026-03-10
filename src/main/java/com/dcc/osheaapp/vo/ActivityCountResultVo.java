package com.dcc.osheaapp.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ActivityCountResultVo {

    @Id
    private Long id;

    private String activityType;

    private Long countActivity;

    private Long assotiatedId;
    private String activityDate;

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Long getCountActivity() {
        return countActivity;
    }

    public void setCountActivity(Long countActivity) {
        this.countActivity = countActivity;
    }

    public Long getAssotiatedId() {
        return assotiatedId;
    }

    public void setAssotiatedId(Long assotiatedId) {
        this.assotiatedId = assotiatedId;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}




