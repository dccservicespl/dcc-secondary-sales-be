package com.dcc.osheaapp.report.attendance.controller;

import java.time.YearMonth;

public class AttendanceSummaryExportInputDto {
   private int year;
   private int month;
   private Long zone;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public Long getZone() {
        return zone;
    }
}
