package com.dcc.osheaapp.report.common.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.model.ReportRegistry;
import com.dcc.osheaapp.report.common.model.ReportType;
import com.dcc.osheaapp.report.common.service.GenerationService;
import com.dcc.osheaapp.report.common.service.ReportRegistryService;
import com.dcc.osheaapp.report.purchaseSale.controller.PurchaseRecordExcelExportInputDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class BaseReportHandler {

    @Autowired
    GenerationService generationService;

    @Autowired
    ReportRegistryService reportRegistryService;


    public void generate(String id, ReportInputDto dto) {
       CompletableFuture.supplyAsync(() -> generationService.generate(id, dto) );
    }
    
    public void generateSalePurchase(String id, PurchaseRecordExcelExportInputDto dto) {
        CompletableFuture.supplyAsync(() -> generationService.generateSalePurchase(id, dto) );
     }

    public ResponseEntity<ApiResponse> search(ReportRegistry dto) {
        return reportRegistryService.search(dto);
    }

    public ReportRegistry searchById(String id) {
        return reportRegistryService.searchById(id);
    }
}
