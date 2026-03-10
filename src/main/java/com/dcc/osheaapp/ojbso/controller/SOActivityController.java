package com.dcc.osheaapp.ojbso.controller;

import javax.servlet.http.HttpServletRequest;

import com.dcc.osheaapp.ojbso.vo.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.ojbso.dto.OrderInputVo;
import com.dcc.osheaapp.ojbso.service.SoActivityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
@RestController
@RequestMapping("/so/activity")
@Tag(description = "API related to SO Activity", name = "SO")
public class SOActivityController {

    private static final Logger LOGGER = LogManager.getLogger(SOActivityController.class);

    private SoActivityService soActivityService;
    
    @Autowired
    public SOActivityController(SoActivityService soActivityService) {
    	this.soActivityService = soActivityService;
    }
    
    @PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All SO Activity", description = "Fetch All SO Activity")
	@RequestMapping(value = "/fetchAllSOActivity", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchAllSOActivity(HttpServletRequest request) {
		LOGGER.info("MasterDataController :: fetchAllSOActivity() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.soActivityService.fetchAllSOActivity(), 0), HttpStatus.OK);
	}

    @Operation(summary = "Save Tour Activity By SO", description = "Save Tour Activity By SO")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/saveSOActivity", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> saveTourPlan(@RequestBody SoActivityRegisterVo input) {
        return this.soActivityService.saveSOActivity(input);
    }
    
    @Operation(summary = "Save Order By SO", description = "Save Order By SO")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> saveTourPlan(@RequestBody OrderVo input) {
        return this.soActivityService.saveOrder(input);
    }
    
//    @Operation(summary = "Fetch Tour Plan entry by id", description = "Fetch Tour Plan entry by id")
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/plan/{id}", method = RequestMethod.GET)
//	public ResponseEntity<ApiResponse> fetchById(@PathVariable("id") Long id) {
//		return new ResponseEntity<ApiResponse>(
//				new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.tourPlanService.fetchById(id)), HttpStatus.OK);
//	}

    @Operation(summary = "Fetch sub activity of Official Work" ,description = "Fetch sub activity of Official Work")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/officialWorkSubActivity", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchOfficialSubActivity(HttpServletRequest request){
        LOGGER.info("SOActivityController :: fetchOfficialSubActivity called......");
        return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
                this.soActivityService.fetchOfficialSubActivity(), 0), HttpStatus.OK);
    }
    
    @Operation(summary = "Fetch sub activity of Official Work" ,description = "Fetch sub activity of Official Work")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/fetchDistributorOfUser", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchDistributorOfUser(@RequestParam(value = "soId") Long soId,
    		HttpServletRequest request){
        LOGGER.info("SOActivityController :: fetchDistributorOfUser called......");
        return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
                this.soActivityService.fetchDistributorOfUser(soId), 0), HttpStatus.OK);
    }
    
    @Operation(summary = "Save Order By SO", description = "Save Order By SO")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/getOutletDetails", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> getOutletDetails(@RequestParam(value = "soId") Long soId,
    		@RequestParam(value = "outletId") Long outletId, @RequestParam(value = "beatId") Long beatId,
    		@RequestParam(value = "monYr") String monYr) {
        return this.soActivityService.getOutletDetails(soId, outletId, beatId, monYr);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Fetch Time Line ", description = "Fetch Time Line")
    @RequestMapping(value = "/timeLine", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> fetchTimeLine(@RequestBody TimeLineInputVo input) {
        LOGGER.info("BaseController :: saveAttendance() called......");
        return this.soActivityService.fetchTimeLine(input);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update End Date Time ",description = "Update End Date Time")
    @RequestMapping(value = "/updateEndDateTime",method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> updateEndDateTime(@RequestBody SoActivityRegisterVo input){
        LOGGER.info("Enter SOActivity Controller :: updateEndDateTime()");
       return this.soActivityService.updateEndDateTime(input);

    }
    
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get SO Activity Summary",description = "Get SO Activity Summary")
    @RequestMapping(value = "/getSOActivitySummary",method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> getSOActivitySummary(@RequestParam(value = "soId") Long soId){
        LOGGER.info("Enter SOActivity Controller  :: getSOActivitySummary()");
       return this.soActivityService.getSOActivitySummary(soId);

    }
    
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Search Order By Input",description = "Search Order By Input")
    @RequestMapping(value = "/searchOrderByInput",method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> searchOrderByInput(@RequestBody OrderInputVo input){
        LOGGER.info("Enter SOActivity Controller  :: searchOrderByInput()");
       return this.soActivityService.searchOrderByInput(input);

    }
    
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Fetch Order By id",description = "Fetch Order By id")
    @RequestMapping(value = "/fetchEachOrder",method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchEachOrder(@RequestParam(value = "orderId") Long orderId){
        LOGGER.info("Enter SOActivity Controller  :: fetchEachOrder()");
       return this.soActivityService.fetchEachOrder(orderId);

    }


    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestManagementVo.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "${api.response-codes.badRequest.desc}", content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "${api.response-codes.unauthorized.desc}", content = @Content(mediaType = "application/json", schema = @Schema(hidden = true)))})
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Fetch All Activity change Request", description = "Fetch All Activity change Request")
    @RequestMapping(value = "/fetchAllSOActivityRequest", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> fetchSoActivityRequest(@RequestBody ChangeRequestInputVo input){
        LOGGER.info("SOActivityController :: fetchSoActivityRequest() called......");
        return this.soActivityService.fetchAllSOActivityRequest(input);
    }


    @Operation(summary = "Update Activity Request Status", description = "Update Activity Request Status")
    @RequestMapping(value = "/updateActivityStatus", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> updateStatus(@RequestBody RequestManagementVo input){
        LOGGER.info("SOActivityController :: updateStatus() called....");
        return this.soActivityService.updateStatus(input);
    }

}
