package com.dcc.osheaapp.ojbso.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.ojbso.dto.BeatChangeReqApproval;
import com.dcc.osheaapp.ojbso.dto.BeatChangeRequestDto;
import com.dcc.osheaapp.ojbso.dto.SoBeatNTypeMappingInputVo;
import com.dcc.osheaapp.ojbso.service.BeatService;
import com.dcc.osheaapp.ojbso.dto.BeatChangeReqVo;
import com.dcc.osheaapp.ojbso.vo.BeatChangeReqApprovalInputVo;
import com.dcc.osheaapp.ojbso.vo.BeatChangeSearchInputVo;
import com.dcc.osheaapp.vo.OutletInputVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
@RestController
@RequestMapping("/so/beat")
@Tag(description = "API related to SO App Beat", name = "SO")
public class BeatController {
    private static final Logger LOGGER = LogManager.getLogger(TourPlanController.class);
    private BeatService beatService;
    @Autowired
    public BeatController(BeatService beatService) {
        this.beatService = beatService;
    }
    @Operation(summary = "Save Beat and Beat Type Mapping while Tour Plan saving By SO",
            description = "Save Beat and Beat Type Mapping while Tour Plan saving By SO")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/saveBeatTypeMapping", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> saveBeatTypeMapping(@RequestBody SoBeatNTypeMappingInputVo input) {
        return this.beatService.saveBeatTypeMapping(input);
    }

    @Operation(summary = "Fetch Beat and Beat Type name for Tour Plan Of SO",
            description = "Fetch Beat and Beat Type name for Tour Plan Of SO")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/fetchBeatListOfSO", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchBeatListOfSO(@RequestParam(value = "soId") Long soId) {
        return this.beatService.fetchBeatListOfSO(soId);
    }

    @Operation(summary = "Fetch Beat and Beat Type name for Tour Plan Of SO with details",
            description = "Fetch Beat and Beat Type name for Tour Plan Of SO with details")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/fetchBeatDetailsOfSO", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchBeatDetailsOfSO(@RequestParam(value = "soId") Long soId) {
        return this.beatService.fetchBeatDetailsOfSO(soId);
    }

    @Operation(summary = "Fetch All Beat Type ", description = "Fetch All Beat Type ")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/fetchAllBeatType", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchAllBeatType(HttpServletRequest request) {
        return this.beatService.fetchBeatType();
    }

    @Operation(summary = "Get active beat type mapping", description = "Get active beat type mapping")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/getActiveBeatMapping/{soId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> getActiveBeatMapping(@PathVariable("soId") Long soId){
        return this.beatService.fetchActiveBeatMapping(soId);
    }

    @Operation(summary = "Beat Change Request", description = "Beat Change Request")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/beatChangeReq", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> beatChangeReq(@RequestBody BeatChangeRequestDto input){
        return this.beatService.beatChangeRequest(input);
    }
    @Operation(summary = "Search Beat Change Request ", description = "Search Beat Change Request")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> search(@RequestBody BeatChangeSearchInputVo input) {
        LOGGER.info(" BeatController :: search() called......");
        return this.beatService.searchByInput(input);
    }
    @Operation(summary = "Beat Change Request List by SoId ", description = "Beat Change Request List by SoId")
    @RequestMapping(value = "/getReqBySoId/{soId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> getBeatChangeReBySoId(@PathVariable("soId") Long soId){
        LOGGER.info(" BeatController :: getBeatChangeReBySoId() called......");
        return this.beatService.beatChangeReqListBySOId(soId);
    }
    @Operation(summary = "Fetch beat change request by id", description = "Fetch beat change request by id")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/beatChange/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchById(@PathVariable("id") Long id) {
        return new ResponseEntity<ApiResponse>(
                new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.beatService.fetchBeatChangeById(id)), HttpStatus.OK);
    }
    @Operation(summary = "Update Request Status", description = "Update Request Status")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/updatedApprovalStatus", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> updateApprovalStatus(@RequestBody BeatChangeReqApproval input) {
        return this.beatService.updateApprovalStatus(input);
    }

}
