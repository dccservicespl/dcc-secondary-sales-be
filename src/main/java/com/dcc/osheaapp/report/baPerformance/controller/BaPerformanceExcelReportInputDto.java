package com.dcc.osheaapp.report.baPerformance.controller;

import java.time.Month;

public class BaPerformanceExcelReportInputDto {
    private String monthYr;
    private Long zone;

    public Long getZone() {
        return zone;
    }

    public String getMonthYr() {
        return monthYr;
    }

    public BaPerformanceExcelReportInputDto setMonthYr(String monthYr) {
        this.monthYr = monthYr;
        return this;
    }

    public Month getMonth() {
        return Month.of(Integer.parseInt(monthYr.split("/")[0]));
    }

    public int getYear() {
        return Integer.parseInt(monthYr.split("/")[1]);
    }
}
