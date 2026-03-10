package com.dcc.osheaapp.report.attendance.controller;

public class AttendanceExportInputDto {
    private String fromDate;
    private String toDate;

    private Long zone;

    public Long getZone() {
        return zone;
    }

    public String getFromDate() {
        return fromDate;
    }

    public AttendanceExportInputDto setFromDate(String fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public String getToDate() {
        return toDate;
    }

    public AttendanceExportInputDto setToDate(String toDate) {
        this.toDate = toDate;
        return this;
    }
}
