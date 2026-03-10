package com.dcc.osheaapp.report.user.controller;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.categoryReport.controller.CategoryReportReqHandler;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.report.counterStock.controller.CounterStockReportInputDto;
import com.dcc.osheaapp.report.outlet.controller.OutletExcelReportInputDto;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.service.upload.UserActivityUploadService;
import com.dcc.osheaapp.service.upload.UserUploadService;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.result.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

@Service
public class UserExportReqHandler {

  private static final Logger LOGGER = LogManager.getLogger(UserExportReqHandler.class);
  @Autowired UserUploadService exportService;

  @Autowired ReportWSHandler wsHandler;

  @Autowired IDropdownMastereRepository dropdownMastereRepository;


    public String getUserReportFilename(ReportInputDto dto) {
        DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(8L, "zone").orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
        return "user_" + dropdownMaster.getFieldName() + ".xlsx";
    }
  public OutputStream export(ReportInputDto dto, OutputStream os) {
      return exportService.export(dto.getZone(), os);
  }
  public void exportReportThroughWs(UserExcelReportInputDto dto) {

    CompletableFuture.supplyAsync(
            () -> {
              String fileName =
                  "users_"
                      + dropdownMastereRepository.getName(dto.getZoneId().toString())
                      + ".xlsx";
              LOGGER.info("Generating Report:: " + fileName);
              return new ReportWSPayload(
                  fileName,
                  ((ByteArrayOutputStream)
                          exportService.export(dto.getZoneId(), new ByteArrayOutputStream()))
                      .toByteArray());
            })
        .thenAccept(wsHandler::sendReportReadyNotification)
        .exceptionally(
            (e) -> {
              e.printStackTrace();
              wsHandler.sendReportErrNotification(
                  new ApiResponse(
                      500, "INTERNAL_SERVER_ERROR", "Error while generating report", null, 0));
              return null;
            });
  }
    }
