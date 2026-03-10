package com.dcc.osheaapp.report.baPerformance.controller;

import static com.dcc.osheaapp.common.service.Util.LOGGER;
import static com.dcc.osheaapp.repository.BaPerformanceViewSpecifications.*;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.leaderboard.domain.repository.LeaderBoardRepository;
import com.dcc.osheaapp.report.baPerformance.service.BaPerformanceDtoMapper;
import com.dcc.osheaapp.report.baPerformance.service.PerformanceDto;
import com.dcc.osheaapp.report.baPerformance.service.PerformanceExportService;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.repository.BaPerformanceViewRepo;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.ITopPerformingBARepo;
import com.dcc.osheaapp.repository.IUserCredRepository;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.views.BaPerformanceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class PerformanceReqHandler {
	private final BaPerformanceViewRepo performanceViewRepo;

	private final LeaderBoardRepository leaderBoardRepository;
	private final PerformanceExportService performanceExportService;
	private final IDropdownMastereRepository dropdownMastereRepository;
	private final BaPerformanceDtoMapper mapper;
	private final ReportWSHandler wsHandler;
	private final ITopPerformingBARepo iTopPerformingBARepo;

	public PerformanceReqHandler(BaPerformanceViewRepo performanceViewRepo,
                                 PerformanceExportService performanceExportService, LeaderBoardRepository leaderBoardRepository, IDropdownMastereRepository dropdownMastereRepository,
                                 BaPerformanceDtoMapper mapper, ReportWSHandler wsHandler, ITopPerformingBARepo iTopPerformingBARepo) {
		this.performanceViewRepo = performanceViewRepo;
		this.performanceExportService = performanceExportService;
        this.dropdownMastereRepository = dropdownMastereRepository;
        this.mapper = mapper;
		this.wsHandler = wsHandler;
		this.leaderBoardRepository = leaderBoardRepository;
		this.iTopPerformingBARepo = iTopPerformingBARepo;
	}

	public void exportReportThroughWs(BaPerformanceExcelReportInputDto dto) {
		CompletableFuture.supplyAsync(() -> {
			try {
				return new ReportWSPayload("ba_performance_" + dto.getMonthYr(),
						((ByteArrayOutputStream) export(dto, new ByteArrayOutputStream())).toByteArray());
			} catch (IOException e) {

				wsHandler.sendReportErrNotification(
						new ApiResponse(500, "INTERNAL_SERVER_ERROR", "Error while generating report", null, 0));
				throw new RuntimeException(e);
			}
		}).thenAccept(wsHandler::sendReportReadyNotification).exceptionally((e) -> {
			e.printStackTrace();
			wsHandler.sendReportErrNotification(
					new ApiResponse(500, "INTERNAL_SERVER_ERROR", "Error while generating report", null, 0));
			return null;
		});
	}
	public OutputStream export(BaPerformanceExcelReportInputDto dto, OutputStream os) throws IOException {
		List<BaPerformanceView> data = performanceViewRepo.findAll(ofUserType("BA").and(fromMonth(dto.getMonth()))
				.and(fromYear(dto.getYear()).and(fromZone(dto.getZone()))));
		for (BaPerformanceView performanceView : data) {
			Long userId = performanceView.getUserId();
			String monthString = String.format("%02d", dto.getMonth().getValue());
			String yearMonth = dto.getYear() + "-" + monthString;
			// LOGGER.info("YearMonth ========== >" +yearMonth);
			// LOGGER.info("User ID ========== >" +userId);
			Leaderboard rank = leaderBoardRepository.findRank(yearMonth, userId);
			LOGGER.info("Rank=======>>>>" + rank);
			performanceView.setRank(rank == null ? "N/A" : rank.getRank().toString());

		}
		List<PerformanceDto> records = data.stream().map(mapper::toDto).collect(Collectors.toList());
		return performanceExportService.export(records, os);
	}
	public OutputStream export(ReportInputDto dto, OutputStream os) throws IOException {

		YearMonth yearMonth = YearMonth.parse(dto.getYearMonth());
		List<BaPerformanceView> data = performanceViewRepo.findAll(ofUserType("BA").and(fromMonth(yearMonth.getMonth()))
				.and(fromYear(yearMonth.getYear()).and(fromZone(dto.getZone()))));
		for (BaPerformanceView performanceView : data) {
			Long userId = performanceView.getUserId();
			Leaderboard rank = leaderBoardRepository.findRank(dto.getYearMonth(), userId);
			performanceView.setRank(rank == null ? "N/A" : rank.getRank().toString());

		}
		List<PerformanceDto> records = data.stream().map(mapper::toDto).collect(Collectors.toList());
		return performanceExportService.export(records, os);
	}


	public String getBaPerformanceReportFilename(ReportInputDto dto) {
		DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(8L, "zone").orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
		return "ba_performance_" + dto.getYearMonth() + "_" + dropdownMaster.getFieldName() + ".xlsx";
	}
	public ResponseEntity<ApiResponse> topPerformingBA(String monthYr) throws IOException {
		ApiResponse apiResponse = null;
		List<TopBAPerformanceDto> getDataList = null;
		LOGGER.info("monthYr..." + monthYr);

		getDataList = iTopPerformingBARepo.fetchTopPerformingBA(monthYr);
		apiResponse = new ApiResponse(200, "success", "success", getDataList);
		if (getDataList != null) {
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Users"});
		}
	}
}
