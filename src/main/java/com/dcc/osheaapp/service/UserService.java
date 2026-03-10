package com.dcc.osheaapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.common.model.Password;
import com.dcc.osheaapp.common.service.Util;
import com.dcc.osheaapp.common.service.storage.FileMetadata;
import com.dcc.osheaapp.common.service.storage.FileResponse;
import com.dcc.osheaapp.common.service.storage.S3StorageService;
import com.dcc.osheaapp.config.JwtTokenUtil;
import com.dcc.osheaapp.repository.IActivityCountOfaBaUnderBde;
import com.dcc.osheaapp.repository.IBaDetailsDashboardRepo;
import com.dcc.osheaapp.repository.IBaUnderABdeRepository;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.IFormMediaMappingRepository;
import com.dcc.osheaapp.repository.IListOfBaActivity;
import com.dcc.osheaapp.repository.IOutletRepository;
import com.dcc.osheaapp.repository.IOutletUserMappingRepository;
import com.dcc.osheaapp.repository.ISumOfPurchaseAndSaleOfABa;
import com.dcc.osheaapp.repository.IUserActivityListRepository;
import com.dcc.osheaapp.repository.IUserActivityRepository;
import com.dcc.osheaapp.repository.IUserAssotoationRepository;
import com.dcc.osheaapp.repository.IUserCredRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.repository.IUserSearchOutputRepository;
import com.dcc.osheaapp.repository.IUserTargetRepository;
import com.dcc.osheaapp.repository.IUserTypeRepository;
import com.dcc.osheaapp.vo.AuthenticationResponseDto;
import com.dcc.osheaapp.vo.BAReleaseInputVo;
import com.dcc.osheaapp.vo.BaDetailsDashboardVo;
import com.dcc.osheaapp.vo.FormMediaMappingVo;
import com.dcc.osheaapp.vo.ListOfBaActivityInputVo;
import com.dcc.osheaapp.vo.ListOfBaActivityOutputVo;
import com.dcc.osheaapp.vo.OutletUserMappingVo;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.ProductInputVo;
import com.dcc.osheaapp.vo.RefreshToken;
import com.dcc.osheaapp.vo.SignUpVo;
import com.dcc.osheaapp.vo.SumOfPurchaseAndSaleInputVo;
import com.dcc.osheaapp.vo.SumOfTotalPurchaseAndSaleOutputVo;
import com.dcc.osheaapp.vo.UserActivityListVo;
import com.dcc.osheaapp.vo.UserActivityOPVo;
import com.dcc.osheaapp.vo.UserActivityRegisterVo;
import com.dcc.osheaapp.vo.UserActivitySearchInputVo;
import com.dcc.osheaapp.vo.UserAssotiationDtlVo;
import com.dcc.osheaapp.vo.UserCredVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.UserSearchInputVo;
import com.dcc.osheaapp.vo.UserSearchOutputVo;
import com.dcc.osheaapp.vo.UserTargetVo;
import com.dcc.osheaapp.vo.dto.BaZoneDto;
import com.dcc.osheaapp.vo.dto.UpdateUserDto;
import com.dcc.osheaapp.vo.views.OutletUserView;

@Service
public class UserService {

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);
  private final IUserCredRepository userCredRepository;
  private final JwtTokenUtil jwtTokenUtil;
  private final IUserDetailsRepository iUserDetailsRepository;
  private final IOutletUserMappingRepository iOutletUserMappingRepository;
  private final IUserAssotoationRepository iUserAssotoationRepository;
  private final IUserActivityRepository iUserActivityRepository;
  private final IFormMediaMappingRepository iFormMediaMappingRepository;
  private final IUserSearchOutputRepository iUserSearchOutputRepository;
  private final IOutletRepository iOutletRepository;
  private final IUserCredRepository iUserCredRepository;
  private final IUserActivityListRepository iUserActivityListRepository;
  private final IUserTargetRepository iUserTargetRepository;
  private final IDropdownMastereRepository iDropdownMastereRepository;
  private final IUserTypeRepository iUserTypeRepository;

  private final IBaUnderABdeRepository iBaUnderABdeRepository;

  private final ISumOfPurchaseAndSaleOfABa iSumOfPurchaseAndSaleOfABa;

  private final IActivityCountOfaBaUnderBde iActivityCountOfaBaUnderBde;

  private final IListOfBaActivity iListOfBaActivity;

  private final IBaDetailsDashboardRepo iBaDetailsDashboardRepo;

  private static BaZoneDto baZoneDto;

  @Value("${file.selfieUploadFolder}")
  private String selfieUploadFolder;

  @Autowired
  private S3StorageService s3StorageService;

  @Value("${ba.userCount}")
  private int baUserCount;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired
  private Util util;

  @Autowired
  public UserService(IUserCredRepository userCredRepository, JwtTokenUtil jwtTokenUtil,
      IUserDetailsRepository iUserDetailsRepository, IOutletUserMappingRepository iOutletUserMappingRepository,
      IUserAssotoationRepository iUserAssotoationRepository, IUserActivityRepository iUserActivityRepository,
      IFormMediaMappingRepository iFormMediaMappingRepository,
      IUserSearchOutputRepository iUserSearchOutputRepository, IOutletRepository iOutletRepository,
      IUserCredRepository iUserCredRepository, IUserActivityListRepository iUserActivityListRepository,
      IUserTargetRepository iUserTargetRepository, IDropdownMastereRepository iDropdownMastereRepository,
      IUserTypeRepository iUserTypeRepository, IBaUnderABdeRepository iBaUnderABdeRepository,
      ISumOfPurchaseAndSaleOfABa iSumOfPurchaseAndSaleOfABa,
      IActivityCountOfaBaUnderBde iActivityCountOfaBaUnderBde, IListOfBaActivity iListOfBaActivity,
      IBaDetailsDashboardRepo iBaDetailsDashboardRepo, S3StorageService s3StorageService) {
    this.userCredRepository = userCredRepository;
    this.jwtTokenUtil = jwtTokenUtil;
    this.iUserDetailsRepository = iUserDetailsRepository;
    this.iOutletUserMappingRepository = iOutletUserMappingRepository;
    this.iUserAssotoationRepository = iUserAssotoationRepository;
    this.iUserActivityRepository = iUserActivityRepository;
    this.iFormMediaMappingRepository = iFormMediaMappingRepository;
    this.iUserSearchOutputRepository = iUserSearchOutputRepository;
    this.iOutletRepository = iOutletRepository;
    this.iUserCredRepository = iUserCredRepository;
    this.iUserActivityListRepository = iUserActivityListRepository;
    this.iUserTargetRepository = iUserTargetRepository;
    this.iDropdownMastereRepository = iDropdownMastereRepository;
    this.iUserTypeRepository = iUserTypeRepository;
    this.iBaUnderABdeRepository = iBaUnderABdeRepository;
    this.iSumOfPurchaseAndSaleOfABa = iSumOfPurchaseAndSaleOfABa;
    this.iActivityCountOfaBaUnderBde = iActivityCountOfaBaUnderBde;
    this.iListOfBaActivity = iListOfBaActivity;
    this.iBaDetailsDashboardRepo = iBaDetailsDashboardRepo;
    this.s3StorageService = s3StorageService;
  }

  // private Long loginUserId = loginUserId();

  public UserCredVo findByUsername(String username) {
    LOGGER.info("UserService :: findByUsername :: called :: username ::" + username);
    UserCredVo userVo = userCredRepository.findByUsername(username);
    LOGGER.info("UserService :: findByUsername :: Exiting...");
    return userVo;
  }

  public ResponseEntity<ApiResponse> signup(SignUpVo signUpVo) {
    LOGGER.info("UserService :: register() called...");
    ResponseEntity<ApiResponse> apiResponse = null;
    UserCredVo user = new UserCredVo();

    try {
      String base64passworddecrypt = signUpVo.getPassword();
      String encryptedPassword = Password.getSaltedHash(base64passworddecrypt);
      user.setPassword(encryptedPassword);
      user.setUsername(signUpVo.getUsername());
      LOGGER.info("UserService :: user details :: " + user);
      userCredRepository.save(user);

      apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "User created successfully.", null),
          HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.error("UserService :: signUp() :: ERROR :: " + e.getMessage());
      apiResponse = new ResponseEntity<>(new ApiResponse(400, "ERROR", "Error while processing request.", null),
          HttpStatus.BAD_REQUEST);
    }

    LOGGER.info("UserService :: signUp() exit...");
    return apiResponse;
  }

  public ResponseEntity<ApiResponse> register(UserDetailsVo input) {
    LOGGER.info("UserService :: register() called...");
    ResponseEntity<ApiResponse> apiResponse = null;

    try {
      // Contact number cant be duplicate
      UserDetailsVo findExixtingContactNoOfUser = iUserDetailsRepository
          .findExistingContactNoOfUser(input.getContactNumber());
      if (null != findExixtingContactNoOfUser) {
        return new ResponseEntity<ApiResponse>(
            new ApiResponse(406, "not_accptable", "This phone number already exists in system.", null),
            HttpStatus.NOT_ACCEPTABLE);
      }
      String userType = input.getUserType().getUserType();
      String username = "";
      if (userType.equals("BA")) {
        if (input.getAssotiateOutlet().size() > 1) {
          return new ResponseEntity<ApiResponse>(
              new ApiResponse(406, "not_accptable", "BA can be assigened to only one Outlet", null),
              HttpStatus.NOT_ACCEPTABLE);
        } else {
          // One Outlet number cant be assigned for more than one BA
          LOGGER.info(
              "UserService :: Associated outlet details :: " + input.getAssotiateOutlet().get(0).getId());
          List<OutletUserMappingVo> findExistingAssociatedOutlet = iOutletUserMappingRepository
              .findExistingAssociatedOutlet(input.getAssotiateOutlet().get(0).getId());
          if (null != findExistingAssociatedOutlet) {
            List<UserCredVo> existingUserDetails = iUserCredRepository
                .findExistingUserforAssociatedOutlet(input.getAssotiateOutlet().get(0).getId());
            LOGGER.info("UserService :: Existing User Details :: " + existingUserDetails);
            if (null != existingUserDetails) {
              for (UserCredVo existingUserName : existingUserDetails) {
                // Split the existingUserName by "@" symbol
                String[] tempUserCode = existingUserName.getUsername().split("@");
                String BACode = "";
                // Ensure that there are at least two parts (before and after the "@")
                if (tempUserCode.length >= 2) {
                  BACode = tempUserCode[0];
                  LOGGER.info("UserService :: Existing BACode :: " + BACode);
                } else {
                  LOGGER.info(
                      "UserService :: Existing BACode :: Invalid User Name format in user_credential table"
                          + existingUserName);
                  BACode = existingUserName.getUsername();
                }
                return new ResponseEntity<ApiResponse>(
                    new ApiResponse(406, "not_accptable",
                        BACode + " is already assigned to this outlet.", null),
                    HttpStatus.NOT_ACCEPTABLE);
              }
            }
          }
        }
        // Changed on 04 jan 2024 - SD
        if (null != input.getUserCode() && !input.getUserCode().isEmpty()) {
          username = input.getUserCode();
        } else {
          int n = iUserDetailsRepository.countUser(input.getUserType().getId(),
              input.getProductDivision().getId());
          LOGGER.info("Next baUserCount ============ >> " + baUserCount);
          String num = String.format("%05d", n + baUserCount);
          username = userType + num;
        }
        username = username + "@" + input.getContactNumber();
      } else {
        int n = iUserDetailsRepository.countSalesTeamUser(input.getProductDivision().getId());
        String num = String.format("%04d", n);
        String div = (input.getProductDivision().getId() == 14) ? "OSH"
            : (input.getProductDivision().getId() == 14) ? "LOU" : "";
        username = div + num;
      }
      String base64passworddecrypt = input.getContactNumber();
      String encryptedPassword = Password.getSaltedHash(base64passworddecrypt);
      UserCredVo user = new UserCredVo(encryptedPassword, username, true);
      input.setUserCred(user);
      input.setIsActive(true);
      input.setCreatedOn(new Date());
      input.setDateOfJoining(input.getDateOfJoining());
      input.setUpdatedOn(new Date());
      LOGGER.info("UserService :: user details :: " + input);
      UserDetailsVo saved = iUserDetailsRepository.save(input);

      if (null != saved && null != input.getAssotiateOutlet()) { // for BA and SO, need outlet mapping //&&
        // (userType.equals("BA") ||
        // userType.equals("SO"))
        for (OutletVo outlet : input.getAssotiateOutlet()) {
          OutletUserMappingVo mapping = new OutletUserMappingVo(outlet, saved.getId(),
              input.getDateOfJoining(), null, true);
          OutletUserMappingVo savedMapping = iOutletUserMappingRepository.save(mapping);
          LOGGER.info("UserService :: mapping details :: " + savedMapping);
        }

        UserAssotiationDtlVo assotiation = new UserAssotiationDtlVo(saved, input.getReportingTo(), true);
        UserAssotiationDtlVo savedAssotiation = iUserAssotoationRepository.save(assotiation);
        LOGGER.info("UserService :: assotiation details :: " + savedAssotiation);
      }

      apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "User created successfully.", saved),
          HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("UserService :: signUp() :: ERROR :: " + e.getMessage());
      apiResponse = new ResponseEntity<>(new ApiResponse(400, "ERROR", "Error while processing request.", null),
          HttpStatus.BAD_REQUEST);
    }

    LOGGER.info("UserService :: signUp() exit...");
    return apiResponse;
  }
  // public UserDetailsVo registerAlt(UserDetailsVo input) {
  // LOGGER.info("UserService :: register() called...");
  // // Contact number cant be duplicate
  // UserDetailsVo existingUser =
  // iUserDetailsRepository.findByContactNumber(input.getContactNumber()).orElseThrow(()
  // -> new
  // OjbException(ErrorCode.ALREADY_EXIST, new Object[]{"Phone Number"}));
  // String userType = input.getUserType().getUserType();
  // String username = "";
  //
  //
  //
  // if(userType.equals("BA")) {
  // if(input.getAssotiateOutlet().size() > 1) {
  // throw new OjbException(ErrorCode.INVALID_MULTIPLE_OUTLET_ASSOCIATION, new
  // Object[]{});
  // }else {
  // // One Outlet number cant be assigned for more than one BA
  // LOGGER.info("UserService :: Associated outlet details :: " +
  // input.getAssotiateOutlet().get(0).getId());
  // List<OutletUserMappingVo> findExistingAssociatedOutlet =
  // iOutletUserMappingRepository.findExistingAssociatedOutlet(input.getAssotiateOutlet().get(0).getId());
  // if(null != findExistingAssociatedOutlet &&
  // findExistingAssociatedOutlet.size() > 0) {
  // findExistingAssociatedOutlet.forEach((e) -> {
  // UserDetailsVo user =
  // iUserDetailsRepository.findByIdAndIsActiveAndUserType(e.getAssotiatedUser(),
  // true,
  // "BA").ifPresent((m) -> {
  // String BACode = getBaCode(e.getAssotiatedUser());
  // throw new OjbException(ErrorCode.OUTLET_ALREADY_MAPPED, new Object[]
  // {BACode});
  // });
  // });
  //
  //
  // getBaCode(input);
  // }
  // }
  // int n = iUserDetailsRepository.countUser(input.getUserType().getId(),
  // input.getProductDivision().getId());
  // String num = String.format("%07d", n);
  // username = userType + num ;
  // username = username +"@"+input.getContactNumber();
  // } else {
  // int n =
  // iUserDetailsRepository.countSalesTeamUser(input.getProductDivision().getId());
  // String num = String.format("%04d", n);
  // String div = (input.getProductDivision().getId() == 14) ?
  // "OSH" : (input.getProductDivision().getId() == 14) ? "LOU" : "";
  // username = div + num ;
  // }
  // String base64passworddecrypt = input.getContactNumber();
  // String encryptedPassword = Password.getSaltedHash(base64passworddecrypt);
  // UserCredVo user = new UserCredVo(encryptedPassword, username, true);
  // input.setUserCred(user);
  // input.setIsActive(true);
  // input.setCreatedOn(new Date());
  // input.setDateOfJoining(input.getDateOfJoining());
  // input.setUpdatedOn(new Date());
  // LOGGER.info("UserService :: user details :: " + input);
  // UserDetailsVo saved = iUserDetailsRepository.save(input);
  //
  // if (null != saved && null != input.getAssotiateOutlet()) { // for BA and SO,
  // need outlet
  // mapping //&&
  // // (userType.equals("BA") ||
  // // userType.equals("SO"))
  // for (OutletVo outlet : input.getAssotiateOutlet()) {
  // OutletUserMappingVo mapping = new OutletUserMappingVo(outlet, saved.getId(),
  // input.getDateOfJoining(), null, true);
  // OutletUserMappingVo savedMapping =
  // iOutletUserMappingRepository.save(mapping);
  // LOGGER.info("UserService :: mapping details :: " + savedMapping);
  // }
  //
  // UserAssotiationDtlVo assotiation = new UserAssotiationDtlVo(saved,
  // input.getReportingTo(),
  // true);
  // UserAssotiationDtlVo savedAssotiation =
  // iUserAssotoationRepository.save(assotiation);
  // LOGGER.info("UserService :: assotiation details :: " + savedAssotiation);
  // }
  //
  // apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "User
  // created
  // successfully.", saved),
  // HttpStatus.OK);
  //
  // e.printStackTrace();
  // LOGGER.error("UserService :: signUp() :: ERROR :: " + e.getMessage());
  // apiResponse = new ResponseEntity<>(new ApiResponse(400, "ERROR", "Error while
  // processing
  // request.", null),
  // HttpStatus.BAD_REQUEST);
  //
  // LOGGER.info("UserService :: signUp() exit...");
  // return apiResponse;
  // }
  //
  // private void getBaCode(Long userId) {
  // List<UserCredVo> existingUserDetails =
  // iUserCredRepository.findExistingUserforAssociatedOutlet(input.getAssotiateOutlet().get(0).getId());
  // LOGGER.info("UserService :: Existing User Details :: " +
  // existingUserDetails);
  // if(null != existingUserDetails) {
  // for(UserCredVo existingUserName : existingUserDetails) {
  // // Split the existingUserName by "@" symbol
  // String[] tempUserCode = existingUserName.getUsername().split("@");
  // String BACode = "";
  // // Ensure that there are at least two parts (before and after the "@")
  // if (tempUserCode.length >= 2) {
  // BACode = tempUserCode[0];
  // LOGGER.info("UserService :: Existing BACode :: " + BACode);
  // } else {
  // LOGGER.info("UserService :: Existing BACode :: Invalid User Name format in
  // user_credential
  // table" + existingUserName);
  // BACode = existingUserName.getUsername();
  // }
  // }
  // }
  // }

  public ResponseEntity<ApiResponse> registerUpload(UserDetailsVo input) {
    LOGGER.info("UserService :: register() called...");
    ResponseEntity<ApiResponse> apiResponse = null;

    try {
      // Contact number cant be duplicate
      UserDetailsVo findExixtingContactNoOfUser = iUserDetailsRepository
          .findExistingContactNoOfUser(input.getContactNumber());
      if (null != findExixtingContactNoOfUser) {
        return new ResponseEntity<ApiResponse>(
            new ApiResponse(406, "not_accptable", "This phone number already exists in system.", null),
            HttpStatus.NOT_ACCEPTABLE);
      }
      String userType = input.getUserType().getUserType();
      String username = "";
      if (userType.equals("BA")) {
        if (input.getAssotiateOutlet().size() > 1) {
          return new ResponseEntity<ApiResponse>(
              new ApiResponse(406, "not_accptable", "BA can be assigened to only one Outlet", null),
              HttpStatus.NOT_ACCEPTABLE);
        } else {
          // One Outlet number cant be assigned for more than one BA
          LOGGER.info(
              "UserService :: Associated outlet details :: " + input.getAssotiateOutlet().get(0).getId());
          List<OutletUserMappingVo> findExistingAssociatedOutlet = iOutletUserMappingRepository
              .findExistingAssociatedOutlet(input.getAssotiateOutlet().get(0).getId());
          if (null != findExistingAssociatedOutlet) {
            List<UserCredVo> existingUserDetails = iUserCredRepository
                .findExistingUserforAssociatedOutlet(input.getAssotiateOutlet().get(0).getId());
            LOGGER.info("UserService :: Existing User Details :: " + existingUserDetails);
            if (null != existingUserDetails) {
              for (UserCredVo existingUserName : existingUserDetails) {
                // Split the existingUserName by "@" symbol
                String[] tempUserCode = existingUserName.getUsername().split("@");
                String BACode = "";
                // Ensure that there are at least two parts (before and after the "@")
                if (tempUserCode.length >= 2) {
                  BACode = tempUserCode[0];
                  LOGGER.info("UserService :: Existing BACode :: " + BACode);
                } else {
                  LOGGER.info(
                      "UserService :: Existing BACode :: Invalid User Name format in user_credential table"
                          + existingUserName);
                  BACode = existingUserName.getUsername();
                }
                return new ResponseEntity<ApiResponse>(
                    new ApiResponse(406, "not_accptable",
                        BACode + " is already assigned to this outlet.", null),
                    HttpStatus.NOT_ACCEPTABLE);
              }
            }
          }
        }
        int n = iUserDetailsRepository.countUser(input.getUserType().getId(),
            input.getProductDivision().getId());
        LOGGER.info("Next baUserCount ============ >> " + baUserCount);
        String num = String.format("%05d", n + baUserCount);
        username = input.getBaCodeXl();
        username = username + "@" + input.getContactNumber();
      } else {
        int n = iUserDetailsRepository.countSalesTeamUser(input.getProductDivision().getId());
        String num = String.format("%04d", n);
        String div = (input.getProductDivision().getId() == 14) ? "OSH"
            : (input.getProductDivision().getId() == 14) ? "LOU" : "";
        username = div + num;
      }
      String base64passworddecrypt = input.getContactNumber();
      String encryptedPassword = Password.getSaltedHash(base64passworddecrypt);
      UserCredVo user = new UserCredVo(encryptedPassword, username, true);
      input.setUserCred(user);
      input.setIsActive(true);
      input.setCreatedOn(new Date());
      input.setDateOfJoining(input.getDateOfJoining());
      input.setUpdatedOn(new Date());
      LOGGER.info("UserService :: user details :: " + input);
      UserDetailsVo saved = iUserDetailsRepository.save(input);

      if (null != saved && null != input.getAssotiateOutlet()) { // for BA and SO, need outlet mapping //&&
        // (userType.equals("BA") ||
        // userType.equals("SO"))
        for (OutletVo outlet : input.getAssotiateOutlet()) {
          OutletUserMappingVo mapping = new OutletUserMappingVo(outlet, saved.getId(),
              input.getDateOfJoining(), null, true);
          OutletUserMappingVo savedMapping = iOutletUserMappingRepository.save(mapping);
          LOGGER.info("UserService :: mapping details :: " + savedMapping);
        }

        UserAssotiationDtlVo assotiation = new UserAssotiationDtlVo(saved, input.getReportingTo(), true);
        UserAssotiationDtlVo savedAssotiation = iUserAssotoationRepository.save(assotiation);
        LOGGER.info("UserService :: assotiation details :: " + savedAssotiation);
      }

      apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "User created successfully.", saved),
          HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("UserService :: signUp() :: ERROR :: " + e.getMessage());
      apiResponse = new ResponseEntity<>(new ApiResponse(400, "ERROR", "Error while processing request.", null),
          HttpStatus.BAD_REQUEST);
    }

    LOGGER.info("UserService :: signUp() exit...");
    return apiResponse;
  }

  public UserDetailsVo update(UpdateUserDto input) {
    LOGGER.info("UserService :: update() called...");
    UserDetailsVo user = iUserDetailsRepository.findById(input.getId())
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "User" }));

    UserCredVo updateUserCred = new UserCredVo();

    // contact no duplicate check, user update not done, validation error
    if (!user.getContactNumber().contentEquals(input.getContactNumber())) {
      UserDetailsVo findExistingContactNoOfUser = iUserDetailsRepository
          .findExistingContactNoOfUser(input.getContactNumber());
      if (null != findExistingContactNoOfUser) {
        throw new OjbException(ErrorCode.ALREADY_EXIST, new Object[] { "Phone number" });
      } else {
        updateUserCred = updateUserCredOnUserUpdate(input);
        LOGGER.info("UserService :: updateUserCred details :: " + updateUserCred);
      }
    }

    BeanUtils.copyProperties(input, user);
    user.setUpdatedOn(new Date());
    LOGGER.info("UserService :: user details :: " + input);
    // if no validation for contact no, then save user_details
    UserDetailsVo saved = iUserDetailsRepository.save(user);

    // after updating user details, for BA user, update outlet
    List<OutletUserMappingVo> updateAssociatedOutlet = new ArrayList<>();
    if (user.getUserType().getId() == 4) {
      List<OutletUserMappingVo> associatedOutletByUserId = iOutletUserMappingRepository
          .findAssociatedOutletByUserId(input.getId());

      LOGGER.info("UserService :: user.getAssotiateOutlet() :: "
          + associatedOutletByUserId.get(0).getOutlet().getId());
      LOGGER.info("UserService :: input.getAssotiateOutlet() :: " + input.getAssotiateOutlet().get(0).getId());
      if (!associatedOutletByUserId.get(0).getOutlet().getId().toString()
          .contentEquals(input.getAssotiateOutlet().get(0).getId().toString())) {
        updateAssociatedOutlet = updateAssociatedOutletOnUserUpdate(input, saved);
        LOGGER.info("UserService :: updateAssociatedOutlet details :: " + updateAssociatedOutlet);
      }
    }
    LOGGER.info("UserService :: update() exit...");
    return fetchById(saved.getId());
  }

  private UserCredVo updateUserCredOnUserUpdate(UpdateUserDto input) {
    UserDetailsVo user = iUserDetailsRepository.findById(input.getId())
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "User" }));
    // Update User cred
    UserCredVo userCred = iUserCredRepository.findExistingUserCredforUpdate(user.getUserCred().getId());
    String previousUserName = userCred.getUsername();
    String previousEncryptedPassword = userCred.getPassword();
    String[] tempUserCred = userCred.getUsername().split("@");
    String PreviousContactNumber = "";
    if (tempUserCred.length >= 2) {
      PreviousContactNumber = tempUserCred[1];
    } else {
      LOGGER.info(
          "UserService :: Update User :: Previous Contact Number :: Invalid User Name format in user_credential table"
              + userCred);
    }
    String NewContactNumber = input.getContactNumber();
    String newEncryptedPassword = "";
    try {
      String base64passworddecrypt = input.getContactNumber();
      newEncryptedPassword = Password.getSaltedHash(base64passworddecrypt);
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("UserService :: signUp() :: ERROR :: " + e.getMessage());
    }
    // Updated UserName in user_credential table
    String updatedUserName = previousUserName.replace(PreviousContactNumber, NewContactNumber);
    String updatedEncryptedPassword = previousEncryptedPassword.replace(previousEncryptedPassword,
        newEncryptedPassword);

    userCred.setUsername(updatedUserName);
    userCred.setPassword(updatedEncryptedPassword);

    return userCred;
  }

  private List<OutletUserMappingVo> updateAssociatedOutletOnUserUpdate(UpdateUserDto input,
      UserDetailsVo userDetailsVo) {
    List<OutletUserMappingVo> findExistingAssociatedOutlet = new ArrayList<>();
    List<OutletUserMappingVo> updateAssociatedOutletOnUserUpdate = new ArrayList<>();
    UserDetailsVo user = iUserDetailsRepository.findById(input.getId())
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "User" }));
    List<OutletUserMappingVo> associatedOutletByUserId = iOutletUserMappingRepository
        .findAssociatedOutletByUserId(input.getId());
    // One Outlet number cant be assigned for more than one BA
    LOGGER.info("UserService :: New outlet details :: " + input.getAssotiateOutlet().get(0).getId());
    LOGGER.info("UserService :: Previous outlet details :: " + associatedOutletByUserId.get(0).getOutlet().getId());
    if (associatedOutletByUserId.get(0).getOutlet().getId().toString()
        .contentEquals(input.getAssotiateOutlet().get(0).getId().toString())) {
      // Associated Outlet cant be duplicate
      findExistingAssociatedOutlet = iOutletUserMappingRepository
          .findExistingAssociatedOutlet(input.getAssotiateOutlet().get(0).getId());

      String BACode = "";
      if (null != findExistingAssociatedOutlet) {
        List<UserCredVo> existingUserDetails = iUserCredRepository
            .findExistingUserforAssociatedOutlet(input.getAssotiateOutlet().get(0).getId());
        LOGGER.info("UserService :: Existing User Details :: " + existingUserDetails);
        if (null != existingUserDetails) {
          for (UserCredVo existingUserName : existingUserDetails) {
            // Split the existingUserName by "@" symbol
            String[] tempUserCode = existingUserName.getUsername().split("@");
            // Ensure that there are at least two parts (before and after the "@")
            if (tempUserCode.length >= 2) {
              BACode = tempUserCode[0];
              LOGGER.info("UserService :: Existing BACode :: " + BACode);
            } else {
              LOGGER.info(
                  "UserService :: Existing BACode :: Invalid User Name format in user_credential table"
                      + existingUserName);
              BACode = existingUserName.getUsername();
            }
          }
        }
        updateAssociatedOutletOnUserUpdate = findExistingAssociatedOutlet;
        throw new OjbException(ErrorCode.OUTLET_ALREADY_MAPPED, new Object[] { BACode });
      }
    } else {
      if (null != user.getAssotiateOutlet()) {
        List<OutletUserMappingVo> newMapping = reassociateOutlets(user, userDetailsVo);
        iOutletUserMappingRepository.saveAll(newMapping);
        updateAssociatedOutletOnUserUpdate = newMapping;
      }
      UserAssotiationDtlVo savedAssotiation = reassociateUser(user, userDetailsVo);
      LOGGER.info("UserService :: assotiation details :: " + savedAssotiation);
    }

    return updateAssociatedOutletOnUserUpdate;
  }

  private UserAssotiationDtlVo reassociateUser(UserDetailsVo user, UserDetailsVo saved) {
    Optional<UserAssotiationDtlVo> userAssociation = iUserAssotoationRepository.findByUser(saved.getId(), true);
    UserAssotiationDtlVo newAssociation = null;
    if (userAssociation.isPresent()) {
      UserAssotiationDtlVo assotiationDtlVo = userAssociation.get();
      assotiationDtlVo.setIsActive(false);
      UserAssotiationDtlVo savedAssotiation = iUserAssotoationRepository.save(assotiationDtlVo);
    }
    newAssociation = new UserAssotiationDtlVo(saved, user.getReportingTo(), true);
    UserAssotiationDtlVo savedAssotiation = iUserAssotoationRepository.save(newAssociation);
    return savedAssotiation;
  }

  private List<OutletUserMappingVo> reassociateOutlets(UserDetailsVo user, UserDetailsVo saved) {
    List<OutletUserMappingVo> existingAssociatedMapping = iOutletUserMappingRepository
        .findByAssotiatedUserAndIsActive(user.getId(), true);
    List<OutletUserMappingVo> newMapping = new ArrayList<>();
    Map<Long, OutletUserMappingVo> commonOutlets = new HashMap<>();

    existingAssociatedMapping.forEach(mapping -> {
      if (user.getAssotiateOutlet().stream().map(OutletVo::getId).collect(Collectors.toList())
          .contains(mapping.getOutlet().getId())) {
        commonOutlets.put(mapping.getOutlet().getId(), mapping);
        newMapping.add(mapping);
      } else {
        mapping.setLeftOn(new Date());
        mapping.setIsActive(false);
        iOutletUserMappingRepository.save(mapping);
      }
    });

    user.getAssotiateOutlet().forEach(outlet -> {
      if (!commonOutlets.containsKey(outlet.getId())) {
        newMapping.add(new OutletUserMappingVo(outlet, saved.getId(), new Date(), null, true));
      }
    });
    return newMapping;
  }

  public ResponseEntity<ApiResponse> generateToken(SignUpVo signUpVo, HttpServletResponse response) {
    LOGGER.info("UserService :: generateToken() called...");
    ApiResponse apiResponse = null;
    boolean checkPassword = false;
    ResponseEntity<ApiResponse> responseEntity = null;

    try {
      if (signUpVo.getUsername().startsWith("BA")) {
        signUpVo.setUsername(signUpVo.getUsername() + "@" + signUpVo.getPassword());
      }
      UserCredVo user = userCredRepository.findByUsername(signUpVo.getUsername());
      if (user != null) {
        if (user.getIsActive()) {
          checkPassword = Password.check(signUpVo.getPassword(), user.getPassword());
          if (!checkPassword) {
            apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Username or password is invalid.", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
          } else {
            final String token = jwtTokenUtil.generateToken(user);
            final RefreshToken refreshToken = refreshTokenService.create(user.getUsername());

            apiResponse = new ApiResponse(200, "SUCCESS", "Token generated successfully.",
                new AuthenticationResponseDto(user.getUsername(), token, refreshToken.getToken()));
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true).domain("localhost").sameSite("Lax").path("/refresh").build();
            response.setHeader("Set-Cookie", refreshTokenCookie.toString());
            responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
          }
        } else {
          apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Sorry your account is deactivated now.", null);
          return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
      } else {
        apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Username or password is invalid.", null);
        responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception e) {
      LOGGER.error("UserService :: generateToken() :: ERROR :: " + e.getMessage());
      responseEntity = new ResponseEntity<>(
          new ApiResponse(400, "ERROR", "Error while processing request.", null), HttpStatus.BAD_REQUEST);
    }

    LOGGER.info("UserService :: generateToken() exit...");
    return responseEntity;
  }

  public ResponseEntity<ApiResponse> generateTokenBde(SignUpVo signUpVo, HttpServletResponse response) {
    LOGGER.info("UserService :: generateTokenBde() called.....");
    ApiResponse apiResponse = null;
    boolean checkPassword = false;
    ResponseEntity<ApiResponse> responseEntity = null;

    try {
      if (signUpVo.getUsername().startsWith("OSH")) {
        signUpVo.setUsername(signUpVo.getUsername());
      }
      UserCredVo user = userCredRepository.findByUsername(signUpVo.getUsername());

      if (user != null) {
        UserDetailsVo userDetls = iUserDetailsRepository.findByCreadId(user.getId());
        if (user.getIsActive()) {
          checkPassword = Password.check(signUpVo.getPassword(), user.getPassword());
          if (!checkPassword) {
            apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Username or password is invalid.", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
          } else if ((userDetls.getUserType().getId() != 12L) && (userDetls.getUserType().getId() != 2L)
              && (userDetls.getUserType().getId() != 9L) && (userDetls.getUserType().getId() != 10L)) {
            apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Username or password is invalid.", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
          } else {
            final String token = jwtTokenUtil.generateToken(user);
            final RefreshToken refreshToken = refreshTokenService.create(user.getUsername());

            apiResponse = new ApiResponse(200, "SUCCESS", "Token generated successfully.",
                new AuthenticationResponseDto(user.getUsername(), token, refreshToken.getToken()));
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true).domain("localhost").sameSite("Lax").path("/refresh").build();
            response.setHeader("Set-Cookie", refreshTokenCookie.toString());
            responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
          }
        } else {
          apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Sorry your account is deactivated now.", null);
          return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
      } else {
        apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Username and/or password is invalid.", null);
        responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception e) {
      LOGGER.error("UserService :: generateToken() :: ERROR :: " + e.getMessage());
      responseEntity = new ResponseEntity<>(
          new ApiResponse(400, "ERROR", "Error while processing request.", null), HttpStatus.BAD_REQUEST);
    }
    LOGGER.info("UserService :: generateToken() exit...");
    return responseEntity;

  }

  public ResponseEntity<ApiResponse> generateTokenSo(SignUpVo signUpVo, HttpServletResponse response) {
    LOGGER.info("UserService :: generateTokenSo() called.....");
    ApiResponse apiResponse = null;
    boolean checkPassword = false;
    ResponseEntity<ApiResponse> responseEntity = null;

    try {
      if (signUpVo.getUsername().startsWith("OSH")) {
        signUpVo.setUsername(signUpVo.getUsername());
      }
      UserCredVo user = userCredRepository.findByUsername(signUpVo.getUsername());

      if (user != null) {
        UserDetailsVo userDetls = iUserDetailsRepository.findByCreadId(user.getId());
        if (user.getIsActive()) {
          if (userDetls.getUserType().getId() == 6L || userDetls.getUserType().getId() == 7L
              || userDetls.getUserType().getId() == 8L || userDetls.getUserType().getId() == 10L
              || userDetls.getUserType().getId() == 2L || userDetls.getUserType().getId() == 13L
              || userDetls.getUserType().getId() == 3L) {

            checkPassword = Password.check(signUpVo.getPassword(), user.getPassword());
            if (!checkPassword) {
              apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Username or password is invalid.",
                  null);
              return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
            }
            // else if ((userDetls.getUserType().getId() != 12L) &&
            // (userDetls.getUserType().getId() != 2L) ) {
            // apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Username or password is
            // invalid.", null);
            // return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
            // }
            else {
              final String token = jwtTokenUtil.generateToken(user);
              final RefreshToken refreshToken = refreshTokenService.create(user.getUsername());

              apiResponse = new ApiResponse(200, "SUCCESS", "Token generated successfully.",
                  new AuthenticationResponseDto(user.getUsername(), token, refreshToken.getToken()));
              ResponseCookie refreshTokenCookie = ResponseCookie
                  .from("refreshToken", refreshToken.getToken()).httpOnly(true).domain("localhost")
                  .sameSite("Lax").path("/refresh").build();
              response.setHeader("Set-Cookie", refreshTokenCookie.toString());
              responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
            }

          } else {
            apiResponse = new ApiResponse(401, "UNAUTHORIZED", "User not authorized to Login.", null);
            responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
          }

        } else {
          apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Sorry your account is deactivated now.", null);
          return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
      } else {
        apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Username or password is invalid.", null);
        responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception e) {
      LOGGER.error("UserService :: generateToken() :: ERROR :: " + e.getMessage());
      responseEntity = new ResponseEntity<>(
          new ApiResponse(400, "ERROR", "Error while processing request.", null), HttpStatus.BAD_REQUEST);
    }
    LOGGER.info("UserService :: generateToken() exit...");
    return responseEntity;

  }

  public ResponseEntity<ApiResponse> validateToken(String token) {
    LOGGER.info("UserService :: validateToken() called......");
    ApiResponse apiResponse = null;
    ResponseEntity<ApiResponse> responseEntity = null;
    UserCredVo userVo = null;

    try {
      if (!jwtTokenUtil.isTokenExpired(token)) {
        userVo = userCredRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token));
        if (userVo != null) {
          apiResponse = new ApiResponse(200, "SUCCESS", "Token is valid.", true);
          responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
          apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Token is invalid.", false);
          responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
      } else {
        apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Token has expired.", false);
        responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception e) {
      LOGGER.error("UserService :: validateToken() :: ERROR :: " + e.getLocalizedMessage());
      responseEntity = new ResponseEntity<>(
          new ApiResponse(400, "ERROR", "Error while processing request.", false), HttpStatus.BAD_REQUEST);
    }

    LOGGER.info("UserService :: validateToken() exit...");
    return responseEntity;
  }

  public ResponseEntity<ApiResponse> refreshAccessToken(HttpServletRequest request) {
    String token = request.getHeader("X-Refresh");
    LOGGER.info("token-->> " + token);
    RefreshToken refreshToken = refreshTokenService.validate(token);
    LOGGER.info("Username-->> " + refreshToken.getUsername());
    UserCredVo user = userCredRepository.findByUsernameAlt(refreshToken.getUsername())
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "User" }));
    return new ResponseEntity<ApiResponse>(new ApiResponse(200, "SUCCESS", "GENERATED TOKEN",
        jwtTokenUtil.generateToken(user), refreshToken.getToken()), HttpStatus.OK);
  }

  public UserCredVo getUserDetails(HttpServletRequest request, String jwtToken) {
    LOGGER.info("UserService:: getUserDetails:: Entering ");

    String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
    UserCredVo userVo = userCredRepository.findByUsername(userName);

    LOGGER.info("UserService:: getUserDetails:: Exiting:: UserCredVo" + userVo);
    return userVo;
  }

  public List<UserDetailsVo> fetchAllActiveUsers() {
    LOGGER.info("UserService :: fetchAllActiveUsers() called...");
    List<UserDetailsVo> users = iUserDetailsRepository.findByIsActive(true);
    if (users != null) {
      return users;
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Users" });
    }
  }

  public List<UserDetailsVo> findUserByType(UserSearchInputVo input) {
    LOGGER.info("UserService :: findUserByType() called...");
    List<UserDetailsVo> users = iUserDetailsRepository.findUserByType(input.getUserType(), true);
    if (users != null) {
      return users;
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Users" });
    }
  }

  // TODO: More filters
  public ResponseEntity<ApiResponse> searchByInput(UserSearchInputVo inputVo) {
    LOGGER.info("UserService::searchByInput::Entering...");
    ApiResponse apiResponse = null;
    List<UserDetailsVo> getDataList = null;
    String whereClause = " and is_active = true ";
    String limitStr = "";
    String orderByStr = " order by full_name ASC ";
    if (null == inputVo.getPage() && null == inputVo.getSize()) {
      // do nothing
    } else {
      Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
      Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
      limitStr = " limit " + size + " offset " + page;
      LOGGER.info("limitStr..." + limitStr);
    }

    if (null != inputVo.getUserType()) {
      whereClause += " and user_type = " + inputVo.getUserType();
    }

    if (null != inputVo.getFormType()) {
      whereClause += " and user_type in ( select id from user_type_mst where form_type = '"
          + inputVo.getFormType() + "')";
    }
    if (null != inputVo.getUsername()) {
      whereClause += " and user_cred in ( select id from user_credential where username = '"
          + inputVo.getUsername() + "')";
    }
    if (null != inputVo.getFullName() && !inputVo.getFullName().trim().isEmpty()) {
      LOGGER.info("vo.getFullName() ------------ " + inputVo.getFullName());
      whereClause += " and full_name like '%" + inputVo.getFullName().trim() + "%' ";
    }

    if (null != inputVo.getDivisionId()) {
      whereClause += " product_division = " + inputVo.getDivisionId();
    }

    LOGGER.info("Whereclause..." + whereClause);
    getDataList = iUserDetailsRepository.searchByInput(whereClause + orderByStr, "user_details", limitStr);

    // for (FIRNewVo vo : getDataList) {
    // vo = getFullVo(vo, 0);
    // }

    // To fetch total no of data which satisfy this where clause
    int totalNo = iUserDetailsRepository.getTotalCountByInput(whereClause, "user_details").intValue();
    apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
    LOGGER.info("UserService::searchByInput::Exiting...");
    // return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    if (getDataList != null) {
      return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Users" });
    }
  }

  public ResponseEntity<ApiResponse> storeCheckin(UserActivityRegisterVo input) {
    LOGGER.info("UserService :: storeCheckin() called...");
    try {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String username = ((UserDetails) principal).getUsername();
      Long loginUserId = userCredRepository.loginUserId(username);
      LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
      if (null == loginUserId || loginUserId == 0L) {
        loginUserId = input.getCreatedBy();
        LOGGER.info(" loginUserId from input =========== > " + loginUserId);
      }

      // If marked leave then cant store checkin ============
      List<String> userActivity = iUserActivityRepository.findActivityOfUser(input.getOutlet().getId(),
          loginUserId, new Date());
      if (userActivity.contains("leave")) {
        return new ResponseEntity<ApiResponse>(
            new ApiResponse(406, "not_allowed", "You have marked as 'Leave' Today, Can't do store-checkIn"),
            HttpStatus.NOT_ACCEPTABLE);
      } else {
        String userLastActivity = iUserActivityRepository.findLastActivityOfUser(input.getOutlet().getId(),
            loginUserId, new Date());
        if (null != userLastActivity && userLastActivity.equalsIgnoreCase(input.getActivityType().name())) {
          String msg = userLastActivity.equals("store_login") ? "Logged in "
              : userLastActivity.equals("store_logout") ? "Logged out " : "";
          return new ResponseEntity<ApiResponse>(
              new ApiResponse(406, "not_allowed", "You are already " + msg + ""),
              HttpStatus.NOT_ACCEPTABLE);
        }
      }

      String filename = "";
      MultipartFile file = null;
      if (null != input.getSelfie()) {
        file = input.getSelfie();
        filename = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = FilenameUtils.getExtension(filename);
        filename = filename.replace("." + ext, "");
        filename = filename + "_" + System.currentTimeMillis() + "." + ext;
        input.setSelfieImagePath(filename);
      }
      input.setActivityTime(new Date());
      input.setCreatedBy(loginUserId);

      UserActivityRegisterVo savedData = iUserActivityRepository.save(input);
      if (savedData != null && null != input.getSelfie()) {
        String s3SelfieKeyPrefix = selfieUploadFolder;
        FileResponse s3Response = s3StorageService.upload(new FileMetadata(file, filename), s3SelfieKeyPrefix);

        String formType = "BA image";
        String tabName = "BA Selfie";

        FormMediaMappingVo savedMediaData = new FormMediaMappingVo(null, savedData.getId(), formType, tabName,
            filename, s3Response.getFileUrl(), new Date());

        savedMediaData = iFormMediaMappingRepository.save(savedMediaData);

        OutletVo outlet = iOutletRepository.findById(input.getOutlet().getId()).orElse(null);
        if (outlet != null) {
          if (outlet.getLatitude() == null || outlet.getLatitude().isEmpty())
            outlet.setLatitude(input.getLat().toString());
          if (outlet.getLongitude() == null || outlet.getLongitude().isEmpty())
            outlet.setLongitude(input.getLon().toString());
          iOutletRepository.save(outlet);
        }
        if (savedMediaData != null) {
          LOGGER.info("User Media Data saved in DB (S3) -----------" + savedMediaData);
          savedData.setSelfieImagePath(s3Response.getFileUrl());
        } else {
          LOGGER.info("User Media Data NOT saved in DB (S3) -----------" + savedMediaData);
          throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserMediaData (S3)" });
        }
      }
      savedData.setSelfie(null);
      LOGGER.info(" UserActivityRegisterVo from storeCheckin for " + input.getActivityType().name()
          + " activity ====================>" + savedData);
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
          HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserActivityRegister" });
  }

  public ResponseEntity<ApiResponse> fetchUserDetails() {
    LOGGER.info("UserService :: fetchUserDetails() called......");
    ApiResponse apiResponse = null;
    ResponseEntity<ApiResponse> responseEntity = null;
    UserDetailsVo userVo = util.loggedInUser();
    if (userVo != null) {
      UserDetailsVo userDtl = iUserDetailsRepository.findByIdAndIsActive(userVo.getId(), true)
          .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "user details" }));
      List<OutletUserMappingVo> assotiatedOutlet = iOutletUserMappingRepository
          .findByAssotiatedUserAndIsActive(userDtl.getId(), true);
      userDtl.setAssotiateOutletList(assotiatedOutlet);
      apiResponse = new ApiResponse(200, "SUCCESS", "User details found.", userDtl);
      responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
    } else {
      apiResponse = new ApiResponse(401, "UNAUTHORIZED", "Token is invalid.", false);
      responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    LOGGER.info("UserService :: validateToken() exit...");
    return responseEntity;
  }

  public ResponseEntity<ApiResponse> uploadUserFiles(Long transactionId, MultipartFile[] files) throws IOException {
    LOGGER.info("UserService :: uploadUserFiles() called...");
    ResponseEntity<ApiResponse> apiResponse = null;
    List<FormMediaMappingVo> mediaDataList = new ArrayList<>();
    int count = 0;
    for (MultipartFile file : files) {
      if (!file.isEmpty()) {
        count++;
        LOGGER.info("File Count --------------------------" + count);

        String UPLOAD_FOLDER = selfieUploadFolder;
        // byte[] bytes = file.getBytes();
        // Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
        // Files.write(path, bytes);
        Path path = Paths.get(UPLOAD_FOLDER);
        if (!Files.exists(path)) {
          Files.createDirectories(path);
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Files.copy(file.getInputStream(), path.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        String tempFileName = filename;
        Path filePath = Paths.get(selfieUploadFolder + tempFileName);
        String ext = FilenameUtils.getExtension(tempFileName);
        String formType = "BA image";
        String tabName = "BA Selfie";
        String tempFilePath = filePath.toString();

        FormMediaMappingVo savedMediaData = new FormMediaMappingVo();

        savedMediaData.setTransactionId(transactionId);
        savedMediaData.setFormType(formType);
        savedMediaData.setTabName(tabName);
        savedMediaData.setFilePath(tempFilePath);
        savedMediaData.setImageName(filename);
        savedMediaData.setCreatedOn(new Date());

        savedMediaData = iFormMediaMappingRepository.save(savedMediaData);
        if (savedMediaData != null) {
          LOGGER.info("User Media Data saved in DB -----------" + savedMediaData);
          mediaDataList.add(savedMediaData);
        } else {
          LOGGER.info("User Media Data NOT saved in DB -----------" + savedMediaData);
          throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserMediaData" });
        }
      }
    }
    apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS",
        "Total " + count + " Media Data Saved Successfully.", mediaDataList, count), HttpStatus.OK);

    LOGGER.info("UserService :: uploadUserFiles() :: Exiting...");
    return apiResponse;
  }

  public UserDetailsVo fetchById(Long id) {
    UserDetailsVo data = iUserDetailsRepository.findById(id).orElse(null);
    if (null != data) {
      data.setAssotiateOutletList(
          iOutletUserMappingRepository.findByAssotiatedUserAndIsActive(data.getId(), true));
      data.setAssotiateOutlet(iOutletRepository.findAssotiatedOutlet(data.getId(), true));
      List<UserDetailsVo> assotiate = iUserDetailsRepository.findAssotiatedUser(data.getId(), true);
      data.setReportingTo((null != assotiate && assotiate.size() > 0) ? assotiate.get(0) : null);
    } else {
      new OjbException(ErrorCode.NOT_FOUND, new Object[] { "User" });
    }
    // return iUserDetailsRepository.findById(id).orElseThrow(() -> new
    // OjbException(ErrorCode.NOT_FOUND, new Object[]{"User"}));
    return data;
  }

  public ResponseEntity<ApiResponse> searchUserByInput(UserSearchInputVo inputVo) {
    LOGGER.info("UserService::searchUserByInput::Entering...");
    ApiResponse apiResponse = null;
    List<UserSearchOutputVo> getDataList = null;
    String whereClause = "  ";
    String limitStr = "";
    String orderByStr = " order by fullName ASC ";
    if (null == inputVo.getPage() && null == inputVo.getSize()) {
      // do nothing
    } else {
      Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
      Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
      limitStr = " limit " + size + " offset " + page;
      LOGGER.info("limitStr..." + limitStr);
    }

    if (null != inputVo.getActive()) {
      if (inputVo.getActive().isActive())
        whereClause += " and isActive = true";
      if (inputVo.getActive().isInactive())
        whereClause += " and isActive = false";
    }
    if (null != inputVo.getUserType()) {
      whereClause += " and userType = " + inputVo.getUserType();
    }

    if (null != inputVo.getFormType()) {
      whereClause += " and userType in ( select id from user_type_mst where form_type = '" + inputVo.getFormType()
          + "')";
    }
    if (null != inputVo.getUsername()) {
      whereClause += " and username like '%" + inputVo.getUsername().trim() + "%' ";
    }
    if (null != inputVo.getOutletName()) {
      whereClause += " and outletName like '%" + inputVo.getOutletName() + "%' ";
    }

    if (null != inputVo.getReportingTo()) {
      whereClause += " and reportingTo =" + inputVo.getReportingTo();
    }

    if (null != inputVo.getCompanyZone()) {
      whereClause += " and companyZone =" + inputVo.getCompanyZone().toString();
    }

    if (null != inputVo.getFullName() && !inputVo.getFullName().trim().isEmpty()) {
      LOGGER.info("vo.getFullName() ------------ " + inputVo.getFullName());
      whereClause += " and fullName like '%" + inputVo.getFullName().trim() + "%' ";
    }

    if (null != inputVo.getDivisionId())
      whereClause += " and productDivision = " + inputVo.getDivisionId();
    LOGGER.info("Whereclause..." + whereClause);

    getDataList = iUserSearchOutputRepository.searchByInput(whereClause + orderByStr, limitStr);

    // To fetch total no of data which satisfy this where clause
    // int totalNo = 100;
    int totalNo = iUserSearchOutputRepository.getTotalCountByInput(whereClause).intValue();
    apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
    LOGGER.info("UserService::searchByInput::Exiting...");
    // return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    if (getDataList != null) {
      return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Users" });
    }
  }

  public ResponseEntity<ApiResponse> updateStatus(ProductInputVo input) {
    LOGGER.info("UserService :: updateStatus() called...");
    UserDetailsVo user = iUserDetailsRepository.getById(input.getId());
    String contact = user.getContactNumber();
    List<UserDetailsVo> userDetails = iUserDetailsRepository.findByContactNumber(contact);
    boolean hasActiveUser = userDetails.stream().anyMatch(users -> Boolean.TRUE.equals(users.getIsActive()));
    if (hasActiveUser) {
      throw new OjbException(ErrorCode.ALREADY_EXIST, new Object[] { "Active User with this Phone Number " });
    }

    int savedData = iUserDetailsRepository.updateStatus(input.getId(), input.getStatus());
    if (savedData != 0) {
      int savedDataCred = iUserCredRepository.updateCredStatus(input.getId(), input.getStatus());
      LOGGER.info("UserService :: savedDataCred..." + savedDataCred);
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Status Updated", null), HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserStatus" });
    }
  }

  public ResponseEntity<ApiResponse> uploadDataFromExcel(MultipartFile excelInput) {
    LOGGER.info("UserService :: uploadDataFromExcel() called...");
    ResponseEntity<ApiResponse> responseEntity = null;
    ApiResponse apiResponse = null;
    try {
      XSSFWorkbook workbook = new XSSFWorkbook(excelInput.getInputStream());
      XSSFSheet worksheet = workbook.getSheetAt(0);
      // XSSFSheet worksheet = workbook.getSheet("outlets");
      int rows = worksheet.getLastRowNum();
      LOGGER.info("OutletService :: saveOutletFromExcel() :: Rows in WorkSheet :: " + rows);
      LOGGER.info("OutletService :: saveOutletFromExcel() :: Defined Rows in WorkSheet :: "
          + worksheet.getPhysicalNumberOfRows());
      DataFormatter format = new DataFormatter();
      // StringBuilder msg = new StringBuilder();

      List<OutletVo> dataList = new ArrayList<>();

      int count = 0;
      // for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
      //// for (int i = 1; i <= rows; i++) {
      // XSSFRow row = worksheet.getRow(i);
      // OutletVo vo = null;
      //// LOGGER.info("OutletService :: saveOutletFromExcel() :: row.getCell(0)) ::
      // "+row.getCell(0));
      //// try {
      // if (null != row) {
      // String outletName = format.formatCellValue(row.getCell(0));
      // Long outletType = iDropdownMastereRepository
      // .getIDByFieldName(format.formatCellValue(row.getCell(1)));
      // String address = format.formatCellValue(row.getCell(2));
      // String city = format.formatCellValue(row.getCell(3));
      // String regionName = format.formatCellValue(row.getCell(4));
      // Long outletChannel = iOutletChannelRepository
      // .getIDByOutletChannelName(format.formatCellValue(row.getCell(5)));
      // String market = format.formatCellValue(row.getCell(6));
      // Long soName = Long.valueOf(format.formatCellValue(row.getCell(7))); //
      // clarify this :
      // Long in
      // // OutletVo > which Vo is
      // // referred? DD in excel
      // Long distributor = iDistributorRepository
      // .getIDByDistributorName(format.formatCellValue(row.getCell(8))); // clarify
      // this :
      // Not in
      // // OutletVo now
      // Long beat =
      // iBeatNameRepository.getIDByBeatName(format.formatCellValue(row.getCell(9)));
      // Long productDivision = iProductDivisionRepository
      // .getIDByProductDivisionName(format.formatCellValue(row.getCell(10))); //
      // clarify this
      // : Long
      // // in OutletVo >
      // // which Vo is
      // // referred? DD in
      // // excel
      // Long companyZone = iCompanyZoneRepository
      // .getIDByCompanyZoneName(format.formatCellValue(row.getCell(11)));
      //
      // // NOT in Excel but in VO
      // String latitude = "22.580664781364284";
      // String longitude = "88.43723466902894";
      // Long state = Long.valueOf("1");
      // Long country = Long.valueOf("1");
      // Long pin = Long.valueOf("700010");
      // Long createdBy = Long.valueOf("1");
      //
      // vo = new OutletVo();
      //
      // vo.setOutletName(outletName);
      // vo.setLatitude(latitude); // clarify this : No field in Excel
      // vo.setLongitude(longitude); // clarify this : No field in Excel
      // vo.setSoUserId(soName);
      // vo.setOutletChannel(outletChannel);
      // vo.setOutletType(outletType);
      // vo.setMarket(market);
      // vo.setBeat(beat);
      // vo.setCompanyZone(companyZone);
      //// vo.setOwnerFullName("Demo Owner Full Name"); //Clarify this : No Field in
      // Excel but
      // present in Vo
      //// vo.setOwnerContactNumber("9876543213"); //Clarify this : No Field in Excel
      // but
      // present in Vo
      // vo.setRegionName(regionName);
      // vo.setAddress(address);
      // vo.setCity(city);
      // vo.setState(state); // Clarify this : No Field in Excel but present in Vo
      // vo.setCountry(country); // Clarify this : No Field in Excel but present in Vo
      // vo.setPin(pin); // Clarify this : No Field in Excel but present in Vo
      // vo.setIsActive(true);
      // vo.setCreatedOn(new Date());
      // vo.setCreatedBy(createdBy); // clarify this
      // vo.setUpdatedOn(new Date());
      // vo.setUpdatedBy(createdBy); // clarify this
      //
      // // Present in Excel but not in Vo
      // vo.setDistributor(distributor);
      // vo.setProductDivision(productDivision);
      //
      // OutletVo resultVo = iOutletRepository.save(vo);
      // LOGGER.info("resultVo ----------------:" + resultVo);
      // dataList.add(resultVo);
      // if (resultVo != null) {
      // count++;
      // LOGGER.info("COUNT --------------------------" + count);
      // LOGGER.info("Outlet Data saved in DB -----------");
      // } else {
      // LOGGER.info("Outlet Data NOT saved in DB -----------" + vo);
      //// dataNotSaveList.add(i);
      // }
      //// apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS",
      // "Outlet(s) created
      // successfully.", resultVo), HttpStatus.OK);
      // } else {
      // break;
      // }
      // }
      apiResponse = new ApiResponse(200, "success", "Total " + count + " outlet saved successfully.",
          HttpStatus.OK);

    } catch (Exception exp) {
      LOGGER.info(
          "--------------------------------------------- Exception 1---------------------------------------------");
      apiResponse = new ApiResponse(401, "ERROR", "Not a valid excel format.", "");
      LOGGER.error(exp.getMessage());
      return responseEntity;
    }
    return responseEntity;
  }

  public ResponseEntity<ApiResponse> searchUserActivityByInput(UserActivitySearchInputVo inputVo) {
    LOGGER.info("UserService::searchUserActivityByInput::Entering...");
    ApiResponse apiResponse = null;
    List<UserActivityRegisterVo> getDataList = null;
    String whereClause = " ";
    String limitStr = "";
    if (null == inputVo.getPage() && null == inputVo.getSize()) {
      // do nothing
    } else {
      Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
      Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
      limitStr = " order by activity_time desc limit " + size + " offset " + page;
      LOGGER.info("limitStr..." + limitStr);
    }

    if (null != inputVo.getActivityType() && !inputVo.getActivityType().trim().isEmpty()) {
      whereClause += " and activity_type = '" + inputVo.getActivityType() + "'";
    }

    if (null != inputVo.getUserId()) {
      whereClause += " and created_by = " + inputVo.getUserId();
    }

    if (null != inputVo.getOutlet()) {
      whereClause += " and outlet_id = " + inputVo.getOutlet();
    }

    if (null != inputVo.getActivityDate() && !inputVo.getActivityDate().trim().isEmpty()) {
      LOGGER.info("vo.getActivityDate() ------------ " + inputVo.getActivityDate());
      whereClause += " and DATE_FORMAT(activity_time,'%Y-%m-%d') = DATE_FORMAT('" + inputVo.getActivityDate()
          + "','%Y-%m-%d') ";
    }

    if (null != inputVo.getFromDate() && !inputVo.getFromDate().trim().isEmpty() && null != inputVo.getToDate()
        && !inputVo.getToDate().trim().isEmpty()) {
      LOGGER.info("vo.getToDate() ------------ " + inputVo.getToDate());
      whereClause += " and DATE_FORMAT(activity_time,'%Y-%m-%d') BETWEEN DATE_FORMAT('" + inputVo.getFromDate()
          + "','%Y-%m-%d') " + "AND DATE_FORMAT('" + inputVo.getToDate() + "','%Y-%m-%d') ";
    }

    LOGGER.info("Whereclause..." + whereClause);
    getDataList = iUserActivityRepository.searchByInput(whereClause, "user_activity_register", limitStr);

    // To fetch total no of data which satisfy this where clause
    int totalNo = iUserActivityRepository.getTotalCountByInput(whereClause, "user_activity_register").intValue();
    LOGGER.info("UserService::searchUserActivityByInput::Exiting...");
    if (getDataList != null) {
      apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
      return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "UserActivity" });
    }
  }

  public ResponseEntity<ApiResponse> searchUserActivityList(UserActivitySearchInputVo inputVo) {
    LOGGER.info("UserService::searchUserActivityList::Entering...");
    ApiResponse apiResponse = null;
    List<UserActivityListVo> getDataList = null;
    String whereClause = " ";
    String limitStr = "";
    if (null == inputVo.getPage() && null == inputVo.getSize()) {
      // do nothing
    } else {
      Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
      Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
      limitStr = " limit " + size + " offset " + page; // order by activity_time desc
      LOGGER.info("limitStr..." + limitStr);
    }

    // if (null != inputVo.getActivityType() &&
    // !inputVo.getActivityType().trim().isEmpty()) {
    // whereClause += " and activity_type = '"+inputVo.getActivityType() + "'";
    // }

    if (null != inputVo.getUserId()) {
      whereClause += " and uac.created_by = " + inputVo.getUserId();
    }

    if (null != inputVo.getOutlet()) {
      whereClause += " and outlet_id = " + inputVo.getOutlet();
    }

    if (null != inputVo.getActivityDate() && !inputVo.getActivityDate().trim().isEmpty()) {
      LOGGER.info("vo.getActivityDate() ------------ " + inputVo.getActivityDate());
      whereClause += " and DATE_FORMAT(activity_time,'%Y-%m-%d') = DATE_FORMAT('" + inputVo.getActivityDate()
          + "','%Y-%m-%d') ";
    }

    if (null != inputVo.getFromDate() && !inputVo.getFromDate().trim().isEmpty() && null != inputVo.getToDate()
        && !inputVo.getToDate().trim().isEmpty()) {
      LOGGER.info("vo.getToDate() ------------ " + inputVo.getToDate());
      whereClause += " and DATE_FORMAT(activity_time,'%Y-%m-%d') BETWEEN DATE_FORMAT('" + inputVo.getFromDate()
          + "','%Y-%m-%d') " + "AND DATE_FORMAT('" + inputVo.getToDate() + "','%Y-%m-%d') ";
    }

    LOGGER.info("Whereclause..." + whereClause);
    getDataList = iUserActivityListRepository.searchByInput(whereClause, limitStr);

    // To fetch total no of data which satisfy this where clause
    int totalNo = iUserActivityListRepository.searchByInput(whereClause, "").size();
    // int totalNo = iUserActivityRepository.getTotalCountByInput(whereClause,
    // "user_activity_register").intValue();
    LOGGER.info("UserService::searchUserActivityList::Exiting...");
    if (getDataList != null) {
      apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
      return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "UserActivity" });
    }
  }

  public ResponseEntity<ApiResponse> userActivity(UserActivityRegisterVo input) {
    LOGGER.info("UserService :: userActivity() called...");
    try {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String username = ((UserDetails) principal).getUsername();
      Long loginUserId = userCredRepository.loginUserId(username);
      LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
      if (null == loginUserId || loginUserId == 0L) {
        loginUserId = input.getCreatedBy();
        LOGGER.info(" loginUserId from input =========== > " + loginUserId);
      }

      List<String> userActivity = iUserActivityRepository.findActivityOfUser(input.getOutlet().getId(),
          loginUserId, new Date());
      if ((userActivity.contains("store_login") || userActivity.contains("office_work"))
          && (input.getActivityType().name().equals("leave")
              || input.getActivityType().name().equals("holiday")
              || input.getActivityType().name().equals("week_off")
              || input.getActivityType().name().equals("comp_off"))) {
        return new ResponseEntity<ApiResponse>(new ApiResponse(406, "not_allowed",
            "You have already marked as store-checkIn/ Office work, can't mark as leave/ holiday/ week off/ comp. off today."),
            HttpStatus.NOT_ACCEPTABLE);
      } else if ((input.getActivityType().name().equals("store_login")
          || input.getActivityType().name().equals("office_work"))
          && (userActivity.contains("leave") || userActivity.contains("holiday")
              || userActivity.contains("week_off") || userActivity.contains("comp_off"))) {
        return new ResponseEntity<ApiResponse>(new ApiResponse(406, "not_allowed",
            "You have already marked as leave/ holiday/ week off/ comp. off, can't mark as store-checkIn/ Office work today."),
            HttpStatus.NOT_ACCEPTABLE);
      } else if ((input.getActivityType().name().equals("leave")
          || input.getActivityType().name().equals("holiday")
          || input.getActivityType().name().equals("week_off")
          || input.getActivityType().name().equals("comp_off"))
          && (userActivity.contains("leave") || userActivity.contains("holiday") // userActivity.contains("store_login")
                                                                                 // ||
                                                                                 // userActivity.contains("office_work")
                                                                                 // ||
              || userActivity.contains("week_off") || userActivity.contains("comp_off"))) {
        return new ResponseEntity<ApiResponse>(
            new ApiResponse(406, "not_allowed",
                "You have already marked today as leave/ holiday/ week off/ comp. off. "),
            HttpStatus.NOT_ACCEPTABLE);
      } else if (userActivity.contains("office_work") && (input.getActivityType().name().contains("store_login")
          || input.getActivityType().name().equals("leave")
          || input.getActivityType().name().equals("holiday")
          || input.getActivityType().name().equals("week_off")
          || input.getActivityType().name().equals("comp_off")
          || input.getActivityType().name().contains("office_work"))) {
        return new ResponseEntity<ApiResponse>(
            new ApiResponse(406, "not_allowed", "You have already marked today as office work."),
            HttpStatus.NOT_ACCEPTABLE);
      }
      if (input.getActivityType().name().contains("store_login")) {
        input.setWorkingHours(Long.valueOf("0"));
      }
      if (input.getActivityType().name().contains("store_logout")) {
        UserActivityRegisterVo findStoreLoginOfUser = iUserActivityRepository.findStoreLoginOfUser(loginUserId,
            new Date());
        Date StoreLoginTime = findStoreLoginOfUser.getActivityTime();
        LOGGER.info("UserService :: userActivity() :: Store Login Time :: " + StoreLoginTime);
        Date StoreLogoutTime = new Date();
        LOGGER.info("UserService :: userActivity() :: Current Time :: " + StoreLogoutTime);
        long duration = StoreLogoutTime.getTime() - StoreLoginTime.getTime();
        long workingHoursInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        LOGGER.info("UserService :: userActivity() :: Working Hours in Minutes :: " + workingHoursInMinutes);
        input.setWorkingHours(Long.valueOf(workingHoursInMinutes));
      }
      input.setActivityTime(new Date());
      input.setCreatedBy(loginUserId);
      UserActivityRegisterVo savedData = iUserActivityRepository.save(input);
      LOGGER.info(" UserActivityRegisterVo from userActivity for " + input.getActivityType().name()
          + " activity ====================>" + savedData);
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
          HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserActivityRegister" });
  }

  public ResponseEntity<ApiResponse> userTargetSet(UserTargetVo input) {
    LOGGER.info("UserService :: userTarget() called...");
    try {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String username = ((UserDetails) principal).getUsername();
      Long loginUserId = userCredRepository.loginUserId(username);

      if (null == input.getId()) {
        input.setCreatedOn(new Date());
        input.setCreatedBy(loginUserId);
      }
      input.setUpdatedOn(new Date());
      input.setUpdatedBy(loginUserId);
      UserTargetVo savedData = iUserTargetRepository.save(input);

      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
          HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserActivityRegister" });
  }

  public void storeLogoutLoggedInUsers(Date currentDate) {
    List<UserActivityRegisterVo> activities = iUserActivityRepository.findAllActivityOfUser(currentDate);

    Map<Long, List<UserActivityRegisterVo>> loginActivityMap = new HashMap<>();
    Map<Long, List<UserActivityRegisterVo>> logoutActivityMap = new HashMap<>();

    // get all the login and logout activities and put in the hashMap with a key of
    // userId
    activities.stream().filter(e -> e.getActivityType().equals(Constants.BA_Activity_Enum.store_login))
        .forEach(e -> {
          if (!loginActivityMap.containsKey(e.getCreatedBy())) {
            List<UserActivityRegisterVo> acList = new ArrayList<>();
            acList.add(e);
            loginActivityMap.put(e.getCreatedBy(), acList);
          } else {
            List<UserActivityRegisterVo> acList = loginActivityMap.get(e.getCreatedBy());
            acList.add(e);
            loginActivityMap.put(e.getCreatedBy(), acList);
          }
          ;
        });
    activities.stream().filter(e -> e.getActivityType().equals(Constants.BA_Activity_Enum.store_logout))
        .forEach(e -> {
          if (!logoutActivityMap.containsKey(e.getCreatedBy())) {
            List<UserActivityRegisterVo> acList = new ArrayList<>();
            acList.add(e);
            logoutActivityMap.put(e.getCreatedBy(), acList);
          } else {
            List<UserActivityRegisterVo> acList = logoutActivityMap.get(e.getCreatedBy());
            acList.add(e);
            logoutActivityMap.put(e.getCreatedBy(), acList);
          }
          ;
        });

    loginActivityMap.forEach((key, value) -> {
      if ((logoutActivityMap.get(key) == null) || value.size() > logoutActivityMap.get(key).size()) {
        // get one activity
        UserActivityRegisterVo ac = value.get(0);
        logoutFromStore(ac.getCreatedBy(), ac.getOutlet());
      }
    });
  }

  public Long getWorkingHours(Long userId) {
    UserActivityRegisterVo findStoreLoginOfUser = iUserActivityRepository.findStoreLoginOfUser(userId, new Date());
    Date StoreLoginTime = findStoreLoginOfUser.getActivityTime();
    LOGGER.info("UserService :: userActivity() :: Store Login Time :: " + StoreLoginTime);
    Date StoreLogoutTime = new Date();
    LOGGER.info("UserService :: userActivity() :: Current Time :: " + StoreLogoutTime);
    long duration = StoreLogoutTime.getTime() - StoreLoginTime.getTime();
    long workingHoursInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
    LOGGER.info("UserService :: userActivity() :: Working Hours in Minutes :: " + workingHoursInMinutes);
    return workingHoursInMinutes;
  }

  public void logoutFromStore(Long userId, OutletVo outlet) {
    LOGGER.info("Logging out: " + userId);
    UserActivityRegisterVo userActivityRegisterVo = new UserActivityRegisterVo(
        Constants.BA_Activity_Enum.store_logout, new Date(), outlet, null, userId);
    userActivityRegisterVo.setWorkingHours(getWorkingHours(userId));
    iUserActivityRepository.save(userActivityRegisterVo);
  }

  public boolean isBA(Long userId) {
    UserDetailsVo user = iUserDetailsRepository.findById(userId)
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "User: " + userId }));
    return user.getUserType().getUserType().equals("BA");
  }

  public boolean isActive(Long userId) {
    UserDetailsVo user = iUserDetailsRepository.findById(userId)
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "User: " + userId }));
    return user.getIsActive();
  }

  public ResponseEntity<ApiResponse> userDateWiseActivity(UserActivitySearchInputVo input) {
    LOGGER.info("UserService :: userDateWiseActivity() called...");
    UserActivityOPVo result = null;
    List<UserActivityListVo> dataList = iUserActivityListRepository.getUserActivityByDate(input.getMonYr(),
        input.getUserId());
    if (null != dataList && dataList.size() > 0) {
      result = new UserActivityOPVo();
      Long storeVisit = dataList.stream().filter(c -> "Store Visit".equals(c.getActivity())).count();
      Long leave = dataList.stream().filter(c -> "Leave".equals(c.getActivity())).count();
      Long officeWork = dataList.stream().filter(c -> "Office Work".equals(c.getActivity())).count();
      Long holiday = dataList.stream().filter(c -> "Holiday".equals(c.getActivity())).count();
      Long weekOff = dataList.stream().filter(c -> "Week Off".equals(c.getActivity())).count();
      Long compOff = dataList.stream().filter(c -> "Comp Off".equals(c.getActivity())).count();
      result.setTotalStoreVisit(storeVisit);
      result.setTotalLeave(leave);
      result.setTotalOfficeWork(officeWork);
      result.setTotalHoliday(holiday);
      result.setTotalWeekOff(weekOff);
      result.setTotalCompOff(compOff);
      result.setActivitySummary(dataList);
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", result, dataList.size()),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "User Date Wise Activity Data not found"),
          HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<ApiResponse> releaseUser(BAReleaseInputVo input) {
    LOGGER.info("UserService :: releaseUser() called...");
    try {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String username = ((UserDetails) principal).getUsername();
      Long loginUserId = userCredRepository.loginUserId(username);

      Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(input.getReleaseDate());
      int savedData = iUserDetailsRepository.updateReleaseStatus(input.getBaId(), releaseDate, loginUserId,
          new Date());

      int outletAssotoation = iOutletUserMappingRepository.updateReleaseStatus(input.getBaId(), releaseDate);
      LOGGER.info(" outlet user mapping after releaseUser ---------------- > " + outletAssotoation);
      int userAssotoation = iUserAssotoationRepository.updateReleaseStatus(input.getBaId());
      LOGGER.info(" userAssotoation after releaseUser ---------------- > " + userAssotoation);
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "User inactivated Successfully", savedData),
          HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "Release User" });
  }

  public ResponseEntity<ApiResponse> baActiveInactiveCount(String currDate) {
    ResponseEntity<ApiResponse> responseEntity = null;
    // SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

    // String currDate = ft.format(new Date());
    // String currDate = "2024-02-05";
    try {
      // Fetch counts from the repository
      Long totalBa = iUserDetailsRepository.baCount();

      Long eastZone = iUserDetailsRepository.baEastCount();
      Long westZone = iUserDetailsRepository.baWestCount();
      Long northZone = iUserDetailsRepository.baNorthCount();
      Long southZone = iUserDetailsRepository.baSouthCount();

      Long activeBa = iUserActivityRepository.baActiveCount(4, currDate);

      Long baActiveEast = iUserActivityRepository.baActiveZone(4, 8, currDate);
      Long baActiveWest = iUserActivityRepository.baActiveZone(4, 9, currDate);
      Long baActiveNorth = iUserActivityRepository.baActiveZone(4, 10, currDate);
      Long baActiveSouth = iUserActivityRepository.baActiveZone(4, 11, currDate);

      Long leaveBa = iUserActivityRepository.baInActiveCount(4, currDate);

      Long baLeaveEast = iUserActivityRepository.baInActiveZone(4, 8, currDate);
      Long baLeaveWest = iUserActivityRepository.baInActiveZone(4, 9, currDate);
      Long baLeaveNorth = iUserActivityRepository.baInActiveZone(4, 10, currDate);
      Long baLeaveSouth = iUserActivityRepository.baInActiveZone(4, 11, currDate);

      // Long absentBa = iUserActivityRepository.baInActiveCount(4,currDate);
      Long absentBa = totalBa - (activeBa + leaveBa);

      // Long baInActiveEast = iUserActivityRepository.baInActiveZone(4,8,currDate);
      // Long baInActiveWest = iUserActivityRepository.baInActiveZone(4,9,currDate);
      // Long baInActiveNorth = iUserActivityRepository.baInActiveZone(4,10,currDate);
      // Long baInActiveSouth = iUserActivityRepository.baInActiveZone(4,11,currDate);

      Long baInActiveEast = eastZone - (baActiveEast + baLeaveEast);
      Long baInActiveWest = westZone - (baActiveWest + baLeaveWest);
      Long baInActiveNorth = northZone - (baActiveNorth + baLeaveNorth);
      Long baInActiveSouth = southZone - (baActiveSouth + baLeaveSouth);

      Map<String, BaZoneDto> counts = new LinkedHashMap<>();

      if (totalBa > 0) {
        counts.put("TotalBa", new BaZoneDto(totalBa, eastZone, westZone, northZone, southZone));
      } else {
        counts.put("total", new BaZoneDto(0L, 0L, 0L, 0L, 0L));
      }

      if (activeBa > 0) {
        counts.put("ActiveBa",
            new BaZoneDto(activeBa, baActiveEast, baActiveWest, baActiveNorth, baActiveSouth));
      } else {
        counts.put("Active", new BaZoneDto(0L, 0L, 0L, 0L, 0L));
      }

      if (absentBa > 0) {
        counts.put("InactiveBa",
            new BaZoneDto(absentBa, baInActiveEast, baInActiveWest, baInActiveNorth, baInActiveSouth));
      } else {
        counts.put("Inactive", new BaZoneDto(0L, 0L, 0L, 0L, 0L));
      }

      if (leaveBa > 0)
        counts.put("Leave_BA", new BaZoneDto(leaveBa, baLeaveEast, baLeaveWest, baLeaveNorth, baLeaveSouth));
      else
        counts.put("Leave", new BaZoneDto(0L, 0L, 0L, 0L, 0L));

      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetch Successfully", counts), HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "BaActiveInactive" });

  }

  private void setZeroZoneCounts(Map<String, Number> zoneCounts) {
    zoneCounts.put("East", 0);
    zoneCounts.put("West", 0);
    zoneCounts.put("North", 0);
    zoneCounts.put("South", 0);
  }

  public Optional<UserDetailsVo> findUserById(Long id) {
    return iUserDetailsRepository.findById(id);
  }

  public List<OutletUserView> getActiveOutletAssociations(Long outletId) {
    return iOutletUserMappingRepository.findAssociatedOutletListViewByUserId(outletId);
  }

  // public ResponseEntity<ApiResponse> countBaUnderABde(Long userId ){
  // int countBA = iUserAssotoationRepository.countBaunderBde(userId);
  // return new ResponseEntity<>(
  // new ApiResponse(200, "SUCCESS", "Fetch Ba Count Successfully", countBA),
  // HttpStatus.OK);
  // }
  public int countBaUnderABde(Long userId, String Date) {
    return iUserAssotoationRepository.countBaunderBde(userId, Date);
  }

  public ResponseEntity<ApiResponse> sumOfPurchaseAndSaleOfBA(SumOfPurchaseAndSaleInputVo input) {
    // LOGGER.info("User Service :: Sum of purchase and sale :: Entering....");
    //
    //
    // Object principal =
    // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // String username = ((UserDetails) principal).getUsername();
    // Long loginUserId = userCredRepository.loginUserId(username);
    //
    // LOGGER.info("userid :: "+loginUserId);
    //
    // List<SumOfTotalPurchaseAndSaleOutputVo> getDataList =
    // iSumOfPurchaseAndSaleOfABa.getSumOfPurchaseAndSaleOfBA(input.getMonYr(),
    // loginUserId);
    //
    // int totalCount = iSumOfPurchaseAndSaleOfABa.countSumOfPurchaseAndSaleOfBA(
    // loginUserId,input.getMonYr());
    // if (null != getDataList && getDataList.size() > 0) {
    // return new ResponseEntity<>(
    // new ApiResponse(200, "SUCCESS", "Data Found", getDataList,totalCount),
    // HttpStatus.OK
    // );
    // } else {
    // return new ResponseEntity<>(
    // new ApiResponse(404, "NOT_FOUND", "Date Wise Purchase and Sale Total Not
    // found"),
    // HttpStatus.NOT_FOUND
    // );
    // }
    return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Date Wise Purchase and Sale Total Not found"),
        HttpStatus.NOT_FOUND);

  }

  public ResponseEntity<ApiResponse> totalSumOfSaleAndPurchase(SumOfPurchaseAndSaleInputVo input) {
    LOGGER.info("User Service ::Total  Sum of purchase and sale :: Entering....");

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = ((UserDetails) principal).getUsername();
    Long loginUserId = userCredRepository.loginUserId(username);

    LOGGER.info("userid :: " + loginUserId);

    // List<OutletUserMappingVo> outletsUserRes =
    // iOutletUserMappingRepository.getOutletMappingResult(input.getBdeId());
    // // Sort and deduplicate by outlet_id with minimum id
    // List<Long> uniqueOutletIds = outletsUserRes.stream()
    // .sorted(Comparator.comparingLong(o -> ((Number)
    // o.getOutlet().getId()).longValue())) // Sort by id first
    // .collect(Collectors.toMap(
    // o -> ((Number) o.getOutlet().getId()).longValue(), // Key: outlet_id
    // o -> ((Number) o.getId()).longValue(), // Value: id
    // Long::min // Keep the smallest id for each outlet_id
    // ))
    // .keySet()
    // .stream()
    // .sorted()
    // .collect(Collectors.toList());

    SumOfTotalPurchaseAndSaleOutputVo getDataList = iSumOfPurchaseAndSaleOfABa
        .getTotalPurchaseAndSaleOfBa(input.getBdeId(), input.getStartDate(), input.getEndDate());

    if (null != getDataList) {
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", getDataList), HttpStatus.OK);
    } else if (null == getDataList) {
      return new ResponseEntity<>(new ApiResponse(204, "SUCCESS", "No Content"), HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Date Wise Purchase and Sale Total Not found"),
        HttpStatus.NOT_FOUND);

  }

  // public ResponseEntity<ApiResponse> activityCountOfBaUnderBde(String
  // currDate){
  //
  //
  // Object principal =
  // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  // String username = ((UserDetails) principal).getUsername();
  // Long loginUserId = userCredRepository.loginUserId(username);
  //
  // List<UserActivityRegisterVo> activityCounts =
  // iUserActivityRepository.activityCounts(loginUserId,currDate);
  //
  // int attendence =
  // iUserAssotoationRepository.baAttendence(loginUserId,currDate);
  //
  //
  // if (activityCounts != null && !activityCounts.isEmpty()) {
  //
  // ApiResponse apiResponse = new ApiResponse(200, "SUCCESS", "Data Found",
  // activityCounts);
  // return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  // } else {
  // ApiResponse apiResponse = new ApiResponse(204, "NOT_FOUND", "No data found",
  // null);
  // return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  // }
  // }

  public ResponseEntity<ApiResponse> activityCountOfBaUnderBde(String currDate) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = ((UserDetails) principal).getUsername();
    Long loginUserId = userCredRepository.loginUserId(username);

    List<UserActivityRegisterVo> activityCounts = iUserActivityRepository.activityCounts(loginUserId, currDate);

    int attendanceCount = 0;
    int leaveCount = 0;
    int totalBa = iUserAssotoationRepository.countBaunderBde(loginUserId, currDate);
    LOGGER.info("total BA: " + totalBa);

    if (activityCounts != null && !activityCounts.isEmpty()) {
      for (UserActivityRegisterVo activity : activityCounts) {
        String activityType = String.valueOf(activity.getActivityType());
        // Check if activity type is related to attendance
        if (activityType.equalsIgnoreCase("store_login") || activityType.equalsIgnoreCase("office_work")) {
          attendanceCount++;
        }
        // Check if activity type is related to leave
        else if (activityType.equalsIgnoreCase("holiday") || activityType.equalsIgnoreCase("leave")
            || activityType.equalsIgnoreCase("week_off") || activityType.equalsIgnoreCase("comp_off")) {
          leaveCount++;
        }
      }

      LOGGER.info("attendence:" + attendanceCount);
      LOGGER.info("leave:" + leaveCount);

      // Create a response object with attendance and leave counts
      Map<String, Integer> counts = new HashMap<>();
      counts.put("attendance", attendanceCount);
      counts.put("leave", leaveCount);
      counts.put("absent", totalBa - (attendanceCount + leaveCount));

      ApiResponse apiResponse = new ApiResponse(200, "SUCCESS", "Data Found", counts);
      return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    } else {
      ApiResponse apiResponse = new ApiResponse(204, "NOT_FOUND", "No data found", null);
      return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
  }

  public ResponseEntity<ApiResponse> listOfBaActivity(ListOfBaActivityInputVo input) {

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = ((UserDetails) principal).getUsername();
    Long loginUserId = userCredRepository.loginUserId(username);

    // Bind all activityType in attendence List and leave List
    List<String> attendence = new ArrayList<>();
    attendence.add("store_login");
    attendence.add("office_work");

    List<String> leave = new ArrayList<>();
    leave.add("holiday");
    leave.add("leave");
    leave.add("week_off");
    leave.add("comp_off");

    String absent;

    List<ListOfBaActivityOutputVo> baActivityListUnderBde;
    int totalCount = 0;

    // checking with "attendence" and "leave" string
    if ("attendance".equalsIgnoreCase(input.getActivityTypes())) {

      baActivityListUnderBde = iListOfBaActivity.baActivityList(loginUserId, attendence, input.getCurrDate());
      totalCount = iListOfBaActivity.totalCountBaActivityList(loginUserId, attendence, input.getCurrDate());

    } else if ("leave".equalsIgnoreCase(input.getActivityTypes())) {

      baActivityListUnderBde = iListOfBaActivity.baActivityList(loginUserId, leave, input.getCurrDate());
      totalCount = iListOfBaActivity.totalCountBaActivityList(loginUserId, leave, input.getCurrDate());
    } else if ("absent".equalsIgnoreCase(input.getActivityTypes())) {
      baActivityListUnderBde = iListOfBaActivity.baAbsentList(loginUserId, input.getCurrDate());

    } else {

      ApiResponse apiResponse = new ApiResponse(400, "BAD_REQUEST", "Invalid activity type", null);
      return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    if (baActivityListUnderBde != null && !baActivityListUnderBde.isEmpty()) {

      ApiResponse apiResponse = new ApiResponse(200, "SUCCESS", "SUCCESS", baActivityListUnderBde, totalCount);
      return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    } else {
      ApiResponse apiResponse = new ApiResponse(404, "NO_CONTENT_FOUND", "No data found", null);
      return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

  }

  public ResponseEntity<ApiResponse> fetchBaTotalPurchaseSaleById(Long id) {

    Long outletId = iOutletUserMappingRepository.findAssociatedOutletIdByUserId(id);
    BaDetailsDashboardVo baDetailsDashboardVo = iBaDetailsDashboardRepo.baAssoDetails(outletId);

    if (baDetailsDashboardVo != null) {
      LOGGER.info("OutletMap :: " + baDetailsDashboardVo);

      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", baDetailsDashboardVo),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data not found"), HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<ApiResponse> getSrUserList(Long soId) {
    return new ResponseEntity<>(
        new ApiResponse(200, "SUCCESS", "Data Found", iUserDetailsRepository.findSrUsers(soId)),
        HttpStatus.OK);
  }

}
