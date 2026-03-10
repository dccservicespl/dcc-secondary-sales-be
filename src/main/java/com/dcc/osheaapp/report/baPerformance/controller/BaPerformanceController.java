package com.dcc.osheaapp.report.baPerformance.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dcc.osheaapp.common.model.ApiResponse;

@CrossOrigin(
    originPatterns = {"*"},
    allowedHeaders = "*",
    maxAge = 4800,
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@Tag(description = "API related to performance Reports", name = "Performance Report")
@RequestMapping( "/api/report/performance")
public class BaPerformanceController {

  @Autowired PerformanceReqHandler performanceReqHandler;

  @Operation(
      summary = "Export BA Performance Excel Sheet",
      description = "Export BA performance in Excel Sheet ")
  @PostMapping
  public Object export(
          @RequestBody BaPerformanceExcelReportInputDto dto, HttpServletRequest request,  HttpServletResponse response)
      throws IOException {
    String FILE_NAME = "performance_report_" + dto.getMonth().toString() + ".xlsx";
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
     performanceReqHandler.export(dto, response.getOutputStream());
     return "exported";
  }

  @Operation(
      summary = "Export BA Performance Excel Sheet",
      description = "Export BA performance in Excel Sheet ")
  @PostMapping("/ws")
  public Object exportWS(
      @RequestBody BaPerformanceExcelReportInputDto dto,
      HttpServletResponse response)
      throws IOException {
    performanceReqHandler.exportReportThroughWs(dto);
    return new ResponseEntity<>(
            new com.dcc.osheaapp.common.model.ApiResponse(
                    202, "ACCEPTED", "Please wait while the report is being processed.", null, 1),
            HttpStatus.ACCEPTED);
    }


  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Fetch Top Performing BA", description = "Fetch Top Performing BA")
  @RequestMapping(value = "/fetchTopPerformingBA", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> fetchTopPerformingBA(@RequestParam("monthYr") String monthYr) throws IOException {
//    LOGGER.info("BaPerformanceController :: fetchTopPerformingBA() called......");
    return this.performanceReqHandler.topPerformingBA(monthYr);
  }
  
}
