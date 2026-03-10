package com.dcc.osheaapp.report.outlet.controller;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.service.upload.OutletUploadService;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutletReportExportReqHandler {

    private static final Logger LOGGER = LogManager.getLogger(OutletReportExportReqHandler.class);

    @Autowired
    OutletUploadService exportService;

    @Autowired
    ReportWSHandler wsHandler;

    @Autowired
    IDropdownMastereRepository dropdownMastereRepository;

    public OutputStream export(ReportInputDto dto, OutputStream os) {
        return exportService.export(dto.getZone(), os);
    }
    public String getOutletReportFilename(ReportInputDto dto) {
        DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(dto.getZone(), "zone").orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
        return "outlet_" + dropdownMaster.getFieldName() + ".xlsx";
    }

  public void exportReportThroughWs(OutletExcelReportInputDto dto) {
    CompletableFuture.supplyAsync(
            () -> {
              String fileName = "outlets_" + dropdownMastereRepository.getName(dto.getZoneId().toString()) + ".xlsx";
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
