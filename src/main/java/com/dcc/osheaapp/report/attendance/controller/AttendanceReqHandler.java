package com.dcc.osheaapp.report.attendance.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.common.model.LogExecutionTime;
import com.dcc.osheaapp.common.service.storage.S3OutputStream;
import com.dcc.osheaapp.report.attendance.model.ActivityModel;
import com.dcc.osheaapp.report.attendance.model.OutletMappingView;
import com.dcc.osheaapp.report.attendance.model.UserActivityModel;
import com.dcc.osheaapp.report.attendance.service.AttendanceDetailsDto;
import com.dcc.osheaapp.report.attendance.service.AttendanceDetailsDtoMapper;
import com.dcc.osheaapp.report.attendance.service.AttendanceExportService;
import com.dcc.osheaapp.report.attendance.service.AttendanceSummaryExportService;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.report.common.model.ReportGeneratedEvent;
import com.dcc.osheaapp.report.common.model.ReportType;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.report.common.service.UploadReportService;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.IOutletUserMappingRepository;
import com.dcc.osheaapp.repository.IUserActivityRepository;
import com.dcc.osheaapp.repository.UserViewRepository;
import com.dcc.osheaapp.service.UserChainService;
import com.dcc.osheaapp.service.UserService;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.UserActivityRegisterVo;
import com.dcc.osheaapp.vo.views.UserView;

@Service
public class AttendanceReqHandler {

	private static final Logger LOGGER = LogManager.getLogger(AttendanceReqHandler.class);
	private final IUserActivityRepository userActivityRepository;
	private final UserService userService;
	private final AttendanceExportService exportService;
	private final AttendanceSummaryExportService attendanceSummaryExportService;
	private final AttendanceDetailsDtoMapper mapper;
	private final ReportWSHandler wsHandler;
	private final UploadReportService uploadReportService;
	private final IDropdownMastereRepository dropdownMastereRepository;
	private final UserViewRepository userViewRepository;
	private final IOutletUserMappingRepository outletUserMappingRepository;
	private final UserChainService userChainService;
	@Autowired
	private ApplicationEventPublisher publisher;

	public AttendanceReqHandler(IUserActivityRepository userActivityRepository, UserService userService,
			AttendanceExportService exportService, AttendanceSummaryExportService attendanceSummaryExportService,
			AttendanceDetailsDtoMapper mapper, ReportWSHandler wsHandler, UploadReportService uploadReportService,
			IDropdownMastereRepository dropdownMastereRepository, UserViewRepository userViewRepository,
			IOutletUserMappingRepository outletUserMappingRepository, UserChainService userChainService) {
		this.userActivityRepository = userActivityRepository;
		this.userService = userService;
		this.exportService = exportService;
		this.attendanceSummaryExportService = attendanceSummaryExportService;
		this.mapper = mapper;
		this.wsHandler = wsHandler;
		this.uploadReportService = uploadReportService;
		this.dropdownMastereRepository = dropdownMastereRepository;
		this.userViewRepository = userViewRepository;
		this.outletUserMappingRepository = outletUserMappingRepository;
		this.userChainService = userChainService;
	}

	public void exportDetailsWS(AttendanceSummaryExportInputDto dto) {
		CompletableFuture.supplyAsync(() -> {
			try {

				String fileName = "ba_attendance_details_" + dto.getMonth() + "-" + dto.getYear();
				return new ReportWSPayload(fileName,
						((ByteArrayOutputStream) exportDetails(dto, new ByteArrayOutputStream())).toByteArray());
			} catch (IOException e) {
				wsHandler.sendReportErrNotification(
						new ApiResponse(500, "INTERNAL_SERVER_ERROR", "Error while generating report", null, 0));
				throw new RuntimeException(e);
			}
		}).thenAccept(wsHandler::sendReportReadyNotification).exceptionally(e -> {
			e.printStackTrace();
			wsHandler.sendReportErrNotification(
					new ApiResponse(500, "INTERNAL_SERVER_ERROR", "Error while generating report", null, 0));
			return null;
		});
	}

	public Object exportDetails(AttendanceSummaryExportInputDto dto, OutputStream os) throws IOException {
		long start = System.currentTimeMillis();
		List<UserActivityRegisterVo> activities = getAttendanceActivities(dto);
		LOGGER.info("[Attendance Details] Data ingestion:: Elapsed:: " + (System.currentTimeMillis() - start));

		long start1 = System.currentTimeMillis();
		List<AttendanceDetailsDto> attendanceDetailsDtos = mapper.toDtos(activities);
		LOGGER.info("[Attendance Details] Data Transformation:: Elapsed:: " + (System.currentTimeMillis() - start1));

		OutputStream result = (ByteArrayOutputStream) exportService.export(attendanceDetailsDtos, new S3OutputStream());

		publisher.publishEvent(
				new ReportGeneratedEvent(this, result, "Attendance_details.xlsx", ReportType.COUNTER_STOCK));
		return result;
	}

	public OutputStream exportDetails(ReportInputDto dto, OutputStream os) throws IOException {
		long start = System.currentTimeMillis();
		List<UserActivityRegisterVo> activities = getAttendanceActivities(dto);
		LOGGER.info("[Attendance Details] Data ingestion:: Elapsed:: " + (System.currentTimeMillis() - start));
		long start1 = System.currentTimeMillis();
		List<AttendanceDetailsDto> attendanceDetailsDtos = mapper.toDtos(activities);
		attendanceDetailsDtos.sort(Comparator.comparing(AttendanceDetailsDto::getDate));
		LOGGER.info("[Attendance Details] Data Transformation:: Elapsed:: " + (System.currentTimeMillis() - start1));
		return exportService.export(attendanceDetailsDtos, new S3OutputStream());
	}

	// public OutputStream exportSummary(AttendanceSummaryExportInputDto dto,
	// OutputStream os) throws IOException {
	// YearMonth yearMonth = YearMonth.of(dto.getYear(), dto.getMonth());
	// long start = System.currentTimeMillis();
	//
	// long start1 = System.currentTimeMillis();
	// List<UserActivityRegisterVo> attendanceActivities =
	// getAttendanceActivities(dto);
	// LOGGER.info("[Attendance Report] Data ingestion:: " +
	// (System.currentTimeMillis() - start1));
	// Map<String, List<UserActivityRegisterVo>> userActivityMap =
	// mapper.getUserActivityMap(attendanceActivities);
	// long elapsed = System.currentTimeMillis() - start;
	// LOGGER.info("[Attendance Report] Data formation:: elapsed:: " + elapsed);
	// return attendanceSummaryExportService.export(userActivityMap, yearMonth, os,
	// dto.getZone());
	// }

	@LogExecutionTime()
	public OutputStream exportSummary(ReportInputDto dto, OutputStream os) throws IOException {
		YearMonth yearMonth = YearMonth.parse(dto.getYearMonth());
		long start = System.currentTimeMillis();
		var zone = dropdownMastereRepository.findByIdAndFieldType(dto.getZone(), "zone")
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
		LocalDate lastDateOfMonth = yearMonth.atEndOfMonth();
		List<UserView> workingUsers = userViewRepository.findAllWorkingBAWithinAZone(lastDateOfMonth.toString(),
				dto.getZone(), 4L);
		List<Long> userIds = workingUsers.stream().map(UserView::getUserId).collect(Collectors.toList());
		Map<Long, UserChainFlat> chainMap = userChainService.retrieveChains(userIds).stream()
				.collect(Collectors.toMap(UserChainFlat::getBaId, e -> e));
		List<UserActivityRegisterVo> attendanceActivities = getAttendanceActivitiesOfWorkingUsers(dto, userIds);
		Map<Long, List<OutletMappingView>> outletMap = new HashMap<>();
		outletUserMappingRepository.findAssociatedOutletsIdByUserIdNew(userIds).stream().forEach(e -> {
			outletMap.putIfAbsent(e.getUserId(), new ArrayList<>());
			outletMap.get(e.getUserId()).add(e);
		});
		var activityMap = mapper.getUserActivityModelMap(attendanceActivities);
		var userActivityModels = workingUsers.stream().map(user -> {
			Map<String, List<ActivityModel>> userActivityModelMap = new HashMap<>();
			activityMap.forEach((key, value) -> {
				var keyPattern = user.getUserId() + "-";
				if (key.startsWith(keyPattern)) {
					var newKey = key.split("-")[1];
					userActivityModelMap.put(newKey, value);
				}
			});

			var outlets = outletMap.get(user.getUserId());
			outlets.sort(Comparator.comparingLong(OutletMappingView::getAssociationId).reversed());
			var outlet = (outlets.isEmpty()) ? null : outlets.get(0);

			return new UserActivityModel().setUserId(user.getUserId()).setFullName(user.getFullName())
					.setContactNo(user.getContactNo()).setCreatedOn(user.getCreatedOn())
					.setUserTypeId(user.getUserTypeId()).setUserType(user.getUserType()).setUserCode(user.getUsername())
					.setJoiningDate(user.getDateOfJoining()).setReleaseDate(user.getReleaseDate())
					.setStatus(user.isActive()).setOutlet(outlet).setZoneId(zone.getId()).setZone(zone.getFieldName())
					.setUserChain(chainMap.get(user.getUserId()) == null
							? new UserChainFlat()
							: chainMap.get(user.getUserId()))
					.setActivityMap(userActivityModelMap);

		}).collect(Collectors.toList());
		String fileName = getAttendanceSummaryFilename(dto);
		return attendanceSummaryExportService.export(userActivityModels, yearMonth, os);
	}

	public String getAttendanceSummaryFilename(ReportInputDto dto) {
		DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(dto.getZone(), "zone")
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
		return "attendance_summary_" + dto.getYearMonth() + "_" + dropdownMaster.getFieldName() + ".xlsx";
	}

	public String getAttendanceDetailsFilename(ReportInputDto dto) {
		DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(dto.getZone(), "zone")
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
		return "attendance_details_" + dto.getYearMonth() + "_" + dropdownMaster.getFieldName() + ".xlsx";
	}

	private List<UserActivityRegisterVo> getAttendanceActivities(ReportInputDto dto) {
		YearMonth yearMonth = YearMonth.parse(dto.getYearMonth());
		return userActivityRepository.searchUserActivityByActivityTypeAndZoneAndMonthYr(
				List.of(Constants.BA_Activity_Enum.attendance.name(), Constants.BA_Activity_Enum.store_login.name(),
						Constants.BA_Activity_Enum.leave.name(), Constants.BA_Activity_Enum.store_logout.name(),
						Constants.BA_Activity_Enum.office_work.name(), Constants.BA_Activity_Enum.comp_off.name(),
						Constants.BA_Activity_Enum.day_in.name(), Constants.BA_Activity_Enum.day_out.name(),
						Constants.BA_Activity_Enum.holiday.name(), Constants.BA_Activity_Enum.week_off.name()),
				yearMonth.toString(), dto.getZone());
	}
	private List<UserActivityRegisterVo> getAttendanceActivitiesOfWorkingUsers(ReportInputDto dto, List<Long> users) {
		YearMonth yearMonth = YearMonth.parse(dto.getYearMonth());
		return userActivityRepository.searchActivityOfWorkingUsers(
				List.of(Constants.BA_Activity_Enum.attendance.name(), Constants.BA_Activity_Enum.store_login.name(),
						Constants.BA_Activity_Enum.leave.name(), Constants.BA_Activity_Enum.store_logout.name(),
						Constants.BA_Activity_Enum.office_work.name(), Constants.BA_Activity_Enum.comp_off.name(),
						Constants.BA_Activity_Enum.day_in.name(), Constants.BA_Activity_Enum.day_out.name(),
						Constants.BA_Activity_Enum.holiday.name(), Constants.BA_Activity_Enum.week_off.name()),
				yearMonth.toString(), users);
	}

	private List<UserActivityRegisterVo> getAttendanceActivities(AttendanceSummaryExportInputDto dto) {
		YearMonth yearMonth = YearMonth.of(dto.getYear(), dto.getMonth());
		return userActivityRepository.searchUserActivityByActivityTypeAndZoneAndMonthYr(
				List.of(Constants.BA_Activity_Enum.attendance.name(), Constants.BA_Activity_Enum.store_login.name(),
						Constants.BA_Activity_Enum.leave.name(), Constants.BA_Activity_Enum.store_logout.name(),
						Constants.BA_Activity_Enum.office_work.name(), Constants.BA_Activity_Enum.comp_off.name(),
						Constants.BA_Activity_Enum.day_in.name(), Constants.BA_Activity_Enum.day_out.name(),
						Constants.BA_Activity_Enum.holiday.name(), Constants.BA_Activity_Enum.week_off.name()),
				yearMonth.toString(), dto.getZone());
	}
}
