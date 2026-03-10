package com.dcc.osheaapp.report.purchaseSale.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.repository.BaPerformanceViewRepo;
import com.dcc.osheaapp.repository.IUserChainRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/report/purchaseSale")
@Tag(description = "API related to Reports", name = "Report")
public class PurchaseSaleController {

  @Autowired
  PurchaseSaleReqHandler purchaseSaleReqHandler;
  
  @Autowired
  BaPerformanceViewRepo performanceViewRepo;
  
  @Autowired
  IUserChainRepository userChainRepository;

  @Operation(
      summary = "Export Products Excel Sheet",
      description = "Export Products in Excel Sheet")
  @PostMapping
  public Object exportPurchases(
      @RequestBody PurchaseRecordExcelExportInputDto dto,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    String FILE_NAME = "Purchase_Record.xlsx";
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    this.purchaseSaleReqHandler.exportPurchases(
        dto,
        response.getOutputStream());
    return new ResponseEntity<ApiResponse>(new ApiResponse(200, "SUCCESS", "Successfully generated", null), HttpStatus.OK);
  }

  @Operation(
          summary = "Export Products Excel Sheet",
          description = "Export Products in Excel Sheet")
  @PostMapping("/ws")
  public Object exportPurchasesWS(
          @RequestBody PurchaseRecordExcelExportInputDto dto,
          HttpServletRequest request,
          HttpServletResponse response)
          throws IOException {
    this.purchaseSaleReqHandler.exportReportThroughWs(dto);
    return new ResponseEntity<>(
            new com.dcc.osheaapp.common.model.ApiResponse(
                    202, "ACCEPTED", "Please wait while the report is being processed.", null, 1),
            HttpStatus.ACCEPTED);
  }
}
