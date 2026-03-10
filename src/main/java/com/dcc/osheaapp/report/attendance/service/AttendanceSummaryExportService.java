package com.dcc.osheaapp.report.attendance.service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.service.Util;
import com.dcc.osheaapp.common.service.exporter.excel.ExcelColumn;
import com.dcc.osheaapp.common.service.exporter.excel.ExcelExporterAlt;
import com.dcc.osheaapp.report.attendance.model.UserActivityModel;
import com.dcc.osheaapp.repository.IOutletUserMappingRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.repository.UserViewRepository;
import com.dcc.osheaapp.service.UserChainService;

@Service
public class AttendanceSummaryExportService {
	private static final Logger LOGGER = LogManager.getLogger(AttendanceSummaryExportService.class);
	@Autowired
	UserViewRepository userViewRepository;

	@Autowired
	UserChainService userChainService;
	Function<SXSSFWorkbook, CellStyle> baseStyle = ExcelStyles::getBaseStyle;
	Function<SXSSFWorkbook, CellStyle> greenHeader = baseStyle.andThen(ExcelStyles::getGreenBackgroundCell);
	Function<SXSSFWorkbook, CellStyle> yellowHeader = baseStyle.andThen(ExcelStyles::getYellowBackgroundCell);
	Function<SXSSFWorkbook, CellStyle> redHeader = baseStyle.andThen(ExcelStyles::getRedBackgroundCell);
	Function<SXSSFWorkbook, CellStyle> darkGreenHeader = baseStyle.andThen(ExcelStyles::getDarkGreenBackgroundCell);
	Function<SXSSFWorkbook, CellStyle> pinkHeader = baseStyle.andThen(ExcelStyles::getPinkBackgroundCell);
	Function<SXSSFWorkbook, CellStyle> orangeHeader = baseStyle.andThen(ExcelStyles::getOrangeBackgroundCell);
	Function<SXSSFWorkbook, CellStyle> lightBlueHeader = baseStyle.andThen(ExcelStyles::getLightBlueBackgroundCell);
	Function<SXSSFWorkbook, CellStyle> whiteHeader = baseStyle.andThen(ExcelStyles::getWhiteBackgroundCell);
	@Autowired
	private IUserDetailsRepository userDetailsRepository;
	@Autowired
	private IOutletUserMappingRepository outletUserMappingRepository;

	private static List<String> getDatesWithDays(YearMonth monthYear) {
		List<String> result = new ArrayList<>();
		String[] parts = monthYear.toString().split("-");
		int month = Integer.parseInt(parts[1]);
		int year = Integer.parseInt(parts[0]);
		LocalDate firstDay = LocalDate.of(year, month, 1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd(EEE)");
		int tillDays = Util.daysUntilToday(monthYear);
		for (int i = 0; i < tillDays; i++) {
			result.add(firstDay.plusDays(i).format(formatter));
		}
		return result;
	}

	public OutputStream export(List<UserActivityModel> userActivities, YearMonth yearMonth, OutputStream outputStream) {
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
		ExcelExporterAlt<Void> exporter = new ExcelExporterAlt<>(getHeaders(yearMonth, workbook),
				getData(userActivities, yearMonth, workbook), workbook, "Summary");
		LOGGER.info("[Attendance Report] Starting Export:: ");
		long start = System.currentTimeMillis();
		exporter.export(new ArrayList<>(), null, outputStream);
		LOGGER.info("[Attendance Report] Export:: elapsed:: " + (System.currentTimeMillis() - start));
		return outputStream;

	}

	private List<ExcelColumn> getHeaders(YearMonth yearMonth, SXSSFWorkbook workbook) {
		CellStyle base = baseStyle.apply(workbook);
		CellStyle darkGreenHeaderStyle = darkGreenHeader.apply(workbook);
		CellStyle greenHeaderStyle = greenHeader.apply(workbook);
		CellStyle yellowHeaderStyle = yellowHeader.apply(workbook);
		CellStyle redHeaderStyle = redHeader.apply(workbook);
		CellStyle pinkHeaderStyle = pinkHeader.apply(workbook);
		CellStyle orangeHeaderStyle = orangeHeader.apply(workbook);
		CellStyle lightBlueHeaderStyle = lightBlueHeader.apply(workbook);
		List<ExcelColumn> headers = new ArrayList<>();

		String[] staticHeaders = {"ZONE", "MD", "ZSM", "BDM/ASM", "SO NAME", "BDE", "ErpId", "BA Name", "Counter Name",
				"Counter Code","Counter Type" ,"BA Contact No", "Joining Date", "Left Date", "Total Days", "Created Date"};
		Arrays.stream(staticHeaders).forEach(e -> headers.add(new ExcelColumn(e, null, pinkHeaderStyle)));
		headers.add(new ExcelColumn("PRESENT", null, greenHeaderStyle));
		headers.add(new ExcelColumn("OFFICIAL WORK(O)", null, yellowHeaderStyle));
		headers.add(new ExcelColumn("LEAVE(L)", null, redHeaderStyle));
		headers.add(new ExcelColumn("COMP OFF.", null, pinkHeaderStyle));
		headers.add(new ExcelColumn("HOLIDAY(H)", null, orangeHeaderStyle));
		headers.add(new ExcelColumn("ABSENT(A)", null, darkGreenHeaderStyle));
		headers.add(new ExcelColumn("WEEKLY OFF(W)", null, lightBlueHeaderStyle));
		List<ExcelColumn> dynamicCols = getDatesWithDays(yearMonth).parallelStream().map(e -> {
			if (e.contains("Sun"))
				return new ExcelColumn(e, null, lightBlueHeaderStyle);
			return new ExcelColumn(e, null, yellowHeaderStyle);
		}).collect(Collectors.toList());
		headers.addAll(dynamicCols);
		headers.add(new ExcelColumn("Remarks", null, pinkHeaderStyle));
		return headers;
	}

	private List<List<ExcelColumn>> getData(List<UserActivityModel> userActivities, YearMonth yearMonth,
			SXSSFWorkbook workbook) {
		List<List<ExcelColumn>> res = new ArrayList<>();
		long start = System.currentTimeMillis();
		CellStyle base = baseStyle.apply(workbook);
		CellStyle darkGreenHeaderStyle = darkGreenHeader.apply(workbook);
		CellStyle greenHeaderStyle = greenHeader.apply(workbook);
		CellStyle yellowHeaderStyle = yellowHeader.apply(workbook);
		CellStyle redHeaderStyle = redHeader.apply(workbook);
		CellStyle pinkHeaderStyle = pinkHeader.apply(workbook);
		CellStyle orangeHeaderStyle = orangeHeader.apply(workbook);
		CellStyle lightBlueHeaderStyle = lightBlueHeader.apply(workbook);
		CellStyle whiteHeaderStyle = whiteHeader.apply(workbook);

		userActivities.forEach(userActivity -> {
			SummaryCalculator summaryCalculator = new SummaryCalculator(userActivity, yearMonth);
			List<ExcelColumn> userExcelCol = new ArrayList<>();
			var chain = userActivity.getUserChain();
			userExcelCol.add(new ExcelColumn("MD", chain.getMd(), base));
			userExcelCol.add(new ExcelColumn("ZSM", chain.getZsm(), base));
			userExcelCol.add(new ExcelColumn("BDM/ASM",
					(chain.getAsm() == null ? "" : chain.getAsm()) + (chain.getBdm() == null ? "" : chain.getBdm()),
					base));
			userExcelCol.add(new ExcelColumn("SO NAME", chain.getSo(), base));
			userExcelCol.add(new ExcelColumn("BDE", chain.getBde(), base));
			userExcelCol.add(new ExcelColumn("ErpId", userActivity.getUserCode(), base));
			userExcelCol.add(new ExcelColumn("BA Name", userActivity.getFullName(), base));
			userExcelCol.add(new ExcelColumn("BA Contact No", userActivity.getContactNo(), base));
			userExcelCol.add(new ExcelColumn("Joining Date", userActivity.getJoiningDate().toString(), base));
			userExcelCol.add(new ExcelColumn("Created Date", userActivity.getCreatedOn(), base));
			userExcelCol.add(new ExcelColumn("Left Date",
					userActivity.getReleaseDate() == null ? "" : userActivity.getReleaseDate().toString(), base));
			userExcelCol.add(new ExcelColumn("Total Days", (long) summaryCalculator.getTotalDays(), base));
			userExcelCol.add(new ExcelColumn("PRESENT", summaryCalculator.getPresentCount(), base));
			userExcelCol.add(new ExcelColumn("OFFICIAL WORK(O)", summaryCalculator.getOfcWorkCount(), base));
			userExcelCol.add(new ExcelColumn("LEAVE(L)", summaryCalculator.getLeaveCount(), base));
			userExcelCol.add(new ExcelColumn("HOLIDAY(H)", summaryCalculator.getHolidayCount(), base));
			userExcelCol.add(new ExcelColumn("COMP OFF.", summaryCalculator.getCompOffCount(), base));
			userExcelCol.add(new ExcelColumn("WEEKLY OFF(W)", summaryCalculator.getWeekOffCount(), base));
			userExcelCol.add(new ExcelColumn("ABSENT(A)", summaryCalculator.getAbsentCount(), base));

			for (String key : getDatesWithDays(yearMonth)) {
				if (summaryCalculator.isPresent(key))
					userExcelCol.add(new ExcelColumn(key, "P", greenHeaderStyle));
				if (summaryCalculator.isAbsent(key))
					userExcelCol.add(new ExcelColumn(key, "A", darkGreenHeaderStyle));
				if (summaryCalculator.isOnOfcWork(key))
					userExcelCol.add(new ExcelColumn(key, "O", yellowHeaderStyle));
				if (summaryCalculator.isOnLeave(key))
					userExcelCol.add(new ExcelColumn(key, "L", redHeaderStyle));
				if (summaryCalculator.isOnCompOff(key))
					userExcelCol.add(new ExcelColumn(key, "C", pinkHeaderStyle));
				if (summaryCalculator.isOnHoliday(key))
					userExcelCol.add(new ExcelColumn(key, "H", orangeHeaderStyle));
				if (summaryCalculator.isOnWeekOff(key))
					userExcelCol.add(new ExcelColumn(key, "W", lightBlueHeaderStyle));
				if (summaryCalculator.isNotApplicable(key))
					userExcelCol.add(new ExcelColumn(key, "", whiteHeaderStyle));
			}
			userExcelCol.add(new ExcelColumn("ZONE", userActivity.getZone(), base));
			userExcelCol.add(new ExcelColumn("Counter Name", userActivity.getOutlet().getOutletName(), base));
			userExcelCol.add(new ExcelColumn("Counter Code", userActivity.getOutlet().getOutletCode(), base));
			userExcelCol.add(new ExcelColumn("Counter Type",userActivity.getOutlet().getOutletType(),base));
			res.add(userExcelCol);
		});

		long elapsed = System.currentTimeMillis() - start;
		LOGGER.info("[Attendance Report] Column Creation:: elapsed:: " + elapsed);
		return res;
	}
}

class ExcelStyles {

	private static CellStyle getPurpleBackgroundStyle(SXSSFWorkbook wb) {
		CellStyle cellStyle = getBaseStyle(wb);
		getGreenBackgroundCell(cellStyle);
		return cellStyle;
	}

	static CellStyle getBaseStyle(SXSSFWorkbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("Calibri");
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);

		cellStyle.setFont(font);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		cellStyle.setBottomBorderColor(IndexedColors.GREY_80_PERCENT.index);
		cellStyle.setTopBorderColor(IndexedColors.GREY_80_PERCENT.index);
		cellStyle.setLeftBorderColor(IndexedColors.GREY_80_PERCENT.index);
		cellStyle.setRightBorderColor(IndexedColors.GREY_80_PERCENT.index);
		return cellStyle;
	}

	static CellStyle getPurpleBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.LAVENDER.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}

	static CellStyle getRedBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.RED.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}

	static CellStyle getGreenBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}

	static CellStyle getDarkGreenBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}

	static CellStyle getYellowBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}

	static CellStyle getPinkBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.PINK.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}

	static CellStyle getOrangeBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.ORANGE.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}

	static CellStyle getLightBlueBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}

	static CellStyle getWhiteBackgroundCell(CellStyle cellStyle) {
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		return cellStyle;
	}
}
