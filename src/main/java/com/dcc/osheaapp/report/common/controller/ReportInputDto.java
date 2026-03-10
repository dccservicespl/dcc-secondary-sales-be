package com.dcc.osheaapp.report.common.controller;

import com.dcc.osheaapp.report.common.model.ReportType;

import java.time.YearMonth;

public class ReportInputDto {
    private ReportType reportType;
    private String yearMonth;
    private String salePurchaseType;

    private Long zone;
    private Boolean status;

    public ReportType getReportType() {
        return reportType;
    }

    public ReportInputDto setReportType(ReportType reportType) {
        this.reportType = reportType;
        return this;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public ReportInputDto setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
        return this;
    }

    public Long getZone() {
        return zone;
    }

    public ReportInputDto setZone(Long zone) {
        this.zone = zone;
        return this;
    }

	public String getSalePurchaseType() {
		return salePurchaseType;
	}

	public void setSalePurchaseType(String salePurchaseType) {
		this.salePurchaseType = salePurchaseType;
	}

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
