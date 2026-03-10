package com.dcc.osheaapp.ojbso.controller;

import com.dcc.osheaapp.ojbso.vo.SoActivityRegisterVo;
import com.dcc.osheaapp.ojbso.vo.TimeLineInputVo;
import com.dcc.osheaapp.vo.UserActivityRegisterVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.ojbso.service.BaseService;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.UserBeatsAssociation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
@RestController
@RequestMapping("/api/so")
@Tag(description = "API related to SO App", name = "SO")
public class basecontroller {

    private static final Logger LOGGER = LogManager.getLogger(basecontroller.class);

    @Autowired
    private BaseService baseService;

    @Operation(summary = "Fetch Beats By SO ID", description = "Fetch Beats By SO ID")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/beats/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchBeatsById(@PathVariable("id") Long id) {
        return this.baseService.fetchBeatsById(id);
    }

    @Operation(summary = "Save Beat By SO", description = "Save Beat By SO")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/beats/save", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> saveBeatBySo(@RequestBody UserBeatsAssociation input) {
        return this.baseService.saveBeatBySo(input);
    }

    @Operation(summary = "Fetch Outlets By Beat", description = "Fetch Outlets By Beat")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/outlets/{id}/{compZone}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchOutletsById(@PathVariable("id") Long id,
                                                        @PathVariable("compZone") Long compZone) {
        return this.baseService.fetchOutletsById(id, compZone);
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Save an outlet entry", description = "Save an outlet entry")
    @RequestMapping(value = "/outletSave", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> outletSave(@ModelAttribute OutletVo input) {
        LOGGER.info("BASEController :: outletSave() called......");
        return this.baseService.outletSave(input);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary= "Fetch All No Order Reason by Parent Id")
    @RequestMapping(value = "/fetchNoOrderReasonList", method= RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchNoOrderReasonList(HttpServletRequest request, @RequestParam(value = "parent") Long parent){
        LOGGER.info("BaseController :: fetchNoOrderReasonList");
        return new ResponseEntity<>(new ApiResponse(200,"SUCCESS","Fetch Successfully",
                this.baseService.fetchNoOrderReasonList(parent),0), HttpStatus.OK);

    }

    @Operation(summary = "Fetch Outlets Dtl By Id", description = "Fetch Outlets Dtl By Id")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/fetchOutletDetails", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> fetchOutletDetails(@RequestParam(value = "outletId") Long outletId) {
        return this.baseService.fetchOutletDetails(outletId);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Save a user activity. Ex : Attendance", description = "Save a single activity")
    @RequestMapping(value = "/saveAttendance", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponse> saveAttendance(@ModelAttribute SoActivityRegisterVo input) {
        LOGGER.info("BaseController :: saveAttendance() called......");
        return this.baseService.saveAttendance(input);
    }
    @Operation(summary = "Present Day  Order Outlets Of So", description = "Present Day  Order Outlets Of So")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/outletOfSo/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> OrderOutletBySoId(@PathVariable("id") Long id) {
        return this.baseService.fetchOrderOutletBySoId(id);
    }
}
