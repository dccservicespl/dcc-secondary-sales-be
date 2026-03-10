package com.dcc.osheaapp.ojbso.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.text.SimpleDateFormat;

import com.dcc.osheaapp.ojbso.repo.INoOrderReasonRepository;
import com.dcc.osheaapp.ojbso.repo.ISOActivityRegisterRepository;
import com.dcc.osheaapp.ojbso.vo.*;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.vo.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.service.storage.FileMetadata;
import com.dcc.osheaapp.common.service.storage.FileResponse;
import com.dcc.osheaapp.common.service.storage.S3StorageService;

@Service
public class BaseService {
  private static final Logger LOGGER = LogManager.getLogger(BaseService.class);

  @Autowired
  private IUserBeatsAssociationRepo iUserBeatsAssociationRepo;

  @Autowired
  private IOutletRepository iOutletRepository;

  @Autowired
  private IBeatNameRepository iBeatNameRepository;

  @Autowired
  private IFormMediaMappingRepository iFormMediaMappingRepository;

  @Autowired
  private INoOrderReasonRepository iNoOrderReasonRepo;

  @Autowired
  private IUserCredRepository userCredRepository;

  @Autowired
  private ISOActivityRegisterRepository iSOActivityRegisterRepo;

  @Autowired
  private IUserDetailsRepository iUserDetailsRepository;

  @Autowired
  private S3StorageService s3StorageService;

  @Value("${file.outletUploadFolder}")
  private String outletUploadFolder;

  @Value("${file.soSelfieUploadFolder}")
  private String soSelfieUploadFolder;

  public ResponseEntity<ApiResponse> fetchBeatsById(Long id) {
    LOGGER.info("BaseService :: fetchBeatsById() called...");
    try {

      List<BeatName> userBeats = iBeatNameRepository.findAllBySoId(id);

      if (userBeats.size() > 0) {
        return new ResponseEntity<>(
            new ApiResponse(200, "SUCCESS", "Fetched Successfully", userBeats), HttpStatus.OK);

      } else {
        return new ResponseEntity<>(
            new ApiResponse(400, "Not_Found", "Data not Fetched", null), HttpStatus.NOT_FOUND);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<ApiResponse> saveBeatBySo(UserBeatsAssociation input) {
    LOGGER.info("BaseService :: saveBeatBySo() called...");
    try {

      Long beatId = iBeatNameRepository.getIDByBeatNameSo(input.getBeatName().toLowerCase());
      if (beatId != null) {
        return new ResponseEntity<>(new ApiResponse(406, "DUPLICATE", "Duplicate Beat Name.", null),
            HttpStatus.NOT_ACCEPTABLE);
      }
      // Save Beatname first then mapped with user
      BeatName inputBeat = new BeatName();
      inputBeat.setBeatName(input.getBeatName());
      inputBeat.setActive(true);
      inputBeat.setCreatedOn(new Date());
      inputBeat.setCreatedBy(input.getUser());
      inputBeat.setCompanyZone(input.getCompanyZone());

      BeatName resBeat = iBeatNameRepository.save(inputBeat);
      // UserBeatsAssociation fetchUserBeat =
      // iUserBeatsAssociationRepo.findByUserAndBeatId(input.getUser(),
      // resBeat.getId());

      // if(fetchUserBeat != null && fetchUserBeat != ''){
      // return new ResponseEntity<>(new ApiResponse(406, "DUPLICATE", "Beat Already
      // Associated with this user!", null), HttpStatus.NOT_ACCEPTABLE);
      // }else{
      //
      // }
      input.setBeatId(resBeat.getId());
      return new ResponseEntity<>(
          new ApiResponse(200, "SUCCESS", "Saved Successfully", iUserBeatsAssociationRepo.save(input)), HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<ApiResponse> fetchOutletsById(Long id, Long compZone) {
    LOGGER.info("BaseService :: fetchOutletsById() called...");
    try {
      List<OutletVo> fetchBeatOutlets = iOutletRepository.findByBeatAndCompanyZone(id, compZone);
      String pattern = "yyyy-MM-dd";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
      String currDate = simpleDateFormat.format(new Date());
      List<SoActivityRegisterVo> soActivities = iSOActivityRegisterRepo.fetchActivitiesByBeatAndDate(id, currDate);

      List<OutletVo> resultOutlets = fetchBeatOutlets;
      if (soActivities.size() > 0) {
        resultOutlets = checkTodaysActivity(soActivities, fetchBeatOutlets);
      }

      if (fetchBeatOutlets != null) {
        return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully", resultOutlets),
            HttpStatus.OK);

      } else {
        return new ResponseEntity<>(new ApiResponse(400, "NOT_FOUND", "Outlet Not Found in this Beat!", null),
            HttpStatus.NOT_FOUND);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private List<OutletVo> checkTodaysActivity(List<SoActivityRegisterVo> soActivities, List<OutletVo> outlets) {
    // TODO Auto-generated method stub
    LOGGER.info("BaseService :: checkTodaysActivity() called..." + soActivities.size());
    for (SoActivityRegisterVo activityRegs : soActivities) {
      // LOGGER.info("In activityRegs :: checkTodaysActivity()
      // called..."+activityRegs.getOutletId());
      if ((activityRegs.getDailyActivityId().getId() == 1L) ||
          (activityRegs.getDailyActivityId().getId() == 4L && activityRegs.getOrderId() == null)) {
        // outlets.stream().map(elem -> elem.setSoOutletvisitType("Visited"));

        outlets.stream().forEach(elem -> {
          // LOGGER.info("In Visited under foreach...:: checkTodaysActivity() called..."+
          // elem.getId());
          if (elem.getId().equals(activityRegs.getOutletId()) && activityRegs.getEndTimeDate() != null) {
            LOGGER.info("In Visited under if...:: checkTodaysActivity() called...");
            if (!elem.getSoOutletvisitType().equals("Order")) {
              elem.setSoOutletvisitType("Visited");
            } else {
              elem.setSoOutletvisitType("Order");
            }
          }
        });
      } else if ((activityRegs.getDailyActivityId().getId() == 1L || activityRegs.getDailyActivityId().getId() == 4L)
          && activityRegs.getOrderId() != null) {
        LOGGER.info("In Order Call.... :: checkTodaysActivity() called...");
        outlets.stream().forEach(elem -> {
          if (elem.getId().equals(activityRegs.getOutletId())) {
            elem.setSoOutletvisitType("Order");
          }
        });
      } else if (activityRegs.getDailyActivityId().getId() == 6L) {
        LOGGER.info("In No Order :: checkTodaysActivity() called...");
        outlets.stream().forEach(elem -> {
          if (elem.getId().equals(activityRegs.getOutletId())) {
            elem.setSoOutletvisitType("No Order");
          }
        });
      } else if (activityRegs.getDailyActivityId().getId() == 2L) {
        LOGGER.info("In Visited Call.... :: checkTodaysActivity() called...");
        outlets.stream().forEach(elem -> {
          if (elem.getId().equals(activityRegs.getOutletId())) {
            elem.setSoOutletvisitType("Visited");
          }
        });
      }
    }
    return outlets;
  }

  public ResponseEntity<ApiResponse> fetchOutletById(Long id) {
    LOGGER.info("BaseService :: fetchOutletsById() called...");
    try {
      Optional<OutletVo> fetchBeatOutlets = iOutletRepository.findById(id);
      if (fetchBeatOutlets != null) {
        return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully", fetchBeatOutlets),
            HttpStatus.OK);

      } else {
        return new ResponseEntity<>(new ApiResponse(400, "NOT_FOUND", "Outlet Not Found in this Beat!", null),
            HttpStatus.NOT_FOUND);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Transactional
  public ResponseEntity<ApiResponse> outletSave(OutletVo input) {
    LOGGER.info("BaseService :: outletSave() called...");
    ResponseEntity<ApiResponse> apiResponse = null;
    try {
      input.setCreatedBy(input.getCreatedBy());
      input.setCreatedOn(new Date());
      input.setUpdatedOn(new Date());
      input.setProductDivision(iUserDetailsRepository.getById(input.getCreatedBy()).getProductDivision().getId());
      if (input.getOutletCode() == null || input.getOutletCode().isEmpty()) {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        input.setOutletCode("D" + randomNumber);
      }
      OutletVo savedData = iOutletRepository.save(input);

      if (savedData != null) {
        if (null != input.getOutletImg()) {
          MultipartFile file = input.getOutletImg();
          String filename = StringUtils.cleanPath(file.getOriginalFilename());
          String ext = FilenameUtils.getExtension(filename);
          filename = filename.replace("." + ext, "");
          filename = filename + "_" + System.currentTimeMillis() + "." + ext; // +"_"+savedData.getId()
          input.setOutletImageLink(filename);

          String s3OutletKeyPrefix = outletUploadFolder;
          FileResponse s3Response = s3StorageService.upload(new FileMetadata(file, filename),
              s3OutletKeyPrefix);

          String formType = "outlet";
          String tabName = "outlet";

          FormMediaMappingVo savedMediaData = new FormMediaMappingVo(null, savedData.getId(), formType,
              tabName, filename, s3Response.getFileUrl(), new Date());

          savedMediaData = iFormMediaMappingRepository.save(savedMediaData);
          if (savedMediaData != null) {
            LOGGER.info("Outlet Data saved in DB -----------" + savedMediaData);
          } else {
            LOGGER.info("Outlet Data NOT saved in DB -----------" + savedMediaData);
            throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserMediaData" });
          }
          savedData.setOutletImg(null);
        }

      } else {
        throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "" });
      }

      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
          HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "StockEntry" });
  }

  public List<NoOrderReasonMstVo> fetchNoOrderReasonList(Long parentId) {
    LOGGER.info("BaseService :: fetchNoOrderReasonList() called .....");
    List<NoOrderReasonMstVo> reason = iNoOrderReasonRepo.findByParentId(parentId);
    LOGGER.info("Reason check--->", reason);
    if (reason != null) {
      return reason;
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "NoOrderReason" });
    }
  }

  public ResponseEntity<ApiResponse> soUserActivity(SoActivityRegisterVo input) {
    LOGGER.info("BaseService :: soUserActivity() called...");

    return null;

  }

  public ResponseEntity<ApiResponse> fetchOutletDetails(Long outletId) {
    LOGGER.info("BaseService :: fetchOutletsById() called...");
    try {
      OutletVo fetchBeatOutlet = null;
      // iOutletRepository.fetchOutletDetails(outletId);
      if (fetchBeatOutlet != null) {
        return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully", fetchBeatOutlet),
            HttpStatus.OK);

      } else {
        return new ResponseEntity<>(new ApiResponse(400, "NOT_FOUND", "Outlet Not Found !!!", null),
            HttpStatus.NOT_FOUND);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<ApiResponse> saveAttendance(SoActivityRegisterVo input) {
    LOGGER.info("BaseService :: saveAttendance() called...");
    try {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String username = ((UserDetails) principal).getUsername();
      Long loginUserId = userCredRepository.loginUserId(username);
      LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
      if (null == loginUserId || loginUserId == 0L) {
        loginUserId = input.getCreatedBy();
        LOGGER.info(" loginUserId from input =========== > " + loginUserId);
      }

      // If marked leave then cant do attendance ============
      List<String> userActivity = iSOActivityRegisterRepo.findActivityOfUser(input.getOutletId(), loginUserId,
          new Date());
      if (userActivity.contains("Leave")) {
        return new ResponseEntity<ApiResponse>(
            new ApiResponse(406, "not_allowed", "You have marked as 'Leave' Today, Can't do Attendance"),
            HttpStatus.NOT_ACCEPTABLE);
      }

      String filename = "";
      MultipartFile file = null;
      if (null != input.getImage()) {
        file = input.getImage();
        filename = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = FilenameUtils.getExtension(filename);
        filename = filename.replace("." + ext, "");
        filename = filename + "_" + System.currentTimeMillis() + "." + ext; // +"_"+savedData.getId()
        input.setImagePath(filename); // selfieUploadFolder + filename
      }
      input.setActivityDate(new Date());
      input.setStartTimeDate(new Date());
      input.setStartTime(new Date().toString());
      input.setCreatedBy(loginUserId);

      DailyActivityMstVo dailyActivityId = new DailyActivityMstVo();
      dailyActivityId.setId(input.getDailyActivityIdtr());
      input.setDailyActivityId(dailyActivityId);

      UserDetailsVo soDtl = new UserDetailsVo();
      soDtl.setId(input.getSoIdtr());
      input.setSoId(soDtl);

      SoActivityRegisterVo savedData = iSOActivityRegisterRepo.save(input);
      if (savedData != null && null != input.getImage()) {
        String s3SoSelfieKeyPrefix = soSelfieUploadFolder;
        FileResponse s3Response = s3StorageService.upload(new FileMetadata(file, filename),
            s3SoSelfieKeyPrefix);
        String formType = "SO image";
        String tabName = "SO Selfie";

        FormMediaMappingVo savedMediaData = new FormMediaMappingVo(null, savedData.getId(), formType,
            tabName, filename, s3Response.getFileUrl(), new Date());

        savedMediaData = iFormMediaMappingRepository.save(savedMediaData);

        if (savedMediaData != null) {
          LOGGER.info("User Media Data saved in DB -----------" + savedMediaData);
          savedData.setImagePath(filename);
        } else {
          LOGGER.info("User Media Data NOT saved in DB -----------" + savedMediaData);
          throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserMediaData" });
        }
      }
      savedData.setImage(null);
      LOGGER.info(" SOUserActivityRegisterVo from Attendance for " + input.getDailyActivityId()
          + " activity ====================>" + savedData);
      return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
          HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserActivityRegister" });
  }

  public ResponseEntity<ApiResponse> fetchOrderOutletBySoId(Long id) {
    LOGGER.info("BaseService :: fetchOrderOutletBySoId() called...");
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    String currDate = simpleDateFormat.format(new Date());
    LOGGER.info("Current Date--->" + currDate);
    try {
      List<OutletVo> data = iOutletRepository.findAllOutletBySoId(id, currDate);

      if (data.size() > 0) {
        return new ResponseEntity<>(
            new ApiResponse(200, "SUCCESS", "Fetched Successfully", data), HttpStatus.OK);

      } else {
        return new ResponseEntity<>(
            new ApiResponse(400, "Not_Found", "Data not Fetched", null), HttpStatus.NOT_FOUND);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
