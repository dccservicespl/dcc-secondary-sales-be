package com.dcc.osheaapp.report.baRank.controller;

import com.dcc.osheaapp.report.outlet.controller.OutletExcelReportInputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.tool.schema.internal.StandardAuxiliaryDatabaseObjectExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import java.io.IOException;

@CrossOrigin(
        originPatterns = {"*"},
        allowedHeaders = "*",
        maxAge = 4800,
        allowCredentials = "true",
        methods = {RequestMethod.GET,RequestMethod.POST})

@RestController
@Tag(description = "API relate to Ba rank ", name="BARank Report")
@RequestMapping("/api/report/userRank")

public class BaRankReportController {

    @Autowired
    BaRankReportReqHandler handler;

    @Operation(
            summary = "Export BA Rank Excel Sheet",
            description = "Export BA data in excel sheet")
    @PostMapping("/ws")
    public Object exportWS(
            @RequestBody BaRankReportInputDto dto)
        throws IOException{
        handler.exportReportThroughWs(dto);
        return new ResponseEntity<>(
                new com.dcc.osheaapp.common.model.ApiResponse(
                        202,"ACCEPTED","please wait while the report is being processed.",null,1),
                HttpStatus.ACCEPTED);
    }
}
