package com.dcc.osheaapp.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.managerial.service.ActivityEnum;
import com.dcc.osheaapp.managerial.service.BaUnderBDEService;
import com.dcc.osheaapp.service.MutateUserService;
import com.dcc.osheaapp.service.UserService;
import com.dcc.osheaapp.managerial.service.SaleAndPurchaseService;
import com.dcc.osheaapp.vo.*;
import com.dcc.osheaapp.vo.dto.UpdateUserDto;
import com.dcc.osheaapp.vo.views.PurchaseSaleInputVo;
import com.dcc.osheaapp.vo.views.PurchaseSaleOutputVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@CrossOrigin(originPatterns = { "*" }, allowedHeaders = "*", maxAge = 4800, allowCredentials = "true", methods = {
		RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
@RestController
@RequestMapping("/api/user")
@Tag(description = "API related to User", name = "User")
public class UserController {

	private static final Logger LOGGER = LogManager.getLogger(UserService.class);
	private final BaUnderBDEService baListUnderBde;
	private final MutateUserService mutateUserService;
	private final UserService userService;

	private final SaleAndPurchaseService saleAndPurchaseService;

	@Autowired
	public UserController(UserService userService, BaUnderBDEService baListUnderBde,
			MutateUserService mutateUserService, SaleAndPurchaseService saleAndPurchaseService) {
		this.userService = userService;
		this.baListUnderBde = baListUnderBde;
		this.mutateUserService = mutateUserService;
		this.saleAndPurchaseService = saleAndPurchaseService;
	}

	@Transactional
	@Operation(summary = "Creates a new User", description = "Creates a new User")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> register(@RequestBody UserDetailsVo input) {
		return new ResponseEntity<>(
				new ApiResponse(201, "CREATED", "User Created Successfully", this.mutateUserService.register(input)),
				HttpStatus.OK);
	}

	@Operation(summary = "Generates a new JWT token", description = "Generates a new JWT token")
	@RequestMapping(value = "/generateToken", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> generateToken(@RequestBody SignUpVo signUpVo, HttpServletResponse response) {
		LOGGER.info("UserController :: generateToken() called......");

		return this.userService.generateToken(signUpVo, response);
	}

	@Operation(summary = "Generates a new JWT token for Bde", description = "Generates a new JWT token for Bde")
	@RequestMapping(value = "/generateTokenBde", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> generateTokenBde(@RequestBody SignUpVo signUpVo, HttpServletResponse response) {
		LOGGER.info("UserController :: generateToken() called......");

		return this.userService.generateTokenBde(signUpVo, response);
	}

	@Operation(summary = "Generates a new JWT token for SO or Higher", description = "Generates a new JWT token for SO or Higher")
	@RequestMapping(value = "/generateTokenSo", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> generateTokenSo(@RequestBody SignUpVo signUpVo, HttpServletResponse response) {
		LOGGER.info("UserController :: generateTokenSo() called......");

		return this.userService.generateTokenSo(signUpVo, response);
	}

	@Operation(summary = "Generates a new token when expired", description = "Generates a new access token when token expires")
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		return userService.refreshAccessToken(request);
	}

	@Operation(summary = "Validates JWT token", description = "Validates JWT token")
	@RequestMapping(value = "/validateToken", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> validateToken(@RequestParam String token) {
		LOGGER.info("UserController :: validateToken() called......");
		return this.userService.validateToken(token);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Fetch All Active Users", description = "Fetch All Active Users")
	@RequestMapping(value = "/fetchUsers", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchUsers(HttpServletRequest request) {
		LOGGER.info("UserController :: fetchUsers() called......");
		return new ResponseEntity<>(
				new ApiResponse(200, "SUCCESS", "Fetched Successfully", this.userService.fetchAllActiveUsers(), 0),
				HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Search users By input", description = "Search users By input")
	@RequestMapping(value = "/searchByInput", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> searchByInput(@RequestBody UserSearchInputVo inputVo) {
		LOGGER.info("UserController :: searchByInput() called......");
		return this.userService.searchByInput(inputVo);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Save a user activity. Ex : Store checkin", description = "Save a single activity")
	@RequestMapping(value = "/storeCheckin", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<ApiResponse> storeCheckin(@ModelAttribute UserActivityRegisterVo input) {
		LOGGER.info("UserController :: storeCheckin() called......");
		return this.userService.storeCheckin(input);
	}

	@Operation(summary = "Fetch user details", description = "Fetch user details")
	@RequestMapping(value = "/fetchUserDetails", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> login() {
		LOGGER.info("UserController :: fetchUserDetails() called......");
		return this.userService.fetchUserDetails();
	}

	@Operation(summary = "Upload User Files in Directory", description = "Upload User Files in Directory")
	@RequestMapping(value = "/uploadUserFiles", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> uploadUserFiles(@RequestParam("transactionId") Long transactionId,
			@RequestParam("files") MultipartFile[] files) throws IOException {
		LOGGER.info("UserController :: uploadUserFiles() called......");
		return this.userService.uploadUserFiles(transactionId, files);
	}

	@Operation(summary = "Fetch user by id", description = "Fetch user by id")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> fetchById(@PathVariable("id") Long id) {
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.userService.fetchById(id)), HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Search users By input", description = "Search users By input")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> searchUserByInput(@RequestBody UserSearchInputVo inputVo) {
		LOGGER.info("UserController :: searchByInput() called......");
		return this.userService.searchUserByInput(inputVo);
	}

	@Operation(summary = "Update User status", description = "Update User status")
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> updateStatus(@RequestBody ProductInputVo input) {
		LOGGER.info("UserController :: save() called......");
		return this.userService.updateStatus(input);
	}

	@Operation(summary = "Update User", description = "Update User")
	@PutMapping
	public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserDto input) {
		LOGGER.info("UserController :: save() called......");
		return new ResponseEntity<>(
				new ApiResponse(200, "UPDATED", "Updated Successfully", this.mutateUserService.update(input)),
				HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Search user activity By input", description = "Search users activity By input")
	@RequestMapping(value = "/searchUserActivityByInput", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> searchUserActivityByInput(@RequestBody UserActivitySearchInputVo inputVo) {
		LOGGER.info("UserController :: searchUserActivityByInput() called......");
		return this.userService.searchUserActivityByInput(inputVo);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Search user activity list By input in admin panel", description = "Search user activity list By input in admin panel")
	@RequestMapping(value = "/searchUserActivityList", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> searchUserActivityList(@RequestBody UserActivitySearchInputVo inputVo) {
		LOGGER.info("UserController :: searchUserActivityList() called......");
		return this.userService.searchUserActivityList(inputVo);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Save a user activity. Ex : Store checkout, Mark leave", description = "Save a single activity")
	@RequestMapping(value = "/userActivity", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> userActivity(@RequestBody UserActivityRegisterVo input) {
		LOGGER.info("UserController :: userActivity() called......");
		return this.userService.userActivity(input);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Save a user(BA) target on product category in amount", description = "Save a user(BA) target on product category in amount")
	@RequestMapping(value = "/userTargetSet", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> userTargetSet(@RequestBody UserTargetVo input) {
		LOGGER.info("UserController :: userTargetSet() called......");
		return this.userService.userTargetSet(input);
	}

	@RequestMapping(value = "/test/ac", method = RequestMethod.GET)
	public void test() {
		LOGGER.info("UserController :: userTargetSet() called......");
		this.userService.storeLogoutLoggedInUsers(new Date());
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "userDateWiseActivity", description = "userDateWiseActivity")
	@RequestMapping(value = "/userDateWiseActivity", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> userDateWiseActivity(@RequestBody UserActivitySearchInputVo input) {
		LOGGER.info("UserController :: userDateWiseActivity() called......");
		return this.userService.userDateWiseActivity(input);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "release User", description = "release User")
	@RequestMapping(value = "/releaseUser", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> releaseUser(@RequestBody BAReleaseInputVo input) {
		LOGGER.info("UserController :: releaseUser() called......");
		return this.userService.releaseUser(input);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "baActiveInactiveCount", description = "baActiveInactiveCount")
	@RequestMapping(value = "/baActiveInactiveCount", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> baActiveInactiveCount(@RequestParam("currDate") String currDate) {
		return userService.baActiveInactiveCount(currDate);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "active outlet associations", description = "Get all the active associations of a user")
	@RequestMapping(value = "/activeOutletAssociations", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> activeAssociations(@RequestParam("userId") Long userId) {
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully",
				userService.getActiveOutletAssociations(userId)), HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "active outlet associations", description = "Get all the active associations of a user")
	@RequestMapping(value = "/removeAssociation", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> removeAssociation(@RequestParam("asscId") Long asscId) {
		mutateUserService.removeOutletAssociation(asscId);
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully", null), HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "getSrUserList", description = "Get all the active seniors of a user")
	@RequestMapping(value = "/getSrUserList", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse> getSrUserList(@RequestParam("soId") Long soId) {
		return userService.getSrUserList(soId);
	}

//    @PreAuthorize("isAuthenticated()")
//    @Operation(
//            summary = "Count BA Under a BDE",
//            description = " Get No of BA Under a BDE"
//    )
//    @RequestMapping(value = "/countBaOfABde/{userId}/{date}", method = RequestMethod.GET)
//    public ResponseEntity<ApiResponse> countBaOfaBde(@PathVariable("userId") Long userId,
//                                                     @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
//        return new ResponseEntity<ApiResponse>(
//                new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.userService.countBaUnderABde(userId, date)), HttpStatus.OK);
//    }

//
//    @PreAuthorize("isAuthenticated()")
//    @Operation(summary = "Ba List Under Bde", description = "Ba Details List Under Bde")
//    @RequestMapping(value = "/baListUnderBde", method = RequestMethod.POST)
//    public ResponseEntity<ApiResponse> listOfBaUnderBde(@RequestBody BaListOfABdeVo input, @RequestParam("activity") ActivityEnum activity) {
//        return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully", this.baListUnderBde.baListUnderBde(input), baListUnderBde.countBaUnderBde(input)), HttpStatus.OK);
//    }

//    @PreAuthorize("isAuthenticated()")
//    @Operation(summary = "Sum of Purchase and Sale of a Ba under Bde",
//            description = "Sum of Purchase and Sale of a Ba under Bde")
//    @RequestMapping(value = "/sumOfPurchaseAndSale", method = RequestMethod.POST)
//    public ResponseEntity<ApiResponse> sumOfPurchaseAndSaleOfABa(@RequestBody SumOfPurchaseAndSaleInputVo input) {
//        LOGGER.info("UserController :: sumOfPurchaseAndSaleOfABa() called ......... ");
//        return this.userService.sumOfPurchaseAndSaleOfBA(input);
//    }

//    @PreAuthorize("isAuthenticated()")
//    @Operation(summary = "leave and officeWork and attendance count  under BDE", description = "leave, officework ,attendence count  under BDE")
//    @RequestMapping(value = "/activityCountOfBaUnderBde", method = RequestMethod.GET)
//    public ResponseEntity<ApiResponse> activityCountOfBaUnderBde(@RequestParam("currDate") String currDate) {
//        return userService.activityCountOfBaUnderBde(currDate);
//    }

//    @PreAuthorize("isAuthenticated()")
//    @Operation(summary = "Activity List of BA under BDE", description = "Activity List of BA under BDE")
//    @RequestMapping(value = "/ListOfActivityBaUnderBde", method = RequestMethod.POST)
//
//    public ResponseEntity<ApiResponse> listOfBaActivity(@RequestBody ListOfBaActivityInputVo input) {
//        LOGGER.info("UserController :: listOfBaActivity() :: ........ ");
//        return this.userService.listOfBaActivity(input);
//    }

//    @PreAuthorize("isAuthenticated()")
//    @Operation(summary = "Ba details dash board", description = "Ba details dashboard with total sale and total purchase")
//    @RequestMapping(value = "/baId/{baId}", method = RequestMethod.GET)
//
//    public ResponseEntity<ApiResponse> fetchBaTotalPurchaseSaleById(@PathVariable("baId") Long id) {
////    return new ResponseEntity<ApiResponse>(
////            new ApiResponse(200,"Success","Fetch Successfull", this.userService.fetchBaTotalPurchaseSaleById(id)),
////            HttpStatus.OK);
//        return this.userService.fetchBaTotalPurchaseSaleById(id);
//
//    }
}