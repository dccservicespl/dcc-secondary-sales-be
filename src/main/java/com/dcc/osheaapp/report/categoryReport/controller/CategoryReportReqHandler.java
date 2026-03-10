package com.dcc.osheaapp.report.categoryReport.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.report.categoryReport.service.PrimaryCategoryReportExportService;
import com.dcc.osheaapp.report.categoryReport.service.SecondaryCategoryReportExportService;
import com.dcc.osheaapp.repository.CategoryReportViewRepo;
import com.dcc.osheaapp.repository.UserChainCategoryReportViewRepo;
import com.dcc.osheaapp.service.UserChainService;
import com.dcc.osheaapp.vo.views.CategoryReportView;

@Service
public class CategoryReportReqHandler {

	private static final Logger LOGGER = LogManager.getLogger(CategoryReportReqHandler.class);
	private final CategoryReportViewRepo categoryReportViewRepo;
	private final PrimaryCategoryReportExportService primaryCategoryReportExportService;
	private final SecondaryCategoryReportExportService secondaryCategoryReportExportService;
	private final UserChainCategoryReportViewRepo userChainRepo;
	private final UserChainService userChainService;
	private final ReportWSHandler wsHandler;


	private final IDropdownMastereRepository dropdownMastereRepository;

	@PersistenceContext
	private EntityManager em;

	public CategoryReportReqHandler(CategoryReportViewRepo categoryReportViewRepo,
                                    PrimaryCategoryReportExportService primaryCategoryReportExportService,
                                    UserChainCategoryReportViewRepo userChainRepo, UserChainService userChainService,
                                    SecondaryCategoryReportExportService secondaryCategoryReportExportService, ReportWSHandler wsHandler, IDropdownMastereRepository dropdownMastereRepository) {
		this.categoryReportViewRepo = categoryReportViewRepo;
		this.primaryCategoryReportExportService = primaryCategoryReportExportService;
		this.secondaryCategoryReportExportService = secondaryCategoryReportExportService;
		this.userChainRepo = userChainRepo;
		this.userChainService = userChainService;
		this.wsHandler = wsHandler;
        this.dropdownMastereRepository = dropdownMastereRepository;
    }

	public void exportPrimaryCatReportThroughWs(CategoryReportInputDto dto) {
		CompletableFuture.supplyAsync(() -> {
			try {

				String fileName = "OJB_Herbals_Pvt_Ltd_Product_Primary_Category_Analysis_" + dto.getMonth().toString()
						+ ".xlsx";
				LOGGER.info("Starting Report Generation:: " + fileName);
				return new ReportWSPayload(fileName,
						((ByteArrayOutputStream) exportPrimary(dto, new ByteArrayOutputStream())).toByteArray());
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
	public Object exportPrimary(CategoryReportInputDto dto, OutputStream os) throws IOException {
		String whereClause = "";
		List<CategoryReportView> data = categoryReportViewRepo.findPrimaryCategoryReport(whereClause, dto.getMonthYr());

		Double totalPurchase = data.stream().mapToDouble(x -> Double.parseDouble(x.getPurchaseAmount())).reduce(0,
				Double::sum);
		Double totalSale = data.stream().mapToDouble(x -> Double.parseDouble(x.getSaleAmount())).reduce(0, Double::sum);

		Long totalPurchaseQty = data.stream().map(CategoryReportView::getPurchase).reduce(0L, Long::sum);
		Long totalSaleQty = data.stream().map(CategoryReportView::getSale).reduce(0L, Long::sum);


		for (CategoryReportView each : data) {
			em.clear();
			each.setUserChain(userChainService.generateChainFlattened(each.getCreatedBy()));
			String purPer = String.format("%.0f",
					((Double.parseDouble(each.getPurchaseAmount()) / totalPurchase) * 100));
			each.setPurchasePercentage(purPer + "%");
			String salePer = String.format("%.0f", ((Double.parseDouble(each.getSaleAmount()) / totalSale) * 100));
			each.setSalePercentage(salePer + "%");
		}
		CategoryReportView extraRow = new CategoryReportView();
		extraRow.setCategoryName("Total");
		extraRow.setPurchase(totalPurchaseQty);
		extraRow.setPurchaseAmount(totalPurchase.toString());
		extraRow.setSale(totalSaleQty);
		extraRow.setSaleAmount(totalSale.toString());
		extraRow.setUserChain(new UserChainFlat());
		data.add(extraRow);
		return primaryCategoryReportExportService.export(data, os);
	}

	public OutputStream exportPrimary(ReportInputDto dto, OutputStream os) throws IOException {
		String whereClause = "";
		List<CategoryReportView> data = categoryReportViewRepo.findPrimaryCategoryReport(whereClause, dto.getYearMonth(),dto.getZone());

		Double totalPurchase = data.stream().mapToDouble(x -> Double.parseDouble(x.getPurchaseAmount())).reduce(0,
				Double::sum);
		Double totalSale = data.stream().mapToDouble(x -> Double.parseDouble(x.getSaleAmount())).reduce(0, Double::sum);

		Long totalPurchaseQty = data.stream().map(CategoryReportView::getPurchase).reduce(0L, Long::sum);
		Long totalSaleQty = data.stream().map(CategoryReportView::getSale).reduce(0L, Long::sum);


		for (CategoryReportView each : data) {
			em.clear();
			each.setUserChain(userChainService.generateChainFlattened(each.getCreatedBy()));
			String purPer = String.format("%.0f",
					((Double.parseDouble(each.getPurchaseAmount()) / totalPurchase) * 100));
			each.setPurchasePercentage(purPer + "%");
			String salePer = String.format("%.0f", ((Double.parseDouble(each.getSaleAmount()) / totalSale) * 100));
			each.setSalePercentage(salePer + "%");
		}
		CategoryReportView extraRow = new CategoryReportView();
		extraRow.setCategoryName("Total");
		extraRow.setPurchase(totalPurchaseQty);
		extraRow.setPurchaseAmount(totalPurchase.toString());
		extraRow.setSale(totalSaleQty);
		extraRow.setSaleAmount(totalSale.toString());
		extraRow.setUserChain(new UserChainFlat());
		data.add(extraRow);
		return primaryCategoryReportExportService.export(data, os);
	}

	public void exportSecondaryCatReportThroughWs(CategoryReportInputDto dto) {
		CompletableFuture.supplyAsync(() -> {
			try {

				return new ReportWSPayload(
						"OJB_Herbals_Pvt_Ltd_Product_Secondary_Category_Analysis_" + dto.getMonth().toString()
								+ ".xlsx",
						((ByteArrayOutputStream) exportSecondary(dto, new ByteArrayOutputStream())).toByteArray());
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
	public String getPrimaryReportFilename(ReportInputDto dto) {
		DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(dto.getZone(), "zone").orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
		return "primary_category_analysis_" + dto.getYearMonth() + "_" + dropdownMaster.getFieldName() + ".xlsx";
	}
	public Object exportSecondary(CategoryReportInputDto dto, OutputStream os) throws IOException {
		String whereClause = "";
		// if(null != dto.getCompanyZone() && !dto.getCompanyZone().isEmpty()) {
		// whereClause = " and o.company_zone = "+dto.getCompanyZone();
		// }
		DecimalFormat df = new DecimalFormat("#.##");
		List<CategoryReportView> data = categoryReportViewRepo.findSecondaryCategoryReport(whereClause,
				dto.getMonthYr());
		Double totalPurchase = data.stream().mapToDouble(x -> Double.parseDouble(x.getPurchaseAmount())).reduce(0,
				Double::sum);
		Double totalSale = data.stream().mapToDouble(x -> Double.parseDouble(x.getSaleAmount())).reduce(0, Double::sum);

		Long totalPurchaseQty = data.stream().map(CategoryReportView::getPurchase).reduce(0L, Long::sum);
		Long totalSaleQty = data.stream().map(CategoryReportView::getSale).reduce(0L, Long::sum);
		for (CategoryReportView each : data) {
			em.clear();
			each.setUserChain(userChainService.generateChainFlattened(each.getCreatedBy()));
			String purPer = String.format("%.0f",
					((Double.parseDouble(each.getPurchaseAmount()) / totalPurchase) * 100));
			each.setPurchasePercentage(purPer + "%");
			String salePer = String.format("%.0f", ((Double.parseDouble(each.getSaleAmount()) / totalSale) * 100));
			each.setSalePercentage(salePer + "%");
			// each.setPurchasePercentage(df.format(((Double.parseDouble(each.getPurchaseAmount())/
			// totalPurchase)*100))+"%");
			// each.setSalePercentage(df.format(((Double.parseDouble(each.getSaleAmount())/
			// totalSale)*100))+"%");
		}

		CategoryReportView extraRow = new CategoryReportView();
		extraRow.setCategoryName("Total");
		extraRow.setPurchase(totalPurchaseQty);
		extraRow.setPurchaseAmount(totalPurchase.toString());
		extraRow.setSale(totalSaleQty);
		extraRow.setSaleAmount(totalSale.toString());
		extraRow.setUserChain(new UserChainFlat());
		data.add(extraRow);
		return secondaryCategoryReportExportService.export(data, os);
	}

	public OutputStream exportSecondary(ReportInputDto dto, OutputStream os) throws IOException {
		String whereClause = "";
		DecimalFormat df = new DecimalFormat("#.##");
		List<CategoryReportView> data = categoryReportViewRepo.findSecondaryCategoryReport(whereClause,
				dto.getYearMonth(),dto.getZone());
		Double totalPurchase = data.stream().mapToDouble(x -> Double.parseDouble(x.getPurchaseAmount())).reduce(0,
				Double::sum);
		Double totalSale = data.stream().mapToDouble(x -> Double.parseDouble(x.getSaleAmount())).reduce(0, Double::sum);

		Long totalPurchaseQty = data.stream().map(CategoryReportView::getPurchase).reduce(0L, Long::sum);
		Long totalSaleQty = data.stream().map(CategoryReportView::getSale).reduce(0L, Long::sum);
		for (CategoryReportView each : data) {
			em.clear();
			each.setUserChain(userChainService.generateChainFlattened1(each.getBaId()));
			String purPer = String.format("%.0f",
					((Double.parseDouble(each.getPurchaseAmount()) / totalPurchase) * 100));
			each.setPurchasePercentage(purPer + "%");
			String salePer = String.format("%.0f", ((Double.parseDouble(each.getSaleAmount()) / totalSale) * 100));
			each.setSalePercentage(salePer + "%");
		}

		CategoryReportView extraRow = new CategoryReportView();
		extraRow.setCategoryName("Total");
		extraRow.setPurchase(totalPurchaseQty);
		extraRow.setPurchaseAmount(totalPurchase.toString());
		extraRow.setSale(totalSaleQty);
		extraRow.setSaleAmount(totalSale.toString());
		extraRow.setUserChain(new UserChainFlat());
		data.add(extraRow);
		return secondaryCategoryReportExportService.export(data, os);
	}

	public String getSecondaryReportFilename(ReportInputDto dto) {
		DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(dto.getZone(), "zone").orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
		return "secondary_category_analysis_" + dto.getYearMonth() + "_" + dropdownMaster.getFieldName() + ".xlsx";
	}
}
