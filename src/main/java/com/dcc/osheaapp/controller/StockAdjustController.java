package com.dcc.osheaapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.service.StockAdjustmentService;
import com.dcc.osheaapp.vo.dto.PocketMISInputVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
		RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
@RestController
@RequestMapping("/stockAdjust")
@Tag(description = "API related to Stock", name = "Stock")
public class StockAdjustController {

	private static final Logger LOGGER = LogManager.getLogger(StockAdjustController.class);
	private StockAdjustmentService stockAdjustmentService;

	@Autowired
	public StockAdjustController(StockAdjustmentService stockAdjustmentService) {
		this.stockAdjustmentService = stockAdjustmentService;
    }

//	@Operation(summary = "pocket MIS", description = "pocket MIS")
//	@RequestMapping(value = "/pocketMIS", method = RequestMethod.GET)
//	public ResponseEntity<ApiResponse> pocketMIS(@RequestParam("outletId") Long outletId,
//			@RequestParam("monthYr") String monthYr) {
//		LOGGER.info("StockController :: pocketMIS() called......");
//		return this.stockService.pocketMIS(outletId, monthYr);
//	}

	@Operation(summary = "Get pocket MIS product details", description = "Get pocket MIS product details")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/adjust", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> callForAdjustment(@RequestBody PocketMISInputVo input) {
		return this.stockAdjustmentService.callForAdjustment(input);
	}
}
