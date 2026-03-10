package com.dcc.osheaapp.report.attendance.controller;

import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.report.common.model.ReportType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SpringBootTest
class AttendanceReqHandlerTest {

    @Autowired
    AttendanceReqHandler handler;

    @Test
    void exportSummary() throws IOException {
        var dto = new ReportInputDto().setReportType(ReportType.ATTENDANCE_SUMMARY).setZone(8L).setYearMonth("2024-08");
        handler.exportSummary(dto, new ByteArrayOutputStream());

    }
}