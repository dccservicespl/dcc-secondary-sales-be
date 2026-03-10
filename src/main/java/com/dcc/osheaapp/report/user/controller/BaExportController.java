package com.dcc.osheaapp.report.user.controller;

import com.dcc.osheaapp.report.counterStock.controller.CounterStockReportInputDto;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.service.upload.UserUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(
        originPatterns = {"*"},
        allowedHeaders = "*",
        maxAge = 4800,
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@Tag(description = "API related to Outlet/ counter stock", name = "Stock Report")
@RequestMapping( "/api/report/users")
public class BaExportController {


    @Autowired
    IDropdownMastereRepository dropdownMastereRepository;
    @Autowired
    UserExportReqHandler handler;

    @Autowired
    UserUploadService exportService;

    @Operation(
            summary = "Export users Excel Sheet",
            description = "Export users data in excel sheet ")
    @PostMapping("/ws")
    public Object exportWS(
            @RequestBody UserExcelReportInputDto dto)
            throws IOException {
        handler.exportReportThroughWs(dto);
        return new ResponseEntity<>(
                new com.dcc.osheaapp.common.model.ApiResponse(
                        202, "ACCEPTED", "Please wait while the report is being processed.", null, 1),
                HttpStatus.ACCEPTED);
    }

    @Operation(
            summary = "Export users Excel Sheet",
            description = "Export users data in excel sheet ")
    @PostMapping()
    public Object export(
            @RequestBody UserExcelReportInputDto dto, HttpServletResponse response)
            throws IOException {
        String fileName = "users_" + dropdownMastereRepository.getName(dto.getZoneId().toString())  + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        exportService.export(dto.getZoneId(), response.getOutputStream());
        return "exported";

    }
}
