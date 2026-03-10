package com.dcc.osheaapp.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.service.TargetService;
import com.dcc.osheaapp.vo.dto.SearchTargetDto;
import com.dcc.osheaapp.vo.dto.SetTargetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Month;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
    originPatterns = {"*"},
    allowedHeaders = "*",
    maxAge = 4800,
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/target")
@Tag(description = "API related to BA target", name = "Target")
public class TargetController {

  private static final Logger LOGGER = LogManager.getLogger(TargetController.class);

  @Autowired TargetService targetService;

  //    @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Set a target", description = "Set a new target for BA.")
  @PostMapping
  public ResponseEntity<ApiResponse> set(@RequestBody SetTargetDto input) {
    LOGGER.info("TargetController :: set() called......");
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(
            200, "SUCCESS", "Target Set Successfully.", this.targetService.setTarget(input)),
        HttpStatus.OK);
  }

  @Operation(
      summary = "Export User Activity in Excel Sheet",
      description = "Export User Activity in Excel Sheet")
  @RequestMapping(value = "/export", method = RequestMethod.GET)
  public Object exportUserActivity(
      @RequestParam("month") Month month,
      @RequestParam("year") int year,
      @RequestParam("division") Long division,
      @RequestParam("zone") Long zone,
      HttpServletRequest request,
      HttpServletResponse response) {
    String FILE_NAME = "BaTarget.xlsx";
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
    return this.targetService.exportTargets(month, year, division, zone, response);
  }

  @Operation(summary = "Get target of a BA", description = "Get targets of a BA")
  @RequestMapping(value = "/{id}/{month}/{year}", method = RequestMethod.GET)
  public Object getTarget(
      @PathVariable("id") Long id,
      @PathVariable("month") Month month,
      @PathVariable("year") int year) {
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(
            200,
            "SUCCESS",
            "Target Fetched Successfully.",
            this.targetService.getTargetV2(id, month, year)),
        HttpStatus.OK);
  }

  @Operation(summary = "Get target of a BDE", description = "Get targets of a BDE")
  @RequestMapping(value = "/bdeTarget/{id}/{month}/{year}", method = RequestMethod.GET)
  public Object getBdeTarget(
          @PathVariable("id") Long id,
          @PathVariable("month") String month,
          @PathVariable("year") int year) {
    return new ResponseEntity<ApiResponse>(
            new ApiResponse(
                    200,
                    "SUCCESS",
                    "Target Fetched Successfully.",
                    this.targetService.getBdeTargetV2(id, month, year)),
            HttpStatus.OK);
  }

  @Operation(
      summary = "Update target from Excel Sheet",
      description = "Upload target from Excel Sheet")
  @RequestMapping(value = "/update/bulk", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> saveTargetFromExcel(
      @RequestParam("uploadFile") MultipartFile excelInput) {
    LOGGER.info("TargetController :: saveTargetFromExcel() called......");
    this.targetService.bulkUpdate(excelInput);
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(200, "SUCCESS", "Target Updated Successfully", null), HttpStatus.OK);
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Search target", description = "Search target")
  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> searchTarget(@RequestBody SearchTargetDto inputVo) {
    LOGGER.info("Targetcontroller :: searchTargetCalled() called......");
    return this.targetService.searchTarget(inputVo);
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Sync targets", description = "Sync targets")
  @RequestMapping(value = "/sync", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> sync() {
    LOGGER.info("Targetcontroller :: sync() called......");
    this.targetService.syncAchievementsv2();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(200, "SUCCESS", "Achievements Synced Successfully", null), HttpStatus.OK);
  }
//  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Sync targets", description = "Sync targets")
  @RequestMapping(value = "/syncBde/{bdeId}", method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> syncByBde(@PathVariable("bdeId") Long bdeId ) {
    LOGGER.info("Targetcontroller :: syncByBde() called......");
    this.targetService.syncAchievementsByBde(bdeId);
    return new ResponseEntity<ApiResponse>(
            new ApiResponse(200, "SUCCESS", "Achievements Synced Successfully", null), HttpStatus.OK);
  }
}
