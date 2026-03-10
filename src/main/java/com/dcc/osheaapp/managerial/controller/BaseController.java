package com.dcc.osheaapp.managerial.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.managerial.service.ActivityEnum;
import com.dcc.osheaapp.managerial.service.BaUnderBDEService;
import com.dcc.osheaapp.managerial.service.SaleAndPurchaseService;
import com.dcc.osheaapp.service.UserService;
import com.dcc.osheaapp.vo.BaListOfABdeVo;
import com.dcc.osheaapp.vo.SumOfPurchaseAndSaleInputVo;
import com.dcc.osheaapp.vo.views.PurchaseSaleInputVo;
import com.dcc.osheaapp.vo.views.PurchaseSaleOutputVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
	RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
@RestController
@RequestMapping("/api/managerial")
@Tag(description = "API related to Managerial App", name = "Managerial")
public class BaseController {
    private static final Logger LOGGER = LogManager.getLogger(BaseController.class);

    private final SaleAndPurchaseService saleAndPurchaseService;



    public BaseController(SaleAndPurchaseService saleAndPurchaseService){
        this.saleAndPurchaseService = saleAndPurchaseService;
    }

    @Autowired
    BaseHandler handler;

    @Autowired
    BaUnderBDEService baUnderBDEService;


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Ba List Under Bde", description = "Ba Details List Under Bde")
    @RequestMapping(value = "/baListUnderBde", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> listOfBaUnderBde(@RequestBody BaListOfABdeVo input,
	    @RequestParam(value = "activity", required = false) ActivityEnum activity) {
	return new ResponseEntity<>(
		new ApiResponse(200, "SUCCESS", "Fetched Successfully", this.handler.getBaList(input, activity)),
		HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Ba List Under Bde", description = "Ba Details List Under Bde")
    @RequestMapping(value = "/countBaListUnderBde", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> countListOfBaUnderBde(@RequestBody BaListOfABdeVo input,
	    @RequestParam(value = "activity", required = false) ActivityEnum activity) {
	return new ResponseEntity<>(
		new ApiResponse(200, "SUCCESS", "Fetched Successfully", this.handler.countBaList(input, activity)),
		HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Total Sum of Purchase and Sale of  Bde",
            description = "Total Sum of Purchase and Sale of Bde")
    @RequestMapping(value = "/totalSumOfPurchaseAndSale", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> totalSumOfSaleAndPurchase(@RequestBody SumOfPurchaseAndSaleInputVo input) {
        LOGGER.info("UserController :: totalSumOfSaleAndPurchase called ......... ");
        return this.saleAndPurchaseService.totalSumOfSaleAndPurchase(input);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Edit Request Count", description = "Edit Request Count")
    @RequestMapping(value = "/editReqCount/{baId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> editReqCount(@PathVariable("baId") Long id) {
        return this.saleAndPurchaseService.fetchEditRequestCount(id);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Edit Request Count of BA", description = "Edit Request Count of BA")
    @RequestMapping(value = "/editReqCountOfBaUnderBde/{bdeId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> editReqCountOfBas(@PathVariable("bdeId") Long bdeId) {
        return this.baUnderBDEService.editReqCountOfBaUnderBde(bdeId);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Edit Request List of BA", description = "Edit Request list of BA")
    @RequestMapping(value = "/editReqListOfBaUnderBde/{bdeId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> editReqListOfBaUnderBde(@PathVariable("bdeId") Long bdeId) {
        return this.baUnderBDEService.editReqListOfBaUnderBde(bdeId);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "All Edit Request List of BA", description = "Edit Request list of BA")
    @RequestMapping(value = "/fetchEditRequestListOfBa/{baId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchEditRequestListOfBa(@PathVariable("baId") Long baId) {
        return this.baUnderBDEService.fetchEditRequestListOfBa(baId);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Purchase and Sale on specific date", description = "Purchase and Sale on specific date")
    @RequestMapping(value = "/baPurchaseAndSale", method = RequestMethod.POST)

    public ResponseEntity<ApiResponse> purchaseSale (@RequestBody PurchaseSaleInputVo input){
        LOGGER.info("userController :: purchaseSale() :: .....");
      return this.saleAndPurchaseService.saleAndPurchase(input);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "BDE Target&Achievement", description = "Fetch BDE Target & Achievement")
    @RequestMapping(value = "/bdeTargetAchievement/{id}/{month}/{year}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> bdeTargetAchievement(@PathVariable("id") Long id,
                                                    @PathVariable("month") Month month,
                                                    @PathVariable("year") int year) {
        return this.saleAndPurchaseService.fetchEditRequestCount(id);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "BDE CounterWise Achievement", description = "Fetch BDE Target & Achievement")
    @RequestMapping(value = "/bdeCounterWiseAchievement", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchAcheivementCounterWise(@RequestParam(value = "rangeType1") String rangeType1, 
    		@RequestParam(value = "rangeType2") String rangeType2,
    		@RequestParam(value = "bdeId") Long bdeId) {
        return this.baUnderBDEService.fetchAcheivementCounterWiseV2(rangeType1,rangeType2, bdeId);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List of BDE under any BDM/ASM/RSM", description = "List of BDE under any BDM/ASM/RSM")
    @RequestMapping(value = "/listOfBDe/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchBde(@PathVariable("id") Long id) {
        return this.baUnderBDEService.listOfBde(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "BDE Details by id", description = "BDE details by id")
    @RequestMapping(value = "/bdeDetails/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchBdeDetails(@PathVariable("id") Long id) {
        return this.baUnderBDEService.fetchBdeDetails(id);

    }
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "left Ba of BDE Achievement", description = "left Ba of BDE Achievement")
    @RequestMapping(value = "/leftBaOfBdeAchievement", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> leftBaOfBDEAchievement(@RequestParam(value = "bdeId") Long bdeId) {
        return this.baUnderBDEService.leftBaOfBDEAchievement(bdeId);
    }
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Fetch Active Version", description = "Fetch Active Version")
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> getVersion(@RequestParam(value = "appType", required = false) String appType) {
        LOGGER.info("MasterController :: getVersion() called......");
        return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
                this.baUnderBDEService.getActiveCategory(appType), 1), HttpStatus.OK);
    }

}

//file modified 123