package com.dcc.osheaapp.report.common.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.model.ReportRegistry;
import com.dcc.osheaapp.report.common.model.ReportType;
import com.dcc.osheaapp.report.counterStock.controller.CounterStockReportInputDto;
import com.dcc.osheaapp.report.purchaseSale.controller.PurchaseRecordExcelExportInputDto;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@CrossOrigin(originPatterns = {"*"}, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
		RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/api/v2/report")
@Tag(description = "API related to reports", name = "Report")
public class ReportBaseController {

	@Autowired
	private BaseReportHandler handler;

	@Operation(summary = "Export ", description = "Export report as Excel")
	@PostMapping
	public ResponseEntity<ApiResponse> export(
			@RequestBody ReportInputDto dto) throws IOException {
		String id = UUID.randomUUID().toString();
		handler.generate(id, dto);
		return new ResponseEntity<>(
				new ApiResponse(200, "IN_PROGRESS", "Generation in progress", null),
				HttpStatus.OK);
	}
	
	@Operation(summary = "Export Sale Purchase ", description = "Export Sale Purchase ")
	@PostMapping("/salePurchase")
	public ResponseEntity<ApiResponse> exportSalePurchase(
			@RequestBody PurchaseRecordExcelExportInputDto dto) throws IOException {
		String id = UUID.randomUUID().toString();
		handler.generateSalePurchase(id, dto);
		return new ResponseEntity<>(
				new ApiResponse(200, "IN_PROGRESS", "Generation in progress", null),
				HttpStatus.OK);
	}


	@Operation(summary = "Search ", description = "Search report registry")
	@PostMapping("/search")
	public ResponseEntity<ApiResponse> search(
											  @RequestBody ReportRegistry dto) throws IOException {
		return handler.search(dto);
	}

	@Operation(summary = "Search ", description = "Search report registry")
	@PostMapping("/search/{id}")
	public ResponseEntity<ApiResponse> search(
			@PathVariable String id) throws IOException {
		return new ResponseEntity<>(
				new ApiResponse(200, "SUCCESS", "Generation in progress", null),
				HttpStatus.OK);
	}

}
