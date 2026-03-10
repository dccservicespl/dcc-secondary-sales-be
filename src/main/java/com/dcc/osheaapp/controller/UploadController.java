package com.dcc.osheaapp.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.service.UserService;
import com.dcc.osheaapp.service.upload.UploadServiceFactory;
import com.dcc.osheaapp.service.upload.UserActivityUploadService;
import com.dcc.osheaapp.vo.dto.UserActivityInputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
    originPatterns = {"*"},
    allowedHeaders = "*",
    maxAge = 4800,
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RestController
@RequestMapping("/api/upload")
@Tag(description = "API related to Upload", name = "Upload")
public class UploadController {

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  @Autowired private UploadServiceFactory uploadServiceFactory;

  @Operation(
      summary = "Upload Outlet from Excel Sheet",
      description = "Upload Outlet from Excel Sheet")
  @RequestMapping(value = "/saveOutletFromExcel", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> saveOutletFromExcel(
      @RequestParam("uploadFile") MultipartFile excelInput) {
    LOGGER.info("OutletController :: saveProductFromExcel() called......");
    this.uploadServiceFactory.getOutletUploadService().upload(excelInput);
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(200, "SUCCESS", "Successfully Uploaded", null), HttpStatus.OK);
  }
  
  @Operation(
	      summary = "Upload Outlet and BA Users from Excel Sheet",
	      description = "Upload Outlet and BA Users from Excel Sheet")
	  @RequestMapping(value = "/saveOutletAndBaFromExcel", method = RequestMethod.POST)
	  public ResponseEntity<ApiResponse> saveOutletAndBaFromExcel(
	      @RequestParam("uploadFile") MultipartFile excelInput) {
	    LOGGER.info("OutletController :: saveOutletAndBaFromExcel() called......");
	    this.uploadServiceFactory.getOutletUploadService().uploadOutletAndBa(excelInput);
	    return new ResponseEntity<ApiResponse>(
	        new ApiResponse(200, "SUCCESS", "Successfully Uploaded", null), HttpStatus.OK);
	  }

//  @Operation(summary = "Upload User from Excel Sheet", description = "Upload User from Excel Sheet")
//  @RequestMapping(value = "/uploadUserFromExcel", method = RequestMethod.POST)
//  public ResponseEntity<ApiResponse> uploadUserFromExcel(
//      @RequestParam("uploadFile") MultipartFile excelInput) {
//    LOGGER.info("UploadController :: uploadUserFromExcel() called......");
//    this.uploadServiceFactory.getUserUploadService().upload(excelInput);
//    return new ResponseEntity<ApiResponse>(
//        new ApiResponse(200, "SUCCESS", "Uploaded Successfully", null), HttpStatus.OK);
//  }

//  @PreAuthorize("isAuthenticated()")
//  @Operation(summary = "Export User in Excel Sheet", description = "Export User in Excel Sheet")
//  @RequestMapping(value = "/exportUser", method = RequestMethod.GET)
//  public Object exportUser(@RequestParam Long id, HttpServletResponse response) {
//    LOGGER.info("UploadController :: exportUser() called......"+id);
//
//    String FILE_NAME = "BAUsers.xlsx";
//
//    response.setContentType("application/vnd.ms-excel");
//    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
//    return this.uploadServiceFactory.getUserUploadService().export(id,response);
//
//  }

  @Operation(
      summary = "Export User Activity in Excel Sheet - NOT IN USE",
      description = "Export User Activity in Excel Sheet - NOT IN USE")
  @RequestMapping(value = "/exportUserActivity", method = RequestMethod.POST)
  public Object exportUserActivity(
      @RequestBody UserActivityInputDto inputVo,
      HttpServletRequest request,
      HttpServletResponse response) {
    LOGGER.info("UploadController :: exportUserActivity() called......");

    String FILE_NAME = "UserActivity.xlsx";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
    return this.uploadServiceFactory
        .getUserActivityUploadService()
        .exportUserActivity(inputVo, response);
  }

  @Operation(
      summary = "Export User All Activities in Excel Sheet",
      description = "Export User All Activities in Excel Sheet")
  @RequestMapping(value = "/exportUserAllActivity", method = RequestMethod.POST)
  public Object exportUserAllActivity(
      @RequestBody UserActivityInputDto inputVo,
      HttpServletRequest request,
      HttpServletResponse response) {
    LOGGER.info("UploadController :: exportUserAllActivity() called......");
    String FILE_NAME = "";

    if (inputVo.getUserID() == null) {
      FILE_NAME = "UsersActivity.xlsx"; // All User
    } else {
      FILE_NAME = "UserActivity.xlsx"; // One User
    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
    return this.uploadServiceFactory
        .getUserActivityUploadService()
        .exportUserAllActivity(inputVo, response);
  }

  @Operation(
      summary = "Export Products Excel Sheet",
      description = "Export Products in Excel Sheet")
  @RequestMapping(value = "/exportProducts", method = RequestMethod.GET)
  public Object exportUserActivity(@RequestParam("division") Long division,HttpServletRequest request, HttpServletResponse response) {
    LOGGER.info("UploadController :: exportUserActivity() called......");

    String FILE_NAME = "Products.xlsx";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);

    return this.uploadServiceFactory.getProductUploadService().export(response, division);
  }

  @Operation(
      summary = "Upload product from Excel Sheet",
      description = "Upload product from Excel Sheet")
  @RequestMapping(value = "/saveProductFromExcel", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> saveProductFromExcel(
      @RequestParam("uploadFile") MultipartFile excelInput) {
    LOGGER.info("UploadController :: saveProductFromExcel() called......");
    this.uploadServiceFactory.getProductUploadService().upload(excelInput);
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(200, "UPLOADED", "Products uploaded successfully", null), HttpStatus.OK);
  }


//  @PreAuthorize("isAuthenticated()")
//  @Operation(summary = "Export BARank in Excel Sheet", description = "Export BARank in Excel Sheet")
//  @RequestMapping(value = "/exportBARank", method = RequestMethod.GET)
//  public Object exportBARank(@RequestParam String yearMonth, Long zoneId, HttpServletResponse response){
//    LOGGER.info("User Controller :: exportBARank() called.........");
//    String FILE_NAME = "BARank.xlsx";
//
//    response.setContentType("application/vbn.ms-excel");
//    response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
//    return this.user.exportBARank(yearMonth,zoneId,response);
//
//  }

  //    @Operation(summary = "Export Sales Excel Sheet",
  //            description = "Export Sales in Excel Sheet")
  //    @RequestMapping(value = "/exportSales", method = RequestMethod.POST)
  //    public Object exportSales(@RequestBody SalesRecordExcelExportInputDto dto,
  //            HttpServletRequest request, HttpServletResponse response) {
  //        LOGGER.info("UploadController :: exportSales() called......");
  //        String FILE_NAME = "Sales_Record.xlsx";
  //        response.setContentType("application/vnd.ms-excel");
  //        response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
  //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  //        return
  // this.uploadServiceFactory.getPurchaseRecordUploadService().exportSales(dto.getFromDate(),
  // dto.getToDate(), dto.getOutlet(),dto.getActivity(), dto.getStockStatus(), response);
  //    }

}
