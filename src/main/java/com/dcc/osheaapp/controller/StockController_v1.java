package com.dcc.osheaapp.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.service.StockService_v1;
import com.dcc.osheaapp.service.StockTransferService;
import com.dcc.osheaapp.vo.StockApprovalInputVo;
import com.dcc.osheaapp.vo.StockEntryVo;
import com.dcc.osheaapp.vo.StockSearchInputVo;
import com.dcc.osheaapp.vo.dto.PocketMISInputVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
		RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
@RestController
@RequestMapping("/stock_v1")
@Tag(description = "API related to Stock", name = "Stock")
public class StockController_v1 {

	private static final Logger LOGGER = LogManager.getLogger(StockController_v1.class);
	private StockService_v1 stockService;
	private final StockTransferService stockTransferService;

	@Autowired
	public StockController_v1(StockService_v1 stockService, StockTransferService stockTransferService) {
		this.stockService = stockService;
        this.stockTransferService = stockTransferService;
    }

	@Operation(summary = "Save a stock", description = "Save a stock")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> save(@RequestBody StockEntryVo input) {
		LOGGER.info("StockController :: save() called......");
		return this.stockService.save(input);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Save a sale/purchase entry for first time", description = "Save a sale/purchase entry for first time")
	@RequestMapping(value = "/draft", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<ApiResponse> draft(@ModelAttribute StockEntryVo input) {
		LOGGER.info("UserController :: draft() called......");
		return this.stockService.draft(input);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "List sale/purchase entry", description = "List sale/purchase entry")
	@RequestMapping(value = "/searchByInput", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> searchByInput(@RequestBody StockSearchInputVo input) {
		LOGGER.info("UserController :: searchByInput() called......");
		return this.stockService.searchByInput(input);
	}

	@Operation(summary = "Fetch sale/purchase entry by id", description = "Fetch sale/purchase entry by id")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/stock/{id}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchById(@PathVariable("id") Long id) {
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.stockService.fetchById(id)), HttpStatus.OK);
	}

	@Operation(summary = "Delete sale/purchase product item by id only in 'Draft' mode", description = "Delete sale/purchase product item by id only in 'Draft' mode. "
			+ "Request will be 'Sent for Approval' if deleted after 'Submit', this this method will not be called.")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteProduct/{id}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long id) {
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(200, "SUCCESS", "DELETE SUCCESSFULL", this.stockService.deleteItem(id)), HttpStatus.OK);
	}

	@Operation(summary = "Update status of an edit request from Admin Panel", description = "Update status of an edit request from Admin Panel")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/updatedApprovalStatus", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> updatedApprovalStatus(@RequestBody StockApprovalInputVo input) {
		return this.stockService.updatedApprovalStatus(input);
	}

	@Operation(summary = "pocket MIS", description = "pocket MIS")
	@RequestMapping(value = "/pocketMIS", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> pocketMIS(@RequestParam("outletId") Long outletId,
			@RequestParam("monthYr") String monthYr) {
		LOGGER.info("StockController :: pocketMIS() called......");
		return this.stockService.pocketMIS(outletId, monthYr);
	}

	@Operation(summary = "Get pocket MIS product details", description = "Get pocket MIS product details")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/pocketMISProductDetails", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> pocketMISProductDetails(@RequestBody PocketMISInputVo input) {
		return this.stockService.pocketMISProductDetails(input);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "List purchase invoice reference for purchase return", description = "List purchase invoice reference for purchase return")
	@RequestMapping(value = "/listInvoiceReference", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> listInvoiceReference(@RequestParam("outletId") Long outletId) {
		LOGGER.info("UserController :: listInvoiceReference() called......" + outletId);
		return this.stockService.listInvoiceReference(outletId);
	}

	@Operation(summary = "Delete stock entry by id ", description = "Delete stock entry by id. "
			+ "Request will be 'Sent for Approval' if deleted after 'Submit', this this method will not be called.")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteStock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ApiResponse> deleteStock(@PathVariable("id") Long id) {
		return this.stockService.deleteStock(id);
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/checkIfReturnable/{id}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> checkIfReturnable(@PathVariable("id") Long id) {
		return this.stockService.checkIfReturnable(id);
	}

	@Operation(summary = "Monthly outlet stock, from admin panel", description = "Monthly outlet stock, from admin panel")
	@RequestMapping(value = "/outletStock", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> outletStock(@RequestBody PocketMISInputVo input) {
		LOGGER.info("StockController :: outletStock() called......");
		return this.stockService.outletStock(input);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "getNetSaleAmount", description = "getNetSaleAmount")
	@RequestMapping(value = "/getNetSaleAmount", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> getNetSaleAmount(@RequestBody StockSearchInputVo input) {
		LOGGER.info("UserController :: getNetSaleAmount() called......");
		return this.stockService.getNetSaleAmount(input);
	}

	@Operation(summary = "Delete purchase entry within 1 month", description = "Delete purchase entry within 1 month")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deletePurchase", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> deletePurchase(@RequestBody PocketMISInputVo input) {
		return this.stockService.deletePurchase(input);
	}

	@Operation(summary = "Delete Sale entry within 1 month", description = "Delete Sale entry within 1 month")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteSale", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> deleteSale(@RequestBody PocketMISInputVo input) {
		return this.stockService.deleteSale(input);
	}

	@Operation(summary = "Validation", description = "Validation")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/validation", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> validation(@RequestBody StockEntryVo input) {
		return this.stockService.validation(input);
	}

	@Operation(summary = "Transfer Stocks", description = "Transfer stock of the previous month of all active outlets")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> transferStocks() {
		 this.stockTransferService.transferAllOutletStocksForCurrentMonth();
		 return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Stocks Transferred", null), HttpStatus.OK);
	}

	@Operation(summary = "Check stock transfer", description = "Check if the stock is transferred")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/transfer/{yearMonth}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> transfer(@PathVariable("yearMonth") YearMonth yearMonth) {
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "", stockTransferService.isStocksTransferred(yearMonth)), HttpStatus.OK);
	}
}
