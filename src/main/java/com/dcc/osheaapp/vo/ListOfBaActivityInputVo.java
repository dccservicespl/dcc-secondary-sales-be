package com.dcc.osheaapp.vo;

import java.util.List;

public class ListOfBaActivityInputVo {

    private String activityTypes;
//    private List<String> activityTypes;
    public String currDate;

    public String getCurrDate() {
        return currDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public String getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(String activityTypes) {
        this.activityTypes = activityTypes;
    }


//    public List<String> getActivityTypes() {
//        return activityTypes;
//    }
//
//    public void setActivityTypes(List<String> activityTypes) {
//        this.activityTypes = activityTypes;
//    }
}
