package com.dcc.osheaapp.report.counterStock.controller;


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
@Tag(description = "API related to Outlet/ counter stock", name = "Stock Report")
@RequestMapping( "/api/report/stock")
public class CounterStockReportController {

  @Autowired CounterStockReportReqHandler handler;

  @Operation(
      summary = "Export counter_stock_report Excel Sheet",
      description = "Export counter_stock_report in Excel Sheet ")
  @PostMapping
  public Object export(
          @RequestBody CounterStockReportInputDto dto, HttpServletRequest request,  HttpServletResponse response)
      throws IOException {
    String FILE_NAME = "counter_stock_report_" + dto.getMonth().toString() + ".xlsx";
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
    handler.export(dto, response.getOutputStream());
    return "Exported";
  }


  @Operation(
          summary = "Export counter_stock_report Excel Sheet",
          description = "Export counter_stock_report in Excel Sheet ")
  @PostMapping("/ws")
  public Object exportWS(
          @RequestBody CounterStockReportInputDto dto, HttpServletRequest request,  HttpServletResponse response)
          throws IOException {
     handler.exportReportThroughWs(dto);
    return new ResponseEntity<>(
            new com.dcc.osheaapp.common.model.ApiResponse(
                    202, "ACCEPTED", "Please wait while the report is being processed.", null, 1),
            HttpStatus.ACCEPTED);
  }
}
