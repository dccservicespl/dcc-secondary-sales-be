package com.dcc.osheaapp.report.attendance.service;

import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.vo.views.UserView;

import java.time.LocalTime;
import java.time.Month;
import java.util.Date;

public class AttendanceDetailsDto {
    private UserView user;
    private String zone;
    private UserChainFlat userChain;
    private Month month;
    private String date;
    private String type;
    private String reason;
    private LocalTime store_check_in;
    private LocalTime store_check_out;
    private String remarks;
    private String total_time;

    public UserView getUser() {
        return user;
    }

    public AttendanceDetailsDto setUser(UserView user) {
        this.user = user;
        return this;
    }

    public Month getMonth() {
        return month;
    }

    public AttendanceDetailsDto setMonth(Month month) {
        this.month = month;
        return this;
    }

    public String getZone() {
        return zone;
    }

    public AttendanceDetailsDto setZone(String zone) {
        this.zone = zone;
        return this;
    }

    public UserChainFlat getUserChain() {
        return userChain;
    }

    public AttendanceDetailsDto setUserChain(UserChainFlat userChain) {
        this.userChain = userChain;
        return this;
    }

    public String getDate() {
        return date;
    }

    public AttendanceDetailsDto setDate(String date) {
        this.date = date;
        return this;
    }

    public String getType() {
        return type;
    }

    public AttendanceDetailsDto setType(String type) {
        this.type = type;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public AttendanceDetailsDto setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public LocalTime getStoreCheckIn() {
        return store_check_in;
    }

    public AttendanceDetailsDto setStore_check_in(LocalTime store_check_in) {
        this.store_check_in = store_check_in;
        return this;
    }

    public LocalTime getStoreCheckOut() {
        return store_check_out;
    }

    public AttendanceDetailsDto setStore_check_out(LocalTime store_check_out) {
        this.store_check_out = store_check_out;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public AttendanceDetailsDto setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public String getTotalTime() {
        return total_time;
    }

    public AttendanceDetailsDto setTotal_time(String total_time) {
        this.total_time = total_time;
        return this;
    }
}
