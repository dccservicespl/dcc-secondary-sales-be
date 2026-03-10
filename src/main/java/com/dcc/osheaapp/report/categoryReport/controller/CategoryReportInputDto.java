package com.dcc.osheaapp.report.categoryReport.controller;

import java.time.Month;

public class CategoryReportInputDto {
    private String monthYr;
    private String companyZone;

    public String getMonthYr() {
        return monthYr;
    }

    public CategoryReportInputDto setMonthYr(String monthYr) {
        this.monthYr = monthYr;
        return this;
    }

    public Month getMonth() {
        return Month.of(Integer.parseInt(monthYr.split("-")[1]));
    }

    public int getYear() {
        return Integer.parseInt(monthYr.split("-")[0]);
    }

	public String getCompanyZone() {
		return companyZone;
	}

	public void setCompanyZone(String companyZone) {
		this.companyZone = companyZone;
	}
}
