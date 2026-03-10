package com.dcc.osheaapp.report.attendance.controller;


import com.dcc.osheaapp.common.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        originPatterns = {"*"},
        allowedHeaders = "*",
        maxAge = 4800,
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@Tag(description = "API related to performance Reports", name = "Performance Report")
@RequestMapping("/api/report/attendance")
public class AttendanceReportController {

    @Autowired
    AttendanceReqHandler handler;

    @Operation(
            summary = "Export BA Attendance Excel Sheet",
            description = "Export BA attendance in Excel Sheet ")
    @PostMapping
    public Object export(
            @RequestBody AttendanceSummaryExportInputDto dto, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String FILE_NAME = "ba_attendance_" + dto.getMonth() + "-" + dto.getYear() + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
        return handler.exportDetails(dto, response.getOutputStream());
    }

    @Operation(
            summary = "Export BA Attendance Excel Sheet",
            description = "Export BA attendance in Excel Sheet ")
    @PostMapping("/ws")
    public Object exportDetailsWS(
            @RequestBody AttendanceSummaryExportInputDto dto,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        handler.exportDetailsWS(dto);
        return new ResponseEntity<>(
                new com.dcc.osheaapp.common.model.ApiResponse(
                        202, "ACCEPTED", "Please wait while the report is being processed.", null, 1),
                HttpStatus.ACCEPTED);
    }

//    @Operation(
//            summary = "Export BA Attendance Excel Sheet",
//            description = "Export BA attendance in Excel Sheet ")
//    @PostMapping("/summary")
//    public Object exportSummary(
//            @RequestBody AttendanceSummaryExportInputDto dto, HttpServletRequest request, HttpServletResponse response)
//            throws IOException {
//        String FILE_NAME = "ba_attendance_summary_" + dto.getMonth() + "-" + dto.getYear() + ".xlsx";
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
//        return handler.exportSummary(dto, response.getOutputStream());
//    }



}
