package com.dcc.osheaapp.report.attendance.model;

import com.dcc.osheaapp.common.model.Constants;

import java.time.LocalDate;

public class ActivityModel {
    Constants.BA_Activity_Enum activityType;
    LocalDate activityTime;

    public Constants.BA_Activity_Enum getActivityType() {
        return activityType;
    }

    public ActivityModel setActivityType(Constants.BA_Activity_Enum activityType) {
        this.activityType = activityType;
        return this;
    }

    public LocalDate getActivityTime() {
        return activityTime;
    }

    public ActivityModel setActivityTime(LocalDate activityTime) {
        this.activityTime = activityTime;
        return this;
    }
}
