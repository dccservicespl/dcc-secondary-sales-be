package com.dcc.osheaapp.report.baRank.controller;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.report.outlet.controller.OutletReportExportReqHandler;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.service.upload.UserUploadService;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Access;
import javax.persistence.SecondaryTable;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BaRankReportReqHandler  {

private static final Logger LOGGER = LogManager.getLogger(BaRankReportReqHandler.class);
    @Autowired
    ReportWSHandler wsHandler;

    @Autowired
    UserUploadService exportService;

    @Autowired
    IDropdownMastereRepository dropdownMastereRepository;

    public OutputStream export(ReportInputDto dto, OutputStream os) {
       return exportService.exportBARank(dto.getYearMonth(), dto.getZone(), os);
    }


    public String getBaRankReportFilename(ReportInputDto dto) {
        DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(8L, "zone").orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
        return "ba_rank_" + dto.getYearMonth() + "_" + dropdownMaster.getFieldName() + ".xlsx";
    }
    public void exportReportThroughWs(BaRankReportInputDto dto){

        CompletableFuture.supplyAsync(
                () -> {
                    String fileName = "BA_Rank_" + dto.getYearMonth()+dto.getZoneId()+ ".xlsx";
                    LOGGER.info("Generating Report:: " + fileName);
                    return new ReportWSPayload(
                            fileName,
                            ((ByteArrayOutputStream)
                            exportService.exportBARank(dto.getYearMonth(),dto.getZoneId(), new ByteArrayOutputStream()))
                                    .toByteArray());
                })
                .thenAccept(wsHandler::sendReportReadyNotification)
                .exceptionally(
                        (e) -> {
                            e.printStackTrace();
                            wsHandler.sendReportErrNotification(
                                    new ApiResponse(
                                            500,"INTERNAL_SERVER_ERROR","Error while generating report", null,0));
                            return  null;
                        });

    }

}
