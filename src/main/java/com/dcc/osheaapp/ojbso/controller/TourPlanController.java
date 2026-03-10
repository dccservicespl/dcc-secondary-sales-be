package com.dcc.osheaapp.ojbso.controller;

import javax.servlet.http.HttpServletRequest;

import com.amazonaws.services.directory.model.RadiusSettings;
import com.dcc.osheaapp.vo.views.TourPlaneDaysVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.ojbso.dto.SoBeatNTypeMappingInputVo;
import com.dcc.osheaapp.ojbso.service.TourPlanService;
import com.dcc.osheaapp.ojbso.vo.TourPlanApprovalVo;
import com.dcc.osheaapp.ojbso.vo.TourPlanDaysMst;
import com.dcc.osheaapp.ojbso.vo.TourPlanInputVo;
import com.dcc.osheaapp.ojbso.vo.TourPlanVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,RequestMethod.DELETE })
@RestController
@RequestMapping("/so/tourplan")
@Tag(description = "API related to SO App Tour Plan", name = "SO")
public class TourPlanController {

    private static final Logger LOGGER = LogManager.getLogger(TourPlanController.class);

    private TourPlanService tourPlanService;
    
    @Autowired
    public TourPlanController(TourPlanService tourPlanService) {
    	this.tourPlanService = tourPlanService;
    }
    
    @PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Tour Plan Activity", description = "Fetch All Tour Plan Activity")
	@RequestMapping(value = "/fetchAllTourPlanActivity", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchAllTourPlanActivity(HttpServletRequest request) {
		LOGGER.info("MasterDataController :: fetchAllTourPlanActivity() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.tourPlanService.fetchAllTourPlanActivity(), 0), HttpStatus.OK);
	}

    @Operation(summary = "Save Tour Plan By SO", description = "Save Tour Plan By SO")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/saveTourPlan", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> saveTourPlan(@RequestBody TourPlanVo input) {
        return this.tourPlanService.saveTourPlan(input);
    }
    
    @Operation(summary = "Fetch Tour Plan entry by id", description = "Fetch Tour Plan entry by id")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/plan/{id}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchById(@PathVariable("id") Long id) {
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.tourPlanService.fetchById(id)), HttpStatus.OK);
	}

	@Operation(summary = "Fetch Tour Plan Of SO", description = "Fetch Tour Plan Of SO")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/fetchUserActiveTourPlan", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchTourPlans(@RequestParam(value = "soId") Long soId) {
		return this.tourPlanService.fetchTourPlans(soId);
	}
	
	@Operation(summary = "Fetch Tour Plan Of SO Date wise", description = "Fetch Tour Plan Of SO Date wise")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/fetchTourPlanOfSOByDate", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchTourPlanOfSOByDate(@RequestParam(value = "soId") Long soId,
			@RequestParam(value = "inputDate") String inputDate) {
		return this.tourPlanService.fetchTourPlanOfSOByDate(soId, inputDate);
	}

	@Operation(summary = "Fetch All Tour Plan", description = "Fetch All Tour Plan")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value="/fetchAllTourPlan", method=RequestMethod.POST)
	public ResponseEntity<ApiResponse> fethAllTourPlan(@RequestBody TourPlanInputVo input){
		LOGGER.info("TourPlanController :: call fetchAllTourplan() ");
        return this.tourPlanService.fetchAllTourPlan(input);
	}

	@Operation(summary = "Update Status of Tour Plan", description = "Update Status of Tour Plan by id")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/updateStatusofTourPlan", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> updateStatusofTourPlan(@RequestBody TourPlanVo input) {
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(200, "SUCCESS", "Update SUCCESSFULL", this.tourPlanService.updateStatusofTourPlan(input)), HttpStatus.OK);
	}

	@Operation(summary = "Update status of an edit request from Admin Panel", description = "Update status of an edit request from Admin Panel")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/updatedApprovalStatus", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> updatedApprovalStatus(@RequestBody TourPlanApprovalVo input) {
		return this.tourPlanService.updatedApprovalStatus(input);
	}

	@Operation(summary = "Update status of an edit request from Admin Panel", description = "Update status of an edit request from Admin Panel")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/fetchTourPlanDtlByDate", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchTourPlanDtlByDate( @RequestParam(value = "soId") Long soId
			, @RequestParam(value = "ipDate") String ipDate) {
		return this.tourPlanService.fetchTourPlanDtlByDate(soId, ipDate);
	}
	@Operation(summary = "Save total days of tour plan" , description = "Save total days of tour plan")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/totalTourPlanDays", method=RequestMethod.POST)
	public ResponseEntity<ApiResponse> saveTotalTourPlanDays (@RequestBody TourPlanDaysMst input){
		return this.tourPlanService.saveTotalTourPlanDays((input));
	}
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch Total TourPlan Days", description = "Fetch Total TourPlan Days")
	@RequestMapping(value = "/getTotalTourPlanDays", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> getTotalTourPlanDays(HttpServletRequest request) {
		LOGGER.info("MasterDataController :: getTotalTourPlanDays() called......");
		return new ResponseEntity<>(
				new ApiResponse(200, "SUCCESS", "Fetched Successfully", this.tourPlanService.getTotalTourPlanDays(), 0),
				HttpStatus.OK);
	}
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All TourPlan Days", description = "Fetch All TourPlan Days")
	@RequestMapping(value = "/getAllTourPlanDays", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> getAllTourPlanDays(HttpServletRequest request) {
		LOGGER.info("MasterDataController :: getAllTourPlanDays() called......");
		return new ResponseEntity<>(
				new ApiResponse(200, "SUCCESS", "Fetched Successfully", this.tourPlanService.getAllTourPlanDays(), 0),
				HttpStatus.OK);
	}

	@Operation(summary = "Update TourPlan days status", description = "Update TourPlan days status")
	@RequestMapping(value = "/updateTourplanDaysStatus" , method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> updateTourPlaneDays(@RequestBody TourPlaneDaysVo input){
		LOGGER.info("TourPlaneController :: updateTourPlaneDays() called...");
		return this.tourPlanService.updateTourPlaneDaysStatus(input);
	}

	@Operation(summary = "Delete TourPlan by id ", description = "Delete TourPlan by id. ")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteTourPlanDays/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ApiResponse> deleteTourPlanDaysById(@PathVariable("id") Long id) {
		return this.tourPlanService.deleteTourPlanDays(id);
	}
	
//	@Operation(summary = "Save Beat and Beat Type Mapping while Tour Plan saving By SO",
//					description = "Save Beat and Beat Type Mapping while Tour Plan saving By SO")
//    @PreAuthorize("isAuthenticated()")
//    @RequestMapping(value = "/saveBeatTypeMapping", method = RequestMethod.POST)
//    public ResponseEntity<ApiResponse> saveBeatTypeMapping(@RequestBody SoBeatNTypeMappingInputVo input) {
//        return this.tourPlanService.saveBeatTypeMapping(input);
//    }
//
//	@Operation(summary = "Fetch Beat and Beat Type name for Tour Plan Of SO",
//			description = "Fetch Beat and Beat Type name for Tour Plan Of SO")
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/fetchBeatListOfSO", method = RequestMethod.GET)
//	public ResponseEntity<ApiResponse> fetchBeatListOfSO(@RequestParam(value = "soId") Long soId) {
//		return this.tourPlanService.fetchBeatListOfSO(soId);
//	}
//
//	@Operation(summary = "Fetch Beat and Beat Type name for Tour Plan Of SO with details",
//			description = "Fetch Beat and Beat Type name for Tour Plan Of SO with details")
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/fetchBeatDetailsOfSO", method = RequestMethod.GET)
//	public ResponseEntity<ApiResponse> fetchBeatDetailsOfSO(@RequestParam(value = "soId") Long soId) {
//		return this.tourPlanService.fetchBeatDetailsOfSO(soId);
//	}
//
//	@Operation(summary = "Fetch All Beat Type ", description = "Fetch All Beat Type ")
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/fetchAllBeatType", method = RequestMethod.GET)
//	public ResponseEntity<ApiResponse> fetchAllBeatType(HttpServletRequest request) {
//		return this.tourPlanService.fetchBeatType();
//	}
//
//	@Operation(summary = "Get active beat type mapping", description = "Get active beat type mapping")
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/getActiveBeatMapping/{soId}", method = RequestMethod.GET)
//	public ResponseEntity<ApiResponse> getActiveBeatMapping(@PathVariable("soId") Long soId){
//		return this.tourPlanService.fetchActiveBeatMapping(soId);
//	}


}
