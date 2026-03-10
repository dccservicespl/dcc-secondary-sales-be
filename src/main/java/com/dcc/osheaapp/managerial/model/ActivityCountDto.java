package com.dcc.osheaapp.managerial.model;

import com.dcc.osheaapp.managerial.service.ActivityEnum;

public class ActivityCountDto {
    private ActivityEnum activity;
    private Long count;

    public ActivityCountDto(ActivityEnum activity, Long count) {
        this.activity = activity;
        this.count = count;
    }

    public ActivityEnum getActivity() {
        return activity;
    }

    public ActivityCountDto setActivity(ActivityEnum activity) {
        this.activity = activity;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public ActivityCountDto setCount(Long count) {
        this.count = count;
        return this;
    }
}
