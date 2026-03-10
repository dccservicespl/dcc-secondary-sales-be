package com.dcc.osheaapp.report.common.model;

import com.dcc.osheaapp.report.common.model.ReportType;
import org.springframework.context.ApplicationEvent;

import java.io.OutputStream;

public class ReportGeneratedEvent extends ApplicationEvent {
    private final OutputStream os;
    private final String fName;
    private final ReportType reportType;
    public ReportGeneratedEvent(Object source, OutputStream os, String fName, ReportType reportType) {
        super(source);
        this.os = os;
        this.fName = fName;
        this.reportType = reportType;
    }

    public OutputStream getOutputStream() {
        return os;
    }

    public String getfName() {
        return fName;
    }

    public ReportType getReportType() {
        return reportType;
    }
}
