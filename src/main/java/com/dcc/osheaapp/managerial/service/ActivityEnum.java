package com.dcc.osheaapp.managerial.service;

import com.dcc.osheaapp.common.model.Constants;

import java.util.List;

public enum ActivityEnum {
    PRESENT(List.of(Constants.BA_Activity_Enum.store_login, Constants.BA_Activity_Enum.attendance,
	    Constants.BA_Activity_Enum.office_work)),
    LEAVE(List.of(Constants.BA_Activity_Enum.leave, Constants.BA_Activity_Enum.comp_off,
	    Constants.BA_Activity_Enum.holiday, Constants.BA_Activity_Enum.week_off)),
    ABSENT(List.of()),
    VACANT(List.of());




//    VACANT(List.of(Constants.Vacant_Outlet_Enum.left_on));

    private final List<Constants.BA_Activity_Enum> activities;


    ActivityEnum(List<Constants.BA_Activity_Enum> activityEnums) {
	this.activities = activityEnums;
    }

    public List<Constants.BA_Activity_Enum> getActivities() {
	return activities;
    }
}
