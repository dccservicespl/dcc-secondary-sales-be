package com.dcc.osheaapp.report.categoryReport.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
		RequestMethod.GET, RequestMethod.POST })
@RestController
@Tag(description = "API related to Category Analysis of Sale/ Purchase", name = "Category Analysis Report")
@RequestMapping("/api/report/category")
public class CategoryReportController {

	@Autowired
	CategoryReportReqHandler handler;

	@Operation(summary = "Export OJB Herbals Pvt Ltd_Product Primary Category Analysis Excel Sheet", description = "Export OJB Herbals Pvt Ltd_Product Primary Category Analysis in Excel Sheet ")
	@RequestMapping(value = "/primary", method = RequestMethod.POST)
	public Object exportPrimary(@RequestBody CategoryReportInputDto dto, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String FILE_NAME = "OJB_Herbals_Pvt_Ltd_Product_Primary_Category_Analysis_" + dto.getMonth().toString()
				+ ".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
		 handler.exportPrimary(dto, response.getOutputStream());
		 return "Exported";
	}

	@Operation(summary = "Export OJB Herbals Pvt Ltd_Product Primary Category Analysis Excel Sheet", description = "Export OJB Herbals Pvt Ltd_Product Primary Category Analysis in Excel Sheet ")
	@RequestMapping(value = "/primary/ws", method = RequestMethod.POST)
	public Object exportPrimaryWS(@RequestBody CategoryReportInputDto dto
								) throws IOException {
		handler.exportPrimaryCatReportThroughWs(dto);
		return new ResponseEntity<>(
				new ApiResponse(
						202, "ACCEPTED", "Please wait while the report is being processed...", null, 1),
				HttpStatus.ACCEPTED);
	}
	@Operation(summary = "Export OJB Herbals Pvt Ltd_Product Primary Category Analysis Excel Sheet", description = "Export OJB Herbals Pvt Ltd_Product Primary Category Analysis in Excel Sheet ")
	@RequestMapping(value = "/secondary/ws", method = RequestMethod.POST)
	public Object exportSecondaryWS(@RequestBody CategoryReportInputDto dto
	) throws IOException {
		handler.exportSecondaryCatReportThroughWs(dto);
		return new ResponseEntity<>(
				new ApiResponse(
						202, "ACCEPTED", "Please wait while the report is being processed...", null, 1),
				HttpStatus.ACCEPTED);
	}
	@Operation(summary = "Export OJB Herbals Pvt Ltd_Product Primary Category Analysis Excel Sheet", description = "Export OJB Herbals Pvt Ltd_Product Primary Category Analysis in Excel Sheet ")
	@RequestMapping(value = "/secondary", method = RequestMethod.POST)
	public Object exportSecondary(@RequestBody CategoryReportInputDto dto, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String FILE_NAME = "OJB_Herbals_Pvt_Ltd_Product_Secondary_Category_Analysis_" + dto.getMonth().toString()
				+ ".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
		 handler.exportSecondary(dto, response.getOutputStream());
		 return "exported";
	}
}
