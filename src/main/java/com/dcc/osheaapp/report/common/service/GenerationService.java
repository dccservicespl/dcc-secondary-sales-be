package com.dcc.osheaapp.report.common.service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.service.storage.FileResponse;
import com.dcc.osheaapp.common.service.storage.S3OutputStream;
import com.dcc.osheaapp.report.attendance.controller.AttendanceReqHandler;
import com.dcc.osheaapp.report.baPerformance.controller.PerformanceReqHandler;
import com.dcc.osheaapp.report.baRank.controller.BaRankReportReqHandler;
import com.dcc.osheaapp.report.categoryReport.controller.CategoryReportReqHandler;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.report.common.model.ReportGenStatus;
import com.dcc.osheaapp.report.common.model.ReportRegistry;
import com.dcc.osheaapp.report.common.model.ReportType;
import com.dcc.osheaapp.report.common.repository.ReportRegistryRepo;
import com.dcc.osheaapp.report.counterStock.controller.CounterStockReportReqHandler;
import com.dcc.osheaapp.report.distributor.DistributorReportExportReqHandler;
import com.dcc.osheaapp.report.outlet.controller.OutletReportExportReqHandler;
import com.dcc.osheaapp.report.purchaseSale.controller.PurchaseRecordExcelExportInputDto;
import com.dcc.osheaapp.report.purchaseSale.controller.PurchaseSaleReqHandler;
import com.dcc.osheaapp.report.user.controller.UserExportReqHandler;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

import static com.dcc.osheaapp.common.service.Util.LOGGER;

@Service
public class GenerationService {

	@Autowired
	AttendanceReqHandler attendanceReqHandler;

	@Autowired
	PerformanceReqHandler performanceReqHandler;

	@Autowired
	BaRankReportReqHandler baRankReportReqHandler;

	@Autowired
	CategoryReportReqHandler categoryReportReqHandler;


	@Autowired
	CounterStockReportReqHandler counterStockReportReqHandler;

	@Autowired
	DistributorReportExportReqHandler distributorReportExportReqHandler;

	@Autowired
	OutletReportExportReqHandler outletReportExportReqHandler;
	
	@Autowired
	PurchaseSaleReqHandler purchaseSaleReqHandler;

	@Autowired
	UserExportReqHandler userExportReqHandler;


	@Autowired
	UploadReportService uploadReportService;

	@Autowired
	ReportRegistryRepo reportRegistryRepo;

	public ReportRegistry generate(String id, ReportInputDto dto) {
		S3OutputStream outputStream = new S3OutputStream();
		if (dto.getReportType().equals(ReportType.ATTENDANCE_SUMMARY)) {
			String filename = attendanceReqHandler.getAttendanceSummaryFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = attendanceReqHandler.exportSummary(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				e.printStackTrace();
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}
		}

		if (dto.getReportType().equals(ReportType.ATTENDANCE_DETAILS)) {
			String filename = attendanceReqHandler.getAttendanceDetailsFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = attendanceReqHandler.exportDetails(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				e.printStackTrace();
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}
		}
		
		

		if (dto.getReportType().equals(ReportType.COUNTER_STOCK)) {
			String filename = counterStockReportReqHandler.getCounterStockReportFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = counterStockReportReqHandler.export(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}
		}

		if (dto.getReportType().equals(ReportType.BA_PERFORMANCE)) {
			String filename = performanceReqHandler.getBaPerformanceReportFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = performanceReqHandler.export(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}
		}

		if (dto.getReportType().equals(ReportType.BA_RANK)) {
			String filename = baRankReportReqHandler.getBaRankReportFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = baRankReportReqHandler.export(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}
		}

		if (dto.getReportType().equals(ReportType.PRIMARY_CATEGORY_ANALYSIS)) {
			String filename = categoryReportReqHandler.getPrimaryReportFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = categoryReportReqHandler.exportPrimary(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}

		}


		if (dto.getReportType().equals(ReportType.SECONDARY_CATEGORY_ANALYSIS)) {
			String filename = categoryReportReqHandler.getSecondaryReportFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = categoryReportReqHandler.exportSecondary(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}

		}
		if (dto.getReportType().equals(ReportType.DISTRIBUTOR)) {
			String filename = distributorReportExportReqHandler.getDistributorReportFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = distributorReportExportReqHandler.export(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}

		}

		if (dto.getReportType().equals(ReportType.OUTLET)) {
			String filename = outletReportExportReqHandler.getOutletReportFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = outletReportExportReqHandler.export(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}

		}
		if (dto.getReportType().equals(ReportType.USER)) {
			String filename = userExportReqHandler.getUserReportFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(dto.getReportType())
					.setYearMonth(dto.getYearMonth())
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName).setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = userExportReqHandler.export(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}

		}
			return null;
	}

	public ReportRegistry generateSalePurchase(String id, PurchaseRecordExcelExportInputDto dto) {
		S3OutputStream outputStream = new S3OutputStream();
//		if (dto.getReportType().equals(ReportType.SALE_PURCHASE)) {
		LOGGER.info("Generation Service ::sale_purchase Report :: Entering....");

		String filename = purchaseSaleReqHandler.getSalePurchaseFilename(dto);
			String extension = FilenameUtils.getExtension(filename);
			String fileName = FilenameUtils.removeExtension(filename) + "_" + id + "." + extension;
			ReportType reportType = dto.getActivity().toUpperCase().equals("SALE") ? ReportType.SALE : ReportType.PURCHASE;
			ReportRegistry registry = new ReportRegistry().setId(id).setReportType(reportType)
					.setGenerationStatus(ReportGenStatus.GENERATING).setCreatedBy(1L).setFileName(fileName)
					.setZone(new DropdownMasterVo().setId(dto.getZone()));
			reportRegistryRepo.save(registry);
			try {
				OutputStream excelStream = (OutputStream) purchaseSaleReqHandler.exportPurchases(dto, outputStream);
				FileResponse response = uploadReportService.uploadReport(excelStream, fileName);
				reportRegistryRepo.save(
						registry.setFileUrl(response.getFileUrl()).setGenerationStatus(ReportGenStatus.GENERATED));
			} catch (Exception e) {
				reportRegistryRepo.save(registry.setGenerationStatus(ReportGenStatus.FAILED));
				e.printStackTrace();
				throw new OjbException(e, ErrorCode.ERROR_GENERATING_REPORT, new Object[]{filename});
			}
//		}
			return null;
	}
}
