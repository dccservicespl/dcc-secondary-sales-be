package com.dcc.osheaapp.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.service.OutletService;
import com.dcc.osheaapp.service.upload.OutletUploadService;
import com.dcc.osheaapp.service.upload.UploadServiceFactory;
import com.dcc.osheaapp.vo.OutletInputVo;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.dto.UpdateOutletDto;
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
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(
    originPatterns = {"*"},
    allowedHeaders = "*",
    maxAge = 4800,
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RestController
@RequestMapping("/outlet")
@Tag(description = "API related to Outlet", name = "Outlet")
public class OutletController {

  private static final Logger LOGGER = LogManager.getLogger(OutletService.class);
  private OutletService outletService;

  @Autowired
  public OutletController(OutletService outletService) {
    this.outletService = outletService;
  }

  @Autowired
private UploadServiceFactory uploadServiceFactory;
  @Operation(summary = "Creates a new Outlet", description = "Creates a new Outlet")
  @RequestMapping(value = "/createOutlet", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> createOutlet(@RequestBody OutletVo input) {
    LOGGER.info("OutletController :: createOutlet() called...");
    return this.outletService.createOutlet(input);
  }

  @Operation(summary = "Search outlet ", description = "Search outlet ")
  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> search(@RequestBody OutletInputVo input) {
    LOGGER.info("OutletController :: search() called......");
    return this.outletService.searchByInput(input);
  }

  @Operation(summary = "Search outlet ", description = "Search outlet ")
  @RequestMapping(value = "/searchView", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> searchview(@RequestBody OutletInputVo input) {
    LOGGER.info("OutletController :: search() called......");
    return this.outletService.searchViewByInput(input);
  }

  @Operation(summary = "Update outlet state", description = "Update outlet state")
  @RequestMapping(value = "/status", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> updateStatus(@RequestBody OutletVo outlet) {
    LOGGER.info("OutletController :: updateStatus() called......");
    this.outletService.updateOutletState(outlet);
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(200, "UPDATED", "Outlet status updated successfully.", null),
        HttpStatus.OK);
  }

  @Operation(summary = "Fetch outlet by id", description = "Fetch outlet by id")
  @PreAuthorize("isAuthenticated()")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> fetchById(@PathVariable("id") Long id) {
    LOGGER.info("OutletController :: fetchById() called......");
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.outletService.fetchById(id)),
        HttpStatus.OK);
  }

  @Operation(summary = "Update Outlet", description = "Update Outlet")
  @PutMapping
  public ResponseEntity<ApiResponse> updateOutlet(@RequestBody UpdateOutletDto input) {
    LOGGER.info("OutletController :: updateOutlet() called......");
    return new ResponseEntity<>(
        new ApiResponse(
            200,
            "UPDATED",
            "Outlet details updated successfully",
            this.outletService.updateOutlet(input)),
        HttpStatus.OK);
  }

//  @PreAuthorize("isAuthenticated()")
//  @Operation(summary = "Export Outlet in Excel Sheet", description = "Export Outlet in Excel Sheet")
//  @RequestMapping(value = "/exportOutlet", method = RequestMethod.GET)
//  public Object exportOutlet(@RequestParam Long id, HttpServletResponse response){
//    LOGGER.info("OutletController :: exportOutlet() called....... ");
//    String FILE_NAME = "OutletDump.xlsx";
//
//    response.setContentType("application/vnb.ms-excel");
//    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
//    return this.uploadServiceFactory.getOutletUploadService().export(id,response.getOutputStream());
//  }
}
