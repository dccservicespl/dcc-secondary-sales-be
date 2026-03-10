package com.dcc.osheaapp.report.categoryReport.service;

import com.dcc.osheaapp.common.service.exporter.excel.ExcelExporter;
import com.dcc.osheaapp.common.service.exporter.ExportManager;
import com.dcc.osheaapp.vo.views.CategoryReportView;

import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PrimaryCategoryReportExportService {

    private final String[] HEADER_COLS = {};

    public OutputStream export(List<CategoryReportView> records, OutputStream outputStream) {
        String msgResponse = "Category Report Exported";
        BiFunction<String, CategoryReportView, Object> getter = this::getFieldValue;
        ExportManager<CategoryReportView> exportManager =
                new ExportManager<>(records, "OJB_Herbals_Pvt_Ltd_Product_Primary_Category_Analysis_.xlsx", outputStream);
        ExcelExporter<CategoryReportView> exporter =
                new ExcelExporter<>(getHeaderCols(), "OJB_Herbals_Pvt_Ltd_Product_Primary_Category_Analysis", getter);
        exportManager.export(exporter);
        return outputStream;
    }

    private Object getFieldValue(String header, CategoryReportView view) {
        return Arrays.stream(ColFieldEnum.values())
                .filter(e1 -> e1.name.equals(header))
                .findAny()
                .map(e ->  e.getValue(view))
                .orElse(null);
    }

    enum ColFieldEnum {
//        MD("MD", CategoryReportView::getMd),
//        ZSM("ZSM", CategoryReportView::getZsm),
    	MD("MD", e -> e.getUserChain().getMd()),
        ZSM("ZSM", e -> e.getUserChain().getZsm()),
        MONTH("MONTH",e -> e.getMonYr() == null ? "" : Month.of(Integer.parseInt(e.getMonYr().split("-")[0])).toString()),
        ZONE("ZONE", CategoryReportView::getCompanyZoneName),
        PRIMARY_CATEGORY("PRIMARY CATEGORY", CategoryReportView::getCategoryName),
        PURCHASE("PURCHASE Qty(Pcs)", CategoryReportView::getPurchase),
        PURCHASE_AMOUNT("PURCHASE(VALUE)", e -> Double.valueOf(e.getPurchaseAmount())),
        PURCHASE_PERCENTAGE("% of Value Purchase Contribution", e -> e.getPurchasePercentage() == null ? "" : (e.getPurchasePercentage())),
        SALE("SALE(QTY)", CategoryReportView::getSale),
        SALE_AMOUNT("SALE(VALUE)", e -> Double.valueOf(e.getSaleAmount())),
        SALE_PERCENTAGE("% of Value Sales Contribution",e -> e.getSalePercentage() == null ? "" : (e.getSalePercentage()));

        private final String name;
        private final Function<CategoryReportView, Object> view;

        ColFieldEnum(String name, Function<CategoryReportView, Object> view) {
            this.name = name;
            this.view = view;
        }
        public String getName() {
            return name;
        }

        public Object getValue(CategoryReportView view) {
            return this.view.apply(view);
        }
    }


    private static List<String> getHeaderCols() {
        return Arrays.stream(ColFieldEnum.values()).map(ColFieldEnum::getName).collect(Collectors.toList());
    }
}
