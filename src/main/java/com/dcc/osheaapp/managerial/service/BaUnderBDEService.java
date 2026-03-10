package com.dcc.osheaapp.managerial.service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.managerial.Repository.EditRequestReportRepo;
import com.dcc.osheaapp.managerial.Repository.IBdeUnderBdmRsmAsm;
import com.dcc.osheaapp.managerial.Repository.ILeftBaAchivementOfBde;
import com.dcc.osheaapp.managerial.vo.BACounterAchievementReportVo;
import com.dcc.osheaapp.managerial.vo.BdeListOutputVo;
import com.dcc.osheaapp.managerial.vo.EditRequestReportVo;
import com.dcc.osheaapp.managerial.Repository.IBACounterAchievementReportRepo;
import com.dcc.osheaapp.managerial.vo.LeftBaOfBdeAchievement;
import com.dcc.osheaapp.repository.IBaUnderABdeRepository;
import com.dcc.osheaapp.repository.IBdeOutletMappingRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.repository.IVersionRepository;
import com.dcc.osheaapp.vo.BaListOfABdeVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.Version;
import com.dcc.osheaapp.vo.views.BaListOfABdeOutputVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class BaUnderBDEService {

    private static final Logger LOGGER = LogManager.getLogger(BaUnderBDEService.class);
    @Autowired
    IBaUnderABdeRepository baUnderABdeRepository;

	@Autowired
	EditRequestReportRepo editRequestReportRepo;

	@Autowired
	IBdeUnderBdmRsmAsm iBdeUnderBdmRsmAsm;
	@Autowired
	IBACounterAchievementReportRepo iBACounterAchievementReportRepo;

	@Autowired
	IUserDetailsRepository iUserDetailsRepository;

	@Autowired
	ILeftBaAchivementOfBde iLeftBaAchivementOfBde;
	@Autowired
	IBdeOutletMappingRepository iBdeOutletMappingRepository;
	@Autowired
	IVersionRepository versionRepository;

    private static String getLimitString(BaListOfABdeVo input) {
	String limitStr = " ";

	if (null != input.getPage() || null != input.getSize()) {
	    int size = (null != input.getSize()) ? input.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
	    int page = (null != input.getPage() && input.getPage() > 1) ? (input.getPage() - 1) * size : 0;

	    limitStr = " limit " + size + " offset " + page;
	}
	LOGGER.info("limitStr..." + limitStr);
	return limitStr;
    }

    private static String getFilterString(BaListOfABdeVo input) {
	String whereClause = " ";
	String orderByStr = " order by trim(outletName) ASC  ";
	if (null != input.getId() && null != input.getDate() && !input.getDate().isEmpty()) {
	    String template = " and assotiatedUserId = <asscUser> and "
		    + "((isActive = true and dateOfJoining <= '<currentDate>' ) or (isActive = false and ('<currentDate>' >= dateOfJoining and '<currentDate>' < releaseDate))) and leftOn IS NULL";
	    whereClause = template.replace("<asscUser>", input.getId().toString()).replace("<currentDate>",
		    input.getDate());
	}
	LOGGER.info("Whereclause...." + whereClause + orderByStr);
	return whereClause + orderByStr;
    }

	private static String getFilterStringBa(Long bdeId) {
		String whereClause = " ";
		if (null != bdeId) {
			String template = " uad.assotiated_user_id = <asscUser> and uad.is_active=1 ";
			whereClause = template.replace("<asscUser>", bdeId.toString());
		}
		LOGGER.info("Whereclause...." + whereClause);
		return whereClause;
	}

    public List<BaListOfABdeOutputVo> baListUnderBde(BaListOfABdeVo input) {
	LOGGER.info("UserService :: baListUnderBde :: Entering....");
	String filterStr = getFilterString(input);
	String limitStr = getLimitString(input);
	return baUnderABdeRepository.searchBaListUnderBde(filterStr, limitStr);
    }

	public List<Long> baIdListUnderBde(Long bdeId) {
		LOGGER.info("UserService :: baListUnderBde :: Entering....");
		String filterStr = getFilterStringBa(bdeId);
		LOGGER.info("check filterster----->" + filterStr);
		return baUnderABdeRepository.fetchBaIdListUnderBde(filterStr);
	}

    public Long countBaUnderBde(BaListOfABdeVo input) {
	String whereClause = getFilterString(input);
	return baUnderABdeRepository.countBaListUnderBde(whereClause);
    }

	public ResponseEntity<ApiResponse> editReqCountOfBaUnderBde(Long bdeId) {
		Long countEditReq = editRequestReportRepo.fetchEditRequestCountOfBaUnderBde(bdeId);
		if(countEditReq != null){
			return new ResponseEntity<>(
					new ApiResponse(200, "SUCCESS", "Data Found", countEditReq), HttpStatus.OK);

		}else{
			return new ResponseEntity<>(
					new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
					HttpStatus.OK);
		}
	}

	public ResponseEntity<ApiResponse> editReqListOfBaUnderBde(Long id) {
		LOGGER.info("Ba UnderBDE Service :: Edit Request List :: Enter");

		try {
			List<EditRequestReportVo> editReqList = editRequestReportRepo.fetchEditRequestListOfBaUnderBde(id);

			if (editReqList.size() > 0) {
				LOGGER.info("EditList size :: " + editReqList.size());

				return new ResponseEntity<>(
						new ApiResponse(200, "SUCCESS", "Data Found", editReqList), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
						HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ApiResponse(500, "Server_Err", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}

	public ResponseEntity<ApiResponse> fetchEditRequestListOfBa(Long id) {
		LOGGER.info("Ba Edit Req List :: Edit Request List :: Enter");

		try {
			List<EditRequestReportVo> editReqList = editRequestReportRepo.fetchEditRequestListOfBa(id);

			if (editReqList.size() > 0) {
				LOGGER.info("EditList size :: " + editReqList.size());

				return new ResponseEntity<>(
						new ApiResponse(200, "SUCCESS", "Data Found", editReqList), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
						HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ApiResponse(500, "Server_Err", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}


	public ResponseEntity<ApiResponse> fetchAcheivementCounterWise(String rangeType1, String rangeType2, Long bdeId) {
		LOGGER.info("Ba Counter Total Achievemnt % :: Total Achievemnt % :: Enter");
		try {
			String currentMonth = LocalDate.now()
					.getMonth()
					.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
					.toUpperCase();
			LOGGER.info("Current Month---> "+currentMonth);
			List<Long> baUnderBdeIds = baIdListUnderBde(bdeId);
			LOGGER.info("Ba id list----->" + baUnderBdeIds);
			LOGGER.info("range type1----->" + rangeType1 +"range type2----->" + rangeType2);
			List<BACounterAchievementReportVo> results = iBACounterAchievementReportRepo.fetchAcheivementCounterWise(rangeType1, rangeType2, baUnderBdeIds, currentMonth);

			if (results.size() > 0) {
				LOGGER.info("results size :: " + results.size());


//				results = (List<BACounterAchievementReportVo>) results.stream().mapToDouble(i -> i.getTotalTargetPerDay().doubleValue());

				return new ResponseEntity<>(
						new ApiResponse(200, "SUCCESS", "Data Found", results, results.size()), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
						HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ApiResponse(500, "Server_Err", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}

	public ResponseEntity<ApiResponse> fetchAcheivementCounterWiseV2(String rangeType1, String rangeType2, Long bdeId) {
		LOGGER.info("Ba Counter Total Achievemnt % :: Total Achievemnt % :: Enter");
		try {
			String currentMonth = LocalDate.now()
					.getMonth()
					.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
					.toUpperCase();
			LOGGER.info("Current Month---> "+currentMonth);
//			List<Long> baUnderBdeIds = baIdListUnderBde(bdeId);
//			LOGGER.info("Ba id list----->" + baUnderBdeIds);
			List<Long> bdeOutlets = iBdeOutletMappingRepository.getOutlet(bdeId);
			LOGGER.info("outlet id list----->" + bdeOutlets);
			LOGGER.info("range type1----->" + rangeType1 +"range type2----->" + rangeType2);
			List<BACounterAchievementReportVo> results = iBACounterAchievementReportRepo.fetchAchievementCounterWiseV2(rangeType1, rangeType2, bdeOutlets, currentMonth);

			if (results.size() > 0) {
				LOGGER.info("results size :: " + results.size());


//				results = (List<BACounterAchievementReportVo>) results.stream().mapToDouble(i -> i.getTotalTargetPerDay().doubleValue());

				return new ResponseEntity<>(
						new ApiResponse(200, "SUCCESS", "Data Found", results, results.size()), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
						HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ApiResponse(500, "Server_Err", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}

	public ResponseEntity<ApiResponse> listOfBde(Long id) {
		LOGGER.info("BDE List of ASM/RSM/BDM :: listOfBde() :: Enter");

		try {
			List<BdeListOutputVo> bdeList = iBdeUnderBdmRsmAsm.fetchBde(id);

			if (bdeList.size() > 0) {

				return new ResponseEntity<>(
						new ApiResponse(200, "SUCCESS", "Data Found", bdeList), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
						HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ApiResponse(500, "Server_Err", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}

	public ResponseEntity<ApiResponse> fetchBdeDetails(Long id){

		UserDetailsVo bdedetails =iUserDetailsRepository.bdeDetails(id);

		if (bdedetails != null) {

			return new ResponseEntity<>(
					new ApiResponse(200, "SUCCESS", "Data Found", bdedetails), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
					HttpStatus.OK);
		}

	}

	public ResponseEntity<ApiResponse> leftBaOfBDEAchievement(Long bdeId){
		LOGGER.info("Ba under BDE service :: leftBaOfBDEAchievement()");
		try {
			String currentYrMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%";
			LOGGER.info("Current Month---> "+currentYrMonth);

			List<LeftBaOfBdeAchievement> results = iLeftBaAchivementOfBde.getLeftBaAchievement(bdeId, currentYrMonth);

			if (results.size() > 0) {
				LOGGER.info("results size :: " + results.size());
				return new ResponseEntity<>(
						new ApiResponse(200, "SUCCESS", "Data Found", results), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
						HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ApiResponse(500, "Server_Err", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public Version getActiveCategory(String appType) {
		appType = (null != appType && !appType.trim().isEmpty()) ? appType : "OJB_Managerial";
		return versionRepository.findByStatusAndAppType(true, appType)
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Active Version" }));
	}

	
	
	
	



}
