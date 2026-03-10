package com.dcc.osheaapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.service.MasterDataService;
import com.dcc.osheaapp.service.MediaService;
import com.dcc.osheaapp.vo.BeatName;
import com.dcc.osheaapp.vo.DistributorVo;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.FormType;
import com.dcc.osheaapp.vo.dto.CreateBeatInputDto;
import com.dcc.osheaapp.vo.dto.DistributorInputVo;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
		RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
@RestController
@RequestMapping("/master")
public class MasterDataController {

	private static final Logger LOGGER = LogManager.getLogger(MasterDataController.class);

	private final MasterDataService masterDataService;
	private final MediaService mediaService;

	public MasterDataController(MasterDataService masterDataService, MediaService mediaService) {
		this.masterDataService = masterDataService;
		this.mediaService = mediaService;
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Product Category", description = "Fetch All Product Category")
	@RequestMapping(value = "/fetchAllProductCategory", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchAllProductCategory(HttpServletRequest request) {
		LOGGER.info("UserController :: fetchAllProductCategory() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.masterDataService.fetchAllProductCategoryAlt(), 0), HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All User Type", description = "Fetch All User Type")
	@RequestMapping(value = "/fetchAllUserType", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchAllUserType(HttpServletRequest request,
			@RequestParam(value = "formType", required = false) FormType formType) {
		LOGGER.info("MasterDataController :: fetchAllUserType() called......" + formType);
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.masterDataService.fetchAllUserType(formType), 0), HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Company Zone", description = "Fetch All Company Zone")
	@RequestMapping(value = "/fetchAllCompanyZone", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchAllCompanyZone(HttpServletRequest request) {
		LOGGER.info("MasterDataController :: fetchAllCompanyZone() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.masterDataService.fetchAllCompanyZone(), 0), HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Beat Name", description = "Fetch All Beat Name")
	@RequestMapping(value = "/fetchAllBeatName", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchAllBeatName(HttpServletRequest request) {
		LOGGER.info("MasterDataController :: fetchAllCompanyZone() called......");
		return new ResponseEntity<>(
				new ApiResponse(200, "SUCCESS", "Fetched Successfully", this.masterDataService.fetchAllBeatName(), 0),
				HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Outlet Channel", description = "Fetch All Outlet Channel")
	@RequestMapping(value = "/fetchAllOutletChannel", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchAllOutletChannel(HttpServletRequest request) {
		LOGGER.info("MasterDataController :: fetchAllOutletChannel() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.masterDataService.fetchAllOutletChannel(), 0), HttpStatus.OK);
	}

	// @PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Distributors", description = "Fetch All Distributors")
	@RequestMapping(value = "/fetchAllDistributors", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchAllDistributors(HttpServletRequest request) {
		LOGGER.info("MasterDataController :: fetchAllOutletChannel() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.masterDataService.fetchAllDistributors(), 0), HttpStatus.OK);
	}

	// @PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Active Dropdown Value By Field Type", description = "Fetch All Active Dropdown Value By Field Type")
	@RequestMapping(value = "/fetchAllActiveDropdownValueByFieldType", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> fetchAllActiveDropdownValueByFieldType(@RequestBody DropdownMasterVo inputVo) {
		LOGGER.info("MasterDataController :: fetchAllActiveDropdownValueByFieldType() called......");
		return new ResponseEntity<>(
				new ApiResponse(200, "SUCCESS", "Fetched Successfully",
						this.masterDataService.fetchAllActiveDropdownValueByFieldType(inputVo.getFieldType()), 0),
				HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch Higher Priority User Type", description = "Fetch Higher Priority User Type")
	@RequestMapping(value = "/fetchHigherPriorityUserType", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchHigherPriorityUserType(HttpServletRequest request,
			@RequestParam(value = "pri") Long pri) {
		LOGGER.info("MasterDataController :: fetchHigherPriorityUserType() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.masterDataService.fetchHigherPriorityUserType(pri), 0), HttpStatus.OK);
	}

	@Operation(summary = "Fetch File From Folder", description = "Fetch File From Folder")
	@RequestMapping(value = "/showMedia", method = RequestMethod.GET)
	public ResponseEntity<Resource> showMedia(@RequestParam("imageName") String imageName,
			@RequestParam("formType") String formType) {
		LOGGER.info("MasterDataController :: showMedia() :: called......");
		return mediaService.getMedia(imageName, formType);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Product Category", description = "Fetch All Product Category")
	@RequestMapping(value = "/fetchProductCategoryList", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchProductCategoryList(HttpServletRequest request,
			@RequestParam(value = "parent") Long parent) {
		LOGGER.info("UserController :: fetchAllProductCategory() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.masterDataService.fetchProductCategoryList(parent), 0), HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch Active Version", description = "Fetch Active Version")
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> getVersion(@RequestParam(value = "appType", required = false) String appType) {
		LOGGER.info("MasterController :: getVersion() called......");
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				this.masterDataService.getActiveCategory(appType), 1), HttpStatus.OK);
	}

	// @Operation(summary = "Creates a new Beat Name", description = "Creates a new
	// Beat Name")
	// @RequestMapping(value = "/createBeatName", method = RequestMethod.POST)
	// public ResponseEntity<ApiResponse> createBeatName(@RequestBody BeatName
	// input) {
	// LOGGER.info("MasterController :: createBeatName() called...");
	// return this.masterDataService.createBeatName(input);
	// }
	@Operation(summary = "Creates a new Beat Name", description = "Creates a new Beat Name")
	@RequestMapping(value = "/createBeat", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> createBeat(@RequestBody CreateBeatInputDto input) {
		LOGGER.info("MasterController :: createBeatName() called...");
		return this.masterDataService.createNewBeat(input);
	}

	@Operation(summary = "Update Beat", description = "Update Beat")
	@RequestMapping(value = "/updateBeat", method = RequestMethod.PUT)
	public ResponseEntity<ApiResponse> updateBeat(@RequestBody CreateBeatInputDto input) {
		LOGGER.info("MasterController :: updateBeat() called......");
		return new ResponseEntity<>(
				new ApiResponse(
						200,
						"UPDATED",
						"Beat details updated successfully",
						this.masterDataService.updateBeat(input)),
				HttpStatus.OK);
	}

	@Operation(summary = "Search Beat Name ", description = "Search Beat Name ")
	@RequestMapping(value = "/searchBeatName", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> searchBeatName(@RequestBody BeatName input) {
		LOGGER.info("MasterController :: searchBeatName() called......");
		return this.masterDataService.searchBeatNameByInput(input);
	}

	@Operation(summary = "Update beatname status", description = "Update beatname status")
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> updateStatus(@RequestBody BeatName input) {
		LOGGER.info("MasterController :: updateStatus() called.......");
		return this.masterDataService.updateStatus(input);
	}

	@Operation(summary = "Fetch beatName by id", description = "Fetch beatName by id")
	@PreAuthorize(("isAuthenticated()"))
	@RequestMapping(value = "/beatName/{id}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchById(@PathVariable("id") Long id) {
		LOGGER.info("MasterDataController :: fetchById() called......");
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.masterDataService.fetchById(id)),
				HttpStatus.OK);
	}

	@Operation(summary = "Search Distributor", description = "Search Distributor")
	@RequestMapping(value = "/searchDistributorView", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> searchDistributor(@RequestBody DistributorInputVo input) {
		LOGGER.info("MasterDataContrller :: searDistributor()  called....");
		return this.masterDataService.searchDistributorByTnput(input);
	}

	@Operation(summary = "Create a new Distributor", description = "Create a new Distributor")
	@RequestMapping(value = "/addDistributor", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> addDistributor(@RequestBody DistributorVo input) {
		return this.masterDataService.addDistributor(input);
	}
}
