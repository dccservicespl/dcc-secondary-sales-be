package com.dcc.osheaapp.report.attendance.service;

import com.dcc.osheaapp.common.service.exporter.excel.ExcelColumn;
import com.dcc.osheaapp.common.service.exporter.excel.ExcelExporterAlt;
import com.mysema.commons.lang.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCreationHelper;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AttendanceExportService {

    Function<SXSSFWorkbook, Pair<SXSSFWorkbook, CellStyle>> baseStyle = AttendanceExportService::getBaseStyle;
    Function<SXSSFWorkbook, Pair<SXSSFWorkbook, CellStyle>> headerStyle =
            baseStyle.andThen(AttendanceExportService::getLightBlueBackgroundCell);
    Function<SXSSFWorkbook, Pair<SXSSFWorkbook, CellStyle>> dateCellStyle =
            baseStyle.andThen(AttendanceExportService::getDateCellStyle);
    Function<SXSSFWorkbook, Pair<SXSSFWorkbook, CellStyle>> timeCellStyle =
            baseStyle.andThen(AttendanceExportService::getTimeStyle);
    Function<SXSSFWorkbook, Pair<SXSSFWorkbook, CellStyle>> timeDurationCellStyle =
            baseStyle.andThen(AttendanceExportService::getTimeDurationStyle);

    static Pair<SXSSFWorkbook, CellStyle> getBaseStyle(SXSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        SXSSFCreationHelper creationHelper = new SXSSFCreationHelper(wb);
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("General"));
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
        return new Pair<SXSSFWorkbook, CellStyle>(wb, cellStyle);
    }

    static Pair<SXSSFWorkbook, CellStyle> getLightBlueBackgroundCell(Pair<SXSSFWorkbook, CellStyle> stylePair) {
        CellStyle cellStyle = stylePair.getFirst().createCellStyle();
        cellStyle.cloneStyleFrom(stylePair.getSecond());
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
        cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
        return new Pair<SXSSFWorkbook, CellStyle>(stylePair.getFirst(), cellStyle);
    }

    private static Pair<SXSSFWorkbook, CellStyle> getDateCellStyle(Pair<SXSSFWorkbook, CellStyle> stylePair) {
        CellStyle cellStyle = stylePair.getFirst().createCellStyle();
        SXSSFCreationHelper creationHelper = new SXSSFCreationHelper(stylePair.getFirst());
        cellStyle.cloneStyleFrom(stylePair.getSecond());
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("d-mmm-yy"));
        return new Pair<SXSSFWorkbook, CellStyle>(stylePair.getFirst(), cellStyle);
    }

    private static Pair<SXSSFWorkbook, CellStyle> getTimeStyle(Pair<SXSSFWorkbook, CellStyle> stylePair) {
        CellStyle cellStyle = stylePair.getFirst().createCellStyle();
        SXSSFCreationHelper creationHelper = new SXSSFCreationHelper(stylePair.getFirst());
        cellStyle.cloneStyleFrom(stylePair.getSecond());
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("h:mm AM/PM"));
        return new Pair<SXSSFWorkbook, CellStyle>(stylePair.getFirst(), cellStyle);
    }

    private static Pair<SXSSFWorkbook, CellStyle> getTimeDurationStyle(Pair<SXSSFWorkbook, CellStyle> stylePair) {
        CellStyle cellStyle = stylePair.getFirst().createCellStyle();
        cellStyle.cloneStyleFrom(stylePair.getSecond());
        SXSSFCreationHelper creationHelper = new SXSSFCreationHelper(stylePair.getFirst());
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("h:mm"));
        return new Pair<SXSSFWorkbook, CellStyle>(stylePair.getFirst(), cellStyle);
    }


    public OutputStream export(List<AttendanceDetailsDto> records, OutputStream outputStream) {
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        ExcelExporterAlt<Void> exporter =
                new ExcelExporterAlt<>(
                        getHeaders(workbook), getData(records, workbook), workbook, "Details");
        exporter.export(new ArrayList<>(), null, outputStream);
        return outputStream;
    }

    private List<ExcelColumn> getHeaders(SXSSFWorkbook wb) {
        String[] staticHeaders = {
                "ZONE",
                "MD",
                "ZSM",
                "BDM/ASM",
                "SO NAME",
                "BDE",
                "ErpId",
                "BA NAME",
                "MONTH",
                "DATE",
                "TYPE",
                "REASON",
                "STORE CHECK IN",
                "STORE CHECK OUT",
                "TOTAL TIME"
        };
        CellStyle headerStyles = headerStyle.apply(wb).getSecond();

        return Arrays.stream(staticHeaders)
                .map(e -> new ExcelColumn(e, null, headerStyles))
                .collect(Collectors.toList());
    }

    public List<List<ExcelColumn>> getData(List<AttendanceDetailsDto> dtos, SXSSFWorkbook wb) {

        CellStyle generalStyle = baseStyle.apply(wb).getSecond();
        CellStyle dateStyle = dateCellStyle.apply(wb).getSecond();
        CellStyle timeStyle = timeCellStyle.apply(wb).getSecond();
        CellStyle timeDurationStyle = timeDurationCellStyle.apply(wb).getSecond();
        var formatter = new SimpleDateFormat("dd/MM/yy");
        return dtos.stream()
                .map(
                        e -> {
                            List<ExcelColumn> columns = new ArrayList<>();
                            columns.add(new ExcelColumn("ZONE", e.getZone(), generalStyle));
                            columns.add(new ExcelColumn("MD", e.getUserChain().getMd(), generalStyle));
                            columns.add(new ExcelColumn("ZSM", e.getUserChain().getZsm(), generalStyle));
                            columns.add(new ExcelColumn("BDM/ASM", (e.getUserChain().getAsm() == null ? "" : e.getUserChain().getAsm()) + (e.getUserChain().getBdm() == null ? "" : e.getUserChain().getBdm()), generalStyle));
                            columns.add(new ExcelColumn("SO NAME", e.getUserChain().getSo(), generalStyle));
                            columns.add(new ExcelColumn("BDE", e.getUserChain().getBde(), generalStyle));
                            columns.add(new ExcelColumn("ErpId", e.getUser().getUsername().split("@")[0], generalStyle));
                            columns.add(new ExcelColumn("BA NAME", e.getUser().getFullName(), generalStyle));

                            columns.add(new ExcelColumn("MONTH", e.getMonth().toString(), generalStyle));
                            columns.add(new ExcelColumn("DATE", (e.getDate()), generalStyle));
                            columns.add(new ExcelColumn("TYPE", e.getType(), generalStyle));
                            columns.add(new ExcelColumn("REASON", e.getReason(), generalStyle));
                            columns.add(
                                    new ExcelColumn(
                                            "STORE CHECK IN",
                                            (e.getStoreCheckIn() == null)
                                                    ? ""
                                                    : DateUtil.convertTime(e.getStoreCheckIn().toString()),
                                            timeStyle));
                            columns.add(
                                    new ExcelColumn(
                                            "STORE CHECK OUT",
                                            e.getStoreCheckOut() == null
                                                    ? ""
                                                    : DateUtil.convertTime(e.getStoreCheckOut().toString()),
                                            timeStyle));
                            columns.add(
                                    new ExcelColumn(
                                            "TOTAL TIME",
                                            (e.getTotalTime() == null || e.getTotalTime().isEmpty()) ? "" : DateUtil.convertTime(e.getTotalTime()),
                                            timeDurationStyle));
                            return columns;
                        })
                .collect(Collectors.toList());
    }
}
