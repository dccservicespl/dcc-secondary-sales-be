package com.dcc.osheaapp.report.baPerformance.service;

import com.dcc.osheaapp.common.service.exporter.ExportManager;
import com.dcc.osheaapp.common.service.exporter.excel.ExcelExporter;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PerformanceExportService {

    private final String[] HEADER_COLS = {};

    public OutputStream export(List<PerformanceDto> records, OutputStream outputStream) {
        BiFunction<String, PerformanceDto, Object> getter = this::getFieldValue;
        ExportManager<PerformanceDto> exportManager =
                new ExportManager<>(records, "BA_PERFORMANCE.xlsx", outputStream);
        ExcelExporter<PerformanceDto> exporter =
                new ExcelExporter<>(getHeaderCols(), "PERFORMANCE", getter);
        exportManager.export(exporter);
        return outputStream;
    }

    private Object getFieldValue(String header, PerformanceDto view) {
        return Arrays.stream(ColFieldEnum.values())
                .filter(e1 -> e1.name.equals(header))
                .findAny()
                .map(e -> e.getValue(view))
                .orElse(null);
    }

    enum ColFieldEnum {
        Zone("ZONE", PerformanceDto::getZone),
        MD("MD", e -> e.getUserChain().getMd()),
        ZSM("ZSM", e -> e.getUserChain().getZsm()),
        ASM("ASM/BDM",e -> (e.getUserChain().getAsm() == null ? "" : e.getUserChain().getAsm()) + (e.getUserChain().getBdm() == null ? "" : e.getUserChain().getBdm())),
        BDE("BDE", e -> e.getUserChain().getBde()),
        SO("SO", e -> e.getUserChain().getSo()),
        DISTRIBUTOR_NAME("DISTRIBUTOR NAME", PerformanceDto::getDistributorName),
        DISTRIBUTOR_CODE("DISTRIBUTOR CODE" , PerformanceDto::getDistributorCode),
        REGION("REGION", PerformanceDto::getRegion),
        COUNTER_CODE("COUNTER NAME", PerformanceDto::getCounterCode),
        COUNTER_NAME("COUNTER CODE", PerformanceDto::getCounterName),
        BA_ErpID("BA ErpID", PerformanceDto::getBaErpId),
        BA_NAME("BA NAME", PerformanceDto::getBaName),
        BA_SALARY("BA SALARY", PerformanceDto::getBaSalary),
        DOJ("DOJ", PerformanceDto::getDoj),
        LEFT_DATE("LEFT DATE", PerformanceDto::getLeftDate),
        MONTH("MONTH", PerformanceDto::getMonth),
        LAST_MONTH_CLOSING_STOCK("LAST MONTH CL. STOCK", e -> e.getLastMonthClStock() == null ? "" : Double.parseDouble(e.getLastMonthClStock())),
        OPENING_STOCK("OP. STOCK", e -> e.getOpStock()  == null ? "" : Double.parseDouble(e.getOpStock())),
        STOCK_DIFFERENCE_O_N("STOCK DIFFERENCE (O-N)", e -> e.getStockDifference() == null ? "" : Double.parseDouble(e.getStockDifference())),
        TARGET_SKIN("TARGET (SKIN)", e -> e.getTargetSkin() == null ? "" : Double.parseDouble(e.getTargetSkin())),
        TARGET_COLOR("TARGET (COLOR)", e-> e.getTargetColor() == null ? "" : Double.parseDouble(e.getTargetColor())),
        TOTAL_TARGET("TOTAL TARGET", e -> e.getTargetTotal() == null ? "" : Double.parseDouble(e.getTargetTotal())),
        ACHIEVEMENT_SKIN("ACHIEVEMENT (SKIN)", e -> e.getAchievementSkin() == null ? "" : Double.parseDouble(e.getAchievementSkin())),
        ACHIEVEMENT_COLOR("ACHIEVEMENT (COLOR)", e -> e.getAchievementColor() == null ? "" : Double.parseDouble(e.getAchievementColor())),
        TOTAL_ACHIEVEMENT("TOTAL ACHIEVEMENT", e -> e.getAchievementTotal() == null ? "" : Double.parseDouble(e.getAchievementTotal())),
        ACHIEVEMENT_SKIN_PERCENTAGE("% ACHIEVEMENT (SKIN)", e -> e.getPercentageAchievementSkin() + "%"),
        ACHIEVEMENT_COLOR_PERCENTAGE("% ACHIEVEMENT (COLOR)", e -> e.getPercentageAchievementColor() + "%"),
        TOTAL_ACHIEVEMENT_PERCENTAGE("% ACHIEVEMENT (TOTAL)", e-> e.getPercentageAchievementTotal() + "%"),

        RANK("RANK",e -> e.getRank() == null ? "" : Long.parseLong(e.getRank()));
        private final String name;
        private final Function<PerformanceDto, Object> view;

        ColFieldEnum(String name, Function<PerformanceDto, Object> view) {
            this.name = name;
            this.view = view;
        }
        public String getName() {
            return name;
        }

        public Object getValue(PerformanceDto view) {
            return this.view.apply(view);
        }
    }


    private static List<String> getHeaderCols() {
        return Arrays.stream(ColFieldEnum.values()).map(ColFieldEnum::getName).collect(Collectors.toList());
    }
}
