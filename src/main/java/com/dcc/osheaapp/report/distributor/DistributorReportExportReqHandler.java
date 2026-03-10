package com.dcc.osheaapp.report.distributor;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.service.upload.OutletUploadService;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.ByteArrayAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.outlet.controller.OutletReportExportReqHandler;
import com.dcc.osheaapp.repository.IDistributorRepository;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.service.upload.DistributorExportService;

import io.swagger.v3.oas.annotations.servers.Server;

@Service
public class DistributorReportExportReqHandler {

    private static final Logger LOGGER = LogManager.getLogger(DistributorReportExportReqHandler.class);


    @Autowired
    IDistributorRepository iDistributorRepository;

    @Autowired
    DistributorExportService distributorExportService;

    @Autowired
    ReportWSHandler wsHandler;

        @Autowired
    IDropdownMastereRepository dropdownMastereRepository;


        public OutputStream export(ReportInputDto dto, OutputStream os) {
            return distributorExportService.export(dto.getZone(), os);
        }

    public String getDistributorReportFilename(ReportInputDto dto) {
        DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(dto.getZone(), "zone").orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
        return "distributor_" + dropdownMaster.getFieldName() + ".xlsx";
    }
    public void exportReportThroughWs(DistributorExcelReportInputDto dto) {
        CompletableFuture.supplyAsync(
                () -> {
                        LOGGER.info("DropdownMaster getName ------>"+dto.getZoneId());
                        String fileName = "Distributor_" + dropdownMastereRepository.getName(dto.getZoneId().toString()) + ".xlsx";
                    LOGGER.info("Generating Report :: " + fileName);
                    return new ReportWSPayload(
                            fileName,
                            ((ByteArrayOutputStream) distributorExportService.export(dto.getZoneId(),
                                    new ByteArrayOutputStream()))
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
