package com.dcc.osheaapp.ojbso.service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.ojbso.dto.*;
import com.dcc.osheaapp.ojbso.repo.*;
import com.dcc.osheaapp.ojbso.vo.*;
import com.dcc.osheaapp.repository.IUserCredRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.vo.FormMediaMappingVo;
import com.dcc.osheaapp.vo.StockEntryVo;
import com.dcc.osheaapp.vo.StockSearchInputVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import io.netty.handler.ssl.OpenSslSessionTicketKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BeatService {
    private static final Logger LOGGER = LogManager.getLogger(TourPlanService.class);
    private final IUserCredRepository userCredRepository;
    private final ISoBeatNTypeMappingRepo soBeatNTypeMappingRepo;
    private final IBeatTypeMstRepo beatTypeMstRepo;
    private final ITourPlanRepository iTourPlanRepository;
    private final ITourPlanDtlRepository iTourPlanDtlRepository;
    private final IBeatChangeRequestRepo iBeatChangeRequestRepo;
    private final IUserDetailsRepository iUserDetailsRepository;
    private final  IBeatChangeReqApprovalRepository iBeatChangeReqApprovalRepository;

    public BeatService(IUserCredRepository userCredRepository, ISoBeatNTypeMappingRepo soBeatNTypeMappingRepo,
                       IBeatTypeMstRepo beatTypeMstRepo, ITourPlanRepository iTourPlanRepository, ITourPlanDtlRepository iTourPlanDtlRepository,
                       IBeatChangeRequestRepo iBeatChangeRequestRepo, IUserDetailsRepository iUserDetailsRepository, IBeatChangeReqApprovalRepository iBeatChangeReqApprovalRepository){
        this.userCredRepository = userCredRepository;
        this.soBeatNTypeMappingRepo = soBeatNTypeMappingRepo;
        this.beatTypeMstRepo = beatTypeMstRepo;
        this.iTourPlanRepository =iTourPlanRepository;
        this.iTourPlanDtlRepository = iTourPlanDtlRepository;
        this.iBeatChangeRequestRepo = iBeatChangeRequestRepo;

        this.iUserDetailsRepository = iUserDetailsRepository;
        this.iBeatChangeReqApprovalRepository = iBeatChangeReqApprovalRepository;
    }

    @Transactional
    public ResponseEntity<ApiResponse> saveBeatTypeMapping(SoBeatNTypeMappingInputVo in) {
        LOGGER.info("BeatService :: saveBeatTypeMapping() called...");
        List<SoBeatNTypeMappingVo> input = in.getBeatTypeMappingList();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Long loginUserId = userCredRepository.loginUserId(username);

        LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
        if (null == loginUserId || loginUserId == 0L) {
            loginUserId = input.get(0).getSoId().getId();
            LOGGER.info(" loginUserId from input =========== > " + loginUserId);
        }
//        Extracting beat_id values from input list
        List<Long> beatIds = input.stream()
                .map(obj -> obj.getBeatId().getId())
                .collect(Collectors.toList());
        LOGGER.info("Beat IDs to be updated: " + beatIds);

         //fetch existing active beat for so
        List<SoBeatNTypeMappingVo> existingMappings = soBeatNTypeMappingRepo.fetchActiveBeatMapping(loginUserId);
        // Convert existing mappings to a Map<beatId, beatTypeId>
        Map<Long, Long> existingBeatTypeMap = existingMappings.stream()
                .collect(Collectors.toMap(
                        mapping -> mapping.getBeatId().getId(), // Key: beatId
                        mapping -> mapping.getBeatTypeId().getId() // Value: beatTypeId
                ));
        // List to store only the new or updated mappings
        List<SoBeatNTypeMappingVo> mappingsToSave = new ArrayList<>();

        for (SoBeatNTypeMappingVo obj : input) {
            Long beatId = obj.getBeatId().getId();
            Long newBeatTypeId = obj.getBeatTypeId().getId();

            // Check if beatId exists in existing mappings and if beatTypeId is different
            if (existingBeatTypeMap.containsKey(beatId)) {
                if (!existingBeatTypeMap.get(beatId).equals(newBeatTypeId)) {
                    // in active the existing mapping
                    int updatedMapping = soBeatNTypeMappingRepo.deactivateExistingMappings(loginUserId, List.of(beatId));
                    LOGGER.info("Existing mapping deactivated for loginUserId: " + loginUserId + " and beatId: " + beatId);
                    mappingsToSave.add(obj); // Add to save list
                }
            } else {
                // if beatId is not found in existing mappings, it's a new mapping
                mappingsToSave.add(obj);
            }
        }

        // new records for saving
        for (SoBeatNTypeMappingVo obj : input) {
            if (null == obj.getId()) {
                obj.setCreatedBy(loginUserId);
                obj.setCreatedOn(new Date());
            }
            obj.setUpdatedBy(loginUserId);
            obj.setUpdatedOn(new Date());
        }

        List<SoBeatNTypeMappingVo> savedData = StreamSupport
                .stream(soBeatNTypeMappingRepo.saveAll(mappingsToSave).spliterator(), false)
                .collect(Collectors.toList());

        if (savedData != null) {
            return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
                    HttpStatus.OK);
        } else {
            throw new OjbException(ErrorCode.NOT_SAVED, new Object[]{"Stock"});
        }
    }

    public ResponseEntity<ApiResponse> fetchBeatListOfSO(Long soId) {
        LOGGER.info("BeatService :: fetchBeatListOfSO() called...");
        try {
            List<SoBeatNTypeMappingMini> beatList = soBeatNTypeMappingRepo.findBysoIdIdAndActiveFlag(soId, true);
            if(beatList != null){
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", beatList),
                        HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Fetched Successfully!"),
                        HttpStatus.OK);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse> fetchBeatDetailsOfSO(Long soId) {
        LOGGER.info("BeatService :: fetchBeatDetailsOfSO() called...");
        try {
            List<SoBeatNTypeMappingVo> beatList = soBeatNTypeMappingRepo.findBeatDetailsBySoId(soId);
            if(beatList != null){
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", beatList),
                        HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Fetched Successfully!"),
                        HttpStatus.OK);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse> fetchBeatType(){
        LOGGER.info("BeatService :: fetchBeatType() called...");
        try {
            List<BeatTypeMst> beatTypeList = beatTypeMstRepo.findAll();
            if(beatTypeList != null){
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", beatTypeList),
                        HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Fetched Successfully!"),
                        HttpStatus.OK);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<ApiResponse> fetchActiveBeatMapping(Long soId){
        LOGGER.info("BeatService :: fetchActiveBeatMapping() called...");
        try {
            List<SoBeatNTypeMappingVo> resultVo = soBeatNTypeMappingRepo.fetchActiveBeatMapping(soId);
            if(resultVo.size() > 0){
                return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", resultVo),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null),
                        HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    public ResponseEntity<ApiResponse> beatChangeRequest(BeatChangeRequestDto input){
        LOGGER.info("BeatService() :: beatChangeRequest() call ....");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Long loginUserId = userCredRepository.loginUserId(username);
        LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
        if (null == loginUserId || loginUserId == 0L) {
            loginUserId = input.getSoId();
            LOGGER.info(" loginUserId from input =========== > " + loginUserId);
        }
//        List<TourPlanVo> tourPlans = iTourPlanRepository.findAllBySoId(input.getSoId());
//        Optional<TourPlanVo> tourPlanVo = tourPlans.stream()
//                .filter(p->p.getTourPlanStartDate().compareTo(input.getCurrentDate()) <=0
//                && p.getTourPlanEndDate().compareTo(input.getCurrentDate()) >=0).findFirst();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TourPlanVo tourPlanVo =  iTourPlanRepository.findTourPlanByDate(input.getSoId(), input.getCurrentDate());
        LOGGER.info("Tour plan ----"+tourPlanVo);
        if(tourPlanVo == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(404,"Error","No tour plan found"));
        // Find today's tour plan detail entry
        LOGGER.info("Tour Plan id --->" + tourPlanVo.getId() + " " + "Current Date" +sdf.format(input.getCurrentDate())  );
        TourPlanDtlVo existingDetail = iTourPlanDtlRepository.findByTourPlanIdAndPlanDate(
                tourPlanVo.getId(), sdf.format(input.getCurrentDate()));
        LOGGER.info("Existing Details--->"+existingDetail);
        if (existingDetail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(404, "ERROR", "No plan found for this date"));
        }
        LOGGER.info("currentDate---->"+sdf.format(input.getCurrentDate()) +"--->"+loginUserId);
       Optional<BeatChangeReqVo>  beatChangeReqVo = iBeatChangeRequestRepo.getByCreatedOnAndSoId(sdf.format(input.getCurrentDate()),loginUserId);

        // check is that day by that soId any pending request is present then any other request does not send
        if (beatChangeReqVo.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(409, "ERROR", "A beat change request is already pending for today"));
        }
        //Check request beat and existing beat same or not
        else if (existingDetail.getSoBeatTypeMapping().getBeatId().equals(input.getUpdatedBeat())) {
            return ResponseEntity.ok(new ApiResponse(200, "SUCCESS", "Today's beat and requested beat are the same", existingDetail));
        }
        //Check already change request for today is send

            // Create a new BeatChangeReqVo object and map fields
            BeatChangeReqVo beatChangeRequest = new BeatChangeReqVo();
            beatChangeRequest.setTourPlanId(existingDetail.getTourPlanVo());
            beatChangeRequest.setTourPlanDate(existingDetail.getPlanDate());
            beatChangeRequest.setLastBeat(existingDetail.getSoBeatTypeMapping().getBeatId());
            beatChangeRequest.setUpdatedBeat(input.getUpdatedBeat());
            beatChangeRequest.setCreatedBy(loginUserId);
            beatChangeRequest.setCreatedOn(new Date());
            beatChangeRequest.setStatus("Approval Pending");  // status for a change request
            beatChangeRequest.setRemarks(input.getRemarks());
            beatChangeRequest.setTourPlanDate(input.getCurrentDate());
            beatChangeRequest.setUpdatedBy(loginUserId);
            beatChangeRequest.setUpdatedOn(new Date());
            beatChangeRequest.setSoId(input.getSoId());
            iBeatChangeRequestRepo.save(beatChangeRequest);
            saveApproval(beatChangeRequest,loginUserId);
        return ResponseEntity.ok(new ApiResponse(200, "SUCCESS", "Beat update request send successfully", existingDetail));
    }
    private void saveApproval(BeatChangeReqVo input, Long loginUserId) {
        if (input.getStatus().equals("Approval Pending")) {
            List<BeatChangeReqApproval> editRequest = new ArrayList<BeatChangeReqApproval>();
            List<UserDetailsVo> assignTo = iUserDetailsRepository.findUserForTourPlanApproval(loginUserId);
            for (UserDetailsVo each : assignTo) {
                BeatChangeReqApproval data = new BeatChangeReqApproval(input, "Approval Pending", new Date(), loginUserId,
                        new Date(), loginUserId, each);
                editRequest.add(data);
            }
            List<BeatChangeReqApproval> editRequestSaved = iBeatChangeReqApprovalRepository.saveAll(editRequest);
            LOGGER.info("Data saved in beat Change Req Approval table == > " + editRequestSaved);
        }
    }

    public ResponseEntity<ApiResponse> searchByInput(BeatChangeSearchInputVo inputVo){
        LOGGER.info("BeatService::searchByInput::Entering...");
        ApiResponse apiResponse = null;
        List<BeatChangeReqVo> getDataList = null;
        String whereClause = " ";
        String limitStr = "";
        if (null == inputVo.getPage() && null == inputVo.getSize()) {
            // do nothing
        } else {
            Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
            Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
            limitStr = " order by created_on desc limit " + size + " offset " + page;
            LOGGER.info("limitStr..." + limitStr);
        }

        if (null != inputVo.getSoId()) {
            whereClause += " and so_id = " + inputVo.getSoId();
        }
//        if (null != inputVo.getBeatId()) {
//            whereClause += " and last_beat = " + inputVo.getBeatId();
//        }
        if (null != inputVo.getPlanStatus()) {
            whereClause += " and status in ('Approved','Approval Pending','Rejected')";
        }
        if (null != inputVo.getBeatId()) {
            whereClause += " and update_beat = " + inputVo.getBeatId();
        }
        else {
            if (null != inputVo.getFromDate() && !inputVo.getFromDate().trim().isEmpty() && null != inputVo.getToDate()
                    && !inputVo.getToDate().trim().isEmpty()) {
                LOGGER.info("vo.getToDate() ------------ " + inputVo.getToDate());
                whereClause += " and DATE_FORMAT((created_on),'%Y-%m-%d') BETWEEN DATE_FORMAT('"
                        + inputVo.getFromDate() + "','%Y-%m-%d') " + "AND DATE_FORMAT('" + inputVo.getToDate()
                        + "','%Y-%m-%d') ";
            }
        }
        getDataList = iBeatChangeRequestRepo.searchByInput(whereClause, "beat_change_request", limitStr);

        int totalNo = iBeatChangeRequestRepo.getTotalCountByInput(whereClause, "beat_change_request").intValue();
        LOGGER.info("UserService::searchByInput::Exiting...");
        if (getDataList != null) {
            apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
        } else {
            throw new OjbException(ErrorCode.NOT_FOUND, new Object[] {"outlet"});
        }
    }

    public ResponseEntity<ApiResponse> beatChangeReqListBySOId(Long soId){
        LOGGER.info("BeatService() :: beatChangeReqListBySOId() call ....");
        List<BeatChangeReqVo> beatChangeReqVo =iBeatChangeRequestRepo.findAllBySoId(soId);
        if (beatChangeReqVo == null || beatChangeReqVo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "NOT_FOUND", "Requested data not found", null));
        }
        List<BeatChangeReqVo> sortedList = beatChangeReqVo.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(BeatChangeReqVo::getCreatedOn, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        LOGGER.info("Sorted List ---> " + sortedList);
        return ResponseEntity.ok(new ApiResponse(200, "SUCCESS", "Success", sortedList));
    }

    public BeatChangeReqVo fetchBeatChangeById(Long id){
        BeatChangeReqVo data = iBeatChangeRequestRepo.findById(id).orElse(null);
        if (null != data) {
            return data;
        }else {
            new OjbException(ErrorCode.NOT_FOUND, new Object[] { "beatChangeRequest" });
        }
        return data ;
    }

    @Transactional
    public ResponseEntity<ApiResponse> updateApprovalStatus( BeatChangeReqApproval input) {
        LOGGER.info("BeatService :: updateApprovalStatus() called...");
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername();
            Long loginUserId = userCredRepository.loginUserId(username);

            String inputStatus = input.getPlanStatus();
            String inputRemarks = input.getRemarks();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            LOGGER.info("BeatService :: inputStatus ============ > " + inputStatus);
        LOGGER.info("BeatService :: inputId ============ > " + input.getBeatChangeReqVo().getId());

//                int requestApproval = iBeatChangeRequestRepo.updateBeatChangeRequestStatus(
//                        inputRemarks, input.getId(), input.getApprovalStatus(), new Date(), loginUserId);
//                if (requestApproval > 0) {
//                    LOGGER.info("BeatService :: Status Update Successfully");
//                    return ResponseEntity.ok(new ApiResponse(200, "SUCCESS", "Status Update Successfully", null));
//                } else {
//                    LOGGER.warn("BeatService :: Failed to update status");
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .body(new ApiResponse(500, "FAILED", "Status update failed", null));
//                }
//
//        } catch (Exception e) {
//            LOGGER.error("BeatService :: Exception occurred while updating status", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse(500, "ERROR", "Internal Server Error", null));
//        }
        try {

            List<BeatChangeReqApproval> getPendingData = iBeatChangeReqApprovalRepository
                    .getPendingData(input.getBeatChangeReqVo().getId());

            /*Below lines 384,386,387,388 is for when beat change request update then also update tour_plan_dtl table
             on request date plan */
            BeatChangeReqVo request = iBeatChangeRequestRepo.findById(input.getBeatChangeReqVo().getId()).
                    orElseThrow(() -> new RuntimeException("Request not found"));
            String dateOnly = sdf.format(request.getTourPlanDate());
            TourPlanDtlVo tourPlanDtlVo = iTourPlanDtlRepository.findByCurrDateAndTourPlanID(request.getTourPlanId().getId(),dateOnly);
            SoBeatNTypeMappingVo beatAndType = soBeatNTypeMappingRepo.findActiveBeatMapping(request.getUpdatedBeat().getId());

            LOGGER.info("Beat approval list-->"+getPendingData);
            boolean isValidUser = false;
            if (null != getPendingData && getPendingData.size() > 0) {
                for (BeatChangeReqApproval each : getPendingData) {
                    // If login user is admin, and he approves
                    if (loginUserId.equals(each.getAssignTo().getId())
                            && input.getUserType().equalsIgnoreCase("admin")) {
                        // call Admin Approval method
                        iBeatChangeReqApprovalRepository.updateApprovalStatus(each.getId(), inputStatus, inputRemarks,
                                new Date(), loginUserId);
                        iBeatChangeRequestRepo.updateTourStatus(
                                input.getBeatChangeReqVo().getId(), input.getPlanStatus(), new Date(),
                                loginUserId);
                        /*
                        when beat change request update then also update tour_plan_dtl table
                        on request date plan.
                         */
                        if(inputStatus.equals("Approved")){
                            tourPlanDtlVo.setUpdateBEatTypeMapping(beatAndType);
                            tourPlanDtlVo.setUpdateDetails(beatAndType.getBeatAndTypeName());
                            iTourPlanDtlRepository.save(tourPlanDtlVo);
                        }
                        isValidUser = true;
                    } else if (loginUserId.equals(each.getAssignTo().getId())
                            && !input.getUserType().equalsIgnoreCase("admin")) {
                        // If login user is NOT admin, then
                        // update status here
                        int tourApproval = iBeatChangeReqApprovalRepository.updateApprovalStatus(each.getId(), inputStatus,
                                inputRemarks, new Date(), loginUserId);
                        if (tourApproval > 0) {
                            int tourStatus = iBeatChangeRequestRepo.updateTourStatus(
                                    input.getBeatChangeReqVo().getId(), "First Level " + inputStatus, new Date(),
                                    loginUserId);
                            LOGGER.info("Tour Plan Service :: updatedApprovalStatus() tourApproval called..."
                                    + tourStatus);
                        }
                        if(!inputStatus.equals("Rejected")){
                            tourPlanDtlVo.setUpdateBEatTypeMapping(beatAndType);
                            tourPlanDtlVo.setUpdateDetails(beatAndType.getBeatAndTypeName());
                            iTourPlanDtlRepository.save(tourPlanDtlVo);
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
}

