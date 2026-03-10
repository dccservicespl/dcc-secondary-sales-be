package com.dcc.osheaapp.ojbso.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.services.backup.model.ReportSetting;
import com.amazonaws.services.codecommit.model.RepositoryNameExistsException;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.ojbso.dto.SoBeatNTypeMappingInputVo;
import com.dcc.osheaapp.ojbso.dto.SoBeatNTypeMappingMini;
import com.dcc.osheaapp.ojbso.repo.*;
import com.dcc.osheaapp.ojbso.vo.*;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.vo.*;
import com.dcc.osheaapp.vo.views.TourPlaneDaysVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.repository.IBeatNameRepository;
import com.dcc.osheaapp.repository.IOutletRepository;
import com.dcc.osheaapp.repository.IUserCredRepository;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class TourPlanService {
    private static final Logger LOGGER = LogManager.getLogger(TourPlanService.class);

    private final IBeatNameRepository iBeatNameRepository;
    private final IUserCredRepository userCredRepository;
    private final IOutletRepository iOutletRepository;
    private final IDailyActivityMstRepository iDailyActivityMstRepository;
    private final ITourPlanActivityMstRepository iTourPlanActivityMstRepository;
    private final ITourPlanRepository iTourPlanRepository;
    private final ITourPlanDtlRepository iTourPlanDtlRepository;
    private final ITourPlanApprovalRepository iTourPlanApprovalRepository;
    private final IUserDetailsRepository iUserDetailsRepository;
    private final ITourPlanDays iTourPlanDays;
    private final ISoBeatNTypeMappingRepo soBeatNTypeMappingRepo;
    private final IBeatTypeMstRepo beatTypeMstRepo;
    private final ITourPlanOutputRepository iTourPlanOutputRepository;

    @Autowired
    public TourPlanService(IBeatNameRepository iBeatNameRepository, IOutletRepository iOutletRepository,
                           IDailyActivityMstRepository iDailyActivityMstRepository, IUserCredRepository userCredRepository,
                           ITourPlanActivityMstRepository iTourPlanActivityMstRepository, ITourPlanRepository iTourPlanRepository,
                           ITourPlanDtlRepository iTourPlanDtlRepository, ITourPlanApprovalRepository iTourPlanApprovalRepository,
                           IUserDetailsRepository iUserDetailsRepository, ITourPlanDays iTourPlanDays, ISoBeatNTypeMappingRepo soBeatNTypeMappingRepo,
                           IBeatTypeMstRepo beatTypeMstRepo, ITourPlanOutputRepository iTourPlanOutputRepository) {
        this.iBeatNameRepository = iBeatNameRepository;
        this.userCredRepository = userCredRepository;
        this.iOutletRepository = iOutletRepository;
        this.iDailyActivityMstRepository = iDailyActivityMstRepository;
        this.iTourPlanActivityMstRepository = iTourPlanActivityMstRepository;
        this.iTourPlanRepository = iTourPlanRepository;
        this.iTourPlanDtlRepository = iTourPlanDtlRepository;
        this.iTourPlanApprovalRepository = iTourPlanApprovalRepository;
        this.iUserDetailsRepository = iUserDetailsRepository;
        this.iTourPlanDays = iTourPlanDays;
        this.soBeatNTypeMappingRepo = soBeatNTypeMappingRepo;
        this.beatTypeMstRepo = beatTypeMstRepo;
        this.iTourPlanOutputRepository = iTourPlanOutputRepository;
    }


    public List<TourPlanActivityMstVo> fetchAllTourPlanActivity() {
        LOGGER.info("TourPlanService :: fetchAllTourPlanActivity() called...");
        List<TourPlanActivityMstVo> dataList = (List<TourPlanActivityMstVo>) iTourPlanActivityMstRepository.findAll();
        if (dataList != null) {
            return dataList;
        } else {
            throw new OjbException(ErrorCode.NOT_FOUND, new Object[]{"TourPlanActivity"});
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> saveTourPlan(TourPlanVo input) {
        LOGGER.info("StockService :: saveTourPlan() called...");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Long loginUserId = userCredRepository.loginUserId(username);

        LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
        if (null == loginUserId || loginUserId == 0L) {
            loginUserId = input.getSoId().getId();
            LOGGER.info(" loginUserId from input =========== > " + loginUserId);
        }
        LocalDate newStartDate = input.getTourPlanStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate newEndDate = input.getTourPlanEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // Check for overlapping tour plans
        long count = iTourPlanRepository.countOverlappingTourPlans(loginUserId,
                Date.from(newStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(newEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if (count > 0) {
//			return ResponseEntity.status(HttpStatus.CONFLICT)
//					.body(new ApiResponse(409, "Conflict", "Tour Plan already exists in the given date range", null));
            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(406, "not_accepted", "Tour Plan already exists in the given date range", null),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        //Calculate the day difference
        LocalDate dateBefore = input.getTourPlanStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateAfter = input.getTourPlanEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysBetween = Duration.between(dateBefore.atStartOfDay(), dateAfter.atStartOfDay()).toDays() + 1;
        TourPlanDaysMst tour = iTourPlanDays.findByIsActive(true);


        if ((daysBetween < tour.getNoOfDays())) {
            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(406, "not_accepted", "Tour Plan must be of 21 days", null),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (null == input.getId()) {
            input.setCreatedBy(loginUserId);
            input.setCreatedOn(new Date());
        }
        input.setPlanUpdatedOn(new Date());
        input.setUpdatedBy(loginUserId);
        input.setTourDays(daysBetween);

        /*below code is for when update tour plan with leave or holiday any other activity
        that not  require beat and beat type mapping */
        if (input.getTourPlanDtlVo() != null) {
            for (TourPlanDtlVo dtl : input.getTourPlanDtlVo()) {
                // Set back-reference
                dtl.setTourPlanVo(input);
                // Fix soBeatTypeMapping
                if (dtl.getSoBeatTypeMapping() != null) {
                    Long beatId = dtl.getSoBeatTypeMapping().getId();
                    if (beatId == null) {
                        dtl.setSoBeatTypeMapping(null);
                    } else {
                        SoBeatNTypeMappingVo beat = soBeatNTypeMappingRepo.findById(beatId)
                                .orElseThrow(() -> new OjbException("Invalid beat mapping", ErrorCode.NOT_FOUND, null));
                        dtl.setSoBeatTypeMapping(beat);
                    }
                }

                // Fix updateBeatTypeMapping
                if (dtl.getUpdateBeatTypeMapping() != null) {
                    Long updateId = dtl.getUpdateBeatTypeMapping().getId();
                    if (updateId == null) {
                        dtl.setUpdateBEatTypeMapping(null);
                    } else {
                        SoBeatNTypeMappingVo updateBeat = soBeatNTypeMappingRepo.findById(updateId)
                                .orElseThrow(() -> new OjbException("Invalid update beat mapping", ErrorCode.NOT_FOUND, null));
                        dtl.setUpdateBEatTypeMapping(updateBeat);
                    }
                }
            }
        }

        TourPlanVo savedData = iTourPlanRepository.save(input);
        saveApproval(savedData, loginUserId);
        if (savedData != null) {
            return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
                    HttpStatus.OK);
        } else {
            throw new OjbException(ErrorCode.NOT_SAVED, new Object[]{"Stock"});
        }
    }

    private void saveApproval(TourPlanVo input, Long loginUserId) {
        if (input.getPlanStatus().equals("Approval Pending")) {
            List<TourPlanApprovalVo> editRequest = new ArrayList<TourPlanApprovalVo>();
            List<UserDetailsVo> assignTo = iUserDetailsRepository.findUserForTourPlanApproval(loginUserId);
            for (UserDetailsVo each : assignTo) {
                TourPlanApprovalVo data = new TourPlanApprovalVo(input, "Approval Pending", new Date(), loginUserId,
                        new Date(), loginUserId, each);
                editRequest.add(data);
            }
            List<TourPlanApprovalVo> editRequestSaved = iTourPlanApprovalRepository.saveAll(editRequest);
            LOGGER.info("Data saved in Tour Approval table == > " + editRequestSaved);

            //take a snap ================== WORK TO DO HERE +++++++++++++++++++++++++
        }
    }

    public TourPlanVo fetchById(Long id) {
        TourPlanVo data = iTourPlanRepository.findById(id).orElse(null);
        LOGGER.info("Tour Plan Data:", data);
        return data;
    }

    public ResponseEntity<ApiResponse> fetchTourPlans(Long soId) {
        LOGGER.info("TourPlanService :: fetchTourPlans() called...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(new Date());
        try {
            List<TourPlanVo> resultVo = iTourPlanRepository.fetchTourPlans(soId,formattedDate);
            if (resultVo.size() > 0) {
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", resultVo),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse> fetchTourPlanOfSOByDate(Long soId, String inputDate) {
        LOGGER.info("TourPlanService :: fetchTourPlanOfSOByDate() called...");
        try {
            TourPlanDtlVo resultVo = iTourPlanDtlRepository.fetchTourPlanOfSOByDate(soId, inputDate);
            if (null != resultVo) {
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", resultVo),
                        HttpStatus.OK);
            } else {
//                return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null),
//                        HttpStatus.NOT_FOUND);
                return ResponseEntity.ok(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse> fetchAllTourPlan(TourPlanInputVo inputVo) {
        LOGGER.info("TourPlanService :: searchByInput::Entering...");
        ApiResponse apiResponse;
        List<TourPlanOutputVo> getDataList=null;
        String whereClause = " ";
        String limitStr = "";
        String orderByStr = "order by created_on ASC";
        if (null == inputVo.getPage() && null == inputVo.getSize()) {

        } else {
            Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
            Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
            limitStr = "limit " + size + " offset " + page;
            LOGGER.info("limitstr..." + limitStr);
        }

        if (null != inputVo.getSoId()) {
            whereClause += " and so_id = " + inputVo.getSoId();
        }
        if (null != inputVo.getPlanStatus()) {
            whereClause += " and plan_status in ('Approved','Approval Pending','Rejected')";
        }

        if (null != inputVo.getZoneId()) {
            whereClause += " and d.id = " + inputVo.getZoneId();
        }


        LOGGER.info("Whereclause..." + whereClause);
        getDataList = iTourPlanOutputRepository.searchTourPlanByInput(whereClause, limitStr);
		int totalNo = getDataList.size();
//        LOGGER.info("TourPlanService::searchTourPlanByInput::Exiting...");
//
//        if (!getDataList.isEmpty()) {
////            apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
//            return new ResponseEntity<>(new ApiResponse(200, "success", "Fetch Successfully", getDataList, totalNo),
//                    HttpStatus.OK);
//        } else {
//            throw new OjbException(ErrorCode.NOT_FOUND, new Object[]{"tour_plan"});
//        }
        apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
        LOGGER.info("UserService::searchByInput::Exiting...");
        // return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
        if (getDataList != null) {
            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
        } else {
            throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Tour Plan" });
        }
    }

    public ResponseEntity<ApiResponse> updateStatusofTourPlan(TourPlanVo input) {
        LOGGER.info("TourPlanService :: fetchTourPlans() called...");
        try {
            iTourPlanRepository.updateStatus(input.getId(), input.getRemarks(), input.getPlanStatus());
            return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!"),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> updatedApprovalStatus(TourPlanApprovalVo input) {
        LOGGER.info("TourPlanService :: updatedApprovalStatus() called...");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Long loginUserId = userCredRepository.loginUserId(username);

        String inputStatus = input.getPlanStatus();
        String inputRemarks = input.getRemarks();

        LOGGER.info("TourPlanService :: inputStatus ============ > " + inputStatus);

        try {


            List<TourPlanApprovalVo> getPendingData = iTourPlanApprovalRepository
                    .getPendingData(input.getTourPlanVo().getId());
            boolean isValidUser = false;
            if (null != getPendingData && getPendingData.size() > 0) {
                for (TourPlanApprovalVo each : getPendingData) {
                    // If login user is admin, and he approves
                    if (loginUserId.equals(each.getAssignTo().getId())
                            && input.getUserType().equalsIgnoreCase("admin")) {
                        // call Admin Approval method
                        iTourPlanApprovalRepository.updateApprovalStatus(each.getId(), inputStatus, inputRemarks,
                                new Date(), loginUserId);
                        iTourPlanRepository.updateTourStatus(
                                input.getTourPlanVo().getId(), input.getPlanStatus(), new Date(),
                                loginUserId);
                        isValidUser = true;
                    } else if (loginUserId.equals(each.getAssignTo().getId())
                            && !input.getUserType().equalsIgnoreCase("admin")) {
                        // If login user is NOT admin, then
                        // update status here
                        int tourApproval = iTourPlanApprovalRepository.updateApprovalStatus(each.getId(), inputStatus,
                                inputRemarks, new Date(), loginUserId);
                        if (tourApproval > 0) {
                            int tourStatus = iTourPlanRepository.updateTourStatus(
                                    input.getTourPlanVo().getId(), "First Level " + inputStatus, new Date(),
                                    loginUserId);
                            LOGGER.info("Tour Plan Service :: updatedApprovalStatus() tourApproval called..."
                                    + tourStatus);
                        }
                        isValidUser = true;
                    }
                }
                if (!isValidUser) {
                    ApiResponse apiResponse = new ApiResponse(406, "not_valid_user",
                            "Not valid user to " + inputStatus + " this Edit request.", null, 0);
                    return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
                }
            } else {
                ApiResponse apiResponse = new ApiResponse(302, "already_" + inputStatus,
                        "This Edit request is already " + inputStatus, null, 0);
                return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OjbException(ErrorCode.NOT_SAVED, new Object[]{"Stock"});
        }
        ApiResponse apiResponse = new ApiResponse(200, "success", "success", null, 0);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    public boolean checkOverLapDate(String ss1, String ee1, String ss2, String ee2) throws ParseException {
        //create object of SimpleDateFormat
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        //create object of Date and initialize values
        Date s1 = s.parse(ss1);
        Date e1 = s.parse(ee1);
        Date s2 = s.parse(ss2);
        Date e2 = s.parse(ee2);

        //conditions for different cases
        if (s1.before(s2) && e1.after(s2) ||
                s1.before(e2) && e1.after(e2) ||
                s1.before(s2) && e1.after(e2) ||
                s1.after(s2) && e1.before(e2)) {
            System.out.print("They overlap");
            return true;
        } else {
            System.out.print("They don't overlap");
            return false;
        }
    }

    public ResponseEntity<ApiResponse> fetchTourPlanDtlByDate(Long soId, String ipDate) {
        LOGGER.info("TourPlanService :: fetchTourPlans() called...");
        try {
            TourPlanDtlVo result = iTourPlanDtlRepository.fetchTourPlanOfSOByDate(soId, ipDate);
            if (result != null) {
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", result),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Fetched Successfully!"),
                        HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> saveTotalTourPlanDays(TourPlanDaysMst input) {
        LOGGER.info("TourPlanService:: saveTotalTourPlanDays()");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Long loginUserId = userCredRepository.loginUserId(username);
        try {
            int changeData = iTourPlanDays.deactivatePreviousRecords();
            if (changeData != 0) {
                input.setActive(true);
                if (null == input.getId()) {
                    input.setCreatedBy(loginUserId);
                    input.setCreatedOn(new Date());
                }
                input.setUpdatedBy(loginUserId);
                input.setUpdatedOn(new Date());
                TourPlanDaysMst saveData = iTourPlanDays.save(input);
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Save Successfully", saveData), HttpStatus.OK);
            } else {
                throw new OjbException(ErrorCode.NOT_SAVED, new Object[]{"TourPlanDays"});
            }
        } catch (Exception e) {
            throw new OjbException(ErrorCode.NOT_SAVED, new Object[]{"TourPlanDays"});
        }
    }

    public TourPlanDaysMst getTotalTourPlanDays() {
        LOGGER.info("TourPlanService :: getTotalTourPlanDays() called...");
        TourPlanDaysMst tour = iTourPlanDays.findByIsActive(true);
        if (tour != null) {
            return tour;
        } else {
            throw new OjbException(ErrorCode.NOT_FOUND, new Object[]{"TourPlan"});
        }
    }

    public List<TourPlanDaysMst> getAllTourPlanDays() {
        LOGGER.info("TourPlanService :: getAllTourPlanDays() called...");
        List<TourPlanDaysMst> tourList = Optional.ofNullable(iTourPlanDays.findAll())
                .filter(tour -> !tour.isEmpty()) // Ensure the list is not null or empty
                .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"TourPlan"}));

        // Sort the list by 'active' status (true first)
        return tourList.stream()
                .sorted((a, b) -> Boolean.compare(b.getActive(), a.getActive()))
                .collect(Collectors.toList());
    }

//	@Transactional
//	public ResponseEntity<ApiResponse> saveBeatTypeMapping(SoBeatNTypeMappingInputVo in) {
//		LOGGER.info("StockService :: saveBeatTypeMapping() called...");
//		List<SoBeatNTypeMappingVo> input = in.getBeatTypeMappingList();
//
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String username = ((UserDetails) principal).getUsername();
//		Long loginUserId = userCredRepository.loginUserId(username);
//
//		LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
//		if (null == loginUserId || loginUserId == 0L) {
//			loginUserId = input.get(0).getSoId().getId();
//			LOGGER.info(" loginUserId from input =========== > " + loginUserId);
//		}
//		//Extracting beat_id values from input list
//		List<Long> beatIds = input.stream()
//				.map(obj -> obj.getBeatId().getId())
//				.collect(Collectors.toList());
//		LOGGER.info("Beat IDs to be updated: " + beatIds);
//
//		//Deactivate existing mappings before inserting new ones
//		int updatedMapping = soBeatNTypeMappingRepo.deactivateExistingMappings(loginUserId, beatIds);
//		LOGGER.info("Existing mappings deactivated for loginUserId: " + loginUserId + " and beatId: " + beatIds);
//		for(SoBeatNTypeMappingVo obj : input) {
//			if (null == obj.getId()) {
//				obj.setCreatedBy(loginUserId);
//				obj.setCreatedOn(new Date());
//			}
//			obj.setUpdatedBy(loginUserId);
//			obj.setUpdatedOn(new Date());
//		}
//		List<SoBeatNTypeMappingVo> savedData = (List<SoBeatNTypeMappingVo>) soBeatNTypeMappingRepo.saveAll(input);
//		if (savedData != null) {
//			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
//					HttpStatus.OK);
//		} else {
//			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "Stock" });
//		}
//	}
//
//	public ResponseEntity<ApiResponse> fetchBeatListOfSO(Long soId) {
//		LOGGER.info("TourPlanService :: fetchBeatListOfSO() called...");
//		try {
//			List<SoBeatNTypeMappingMini> beatList = soBeatNTypeMappingRepo.findBysoIdIdAndActiveFlag(soId, true);
//			if(beatList != null){
//				return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", beatList),
//						HttpStatus.OK);
//			}else{
//				return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Fetched Successfully!"),
//						HttpStatus.OK);
//			}
//
//		} catch (Exception e){
//			e.printStackTrace();
//			return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//
//	public ResponseEntity<ApiResponse> fetchBeatDetailsOfSO(Long soId) {
//		LOGGER.info("TourPlanService :: fetchBeatDetailsOfSO() called...");
//		try {
//			List<SoBeatNTypeMappingVo> beatList = soBeatNTypeMappingRepo.findBeatDetailsBySoId(soId);
//			if(beatList != null){
//				return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", beatList),
//						HttpStatus.OK);
//			}else{
//				return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Fetched Successfully!"),
//						HttpStatus.OK);
//			}
//
//		} catch (Exception e){
//			e.printStackTrace();
//			return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//
//	public ResponseEntity<ApiResponse> fetchBeatType(){
//		LOGGER.info("TourPlanService :: fetchBeatType() called...");
//		try {
//			List<BeatTypeMst> beatTypeList = beatTypeMstRepo.findAll();
//			if(beatTypeList != null){
//				return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", beatTypeList),
//						HttpStatus.OK);
//			}else{
//				return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Fetched Successfully!"),
//						HttpStatus.OK);
//			}
//
//		} catch (Exception e){
//			e.printStackTrace();
//			return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}

    public ResponseEntity<ApiResponse> fetchActiveBeatMapping(Long soId) {
        LOGGER.info("TourPlanService :: fetchActiveBeatMapping() called...");
        try {
            List<SoBeatNTypeMappingVo> resultVo = soBeatNTypeMappingRepo.fetchActiveBeatMapping(soId);
            if (resultVo.size() > 0) {
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", resultVo),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<ApiResponse> updateTourPlaneDaysStatus(TourPlaneDaysVo input) {
        LOGGER.info("TourPlaneService :: updateTourPlaneDaysStatus() called...");
        int saveData = iTourPlanDays.updateTourPlaneDaysStatus(input.getId(),input.getStatus());
        if (saveData != 0) {
            return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Tour Plane Days Updated", null), HttpStatus.OK);
        } else {
            throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "TourPlaneDaysMst" });
        }
    }


    public ResponseEntity<ApiResponse> deleteTourPlanDays(Long id) {
        LOGGER.info("TourPlaneService :: deleteTourPlanDays() called...");

        try{
            Boolean status = iTourPlanDays.getTourPlaneDaysStatus(id);
            if (null != status && !status) {
                // Inactive data will be deleted from DB
                iTourPlanDays.deleteById(id);
                ApiResponse apiResponse = new ApiResponse(200, "success", "TourPlaneDays deleted successfully");
                return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
            }else{
                ApiResponse apiResponse = new ApiResponse(200, "success", "This tourPlaneDays is active, it can't be deleted.", null, 0);
                return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "TourPlanDays" });
        }

    }

}
