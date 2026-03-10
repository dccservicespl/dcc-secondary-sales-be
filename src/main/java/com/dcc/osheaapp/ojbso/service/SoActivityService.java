package com.dcc.osheaapp.ojbso.service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.dcc.osheaapp.ojbso.repo.*;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.vo.CodeValueVo;
import com.dcc.osheaapp.vo.OutletInputVo;
import com.dcc.osheaapp.ojbso.vo.*;
import com.dcc.osheaapp.repository.TimeLineOutputVoRepo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContextShutdownAware;
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
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.ojbso.dto.OrderInputVo;
import com.dcc.osheaapp.ojbso.dto.OutletDetailsVo;
import com.dcc.osheaapp.ojbso.dto.SOActivitySummaryDtlDto;
import com.dcc.osheaapp.ojbso.dto.SOActivitySummaryDto;
import com.dcc.osheaapp.repository.IBeatNameRepository;
import com.dcc.osheaapp.repository.IOutletRepository;
import com.dcc.osheaapp.repository.IUserCredRepository;

import com.dcc.osheaapp.vo.BeatName;
import com.dcc.osheaapp.vo.OutletVo;

@Service
public class SoActivityService {
	private static final Logger LOGGER = LogManager.getLogger(SoActivityService.class);

	private final IUserCredRepository userCredRepository;
	private final ISOActivityRegisterRepository iSOActivityRegisterRepo;

	private final ISOActivityChangeRequestRepo iSOActivityChangeRequestRepo;
	private final IOrderRepository iOrderRepository;
	private final IUserDistributorMappingRepository iUserDistributorMappingRepository;
	private final IOutletRepository iOutletRepository;
	private final ISODailyActivityMstRepository isoDailyActivityMstRepository;
	private final IBeatNameRepository iBeatNameRepository;
	private final IFormMediaMappingRepository iFormMediaMappingRepository;
	private final ICodeValueRepo iCodeValueRepo;
	private final ISOActivitySummaryDtlRepo iSOActivitySummaryDtlRepo;
	private final TimeLineOutputVoRepo timeLineOutputVoRepo;
	private final ITourPlanDtlRepository iTourPlanDtlRepository;

	@Autowired
	public SoActivityService(IUserCredRepository userCredRepository,
			ISOActivityRegisterRepository iSOActivityRegisterRepo, IOrderRepository iOrderRepository,
			IUserDistributorMappingRepository iUserDistributorMappingRepository,
			ISODailyActivityMstRepository isoDailyActivityMstRepository, IOutletRepository iOutletRepository,
			IBeatNameRepository iBeatNameRepository, IFormMediaMappingRepository iFormMediaMappingRepository,
			ISOActivitySummaryDtlRepo iSOActivitySummaryDtlRepo, ICodeValueRepo iCodeValueRepo,
			TimeLineOutputVoRepo timeLineOutputVoRepo,ISOActivityChangeRequestRepo iSOActivityChangeRequestRepo,
							 ITourPlanDtlRepository iTourPlanDtlRepository) {

		this.userCredRepository = userCredRepository;
		this.iSOActivityRegisterRepo = iSOActivityRegisterRepo;
		this.iOrderRepository = iOrderRepository;
		this.iUserDistributorMappingRepository = iUserDistributorMappingRepository;
		this.isoDailyActivityMstRepository = isoDailyActivityMstRepository;
		this.iOutletRepository = iOutletRepository;
		this.iBeatNameRepository = iBeatNameRepository;
		this.iFormMediaMappingRepository = iFormMediaMappingRepository;
		this.iCodeValueRepo = iCodeValueRepo;
		this.timeLineOutputVoRepo = timeLineOutputVoRepo;
		this.iSOActivitySummaryDtlRepo = iSOActivitySummaryDtlRepo;
		this.iSOActivityChangeRequestRepo = iSOActivityChangeRequestRepo;
		this.iTourPlanDtlRepository = iTourPlanDtlRepository;
	}

	public List<SoActivityRegisterVo> fetchAllSOActivity() {
		LOGGER.info("SoActivityService :: fetchAllSOActivity() called...");
		List<SoActivityRegisterVo> dataList = (List<SoActivityRegisterVo>) iSOActivityRegisterRepo.findAll();
		if (dataList != null) {
			return dataList;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "SoActivity" });
		}
	}

	public ResponseEntity<ApiResponse> fetchAllSOActivityRequest(ChangeRequestInputVo inputVo){
		LOGGER.info("SoActivityService :: fetchAllSOActivityRequest() called...");
		ApiResponse apiResponse = null;
		List<RequestManagementVo> getDataList = null;
		String whereClause = " ";
		String limitStr = "";

		if (null == inputVo.getPage() && null == inputVo.getSize()) {
			// do nothing
		} else {
			Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
			Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
			limitStr = " limit " + size + " offset " + page;
			LOGGER.info("limitStr..." + limitStr);
		}

		if (null != inputVo.getSoId()) {
			LOGGER.info("vo.getSoId() ------------ " + inputVo.getSoId());
			whereClause += " and so_id = "+inputVo.getSoId();
		}

		if (null != inputVo.getFromDate() && !inputVo.getFromDate().trim().isEmpty()
				&& null != inputVo.getToDate() && !inputVo.getToDate().trim().isEmpty()) {
			whereClause += " and DATE_FORMAT(created_on,'%Y-%m-%d') BETWEEN DATE_FORMAT('"+inputVo.getFromDate().trim()+"','%Y-%m-%d')"
					+ " AND DATE_FORMAT('"+inputVo.getToDate().trim()+"','%Y-%m-%d') ";
		}

		LOGGER.info("Whereclause..." + whereClause);
		getDataList = iSOActivityChangeRequestRepo.searchByInput(whereClause,"so_request_management",limitStr);


		// To fetch total no of data which satisfy this where clause
		int totalNo = iSOActivityChangeRequestRepo.getTotalCountByInput(whereClause, "so_request_management").intValue();
		LOGGER.info("SoActivityService::searchByInput::Exiting...");
		if (getDataList != null) {
			apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Order" });
		}

	}

	@Transactional
	public ResponseEntity<ApiResponse> saveSOActivity(SoActivityRegisterVo input) {
		LOGGER.info("SoActivityService :: saveSOActivity() called...");
		RequestManagementVo requestManagementVo = new RequestManagementVo();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
		if (null == loginUserId || loginUserId == 0L) {
			loginUserId = input.getSoId().getId();
			LOGGER.info(" loginUserId from input =========== > " + loginUserId);
		}

		// Calculate the hour difference for working hour
		if(input.getEndTimeDate() == null){
			input.setEndTimeDate(new Date());
		}
		if (null != input.getStartTimeDate() && null != input.getEndTimeDate()) {
			LocalDateTime dateBefore = input.getStartTimeDate().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			LocalDateTime dateAfter = input.getEndTimeDate().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();

//			long hourBetween = Duration.between(dateBefore, dateAfter).toMinutes();
//
//			input.setWorkingHour(hourBetween + "");

			// Working hr diff show in hh:mm;ss format
			long totalSeconds = Duration.between(dateBefore, dateAfter).toSeconds();

			long hours = totalSeconds / 3600;
			long minutes = (totalSeconds % 3600) / 60;
			long seconds = totalSeconds % 60;
			String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
			// Set formatted string into workingHour
			input.setWorkingHour(formattedTime);

		}

//		LOGGER.info("beatId--->"+input.getBeatId());
//		LOGGER.info("tourPlan beatId--->"+input.getTourPlanDtlId().getBeatId().getId());
//		LOGGER.info("dailyActivityId--->"+input.getDailyActivityId().getId());
//		LOGGER.info("tour plan dailyActivityId--->"+input.getTourPlanDtlId().getTourPlanActivityMstId().getId());


		TourPlanDtlVo tourPlanDtlRes = new  TourPlanDtlVo();
		if(null != input.getTourPlanDtlId()){
			tourPlanDtlRes = iTourPlanDtlRepository.getById(input.getTourPlanDtlId().getId());
		}

		if(input.getBeatId() != null &&
				(null == input.getTourPlanDtlId() || tourPlanDtlRes.getSoBeatTypeMapping() == null ||
						!input.getBeatId().equals(
						tourPlanDtlRes.getSoBeatTypeMapping().getId())))

			//  ||  !input.getDailyActivityId().getId().equals(input.getTourPlanDtlId().getTourPlanActivityMstId().getId()
		{

			requestManagementVo.setApprovalStatus("Pending");
			if("Pending".equals(requestManagementVo.getApprovalStatus())){ //requestManagementVo.getApprovalStatus() == "Pending"
				requestManagementVo.setActualActivityId(input.getDailyActivityId());
				requestManagementVo.setActualBeatId(input.getBeatId());
				requestManagementVo.setOriginalTourPlanId(input.getTourPlanDtlId());
				requestManagementVo.setPlanActivityId(input.getTourPlanDtlId() != null ?
						input.getTourPlanDtlId().getTourPlanActivityMstId() : null);
				if (tourPlanDtlRes.getSoBeatTypeMapping() != null) {
					requestManagementVo.setPlanBeatId(tourPlanDtlRes.getSoBeatTypeMapping().getBeatId());
				} else {
					requestManagementVo.setPlanBeatId(null);
				}
//				requestManagementVo.setPlanBeatId(tourPlanDtlRes.getSoBeatTypeMapping().getBeatId());
				requestManagementVo.setSystemRemarks(input.getSystemRemarks());
				requestManagementVo.setCreatedOn(new Date());
				requestManagementVo.setUpdatedOn(new Date());
				requestManagementVo.setCreatedBy(input.getSoId().getId());
				requestManagementVo.setSoId(input.getSoId());
				iSOActivityChangeRequestRepo.save(requestManagementVo);
			}

		}

		SoActivityRegisterVo savedData = iSOActivityRegisterRepo.save(input);
		if (savedData != null) {

			LOGGER.info("Request Management Vo Data---->"+requestManagementVo);

			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
					HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "SO Activity" });
		}
	}

	@Transactional
	public ResponseEntity<ApiResponse> saveOrder(OrderVo input) {
		LOGGER.info("SoActivityService :: saveOrder() called...");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
		if (null == loginUserId || loginUserId == 0L) {
			loginUserId = input.getSoId().getId();
			LOGGER.info(" loginUserId from input =========== > " + loginUserId);
		}

		input.setCreatedOn(new Date());
		input.setUpdatedOn(new Date());
		OrderVo savedData = iOrderRepository.save(input);
		if (savedData != null) {
			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
					HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "SO Order" });
		}
	}

	public List<DailyActivityMstVo> fetchOfficialSubActivity() {
		LOGGER.info("SoActivityService :: fetchOfficialSubActivity() called...");
		List<DailyActivityMstVo> dataList = isoDailyActivityMstRepository.fetchSubActivityOfOfficialWork();
		if (dataList != null) {
			return dataList;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "SoActivity" });
		}
	}

	public List<UserDistributorMappingVo> fetchDistributorOfUser(Long soId) {
		LOGGER.info("SoActivityService :: fetchDistributorOfUser() called...");
		List<UserDistributorMappingVo> dataList = iUserDistributorMappingRepository.fetchDistributorOfSO(soId);
		if (dataList != null) {
			return dataList;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "SoActivity" });
		}
	}

	public ResponseEntity<ApiResponse> getOutletDetails(Long soId, Long outletId, Long beatId, String monYr) {

		LOGGER.info("SoActivityService :: getOutletDetails() called...");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
		if (null == loginUserId || loginUserId == 0L) {
			loginUserId = soId;
			LOGGER.info(" loginUserId from input =========== > " + loginUserId);
		}

		OutletDetailsVo resp = new OutletDetailsVo();
		List<OrderVo> orders = iOrderRepository.findLastOrdersOfUser(loginUserId, outletId);

		OutletVo outlet = iOutletRepository.getById(outletId);
		String outletImg = iFormMediaMappingRepository.fetchOutletImage("outlet", "outlet", outletId);

		// for MTD Calculation
//		LocalDate today = LocalDate.now();
//		LocalDate now = LocalDate.now();
//		LocalDate firstDayOfMonth = now.withDayOfMonth(1);

		CodeValueVo codeValue = iCodeValueRepo.fetchMtdValue(monYr, outletId);

		resp.setId(1L);
		resp.setOutlet_id(outlet.getId());
		resp.setOutlet_name(outlet.getOutletName());
		resp.setOutlet_code(outlet.getOutletCode());
		resp.setOutlet_address(outlet.getAddress());
		BeatName beat = iBeatNameRepository.getById(beatId);
		resp.setBeat_id(beat.getId());
		resp.setBeat_name(beat.getBeatName());
		if (orders.size() > 0) {
			resp.setLast_visited_date(orders.get(0).getCreatedOn().toString());
			resp.setLast_order_value(orders.get(0).getTotalAmountOfItem());
			resp.setLast_5_orders(orders);
		}
		resp.setOutlet_image(outletImg);
		resp.setOrder_qty_till_date(codeValue.getCode());
		resp.setOrder_value_till_date(codeValue.getValue());

		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", resp), HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse> fetchTimeLine(TimeLineInputVo input) {

		LOGGER.info("Base service :: fetchTimeLine()");
		LOGGER.info("InputValue :" + input.getActivityDate(), input.getSoId());

		List<TimeLineOutputVO> fetchTimeLine = timeLineOutputVoRepo.fetchTimeLine(input.getSoId(),
				input.getActivityDate());
		if (fetchTimeLine.size() > 0) {
			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", fetchTimeLine),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null),
					HttpStatus.NOT_FOUND);
		}
	}

//	public ResponseEntity<ApiResponse> fetchSoAttendanceByDateAndId(Long soId,String Date){
//		LOGGER.info("Call SOActivityService :: fetchSoActivityRegisterByDateAndId()");
//
//		try{
//			SoActivityRegisterVo data = iSOActivityRegisterRepo.fetchBysoIdAndDate(soId,Date);
//			if(data != null ){
//				return new ResponseEntity<>(new ApiResponse(200,"Success","Fetch Successfully",data),
//						HttpStatus.OK);
//			}else {
//				return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null),
//						HttpStatus.NOT_FOUND);
//			}
//
//		}catch (Exception e){
//			e.printStackTrace();
//			return new ResponseEntity<>(new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//
//		}
//
//	}

	public ResponseEntity<ApiResponse> updateEndDateTime(SoActivityRegisterVo input) {
		LOGGER.info("Call SOActivityService :: fetchSoActivityRegisterByDateAndId()");

		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDateOutput = date.format(input.getStartTimeDate());
		LOGGER.info(strDateOutput);

		try {
			List<SoActivityRegisterVo> data = iSOActivityRegisterRepo.fetchBysoIdAndDate(input.getSoId().getId(),
					strDateOutput);
			LOGGER.info("Print  present data-->" + data);
			if (data != null) {
				if (data.get(0).getEndTimeDate() != null) {
					LOGGER.info("End Date time present ");
					ApiResponse apiResponse = new ApiResponse(302, "Found", "End Date and Time allready present", null,
							0);
					return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.FOUND);
				} else {
					int value = iSOActivityRegisterRepo.updateEndTimeDate(input.getSoId().getId(),
							input.getStartTimeDate(), new Date());
					LOGGER.info("Print after update  end time date -->" + value);
					ApiResponse apiResponse = new ApiResponse(200, "success", "Update Successfully");
					return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
				}
			} else {
				LOGGER.info("Not found data");
				return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null),
						HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ApiResponse(500, "SERVER ERROR", "Something Went Wrong. Please try again!!!"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	public ResponseEntity<ApiResponse> getSOActivitySummary(Long soId) {

		LOGGER.info("SoActivityService :: getSOActivitySummary() called...");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
		if (null == loginUserId || loginUserId == 0L) {
			loginUserId = soId;
			LOGGER.info(" loginUserId from input =========== > " + loginUserId);
		}

		SOActivitySummaryDto resp = new SOActivitySummaryDto();

		String currentDateStr = LocalDate.now().toString(); // "2024-05-20";

		List<SoActivityRegisterVo> todayActivity = iSOActivityRegisterRepo.fetchActivitiesBySOAndDate(loginUserId,
				currentDateStr);
		List<SoActivityRegisterVo> phoneCall = todayActivity.stream()
				.filter(e -> e.getDailyActivityId().getDailyActivitySub().equals("Phone Call"))
				.collect(Collectors.toList());
		List<SoActivityRegisterVo> ovc = todayActivity.stream()
				.filter(e -> e.getDailyActivityId().getDailyActivitySub().equals("OVC")).collect(Collectors.toList());
		List<SoActivityRegisterVo> orders = todayActivity.stream().filter(e -> null != e.getOrderId())
				.collect(Collectors.toList());

		if (null != orders && orders.size() > 0) {
			List<Long> orderNo = orders.stream().map(SoActivityRegisterVo::getOrderId).collect(Collectors.toList());
			List<OrderVo> orderData = iOrderRepository.findAllById(orderNo);

			List<String> orderQty = orderData.stream().map(OrderVo::getTotalNoOfItem).collect(Collectors.toList());
			Double orderQtyTotal = orderQty.stream().mapToDouble(a -> Double.parseDouble(a)).sum();

			List<String> orderVal = orderData.stream().map(OrderVo::getTotalAmountOfItem).collect(Collectors.toList());
			Double orderValTotal = orderVal.stream().mapToDouble(a -> Double.parseDouble(a)).sum();

			resp.setOrderNetQty(orderQtyTotal);
			resp.setOrderNetValue(orderValTotal);

//			List<List<OrderDtlVo>> orderDtlArr = orderData.stream().map(OrderVo::getOrderDtlVo).collect(Collectors.toList());
//			List<OrderDtlVo> orderDtl = orderDtlArr.stream().flatMap(List::stream).collect(Collectors.toList());
			List<SOActivitySummaryDtlDto> primaryCat = iSOActivitySummaryDtlRepo.findCatDtl(orderNo);
			resp.setPrimaryCat(primaryCat);

			List<SOActivitySummaryDtlDto> secCat = iSOActivitySummaryDtlRepo.findSubCatDtl(orderNo);
			resp.setSecondaryCat(secCat);

			List<SOActivitySummaryDtlDto> sku = iSOActivitySummaryDtlRepo.findSKUDtl(orderNo);
			resp.setSku(sku);
		}

		resp.setSC(iSOActivityRegisterRepo.getSCOfSO(loginUserId));
		resp.setPC(phoneCall.size());
		resp.setOVC(ovc.size());
		resp.setTC(phoneCall.size() + ovc.size());

		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully", resp), HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse> searchOrderByInput(OrderInputVo inputVo) {
		LOGGER.info("SoActivityService::searchOrderByInput::Entering...");
		ApiResponse apiResponse = null;
		List<OrderVo> getDataList = null;
		String whereClause = " ";
		String limitStr = "";
		if (null == inputVo.getPage() && null == inputVo.getSize()) {
			// do nothing
		} else {
			Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
			Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
			limitStr = " limit " + size + " offset " + page;
			LOGGER.info("limitStr..." + limitStr);
		}

		if (null != inputVo.getOutletId()) {
			whereClause += " and outlet_id = "+inputVo.getOutletId();
		}
		if (null != inputVo.getSoId()) {
			LOGGER.info("vo.getSoId() ------------ " + inputVo.getSoId());
			whereClause += " and so_id = "+inputVo.getSoId();
		}
		if (null != inputVo.getBeatId()) {
			LOGGER.info("vo.getBeatId() ------------ " + inputVo.getBeatId());
			whereClause += " and beat_id = "+inputVo.getBeatId();
		}
		if (null != inputVo.getFromDate() && !inputVo.getFromDate().trim().isEmpty()
				&& null != inputVo.getToDate() && !inputVo.getToDate().trim().isEmpty()) {
			whereClause += " and DATE_FORMAT(created_on,'%Y-%m-%d') BETWEEN DATE_FORMAT('"+inputVo.getFromDate().trim()+"','%Y-%m-%d')"
					+ " AND DATE_FORMAT('"+inputVo.getToDate().trim()+"','%Y-%m-%d') ";
		}
		LOGGER.info("Whereclause..." + whereClause);
		getDataList = iOrderRepository.searchByInput(whereClause, "so_order", limitStr);

		// To fetch total no of data which satisfy this where clause
		int totalNo = iOrderRepository.getTotalCountByInput(whereClause, "so_order").intValue();
		LOGGER.info("SoActivityService::searchByInput::Exiting...");
		if (getDataList != null) {
			apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Order" });
		}
	}

	public ResponseEntity<ApiResponse> fetchEachOrder(Long orderId) {
		LOGGER.info("SoActivityService" + orderId);

		OrderVo order = iOrderRepository.findById(orderId).orElse(null);
		if (null != order) {
			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Fetched Successfully!", order),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ApiResponse(404, "NOT_FOUND", "Data Not Found!!!", null),
					HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<ApiResponse> updateStatus(RequestManagementVo input){
		LOGGER.info("SoActivityService :: updateStatus() called......");
		int saveData = iSOActivityChangeRequestRepo.updateStatus(input.getId(),input.getApprovalStatus());
		if(saveData != 0) {
			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Status Updated", null), HttpStatus.OK);
		} else {

			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "RequestStatus" });
		}
	}

}