package com.dcc.osheaapp.report.distributor;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.repository.IDistributorRepository;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.service.upload.DistributorExportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST })

@RestController
@Tag(description = "API relate to distributor", name = "Distributor Report")
@RequestMapping("/api/report/distributor")
public class DistributorReportController {
    @Autowired
    IDropdownMastereRepository dropdownMastereRepository;

    @Autowired
    IDistributorRepository iDistributorRepository;

    @Autowired
    DistributorExportService distributorExportService;


    @Autowired
    DistributorReportExportReqHandler handler;

    @Operation(summary = "Export distributor Excel Sheet", description = "Export distribution data in excel sheet")
    @PostMapping("/ws")
    public Object exportWS(
            @RequestBody DistributorExcelReportInputDto dto)
            throws IOException {
        handler.exportReportThroughWs(dto);
        return new ResponseEntity<>(
            new com.dcc.osheaapp.common.model.ApiResponse(
                202, "ACCEPTED", "Please wait while the report is being processed.", null, 1),
        HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Export Distributor Excel sheet ",
    description = "Export distributor data in  excel sheet")
    @PostMapping()
    public Object export( @RequestBody DistributorExcelReportInputDto dto, HttpServletResponse response  )
    throws IOException{
    
        String fileName = "Distributor" + dropdownMastereRepository.getName(dto.getZoneId().toString()) + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        distributorExportService.export(dto.getZoneId(), response.getOutputStream());
        return "exported";
    }

}

// @RestController
// @Tag(description = "API relate to distributor", name = "Distributor Report")
// @RequestMapping("/api/report/distributor")
// public class DistributorReportController {
//     @Autowired
//     IDropdownMastereRepository dropdownMastereRepository;

//     @Autowired
//     IDistributorRepository iDistributorRepository;

//     @Autowired
//     DistributorExportService distributorExportService;

//     @Autowired
//     DistributorReportExportReqHandler handler;

//     @Operation(summary = "Export distributor Excel Sheet", description = "Export distribution data in excel sheet")
//     @PostMapping("/ws")
//     public ResponseEntity<ApiResponse> exportWS(
//             @RequestBody DistributorExcelReportInputDto dto)
//             throws IOException {
//         handler.exportReportThroughWs(dto);
//         return ResponseEntity.ok(new com.dcc.osheaapp.common.model.ApiResponse(
//                 202, "ACCEPTED", "Please wait while the report is being processed.", null, 1));
//     }

//     @Operation(summary = "Export Distributor Excel sheet ",
//     description = "Export distributor data in  excel sheet")
//     @PostMapping()
//     public ResponseEntity<byte[]> export(
//             @RequestBody DistributorExcelReportInputDto dto, HttpServletResponse response)
//             throws IOException {

//         String fileName = "Distributor" + dropdownMastereRepository.getName(dto.getZoneId().toString()) + ".xlsx";
//         response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//         response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

//         ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
//         distributorExportService.export(dto.getZoneId(), excelStream);
//         byte[] excelBytes = excelStream.toByteArray();

//         return ResponseEntity.ok()
//                 .contentLength(excelBytes.length)
//                 .body(excelBytes);
//     }
// }

