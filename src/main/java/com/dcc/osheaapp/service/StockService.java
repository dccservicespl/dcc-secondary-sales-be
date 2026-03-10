package com.dcc.osheaapp.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.common.model.Constants.BA_Activity_Enum;
import com.dcc.osheaapp.common.service.Util;
import com.dcc.osheaapp.common.service.storage.FileMetadata;
import com.dcc.osheaapp.common.service.storage.FileResponse;
import com.dcc.osheaapp.common.service.storage.S3StorageService;
import com.dcc.osheaapp.repository.ICounterStockManageRepository;
//import com.dcc.osheaapp.repository.ICounterStockManageSanpRepository;
import com.dcc.osheaapp.repository.IFormMediaMappingRepository;
import com.dcc.osheaapp.repository.IOutletRepository;
import com.dcc.osheaapp.repository.IPocketMISRepository;
import com.dcc.osheaapp.repository.IStockApprovalRepository;
import com.dcc.osheaapp.repository.IStockEntryDtlRepository;
import com.dcc.osheaapp.repository.IStockEntryLogRepository;
import com.dcc.osheaapp.repository.IStockEntryRepository;
import com.dcc.osheaapp.repository.IUserActivityRepository;
import com.dcc.osheaapp.repository.IUserCredRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
//import com.dcc.osheaapp.vo.CounterStockManageSnapVo;
import com.dcc.osheaapp.vo.CounterStockManageVo;
import com.dcc.osheaapp.vo.FormMediaMappingVo;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.PocketMISOPDto;
import com.dcc.osheaapp.vo.ProductVo;
import com.dcc.osheaapp.vo.SaleRatioOPDto;
import com.dcc.osheaapp.vo.StockApprovalInputVo;
import com.dcc.osheaapp.vo.StockApprovalVo;
import com.dcc.osheaapp.vo.StockEntryDtlVo;
import com.dcc.osheaapp.vo.StockEntryLogVo;
import com.dcc.osheaapp.vo.StockEntryVo;
import com.dcc.osheaapp.vo.StockSearchInputVo;
import com.dcc.osheaapp.vo.UserActivityRegisterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.dto.PocketMISInputVo;
import com.dcc.osheaapp.vo.views.PocketMISDto;
import com.dcc.osheaapp.vo.views.StockReturnView;

@Service
public class StockService {

	private static final Logger LOGGER = LogManager.getLogger(StockService.class);

	private final IStockEntryRepository iStockEntryRepository;
	private final IFormMediaMappingRepository iFormMediaMappingRepository;
	private final IUserDetailsRepository iUserDetailsRepository;
	private final IOutletRepository iOutletRepository;
	private final IStockEntryDtlRepository iStockEntryDtlRepository;
	// private final IProductRepository iProductRepository;
	private final IStockEntryLogRepository iStockEntryLogRepository;
	private final IUserCredRepository userCredRepository;
	private final IUserActivityRepository iUserActivityRepository;
	private final IStockApprovalRepository iStockApprovalRepository;
	private final ICounterStockManageRepository iCounterStockManageRepository;
	private final IPocketMISRepository iPocketMISRepository;
	private final S3StorageService s3StorageService;
	// private final ICounterStockManageSanpRepository snap;

	@Value("${file.invoiceUploadFolder}")
	private String invoiceUploadFolder;

	@Autowired
	public StockService(IStockEntryRepository iStockEntryRepository,
			IFormMediaMappingRepository iFormMediaMappingRepository, IUserDetailsRepository iUserDetailsRepository,
			IOutletRepository iOutletRepository, IUserCredRepository userCredRepository,
			// IProductRepository iProductRepository,
			IStockEntryLogRepository iStockEntryLogRepository,
			IUserActivityRepository iUserActivityRepository, IStockApprovalRepository iStockApprovalRepository,
			ICounterStockManageRepository iCounterStockManageRepository,
			IStockEntryDtlRepository iStockEntryDtlRepository, IPocketMISRepository iPocketMISRepository,
			S3StorageService s3StorageService
	/* ICounterStockManageSanpRepository snap */) {
		this.iStockEntryRepository = iStockEntryRepository;
		this.iFormMediaMappingRepository = iFormMediaMappingRepository;
		this.iUserDetailsRepository = iUserDetailsRepository;
		this.iOutletRepository = iOutletRepository;
		// this.iProductRepository = iProductRepository;
		this.iStockEntryLogRepository = iStockEntryLogRepository;
		this.userCredRepository = userCredRepository;
		this.iUserActivityRepository = iUserActivityRepository;
		this.iStockApprovalRepository = iStockApprovalRepository;
		this.iCounterStockManageRepository = iCounterStockManageRepository;
		this.iStockEntryDtlRepository = iStockEntryDtlRepository;
		this.iPocketMISRepository = iPocketMISRepository;
		this.s3StorageService = s3StorageService;
		// this.snap = snap;
	}

	public ResponseEntity<ApiResponse> validation(StockEntryVo input) {
		LOGGER.info("StockService :: save() called...");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);
		LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
		if (null == loginUserId || loginUserId == 0L) {
			loginUserId = input.getUserId().getId();
			LOGGER.info(" loginUserId from input =========== > " + loginUserId);
		}

		// add validations before stock entry
		ResponseEntity<ApiResponse> resp = validation(input, loginUserId);
		if (null != resp)
			return resp;
		else {
			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Valdation OK", null), HttpStatus.OK);
		}
	}

	@Transactional
	public ResponseEntity<ApiResponse> save(StockEntryVo input) {
		LOGGER.info("StockService :: save() called...");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		// TODO: temporary validation -- needs to be removed
		// if (!input.currentMonthStockEntry()) {
		// return new ResponseEntity<ApiResponse>(
		// new ApiResponse(406, "not_accptable",
		// "Only Current month data edit/delete is possible.", null),
		// HttpStatus.NOT_ACCEPTABLE);
		// }

		LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);
		if (null == loginUserId || loginUserId == 0L) {
			loginUserId = input.getUserId().getId();
			LOGGER.info(" loginUserId from input =========== > " + loginUserId);
		}

		// add validations before stock entry
		// ResponseEntity<ApiResponse> resp = validation(input, loginUserId);
		// if (null != resp)
		// return resp;

		if (null == input.getId()) {
			input.setCreatedBy(loginUserId);
			input.setCreatedOn(new Date());
			input.setTransactionDate((null != input.getTransactionDate()) ? input.getTransactionDate()
					: (null != input.getPoDate() ? input.getPoDate() : new Date()));
		}
		input.setUpdatedOn(new Date());
		input.setUpdatedBy(loginUserId);
		StockEntryVo savedData = iStockEntryRepository.save(input);
		savedData.setRemarks(input.getRemarks()); // To set in log file

		// To save log and activity register
		saveLogAndActivity(savedData, loginUserId);

		if (savedData != null) {
			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
					HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "Stock" });
		}
	}

	@Transactional
	public ResponseEntity<ApiResponse> draft(StockEntryVo input) {
		LOGGER.info("StockService :: draft() called...");

		try {
			// check if invoice number exists
			if (null != input.getInvoiceNo() && !input.getInvoiceNo().trim().isEmpty()) {
				List<String> inv = iStockEntryRepository.getInvoiceNo(input.getInvoiceNo().trim(), input.getOutletId());

				if (null == input.getId() && null != inv && inv.size() != 0) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(302, "Already_Exists",
							"Invoice Number Already exists in this outlet.", null), HttpStatus.FOUND);
				}
			} else if (null == input.getInvoiceNo() || input.getInvoiceNo().trim().isEmpty()) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(302, "entry_required", "Invoice Number is required.", null), HttpStatus.FOUND);
			} else if (null == input.getInvoice() || input.getInvoice().isEmpty()) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(302, "entry_required", "Invoice is required.", null), HttpStatus.FOUND);
			}

			if (null != input.getId()) {
				int resp = iFormMediaMappingRepository.deleteMedia("invoice", "invoice", input.getId());
				LOGGER.info(" deleteMedia response =========== > " + resp);
			} else {
				input.setCreatedBy(input.getCreatedBy());
				input.setCreatedOn(new Date());

				if (null != input.getPoDateStr()) {
					SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
					Date podate = obj.parse(input.getPoDateStr());
					input.setPoDate(podate);
					input.setTransactionDate(podate);
				} else { // for stock entry
					input.setTransactionDate(new Date());
				}
			}
			input.setUpdatedOn(new Date());
			input.setUpdatedBy(input.getUpdatedBy());
			// find objects
			if (null != input.getUserKey()) {
				UserDetailsVo user = iUserDetailsRepository.findById(input.getUserKey()).orElse(null);
				input.setUserId(user);
			}
			if (null != input.getOutletId()) {
				OutletVo outlet = iOutletRepository.findById(input.getOutletId()).orElse(null);
				input.setOutlet(outlet);
			}

			StockEntryVo savedData = iStockEntryRepository.save(input);
			if (savedData != null) {
				if (null != input.getInvoice()) {
					MultipartFile file = input.getInvoice();
					String filename = StringUtils.cleanPath(file.getOriginalFilename());
					String ext = FilenameUtils.getExtension(filename);
					filename = filename.replace("." + ext, "");
					filename = filename + "_" + System.currentTimeMillis() + "." + ext; // +"_"+savedData.getId()
					input.setInvoiceImageLink(filename); // invoiceUploadFolder +

					String s3InvoiceKeyPrefix = invoiceUploadFolder;
					FileResponse s3Response = s3StorageService.upload(new FileMetadata(file, filename),
							s3InvoiceKeyPrefix);

					String formType = "invoice";
					String tabName = "invoice";

					FormMediaMappingVo savedMediaData = new FormMediaMappingVo(null, savedData.getId(), formType,
							tabName, filename, s3Response.getFileUrl(), new Date());

					savedMediaData = iFormMediaMappingRepository.save(savedMediaData);
					if (savedMediaData != null) {
						LOGGER.info("StockEntry invoice Data saved in DB -----------" + savedMediaData);
					} else {
						LOGGER.info("StockEntry invoice Data NOT saved in DB -----------" + savedMediaData);
						throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "UserMediaData" });
					}
					savedData.setInvoice(null);
				}

			} else {
				throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "StockEntry" });
			}

			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", savedData),
					HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "StockEntry" });
	}

	public ResponseEntity<ApiResponse> searchByInput(StockSearchInputVo inputVo) {
		LOGGER.info("StockService::searchByInput::Entering...");
		ApiResponse apiResponse = null;
		List<StockEntryVo> getDataList = null;
		String whereClause = " ";
		String limitStr = "";
		if (null == inputVo.getPage() && null == inputVo.getSize()) {
			// do nothing
		} else {
			Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
			Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
			limitStr = " order by updated_on desc limit " + size + " offset " + page;
			LOGGER.info("limitStr..." + limitStr);
		}

		whereClause = getWhereClause(whereClause, inputVo);

		getDataList = iStockEntryRepository.searchByInput(whereClause, "stock_entry", limitStr);

		// To fetch total no of data which satisfy this where clause
		int totalNo = iStockEntryRepository.getTotalCountByInput(whereClause, "stock_entry").intValue();
		LOGGER.info("StockService::searchByInput::Exiting...");
		if (getDataList != null) {
			for (StockEntryVo each : getDataList) {
				if (each.getActivityType().equalsIgnoreCase("Purchase")) {
					List<FormMediaMappingVo> media = iFormMediaMappingRepository
							.findByFormTypeAndTabNameAndTransactionId("invoice", "invoice", each.getId());
					each.setInvoiceImageLink((null != media && media.size() > 0) ? media.get(0).getImageName() : null);
					Double totalAmt = each.getStockEntryDtlVo().stream()
							.mapToDouble(stockEntryDtlVo -> Double.parseDouble(stockEntryDtlVo.getAmount())).sum();
					each.setTotalAmountOfItem(totalAmt.toString());
				}
				if (each.getActivityType().equalsIgnoreCase("Sale")) {
					// need to fetch available stock for all products
				}
				if (null != each.getStockStatus() && (each.getStockStatus().contains("Approved"))) {
					List<String> adminRemarks = iStockApprovalRepository.findReferenceLog(each.getId());
					each.setAdminRemarks(adminRemarks);
				}
			}
			apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "stockService" });
		}
	}

	private String getWhereClause(String whereClause, StockSearchInputVo inputVo) {
		if (null != inputVo.getActivityType() && inputVo.getActivityType().size() > 0) {
			// whereClause += " and activity_type = '" + inputVo.getActivityType() + "'";
			String statusList = inputVo.getActivityType().toString().replace("[", "'").replace("]", "'")
					.replace(",", "','").replace("' ", "'");
			whereClause += " and activity_type in ( " + statusList + ") ";
		}

		if (null != inputVo.getTransactionType() && !inputVo.getTransactionType().trim().isEmpty()) {
			LOGGER.info("vo.getTransactionType() ------------ " + inputVo.getTransactionType());
			whereClause += " and transaction_type like '%" + inputVo.getTransactionType().trim() + "%' ";
		}

		if (null != inputVo.getUserId()) {
			whereClause += " and user_id = " + inputVo.getUserId();
		}

		if (null != inputVo.getOutlet()) {
			whereClause += " and outlet_id = " + inputVo.getOutlet();
		}

		if (null != inputVo.getInvoiceNo() && !inputVo.getInvoiceNo().trim().isEmpty()) {
			LOGGER.info("vo.getInvoiceNo() ------------ " + inputVo.getInvoiceNo());
			whereClause += " and invoice_no like '%" + inputVo.getInvoiceNo().trim() + "%' ";
		}

		if (null != inputVo.getStockStatus() && inputVo.getStockStatus().size() > 0) {
			LOGGER.info("vo.getStockStatus() ------------ " + inputVo.getStockStatus());
			// String inputStatusList = inputVo.getStockStatus().toString();
			String statusList = inputVo.getStockStatus().toString().replace("[", "'").replace("]", "'")
					.replace(",", "','").replace("' ", "'");
			whereClause += " and stock_status in ( " + statusList + ") ";
		}

		if (null != inputVo.getIsDamageReturn()) {
			whereClause += " and is_damage_return = " + inputVo.getIsDamageReturn();
		}

		if (null != inputVo.getPurchaseDate() && !inputVo.getPurchaseDate().trim().isEmpty()) {
			LOGGER.info("vo.getPurchaseDate() ------------ " + inputVo.getPurchaseDate());
			whereClause += " and DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m-%d') = DATE_FORMAT('"
					+ inputVo.getPurchaseDate() + "','%Y-%m-%d') ";
			/*
			 * whereClause += " and DATE_FORMAT(created_on,'%Y-%m-%d') = DATE_FORMAT('" +
			 * inputVo.getPurchaseDate() + "','%Y-%m-%d') ";
			 */
		}
		if (inputVo.getListingActivityType() != null && inputVo.getListingActivityType().equals("edit_list")) {
			if (null != inputVo.getFromDate() && !inputVo.getFromDate().trim().isEmpty() && null != inputVo.getToDate()
					&& !inputVo.getToDate().trim().isEmpty()) {
				LOGGER.info("vo.getToDate() ------------ " + inputVo.getToDate());
				whereClause += " and DATE_FORMAT((updated_on),'%Y-%m-%d') BETWEEN DATE_FORMAT('"
						+ inputVo.getFromDate() + "','%Y-%m-%d') " + "AND DATE_FORMAT('" + inputVo.getToDate()
						+ "','%Y-%m-%d') ";
				/*
				 * whereClause +=
				 * " and DATE_FORMAT(created_on,'%Y-%m-%d') BETWEEN DATE_FORMAT('" +
				 * inputVo.getFromDate() + "','%Y-%m-%d') " + "AND DATE_FORMAT('" +
				 * inputVo.getToDate() + "','%Y-%m-%d') ";
				 */
			}
		} else {
			if (null != inputVo.getFromDate() && !inputVo.getFromDate().trim().isEmpty() && null != inputVo.getToDate()
					&& !inputVo.getToDate().trim().isEmpty()) {
				LOGGER.info("vo.getToDate() ------------ " + inputVo.getToDate());
				whereClause += " and DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m-%d') BETWEEN DATE_FORMAT('"
						+ inputVo.getFromDate() + "','%Y-%m-%d') " + "AND DATE_FORMAT('" + inputVo.getToDate()
						+ "','%Y-%m-%d') ";
				/*
				 * whereClause +=
				 * " and DATE_FORMAT(created_on,'%Y-%m-%d') BETWEEN DATE_FORMAT('" +
				 * inputVo.getFromDate() + "','%Y-%m-%d') " + "AND DATE_FORMAT('" +
				 * inputVo.getToDate() + "','%Y-%m-%d') ";
				 */
			}
		}

		LOGGER.info("Whereclause..." + whereClause);
		return whereClause;
	}

	public StockEntryVo fetchById(Long id) {
		StockEntryVo data = iStockEntryRepository.findById(id).orElse(null);
		if (null != data) {
			List<FormMediaMappingVo> media = iFormMediaMappingRepository
					.findByFormTypeAndTabNameAndTransactionId("invoice", "invoice", data.getId());
			data.setInvoiceImageLink((null != media && media.size() > 0) ? media.get(0).getImageName() : null);
			data.setMediaFiles(media);
			if (null != data.getStockStatus() && (data.getStockStatus().contains("Approved"))) {
				List<String> adminRemarks = iStockApprovalRepository.findReferenceLog(id);
				data.setAdminRemarks(adminRemarks);
			}
		} else {
			new OjbException(ErrorCode.NOT_FOUND, new Object[] { "stockService" });
		}
		return data;
	}

	public ResponseEntity<ApiResponse> deleteItem(Long id) {
		LOGGER.info("StockService :: deleteItem() called...");

		try {
			iStockEntryDtlRepository.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "Stock" });
		}
		ApiResponse apiResponse = new ApiResponse(200, "success", "success", null, 0);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<ApiResponse> updatedApprovalStatus(StockApprovalInputVo input) {
		LOGGER.info("StockService :: updatedApprovalStatus() called...");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		String inputStatus = input.getApprovalStatus();
		String inputRemarks = input.getRemarks();
		Long stockId = input.getStockEntryVo().getId();
		LOGGER.info("StockService :: inputStatus ============ > " + inputStatus);

		try {

			// if any data is for prev adjustment but Stock is not in 'Submitted/Admin
			// approved', then can't approve
			Long stockEntryId = iStockEntryRepository.stockStatusCurrentPrevAdj(input.getOutletId());
			if (null == stockEntryId && input.getStockStatus().equalsIgnoreCase("prev_adj")) {
				ApiResponse apiResponse = new ApiResponse(406, "cannot_update",
						// "Currently this outlet stock in not 'Submitted/Admin approved', can't approve
						// this request.",
						"Please add current month stock to Approve this Edit Request or Currently this outlet stock in not 'Submitted/Admin approved', can't approve this request.",
						null, 0);
				return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
			}

			List<StockApprovalVo> getPendingData = iStockApprovalRepository
					.getPendingData(input.getStockEntryVo().getId());
			boolean isValidUser = false;
			if (null != getPendingData && getPendingData.size() > 0) {
				for (StockApprovalVo each : getPendingData) {
					// If login user is admin, and he approves
					if (loginUserId.equals(each.getAssignTo().getId())
							&& input.getUserType().equalsIgnoreCase("admin")) {

						// call Admin Approval method
						adminApproval(each, inputStatus, inputRemarks, loginUserId, stockId, stockEntryId,
								input.getOutletId(), input.getStockStatus());
						isValidUser = true;
						break;
					} else if (loginUserId.equals(each.getAssignTo().getId())
							&& !input.getUserType().equalsIgnoreCase("admin")) {
						// If login user is NOT admin, then
						// update status here
						if (loginUserId.equals(each.getAssignTo().getId())
								&& input.getUserType().equalsIgnoreCase("BDE")) {
							int stockApproval = iStockApprovalRepository.updateApprovalStatus(each.getId(), inputStatus,
									inputRemarks, new Date(), loginUserId);
							if (stockApproval > 0) {
								int stockntryStatus = iStockEntryRepository.updateStockStatus(
										input.getStockEntryVo().getId(), "BDE " + inputStatus, new Date(),
										loginUserId);
								LOGGER.info("StockService :: updatedApprovalStatus() stockntryStatus called..."
										+ stockntryStatus);
							}
							isValidUser = true;

						} else {
							int stockApproval = iStockApprovalRepository.updateApprovalStatus(each.getId(), inputStatus,
									inputRemarks, new Date(), loginUserId);
							if (stockApproval > 0) {
								int stockntryStatus = iStockEntryRepository.updateStockStatus(
										input.getStockEntryVo().getId(), "First Level " + inputStatus, new Date(),
										loginUserId);
								LOGGER.info("StockService :: updatedApprovalStatus() stockntryStatus called..."
										+ stockntryStatus);
							}
							isValidUser = true;

						}

					}
				}
				if (!isValidUser) {
					ApiResponse apiResponse = new ApiResponse(406, "not_valid_user",
							"Not valid user to " + inputStatus + " this Edit request.", null, 0);
					return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
				}
			} else {
				ApiResponse apiResponse = new ApiResponse(302, "already_" + inputStatus,
						"This Edit request is already " + inputStatus, null, 0);
				return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "Stock" });
		}
		ApiResponse apiResponse = new ApiResponse(200, "success", "success", null, 0);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@Transactional
	private void adminApproval(StockApprovalVo each, String inputStatus, String inputRemarks, Long loginUserId,
			Long stockId, Long stockEntryId, Long outletId, String stockStatus) {
		LOGGER.info("adminApproval inputStatus ================== > " + inputStatus);
		// update status here
		int stockApproval = iStockApprovalRepository.updateApprovalStatus(each.getId(), inputStatus, inputRemarks,
				new Date(), loginUserId);
		if (stockApproval > 0) {
			int stockEntryStatus = iStockEntryRepository.updateStockStatus(stockId, "Admin " + inputStatus, new Date(),
					loginUserId);
			LOGGER.info("StockService :: updatedApprovalStatus() stockEntryStatus called..." + stockEntryStatus);
		}

		// Update Stock entry details table if approved
		if (inputStatus.contains("Approved")) {
			// Update pocket MIS
			// need to do it before update the amount, otherwise MIS values will not be
			// calculated
			// properly
			StockEntryVo vo = iStockEntryRepository.getById(stockId);
			String activity = vo.getActivityType();
			// BA_Activity_Enum act = findActivity(activity, vo.getStockStatus());
			// stockStatus = (vo.getStockStatus().equals("BDE Approved") || ("BDE
			// Rejected")) ? "Edit" : vo.getStockStatus();
			stockStatus = (vo.getStockStatus().equals("BDE Approved") || vo.getStockStatus().equals("BDE Rejected"))
					? "Edit"
					: vo.getStockStatus();

			BA_Activity_Enum act = findActivity(activity, stockStatus);
			// BA_Activity_Enum act = activity.equalsIgnoreCase("sale") ?
			// BA_Activity_Enum.sale_edit
			// : activity.equalsIgnoreCase("purchase") ? BA_Activity_Enum.purchase_edit :
			// null; //??????

			// ========= Update pocket MIS on Approval
			updatePocketMIS(vo, loginUserId, act, false);

			// Then update data in stock detail table
			int stockDataUpdate = iStockEntryRepository.updateStockEntry(stockId);
			LOGGER.info("StockService :: updatedApprovalStatus() stockDataUpdate called..." + stockDataUpdate);
			int stockDtlDataUpdate = iStockEntryDtlRepository.updateStockEntryDtl(stockId);
			LOGGER.info("StockService :: updatedApprovalStatus() stockDtlDataUpdate called..." + stockDtlDataUpdate);

			// Also update the stock entry for current month year for prev adjustment
			if (null != stockEntryId && stockStatus.equalsIgnoreCase("prev_adj")) {
				String responseFromStoredProc = iStockEntryRepository.updateCurrentStockEntryForAdjustment(stockId,
						outletId);
				LOGGER.info("StockService :: updatedApprovalStatus() responseFromStoredProc called..."
						+ responseFromStoredProc);
			}
		}
	}

	public ResponseEntity<ApiResponse> pocketMIS(Long outletId, String monthYr) {
		LOGGER.info("StockService :: pocketMIS() called...");

		List<PocketMISDto> overall = new ArrayList<PocketMISDto>();
		overall = iPocketMISRepository.findOverallPocketMISOfOutlet(outletId, monthYr);
		LOGGER.info("Pocket MIS: " + overall);
		PocketMISOPDto res = new PocketMISOPDto();
		res.setOverall(overall);

		List<PocketMISDto> categories = new ArrayList<PocketMISDto>();
		categories = iPocketMISRepository.findCategoryPocketMISOfOutlet(outletId, monthYr);
		LOGGER.info("POcket MIS: " + categories);
		res.setCategories(categories);

		List<PocketMISDto> allDetails = new ArrayList<PocketMISDto>();
		allDetails = iPocketMISRepository.findPocketMISOfOutlet(outletId, monthYr);
		res.setAllDetails(allDetails);
		LOGGER.info("POcket MIS: " + allDetails);

		ApiResponse apiResponse = new ApiResponse(200, "success", "success", res, allDetails.size());
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	private void saveLogAndActivity_v1(StockEntryVo input, Long loginUserId) {
		// For 'Sent for approval / Edit request'
		if (null != input.getStockStatus()
				&& (input.getStockStatus().equals("Edit") || input.getStockStatus().equals("Delete"))) {
			// create a log data
			String remarks = (null != input.getRemarks()) ? input.getRemarks()
					: ("Stock " + input.getStockStatus() + " remarks.");
			StockEntryLogVo logged = iStockEntryLogRepository
					.save(new StockEntryLogVo(input, input.getStockStatus(), remarks, new Date(), loginUserId));
			LOGGER.info("Data saved in Log table == > " + logged);

			// create a activity Data
			BA_Activity_Enum activityName = null;
			if (null != input.getActivityType() && "Purchase".equals(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.purchase_edit;
			} else if (null != input.getActivityType() && "Sale".equals(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.sale_edit;
			} else if (null != input.getActivityType() && "Stock".equals(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.stock_edit;
			}
			UserActivityRegisterVo activity = iUserActivityRepository.save(new UserActivityRegisterVo(activityName,
					new Date(), input.getOutlet(), input.getTotalAmountOfItemUpdated(), loginUserId));
			activity.setStockReferenceId(input.getId());
			LOGGER.info("Data saved in UserActivityRegister table == > " + activity);

			// Update pocket MIS ==== PENDING ========== ?????????????? == DONT CALL FROM
			// HERE ==
			// ????????????????????
			// updatePocketMIS(input, loginUserId, activityName, false);

			// send for approval
			List<StockApprovalVo> editRequest = new ArrayList<StockApprovalVo>();
			List<UserDetailsVo> assignTo = iUserDetailsRepository.findUserForEditApproval(loginUserId);
			for (UserDetailsVo each : assignTo) {
				StockApprovalVo data = new StockApprovalVo(input, "Pending Approval", new Date(), loginUserId,
						new Date(), loginUserId, each);

				editRequest.add(data);
			}
			List<StockApprovalVo> editRequestSaved = iStockApprovalRepository.saveAll(editRequest);
			LOGGER.info("Data saved in Stock Approval table == > " + editRequestSaved);
		} else {
			// create a activity Data
			BA_Activity_Enum activityName = null;
			if (null != input.getActivityType() && "Purchase".equalsIgnoreCase(input.getActivityType())) {
				activityName = (input.getStockStatus().equals("Draft")) ? Constants.BA_Activity_Enum.purchase_draft
						: Constants.BA_Activity_Enum.purchase_entry;
			} else if (null != input.getActivityType() && "Sale".equalsIgnoreCase(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.sale_entry;
			} else if (null != input.getActivityType() && "Stock".equalsIgnoreCase(input.getActivityType())) {
				activityName = (input.getStockStatus().equals("Draft")) ? Constants.BA_Activity_Enum.stock_draft
						: Constants.BA_Activity_Enum.stock_entry;
				// activityName = Constants.BA_Activity_Enum.stock_entry;
			} else if (null != input.getActivityType() && "Damage".equalsIgnoreCase(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.damage_entry;
			} else if (null != input.getActivityType() && "purchase_return".equalsIgnoreCase(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.purchase_return;
			} else if (null != input.getActivityType() && "sale_return".equalsIgnoreCase(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.sale_return;
			}
			UserActivityRegisterVo activity = iUserActivityRepository.save(new UserActivityRegisterVo(activityName,
					new Date(), input.getOutlet(), input.getTotalAmountOfItem(), loginUserId));
			activity.setStockReferenceId(input.getId());
			LOGGER.info("Data saved in UserActivityRegister table == > " + activity);

			if (null != input.getStockStatus() && !input.getStockStatus().equals("Draft")) {
				updatePocketMIS(input, loginUserId, activityName, false).execute();
			}
		}
	}

	public CounterStockEntry updatePocketMIS(StockEntryVo input, Long loginUserId, BA_Activity_Enum activityName,
			boolean stockAdjust) {
		List<CounterStockManageVo> dataList = new ArrayList<CounterStockManageVo>();

		// if(activityName.equals(BA_Activity_Enum.purchase_entry)) {
		// return new PurchaseEntry(input, loginUserId, iCounterStockManageRepository);
		// }
		if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.stock_entry.name())) {
			// insert data in counter_stock_manage
			dataList = stockEntry(input, loginUserId, dataList);
		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.sale_entry.name())
				|| activityName.name().equalsIgnoreCase(BA_Activity_Enum.prev_sale_adj.name())) {
			// update data in counter_stock_manage
			dataList = saleEntry(input, loginUserId, dataList);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.purchase_return.name())) {
			// update data in counter_stock_manage
			dataList = purchaseReturn(input, loginUserId, dataList);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.purchase_entry.name())) {
			// update data in counter_stock_manage
			dataList = purchaseEntry(input, loginUserId, dataList, stockAdjust);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.prev_purchase_adj.name())) {
			// update data in counter_stock_manage
			dataList = purchaseEntryPrevAdjust(input, loginUserId, dataList);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.sale_return.name())) {
			// update data in counter_stock_manage
			dataList = saleReturn(input, loginUserId, dataList);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.stock_edit.name())) {
			// update data in counter_stock_manage == TESTING PENDING ====
			// dataList = stockEdit(input, loginUserId, false);
			dataList = stockEditMonthYr(input, loginUserId, false);
		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.sale_edit.name())) {
			// update data in counter_stock_manage
			dataList = saleEdit(input, loginUserId, false);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.purchase_edit.name())) {
			// update data in counter_stock_manage
			dataList = purchaseEdit(input, loginUserId, false);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.damage_entry.name())) {
			// update data in counter_stock_manage
			dataList = damageEntry(input, loginUserId, dataList);
		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.sale_for_adj.name())) {
			// update data in counter_stock_manage
			dataList = saleEntryForAdj(input, loginUserId, dataList);
		}

		List<CounterStockManageVo> dataListSaved = iCounterStockManageRepository.saveAll(dataList);
		LOGGER.info("========= Stock dataListSaved ======= >" + dataListSaved.size());

		/*
		 * if(stockAdjust) {
		 * ModelMapper modelMapper = new ModelMapper();
		 * List<CounterStockManageSnapVo> dataListSnap = new
		 * ArrayList<CounterStockManageSnapVo>();
		 * for(CounterStockManageVo each : dataList) {
		 * CounterStockManageSnapVo as = modelMapper.map(each,
		 * CounterStockManageSnapVo.class);
		 * dataListSnap.add(as);
		 * }
		 * List<CounterStockManageSnapVo> dataListSaved = snap.saveAll(dataListSnap);
		 * LOGGER.info("========= Stock dataListSaved in SNAP======= >" +
		 * dataListSaved.size());
		 * } else {
		 * List<CounterStockManageVo> dataListSaved =
		 * iCounterStockManageRepository.saveAll(dataList);
		 * LOGGER.info("========= Stock dataListSaved ======= >" +
		 * dataListSaved.size());
		 * }
		 */

		return null;
	}

	public CounterStockEntry updatePocketMISAlt(StockEntryVo input, Long loginUserId, BA_Activity_Enum activityName,
			boolean stockAdjust) {
		List<CounterStockManageVo> dataList = new ArrayList<CounterStockManageVo>();

		// if(activityName.equals(BA_Activity_Enum.purchase_entry)) {
		// return new PurchaseEntry(input, loginUserId, iCounterStockManageRepository);
		// }
		if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.stock_entry.name())) {
			// insert data in counter_stock_manage
			dataList = stockEntry(input, loginUserId, dataList, new Date(2024, Calendar.MARCH, 1));
		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.sale_entry.name())
				|| activityName.name().equalsIgnoreCase(BA_Activity_Enum.prev_sale_adj.name())) {
			// update data in counter_stock_manage
			dataList = saleEntry(input, loginUserId, dataList);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.purchase_return.name())) {
			// update data in counter_stock_manage
			dataList = purchaseReturn(input, loginUserId, dataList);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.purchase_entry.name())) {
			// update data in counter_stock_manage
			dataList = purchaseEntry(input, loginUserId, dataList, stockAdjust);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.prev_purchase_adj.name())) {
			// update data in counter_stock_manage
			dataList = purchaseEntryPrevAdjust(input, loginUserId, dataList);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.sale_return.name())) {
			// update data in counter_stock_manage
			dataList = saleReturn(input, loginUserId, dataList);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.stock_edit.name())) {
			// update data in counter_stock_manage == TESTING PENDING ====
			// dataList = stockEdit(input, loginUserId, false);
			dataList = stockEditMonthYr(input, loginUserId, false);
		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.sale_edit.name())) {
			// update data in counter_stock_manage
			dataList = saleEdit(input, loginUserId, false);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.purchase_edit.name())) {
			// update data in counter_stock_manage
			dataList = purchaseEdit(input, loginUserId, false);

		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.damage_entry.name())) {
			// update data in counter_stock_manage
			dataList = damageEntry(input, loginUserId, dataList);
		} else if (activityName.name().equalsIgnoreCase(BA_Activity_Enum.sale_for_adj.name())) {
			// update data in counter_stock_manage
			dataList = saleEntryForAdj(input, loginUserId, dataList);
		}

		List<CounterStockManageVo> dataListSaved = iCounterStockManageRepository.saveAll(dataList);
		LOGGER.info("========= Stock dataListSaved ======= >" + dataListSaved.size());

		/*
		 * if(stockAdjust) {
		 * ModelMapper modelMapper = new ModelMapper();
		 * List<CounterStockManageSnapVo> dataListSnap = new
		 * ArrayList<CounterStockManageSnapVo>();
		 * for(CounterStockManageVo each : dataList) {
		 * CounterStockManageSnapVo as = modelMapper.map(each,
		 * CounterStockManageSnapVo.class);
		 * dataListSnap.add(as);
		 * }
		 * List<CounterStockManageSnapVo> dataListSaved = snap.saveAll(dataListSnap);
		 * LOGGER.info("========= Stock dataListSaved in SNAP======= >" +
		 * dataListSaved.size());
		 * } else {
		 * List<CounterStockManageVo> dataListSaved =
		 * iCounterStockManageRepository.saveAll(dataList);
		 * LOGGER.info("========= Stock dataListSaved ======= >" +
		 * dataListSaved.size());
		 * }
		 */

		return null;
	}

	private List<CounterStockManageVo> stockEntry(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setOpeningStock((null != mis.getOpeningStock()) ? mis.getOpeningStock() + dtl.getNoOfPcs()
							: dtl.getNoOfPcs());
					mis.setOpeningStockAmount((null != mis.getOpeningStockAmount()) ? String.valueOf(
							Double.parseDouble(mis.getOpeningStockAmount()) + Double.parseDouble(dtl.getAmount()))
							: dtl.getAmount());

					/*
					 * mis.setPurchase( (null != mis.getPurchase()) ? mis.getPurchase() +
					 * dtl.getNoOfPcs() : dtl.getNoOfPcs()); mis.setPurchaseAmount((null !=
					 * mis.getPurchaseAmount()) ? String.valueOf(
					 * Double.parseDouble(mis.getPurchaseAmount()) +
					 * Double.parseDouble(dtl.getAmount())) : dtl.getAmount());
					 */

					mis.setClosingStock(mis.getOpeningStock() + mis.getPurchase() + mis.getSaleReturn() - mis.getSale()
							- (mis.getDamage() == null ? 0 : mis.getDamage()) - mis.getPurchaseReturn());
					mis.setClosingStockAmount(String.valueOf(Double.parseDouble(mis.getOpeningStockAmount())
							+ Double.parseDouble(mis.getPurchaseAmount())
							+ Double.parseDouble(mis.getSaleReturnAmount()) - Double.parseDouble(mis.getSaleAmount())
							- Double.parseDouble(mis.getDamageAmount() == null ? "0" : mis.getDamageAmount())
							- Double.parseDouble(mis.getPurchaseReturnAmount())));
					dataList.add(mis);
					match = true;
					break;
				}
			}
			if (!match) {
				LOGGER.info(dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(),
						dtl.getNoOfPcs(), 0L, 0L, dtl.getNoOfPcs(), dtl.getAmount(), "0", "0", dtl.getAmount(), "user",
						dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(), loginUserId, new Date(),
						loginUserId, 0L, 0L, "0", "0", input.getTransactionDate());
				dataList.add(each);
			}
		}
		return dataList;
	}

	private List<CounterStockManageVo> stockEntry(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList, Date date) {
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutlet(input.getOutlet().getId(), date);

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setOpeningStock((null != mis.getOpeningStock()) ? mis.getOpeningStock() + dtl.getNoOfPcs()
							: dtl.getNoOfPcs());
					mis.setOpeningStockAmount((null != mis.getOpeningStockAmount()) ? String.valueOf(
							Double.parseDouble(mis.getOpeningStockAmount()) + Double.parseDouble(dtl.getAmount()))
							: dtl.getAmount());

					/*
					 * mis.setPurchase( (null != mis.getPurchase()) ? mis.getPurchase() +
					 * dtl.getNoOfPcs() : dtl.getNoOfPcs()); mis.setPurchaseAmount((null !=
					 * mis.getPurchaseAmount()) ? String.valueOf(
					 * Double.parseDouble(mis.getPurchaseAmount()) +
					 * Double.parseDouble(dtl.getAmount())) : dtl.getAmount());
					 */

					mis.setClosingStock(mis.getOpeningStock() + mis.getPurchase() + mis.getSaleReturn() - mis.getSale()
							- (mis.getDamage() == null ? 0 : mis.getDamage()) - mis.getPurchaseReturn());
					mis.setClosingStockAmount(String.valueOf(Double.parseDouble(mis.getOpeningStockAmount())
							+ Double.parseDouble(mis.getPurchaseAmount())
							+ Double.parseDouble(mis.getSaleReturnAmount()) - Double.parseDouble(mis.getSaleAmount())
							- Double.parseDouble(mis.getDamageAmount() == null ? "0" : mis.getDamageAmount())
							- Double.parseDouble(mis.getPurchaseReturnAmount())));
					dataList.add(mis);
					match = true;
					break;
				}
			}
			if (!match) {
				LOGGER.info(dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(),
						dtl.getNoOfPcs(), 0L, 0L, dtl.getNoOfPcs(), dtl.getAmount(), "0", "0", dtl.getAmount(), "user",
						dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(), loginUserId, new Date(),
						loginUserId, 0L, 0L, "0", "0", input.getTransactionDate());
				dataList.add(each);
			}
		}
		return dataList;
	}

	private List<CounterStockManageVo> saleEntry(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {

		List<Long> products = input.getStockEntryDtlVo().stream().map(e -> e.getProductId().getId())
				.collect(Collectors.toList());

		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutletAndProducts(input.getOutlet().getId(), input.getTransactionDate(), products);

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					LOGGER.info("Sold product ------ > " + dtl.getProductId().getId());

					if (input.previousMonthTxnCheck()) {
						mis.setSale((null != mis.getSale()) ? mis.getSale() + dtl.getNoOfPcsUpdated()
								: dtl.getNoOfPcsUpdated());

						mis.setSaleAmount((null != mis.getSaleAmount()) ? String.valueOf(
								Double.parseDouble(mis.getSaleAmount()) + Double.parseDouble(dtl.getAmountUpdated()))
								: dtl.getAmountUpdated());
						mis.setAdjustedNoOfPcs(dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated());
						mis.setAdjustedAmount(
								Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated()));
						mis.setAdjustedBy(mis.getUpdatedBy());
						mis.setAdjustedOn(new Date());
					} else {
						mis.setSale((null != mis.getSale()) ? mis.getSale() + dtl.getNoOfPcs() : dtl.getNoOfPcs());

						mis.setSaleAmount((null != mis.getSaleAmount())
								? String.valueOf(
										Double.parseDouble(mis.getSaleAmount()) + Double.parseDouble(dtl.getAmount()))
								: dtl.getAmount());
					}
					mis.setClosingStock(mis.getClosingStockCalc());
					mis.setClosingStockAmount(mis.getClosingStockAmountCalc());

					match = true;
					dataList.add(mis);
					break;
				}
			}
			if (!match) {
				CounterStockManageVo each = new CounterStockManageVo().setOutlet(input.getOutlet())
						.setProductId(dtl.getProductId()).setSale(dtl.getNoOfPcs())
						.setSaleAmount(dtl.getAmount()).setCategoryName(dtl.getCategoryName())
						.setSubCategoryName(dtl.getSubCategoryName()).setTransactionDate(input.getTransactionDate())
						.setCreatedBy(loginUserId).setUpdatedBy(loginUserId).setUpdateType("user");

				if (input.previousMonthTxnCheck()) {
					each = new CounterStockManageVo().setOutlet(input.getOutlet())
							.setProductId(dtl.getProductId()).setSale(dtl.getNoOfPcsUpdated())
							.setSaleAmount(dtl.getAmountUpdated()).setCategoryName(dtl.getCategoryName())
							.setSubCategoryName(dtl.getSubCategoryName()).setTransactionDate(input.getTransactionDate())
							.setCreatedBy(loginUserId).setUpdatedBy(loginUserId).setUpdateType("user");
					each.setAdjustedNoOfPcs(dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated())
							.setAdjustedAmount(
									Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated()))
							.setAdjustedOn(new Date());
				}

				dataList.add(each);

			}
		}
		if (input.previousMonthTxnCheck()) {
			List<CounterStockManageVo> currentMonData = prevMonthStockAdjust(dataList, products,
					input.getOutlet().getId(), loginUserId, "sale");
			dataList.addAll(currentMonData);
		}
		return dataList;
	}

	private List<CounterStockManageVo> purchaseReturn(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					LOGGER.info("Sold product ------ > " + dtl.getProductId().getId());
					mis.setPurchaseReturn((null != mis.getPurchaseReturn()) ? mis.getPurchaseReturn() + dtl.getNoOfPcs()
							: dtl.getNoOfPcs());
					mis.setPurchaseReturnAmount((null != mis.getPurchaseReturnAmount()) ? String.valueOf(
							Double.parseDouble(mis.getPurchaseReturnAmount()) + Double.parseDouble(dtl.getAmount()))
							: dtl.getAmount());

					mis.setClosingStock(mis.getOpeningStock() + mis.getPurchase() + mis.getSaleReturn() - mis.getSale()
							- (mis.getDamage() == null ? 0 : mis.getDamage()) - mis.getPurchaseReturn());
					mis.setClosingStockAmount(String.valueOf(Double.parseDouble(mis.getOpeningStockAmount())
							+ Double.parseDouble(mis.getPurchaseAmount())
							+ Double.parseDouble(mis.getSaleReturnAmount()) - Double.parseDouble(mis.getSaleAmount())
							- Double.parseDouble(mis.getDamageAmount() == null ? "0" : mis.getDamageAmount())
							- Double.parseDouble(mis.getPurchaseReturnAmount())));
					dataList.add(mis);
					// continue;
				}
			}
		}
		return dataList;
	}

	private List<CounterStockManageVo> purchaseEntry_v1(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setPurchase(
							(null != mis.getPurchase()) ? mis.getPurchase() + dtl.getNoOfPcs() : dtl.getNoOfPcs());
					mis.setPurchaseAmount((null != mis.getPurchaseAmount())
							? String.valueOf(
									Double.parseDouble(mis.getPurchaseAmount()) + Double.parseDouble(dtl.getAmount()))
							: dtl.getAmount());

					mis.setClosingStock(mis.getOpeningStock() + mis.getPurchase() + mis.getSaleReturn() - mis.getSale()
							- (mis.getDamage() == null ? 0 : mis.getDamage()) - mis.getPurchaseReturn());
					mis.setClosingStockAmount(String.valueOf(Double.parseDouble(mis.getOpeningStockAmount())
							+ Double.parseDouble(mis.getPurchaseAmount())
							+ Double.parseDouble(mis.getSaleReturnAmount()) - Double.parseDouble(mis.getSaleAmount())
							- Double.parseDouble(mis.getDamageAmount() == null ? "0" : mis.getDamageAmount())
							- Double.parseDouble(mis.getPurchaseReturnAmount())));
					dataList.add(mis);
					match = true;
					break;
				}
			}
			if (!match) {
				LOGGER.info(dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L,
						dtl.getNoOfPcs(), 0L, dtl.getNoOfPcs(), "0", dtl.getAmount(), "0", dtl.getAmount(), "user",
						dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(), loginUserId, new Date(),
						loginUserId, 0L, 0L, "0", "0", null);
				dataList.add(each);
			}
		}
		return dataList;
	}

	private List<CounterStockManageVo> saleReturn(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setSaleReturn(
							(null != mis.getSaleReturn()) ? mis.getSaleReturn() + dtl.getNoOfPcs() : dtl.getNoOfPcs());
					mis.setSaleReturnAmount((null != mis.getSaleReturnAmount())
							? String.valueOf(
									Double.parseDouble(mis.getSaleReturnAmount()) + Double.parseDouble(dtl.getAmount()))
							: dtl.getAmount());

					mis.setClosingStock(mis.getOpeningStock() + mis.getPurchase() + mis.getSaleReturn() - mis.getSale()
							- (mis.getDamage() == null ? 0 : mis.getDamage()) - mis.getPurchaseReturn());
					mis.setClosingStockAmount(String.valueOf(Double.parseDouble(mis.getOpeningStockAmount())
							+ Double.parseDouble(mis.getPurchaseAmount())
							+ Double.parseDouble(mis.getSaleReturnAmount()) - Double.parseDouble(mis.getSaleAmount())
							- Double.parseDouble(mis.getDamageAmount() == null ? "0" : mis.getDamageAmount())
							- Double.parseDouble(mis.getPurchaseReturnAmount())));
					dataList.add(mis);
					match = true;
					break;
				}
			}
			if (!match) {
				LOGGER.info(dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L, 0L, 0L,
						dtl.getNoOfPcs(), "0", "0", "0", dtl.getAmount(), "user", dtl.getCategoryName(),
						dtl.getSubCategoryName(), new Date(), loginUserId, new Date(), loginUserId, 0L,
						dtl.getNoOfPcs(), "0", dtl.getAmount(), input.getTransactionDate());
				dataList.add(each);
			}
		}
		return dataList;
	}

	private List<CounterStockManageVo> saleEdit(StockEntryVo input, Long loginUserId, boolean validation) {

		List<CounterStockManageVo> dataList = new ArrayList<>();

		List<Long> products = input.getStockEntryDtlVo().stream().map(e -> e.getProductId().getId())
				.collect(Collectors.toList());
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutletAndProducts(input.getOutlet().getId(), input.getTransactionDate(), products);

		List<CounterStockManageVo> dbData2 = existingData.stream().map(SerializationUtils::clone)
				.collect(Collectors.toList());
		// boolean match = false;
		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			LOGGER.info("dtl.getProductId().getId() == >" + dtl.getProductId().getId());
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				LOGGER.info("existingData.getProductId().getId() == >" + mis.getProductId().getId());
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setAdjustedNoOfPcs(dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated());
					mis.setAdjustedAmount(
							Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated()));
					mis.setAdjustedBy(mis.getUpdatedBy());
					mis.setAdjustedOn(new Date());
					mis.setSale(mis.getSale() - (dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated())); // 3 + (3 - 2) // this
																								// was + before 22/12/23
					// this was before -- 22/12/23
					mis.setSaleAmount(String.valueOf(Double.parseDouble(mis.getSaleAmount())
							- (Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated()))));
					mis.setClosingStock(mis.getClosingStockCalc());
					mis.setClosingStockAmount(mis.getClosingStockAmountCalc());
					dataList.add(mis);
					match = true;
					// break;
				}

			}
			if (!match) {
				LOGGER.info("New product - >" + dtl.getProductId().getId());
				CounterStockManageVo stock = new CounterStockManageVo().setOutlet(input.getOutlet())
						.setProductId(dtl.getProductId()).setCategoryName(dtl.getCategoryName())
						.setSubCategoryName(dtl.getSubCategoryName()).setSale(dtl.getNoOfPcsUpdated())
						.setSaleAmount(dtl.getAmountUpdated()).setClosingStock(dtl.getNoOfPcsUpdated())
						.setClosingStockAmount(dtl.getAmountUpdated()).setUpdateType("user")
						.setUpdatedOn(new Date()).setUpdatedBy(loginUserId).setCreatedBy(loginUserId)
						.setCreatedOn(new Date());
				dataList.add(stock);
			}
		}
		// boolean isNegativeStock = dataList.stream().filter(o -> o.getClosingStock()
		// <= 0).findFirst().isPresent();
		if (validation) {
			boolean isNegativeStock = dataList.stream().anyMatch(o -> o.getClosingStock() <= 0);
			if (isNegativeStock) {
				// only return negative data
				return dataList.stream().filter(o -> o.getClosingStock() <= 0).collect(Collectors.toList());
			}
			// List<CounterStockManageVo> dbData = iCounterStockManageRepository
			// .findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
			return dbData2;
		}
		if (input.previousMonthTxnCheck()) {
			// call stock update for current month
			// prevMonthStockAdjust( dataList, products, input.getOutlet().getId(),
			// loginUserId);
			List<CounterStockManageVo> prevdata = prevMonthStockAdjust(dataList, products, input.getOutlet().getId(),
					loginUserId, "sale");
			dataList.addAll(prevdata);
		}
		return dataList;
	}

	private List<CounterStockManageVo> purchaseEdit_old(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {
		// List<CounterStockManageVo> existingData = iCounterStockManageRepository
		// .findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
		// for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
		// boolean match = false;
		// for (CounterStockManageVo mis : existingData) {
		// if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
		// mis.setPurchase(mis.getPurchase() - (dtl.getNoOfPcs() -
		// dtl.getNoOfPcsUpdated()));
		// mis.setPurchaseAmount(String.valueOf(Double.parseDouble(mis.getPurchaseAmount())
		// - (Double.parseDouble(dtl.getAmount()) -
		// Double.parseDouble(dtl.getAmountUpdated()))));
		//
		// mis.setClosingStock(mis.getOpeningStock() + mis.getPurchase() +
		// mis.getSaleReturn() - mis.getSale()
		// - (mis.getDamage() == null ? 0 : mis.getDamage()) - mis.getPurchaseReturn());
		// mis.setClosingStockAmount(String.valueOf(Double.parseDouble(mis.getOpeningStockAmount())
		// + Double.parseDouble(mis.getPurchaseAmount())
		// + Double.parseDouble(mis.getSaleReturnAmount()) -
		// Double.parseDouble(mis.getSaleAmount())
		// - Double.parseDouble(mis.getDamageAmount() == null ? "0" :
		// mis.getDamageAmount())
		// - Double.parseDouble(mis.getPurchaseReturnAmount())));
		// dataList.add(mis);
		// match = true;
		// }
		// }
		// if (!match) {
		// LOGGER.info("New product - >" + dtl.getProductId().getId());
		// CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(),
		// dtl.getProductId(), 0L,
		// dtl.getNoOfPcsUpdated(), 0L, dtl.getNoOfPcsUpdated(), "0",
		// dtl.getAmountUpdated(), "0",
		// dtl.getAmountUpdated(), "user", dtl.getCategoryName(),
		// dtl.getSubCategoryName(), new Date(),
		// loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0");
		// dataList.add(each);
		// }
		// }
		return dataList;
	}

	private List<CounterStockManageVo> purchaseEdit_v1(StockEntryVo input, Long loginUserId, boolean validation) {
		List<CounterStockManageVo> dataList = new ArrayList<CounterStockManageVo>();
		List<CounterStockManageVo> existingData = new ArrayList<CounterStockManageVo>();
		Date transDate = input.getTransactionDate();
		// if previous month purchase edit, then search data of previous month
		// boolean ifPrevMonthTransaction = ifPrevMonthTransaction(transDate);

		// existingData =
		// iCounterStockManageRepository.findPocketMISByDateOutlet(input.getOutlet().getId(),
		// new Date());
		existingData = iCounterStockManageRepository.findPocketMISByDateOutlet(input.getOutlet().getId(), transDate);

		List<CounterStockManageVo> dbData2 = existingData.stream().map(SerializationUtils::clone)
				.collect(Collectors.toList());

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setPurchase(mis.getPurchase() - (dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated()));
					mis.setPurchaseAmount(String.valueOf(Double.parseDouble(mis.getPurchaseAmount())
							- (Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated()))));

					mis.setClosingStock(mis.getOpeningStock() + mis.getPurchase() + mis.getSaleReturn() - mis.getSale()
							- (mis.getDamage() == null ? 0 : mis.getDamage()) - mis.getPurchaseReturn());
					mis.setClosingStockAmount(String.valueOf(Double.parseDouble(mis.getOpeningStockAmount())
							+ Double.parseDouble(mis.getPurchaseAmount())
							+ Double.parseDouble(mis.getSaleReturnAmount()) - Double.parseDouble(mis.getSaleAmount())
							- Double.parseDouble(mis.getDamageAmount() == null ? "0" : mis.getDamageAmount())
							- Double.parseDouble(mis.getPurchaseReturnAmount())));
					dataList.add(mis);
					match = true;
				}
			}
			if (!match) {
				LOGGER.info("New product - >" + dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L,
						dtl.getNoOfPcsUpdated(), 0L, dtl.getNoOfPcsUpdated(), "0", dtl.getAmountUpdated(), "0",
						dtl.getAmountUpdated(), "user", dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(),
						loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0", null);
				dataList.add(each);
			}
		}
		// boolean isNegativeStock = dataList.stream().filter(o -> o.getClosingStock()
		// <= 0).findFirst().isPresent();
		if (validation) {
			boolean isNegativeStock = dataList.stream().anyMatch(o -> o.getClosingStock() <= 0);
			if (isNegativeStock) {
				// only return negative data
				return dataList.stream().filter(o -> o.getClosingStock() <= 0).collect(Collectors.toList());
			}
			// List<CounterStockManageVo> dbData = iCounterStockManageRepository
			// .findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
			return dbData2;
		}
		// If previous month purchase, then stock transfer needed
		if (input.previousMonthTxnCheck()) {
			// nirjan help
		}
		return dataList;
	}

	private List<CounterStockManageVo> damageEntry(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					LOGGER.info("Damage product ------ > " + dtl.getProductId().getId());
					mis.setDamage((null != mis.getDamage()) ? mis.getDamage() + dtl.getNoOfPcs() : dtl.getNoOfPcs());
					mis.setDamageAmount((null != mis.getDamageAmount())
							? String.valueOf(
									Double.parseDouble(mis.getDamageAmount()) + Double.parseDouble(dtl.getAmount()))
							: dtl.getAmount());

					mis.setClosingStock(mis.getOpeningStock() + mis.getPurchase() + mis.getSaleReturn() - mis.getSale()
							- (mis.getDamage() == null ? 0 : mis.getDamage()) - mis.getPurchaseReturn());
					mis.setClosingStockAmount(String.valueOf(Double.parseDouble(mis.getOpeningStockAmount())
							+ Double.parseDouble(mis.getPurchaseAmount())
							+ Double.parseDouble(mis.getSaleReturnAmount()) - Double.parseDouble(mis.getSaleAmount())
							- Double.parseDouble(mis.getDamageAmount() == null ? "0" : mis.getDamageAmount())
							- Double.parseDouble(mis.getPurchaseReturnAmount())));
					dataList.add(mis);
				}
			}
		}
		return dataList;
	}

	private List<CounterStockManageVo> stockEdit_v1(StockEntryVo input, Long loginUserId, boolean validation) {
		List<CounterStockManageVo> dataList = new ArrayList<>();
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutletUser(input.getOutlet().getId(), new Date(), loginUserId);
		List<StockEntryDtlVo> purchaseList = new ArrayList<StockEntryDtlVo>();
		List<StockEntryDtlVo> saleList = new ArrayList<StockEntryDtlVo>();

		List<CounterStockManageVo> dbData2 = existingData.stream().map(SerializationUtils::clone)
				.collect(Collectors.toList());

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					if (dtl.getNoOfPcs() > mis.getClosingStock()) { // ager closing stock er theke besi, mane purchase
						dtl.setNoOfPcs(dtl.getNoOfPcs() - mis.getClosingStock());
						dtl.setAmount(""); // ???
						purchaseList.add(dtl);

						mis.setPurchase(dtl.getNoOfPcs());
						mis.setClosingStock(dtl.getNoOfPcs());
					} else if (dtl.getNoOfPcs() < mis.getClosingStock()) { // ager closing stock er theke kom, mane sale
						// hoyechhe
						dtl.setNoOfPcs(mis.getClosingStock() - dtl.getNoOfPcs());
						dtl.setAmount(""); // ???
						saleList.add(dtl);

						mis.setSale(dtl.getNoOfPcs());
						mis.setClosingStock(dtl.getNoOfPcs());
					}

					dataList.add(mis);
					match = true;
					break;
				}
			}
			// If completely new product then, add in MIS stock, also mark as purchase
			if (!match) {
				LOGGER.info("New product ID ==>> " + dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L, 0L, 0L,
						dtl.getNoOfPcs(), "0", "0", "0", dtl.getAmount(), "user", dtl.getCategoryName(),
						dtl.getSubCategoryName(), new Date(), loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0",
						input.getTransactionDate());
				dataList.add(each);

				purchaseList.add(dtl);
			}
		}

		// Add new stock entry data
		if (purchaseList.size() > 0) {
			StockEntryVo purchase = new StockEntryVo(purchaseList, "Purchase", "System-Gen", input.getUserId(),
					input.getOutlet(), String.valueOf(purchaseList.size()), "", "System-Gen", new Date(), loginUserId,
					new Date(), loginUserId);

			StockEntryVo purchaseSaved = iStockEntryRepository.save(purchase);
			LOGGER.info("================ purchaseSaved ========= >>" + purchaseSaved);
		}
		if (saleList.size() > 0) {
			StockEntryVo sale = new StockEntryVo(saleList, "Sale", "System-Gen", input.getUserId(), input.getOutlet(),
					String.valueOf(saleList.size()), "", "System-Gen", new Date(), loginUserId, new Date(),
					loginUserId);

			StockEntryVo saleSaved = iStockEntryRepository.save(sale);
			LOGGER.info("================ saleSaved ========= >>" + saleSaved);
		}

		if (validation) {
			boolean isNegativeStock = dataList.stream().anyMatch(o -> o.getClosingStock() <= 0);
			if (isNegativeStock) {
				// only return negative data
				return dataList.stream().filter(o -> o.getClosingStock() <= 0).collect(Collectors.toList());
			}
			// List<CounterStockManageVo> dbData = iCounterStockManageRepository
			// .findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
			return dbData2;
		}

		return dataList;
	}

	// Stock scheduler
	public ResponseEntity<ApiResponse> schedular() {
		LOGGER.info("StockService :: schedular() called...");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		Date today = new Date();
		// Fetch outlets which stocks needs to be updated todays date
		List<Long> outletsToUpdateStock = iCounterStockManageRepository.outletsToUpdateStock(today);
		List<CounterStockManageVo> stockList = iCounterStockManageRepository.findPocketMISByDate(today);
		Map<OutletVo, List<CounterStockManageVo>> stockPerOutlet = stockList.stream()
				.collect(Collectors.groupingBy(CounterStockManageVo::getOutlet));
		for (Entry<OutletVo, List<CounterStockManageVo>> mapData : stockPerOutlet.entrySet()) {
			// List of products as 'Purchase' in stock table
			LOGGER.info(mapData.getKey().getOutletCode());
			LOGGER.info(mapData.getValue().size());
		}

		// if(savedData != null) {
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Saved Successfully", null), HttpStatus.OK);
		// }else {
		// throw new OjbException(ErrorCode.NOT_SAVED, new Object[]{"Stock"});
		// }
	}

	public ResponseEntity<ApiResponse> pocketMISProductDetails(PocketMISInputVo input) {
		LOGGER.info("StockService :: pocketMIS() called...");

		List<CounterStockManageVo> data = iCounterStockManageRepository.findPocketMISProductDetails(input.getOutletId(),
				input.getCategoryName(), input.getSubCategoryName(), input.getMonthYr());

		ApiResponse apiResponse = new ApiResponse(200, "success", "success", data, data.size());
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse> listInvoiceReference(Long outletId) {
		LOGGER.info("StockService::listInvoiceReference::Entering...");
		ApiResponse apiResponse = null;
		List<StockReturnView> getDataList = iStockEntryRepository.findReferenceInvoice(outletId);

		LOGGER.info("StockService::listInvoiceReference::Exiting...");
		if (getDataList != null) {

			apiResponse = new ApiResponse(200, "success", "success", getDataList, getDataList.size());
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "stockService" });
		}
	}

	private ResponseEntity<ApiResponse> validation_old(StockEntryVo input, Long loginUserId) {
		// if (null == input.getId() && null != input.getStockStatus() &&
		// input.getStockStatus().equals("Edit")
		// && input.getTotalAmountOfItem().equals(input.getTotalAmountOfItemUpdated()))
		// {
		// return new ResponseEntity<ApiResponse>(
		// new ApiResponse(406, "not_updated", "Please update the amount before submit",
		// null),
		// HttpStatus.NOT_ACCEPTABLE);
		// }
		//
		// // checking for Sale/ Purchase Return/ Damage
		// if (input.getActivityType().equalsIgnoreCase("sale")
		// || input.getActivityType().equalsIgnoreCase("purchase_return")
		// || input.getActivityType().equalsIgnoreCase("Damage")) {
		//
		// List<Long> products =
		// input.getStockEntryDtlVo().stream().map(StockEntryDtlVo::getProductId)
		// .map(ProductVo::getId).collect(Collectors.toList());
		// LOGGER.info("Sale / purchase_return/ Damage productlist == >>" + products);
		//
		// List<Long> productForEdit = iStockEntryRepository.productForEdit(products,
		// input.getOutlet().getId());
		// if (null != productForEdit && productForEdit.size() > 0) {
		// return new ResponseEntity<ApiResponse>(new ApiResponse(406,
		// "item_not_accessable",
		// "Products are Pending for 'Admin Approval, Your cant't do Sale /Purchase
		// return/ Damage entry",
		// null), HttpStatus.NOT_ACCEPTABLE);
		// }
		// }
		//
		// // 1 month Date checking for Sale Return/ Purchase Return
		// if (input.getActivityType().equalsIgnoreCase("sale_return")
		// || input.getActivityType().equalsIgnoreCase("purchase_return")) {
		//
		// Long checkIfReturnable =
		// iStockEntryRepository.checkIfReturnable(input.getId(), 1);
		// if (null != checkIfReturnable) {
		// return new ResponseEntity<ApiResponse>(new ApiResponse(406,
		// "return_not_possible",
		// "Sale/ Purchase can be returned upto one month only", null),
		// HttpStatus.NOT_ACCEPTABLE);
		// }
		// }
		//
		// // If existing stock entry exists for one outlet from one BA user
		// if (null == input.getId() &&
		// input.getActivityType().equalsIgnoreCase("stock")) {
		// Long stockEntryUser =
		// iStockEntryRepository.getExistingStockEntryUser(input.getOutlet().getId());
		// if ((null != stockEntryUser) && loginUserId == stockEntryUser) {
		// return new ResponseEntity<ApiResponse>(new ApiResponse(406, "not_accptable",
		// "You have already entried 'Stock' for this outlet.", null),
		// HttpStatus.NOT_ACCEPTABLE);
		// } else if ((null != stockEntryUser) && loginUserId != stockEntryUser) {
		// // This is a BA change case, BA for this outlet is new, she will update stock
		// // again, based
		// // on that purchase, sales, MIS update
		// updatePocketMIS(input, loginUserId, BA_Activity_Enum.stock_edit, true);
		// }
		// }
		//
		// if (null != input.getStockStatus()
		// && (input.getStockStatus().equals("Edit") ||
		// input.getStockStatus().equals("Delete"))) {
		// BA_Activity_Enum activityName = null;
		// if (null != input.getActivityType() &&
		// "Purchase".equals(input.getActivityType())) {
		// activityName = Constants.BA_Activity_Enum.purchase_edit;
		// } else if (null != input.getActivityType() &&
		// "Sale".equals(input.getActivityType())) {
		// activityName = Constants.BA_Activity_Enum.sale_edit;
		// } else if (null != input.getActivityType() &&
		// "Stock".equals(input.getActivityType())) {
		// activityName = Constants.BA_Activity_Enum.stock_edit;
		// }
		// boolean isNegativeStock = updatePocketMIS(input, loginUserId, activityName,
		// false);
		// if (isNegativeStock) {
		// return new ResponseEntity<ApiResponse>(
		// new ApiResponse(406, "not_accptable",
		// "Closing stock is coming negative, can't edit/ delete this entry", null),
		// HttpStatus.NOT_ACCEPTABLE);
		// }
		// }
		return null;
	}

	private ResponseEntity<ApiResponse> validation(StockEntryVo input, Long loginUserId) {

		if (null == input.getId() && null != input.getStockStatus() && input.getStockStatus().equals("Edit")
				&& input.getTotalAmountOfItem().equals(input.getTotalAmountOfItemUpdated())) {
			return new ResponseEntity<ApiResponse>(
					new ApiResponse(406, "not_updated", "Please update the amount before submit", null),
					HttpStatus.NOT_ACCEPTABLE);
		}

		// checking for Sale/ Purchase Return/ Damage
		if (input.getActivityType().equalsIgnoreCase("sale")
				|| input.getActivityType().equalsIgnoreCase("purchase_return")
				|| input.getActivityType().equalsIgnoreCase("Damage")) {

			List<Long> products = input.getStockEntryDtlVo().stream().map(StockEntryDtlVo::getProductId)
					.map(ProductVo::getId).collect(Collectors.toList());
			LOGGER.info("Sale / purchase_return/ Damage productlist == >>" + products);

			List<Long> productForEdit = iStockEntryRepository.productForEdit(products, input.getOutlet().getId());
			if (null != productForEdit && productForEdit.size() > 0) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(406, "item_not_accessable",
						"Products are Pending for 'Admin Approval, Your cant't do Sale /Purchase return/ Damage entry",
						null), HttpStatus.NOT_ACCEPTABLE);
			}
		}

		// 1 month Date checking for Sale Return/ Purchase Return
		if (input.getActivityType().equalsIgnoreCase("sale_return")
				|| input.getActivityType().equalsIgnoreCase("purchase_return")) {

			Long checkIfReturnable = iStockEntryRepository.checkIfReturnable(input.getId(), 1);
			if (null != checkIfReturnable) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(406, "return_not_possible",
						"Sale/ Purchase can be returned upto one month only", null), HttpStatus.NOT_ACCEPTABLE);
			}
		}

		// If existing stock entry exists for one outlet from one BA user
		if (null == input.getId() && input.getActivityType().equalsIgnoreCase("stock")) {
			Long stockEntryUser = iStockEntryRepository.getExistingStockEntryUser(input.getOutlet().getId());
			if ((null != stockEntryUser) && loginUserId == stockEntryUser) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(406, "not_accptable",
						"You have already entried 'Stock' for this outlet.", null), HttpStatus.NOT_ACCEPTABLE);
			} else if ((null != stockEntryUser) && loginUserId != stockEntryUser) {
				// This is a BA change case, BA for this outlet is new, she will update stock
				// again, based
				// on that purchase, sales, MIS update
				updatePocketMIS(input, loginUserId, BA_Activity_Enum.stock_edit, false);
			}
		}

		// check for negative stock
		boolean isNegativeStock = false;
		if (null != input.getStockStatus()
				&& (input.getStockStatus().equals("Edit") || input.getStockStatus().equals("Delete"))) {
			BA_Activity_Enum activityName = null;
			List<CounterStockManageVo> dbData = new ArrayList<CounterStockManageVo>();
			if (null != input.getActivityType() && "Purchase".equals(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.purchase_edit;
				dbData = purchaseEdit(input, loginUserId, true);
				LOGGER.info(dbData);
			} else if (null != input.getActivityType() && "Sale".equals(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.sale_edit;
				dbData = saleEdit(input, loginUserId, true);

			} else if (null != input.getActivityType() && "Stock".equals(input.getActivityType())) {
				activityName = Constants.BA_Activity_Enum.stock_edit;
				// dbData = stockEdit(input, loginUserId, true);
				dbData = stockEditMonthYr(input, loginUserId, true);
			}

			// boolean isNegativeStock = updatePocketMIS(input, loginUserId, activityName,
			// false);
			isNegativeStock = dbData.stream().anyMatch(o -> o.getClosingStock() < 0); // this was <= before -- 22/12/23
			if (isNegativeStock) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(406, "not_accptable",
								"Closing stock is coming negative, can't edit/ delete this entry.", null),
						HttpStatus.NOT_ACCEPTABLE);
			}

			// checking for 1 month
			if (!input.currentMonthStockEntry()) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(406, "not_accptable",
								"Only Current month data edit/delete is possible.", null),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return null;
	}

	public ResponseEntity<ApiResponse> deleteStock(Long id) {
		LOGGER.info("StockService :: deleteStock() called...");

		try {
			String stockStatus = iStockEntryRepository.getStockEntryStatus(id);
			if (null != stockStatus && stockStatus.equals("Draft")) {
				// Drafted data will be deleted from DB
				iStockEntryRepository.deleteById(id);
				ApiResponse apiResponse = new ApiResponse(200, "success", "Data deleted successfully");
				return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "Stock" });
		}
		ApiResponse apiResponse = new ApiResponse(200, "success", "success", null, 0);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse> checkIfReturnable(Long id) {
		LOGGER.info("StockService :: deleteStock() called...");

		Long checkIfReturnable = iStockEntryRepository.checkIfReturnable(id, 1);
		if (null != checkIfReturnable) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(406, "return_not_possible",
					"Sale/ Purchase can be returned upto one month only", null), HttpStatus.NOT_ACCEPTABLE);
		}
		return null;
	}

	public ResponseEntity<ApiResponse> outletStock(PocketMISInputVo input) {
		LOGGER.info("StockService :: outletStock() called...");

		String whereClause = " and DATE_FORMAT(transaction_date,'%Y-%m') = '" + input.getMonthYr()
				+ "' and outlet_id = "
				+ input.getOutletId();
		if (null != input.getCategoryName() && !input.getCategoryName().isEmpty()) {
			whereClause += " and category_name = '" + input.getCategoryName() + "'";
		}
		if (null != input.getSubCategoryName() && !input.getSubCategoryName().isEmpty()) {
			whereClause += " and sub_category_name = '" + input.getSubCategoryName() + "'";
		}
		LOGGER.info("<============ whereClause =============>" + whereClause);
		List<CounterStockManageVo> data = iCounterStockManageRepository.findMonthlyOutletStock(whereClause);

		ApiResponse apiResponse = new ApiResponse(200, "success", "success", data, data.size());
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	/**
	 * @param currentMonthYr
	 * @return get all the stocks of current month and yr of an active outlet, if
	 *         the outlet is inactive it returns empty list.
	 */
	public List<CounterStockManageVo> getStocksOfAnActiveOutlet(YearMonth currentMonthYr) {
		return getStocksForMonthOfActiveOutlets(currentMonthYr);
	}

	/**
	 * @param yearMonth
	 * @return counter stocks of all outlets of a month and year
	 */
	public List<CounterStockManageVo> getStocksForMonthOfActiveOutlets(YearMonth yearMonth) {
		return this.iCounterStockManageRepository
				.findAllMonthlyOutletStock(Util.getMonthYearFormatted(yearMonth, "yyyy-MM")).stream()
				.filter(e -> e.getOutlet().getIsActive()).collect(Collectors.toList());
	}

	public void saveStocksData(List<CounterStockManageVo> stocks) {
		iCounterStockManageRepository.saveAll(stocks);
	}

	public List<StockEntryVo> findStockEntryByDateRangeAndActivityTypeAndStockStatus(Date fromDate, Date toDate,
			List<String> activity, Long outletId, List<String> stockStatus) {
		if (toDate.before(fromDate))
			throw new IllegalArgumentException("toDate must be greater that fromDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return iStockEntryRepository.findByDateRangeAndActivityTypeAndOutletAndStockStatus(formatter.format(fromDate),
				formatter.format(toDate), activity, outletId, stockStatus);
	}

	public List<StockEntryDtlVo> findStockEntryDetails(Long stockEntryId) {
		return this.iStockEntryDtlRepository.findByStockEntryId(stockEntryId);
	}

	public ResponseEntity<ApiResponse> getNetSaleAmount(StockSearchInputVo input) {
		LOGGER.info("StockService :: getNetSaleAmount() called...");

		List<String> data = iCounterStockManageRepository.getNetSaleAmount(input.getFromDate(), input.getToDate());
		List<SaleRatioOPDto> resp = new ArrayList<SaleRatioOPDto>();
		for (String each : data) {
			String[] eachArr = each.split(",");
			SaleRatioOPDto dto = new SaleRatioOPDto();
			dto.setCategoryName(eachArr[1]);
			dto.setSaleAmount(Double.parseDouble(eachArr[0]));
			resp.add(dto);
		}

		ApiResponse apiResponse = new ApiResponse(200, "success", "success", resp, data.size());
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse> deletePurchase(PocketMISInputVo input) {
		LOGGER.info("StockService :: deletePurchase() called...");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findProductForDelete(input.getOutletId(), input.getMonthYr());
		List<StockEntryDtlVo> purchase = iStockEntryDtlRepository.findByStockEntryId(input.getStockId());
		List<CounterStockManageVo> updatedStock = new ArrayList<CounterStockManageVo>();
		boolean negativeStock = false;
		for (CounterStockManageVo stockOutlet : existingData) {
			LOGGER.info("stockOutlet.getProductId() == > " + stockOutlet.getProductId().getId());
			for (StockEntryDtlVo stock : purchase) {
				LOGGER.info("stock.getProductId() == > " + stock.getProductId().getId());

				if (stockOutlet.getProductId() == stock.getProductId()) {
					Long closingStock = stockOutlet.getClosingStock() - stock.getNoOfPcs();
					if (closingStock < 0) {
						negativeStock = true;
						break;
					}
					stockOutlet.setClosingStock(closingStock);
					stockOutlet.setPurchase(stockOutlet.getPurchase() - stock.getNoOfPcs());

					stockOutlet.setClosingStockAmount(
							String.valueOf(Double.parseDouble(stockOutlet.getClosingStockAmount())
									- Double.parseDouble(stock.getAmount())));
					stockOutlet.setPurchaseAmount(String.valueOf(Double.parseDouble(stockOutlet.getPurchaseAmount())
							- Double.parseDouble(stock.getAmount())));
					updatedStock.add(stockOutlet);
				}
			}
		}

		// If any product closing stock is -ve, then no deletion possible
		if (negativeStock) {
			ApiResponse apiResponse = new ApiResponse(406, "delete_not_possible",
					"Stock becomes negative for product, Purchase entry delete not possible.", null);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
		}
		// Update outlet Stock
		List<CounterStockManageVo> updatedStockSaved = iCounterStockManageRepository.saveAll(updatedStock);
		LOGGER.info("Updated outlet stock after purchase delete ================= > " + updatedStockSaved);

		// update stock entry status
		int stockntryStatus = iStockEntryRepository.updateStockStatus(input.getStockId(), "Deleted", new Date(),
				loginUserId);
		LOGGER.info("StockService :: updatedApprovalStatus() stockntryStatus called..." + stockntryStatus);
		ApiResponse apiResponse = new ApiResponse(200, "success", "Purchase entry deleted successfully.");
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse> deleteSale(PocketMISInputVo input) {
		LOGGER.info("StockService :: deleteSale() called...");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findProductForDelete(input.getOutletId(), input.getMonthYr());
		List<StockEntryDtlVo> sale = iStockEntryDtlRepository.findByStockEntryId(input.getStockId());
		List<CounterStockManageVo> updatedStock = new ArrayList<CounterStockManageVo>();
		for (CounterStockManageVo stockOutlet : existingData) {
			for (StockEntryDtlVo stock : sale) {
				if (stockOutlet.getProductId() == stock.getProductId()) {
					Long closingStock = stockOutlet.getClosingStock() + stock.getNoOfPcs();
					stockOutlet.setClosingStock(closingStock);

					stockOutlet.setSale(stockOutlet.getSale() - stock.getNoOfPcs());

					stockOutlet.setClosingStockAmount(
							String.valueOf(Double.parseDouble(stockOutlet.getClosingStockAmount())
									+ Double.parseDouble(stock.getAmount())));
					stockOutlet.setSaleAmount(String.valueOf(
							Double.parseDouble(stockOutlet.getSaleAmount()) - Double.parseDouble(stock.getAmount())));
					updatedStock.add(stockOutlet);
				}
			}
		}
		// Update outlet Stock
		List<CounterStockManageVo> updatedStockSaved = iCounterStockManageRepository.saveAll(updatedStock);
		LOGGER.info("Updated outlet stock after purchase delete ================= > " + updatedStockSaved);

		// update stock entry status
		int stockntryStatus = iStockEntryRepository.updateStockStatus(input.getStockId(), "Deleted", new Date(),
				loginUserId);
		LOGGER.info("StockService :: updatedApprovalStatus() stockntryStatus called..." + stockntryStatus);
		ApiResponse apiResponse = new ApiResponse(200, "success", "Purchase entry deleted successfully.");
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	private boolean ifPrevMonthTransaction(Date transDate) {
		// if previous month purchase edit, then search data of previous month
		boolean stockTransfer = false;
		YearMonth yearMonthCurrent = YearMonth.now();
		YearMonth yearMonthPrevious = YearMonth.now().minusMonths(1);
		// Date transDate = input.getTransactionDate();
		if (yearMonthPrevious.getMonth().ordinal() == transDate.getMonth()
				|| yearMonthCurrent.getMonth().ordinal() == transDate.getMonth()) {
			stockTransfer = true;
		}
		return stockTransfer;
	}

	private void saveLogAndActivity(StockEntryVo input, Long loginUserId) {
		// For 'Sent for approval / Edit request'
		// if previous month purchase edit, then search data of previous month
		// boolean ifPrevMonthTransaction =
		// ifPrevMonthTransaction(input.getTransactionDate()); //Transaction date
		// MANDATORY
		LOGGER.info("<<====== Check ifPrevMonthTransaction ========>>" + input.previousMonthTxnCheck());
		// ifPrevMonthTransaction = false;
		if (null != input.getStockStatus()
				&& ((input.getStockStatus().equals("Edit") || input.getStockStatus().equals("Prev_Adj")
						|| input.getStockStatus().equals("Delete") || input.getStockStatus().equals("Edit_Draft"))
						|| input.previousMonthTxnCheck())) { // Added on 09/01/24 for prev month sale/purchase/ edit
			// create a log data
			String remarks = (null != input.getRemarks()) ? input.getRemarks()
					: ("Stock " + input.getStockStatus() + " remarks.");
			StockEntryLogVo logged = iStockEntryLogRepository
					.save(new StockEntryLogVo(input, input.getStockStatus(), remarks, new Date(), loginUserId));
			LOGGER.info("Data saved in Log table == > " + logged);

			// create a activity Data
			String activityType = input.getActivityType();
			BA_Activity_Enum activityName = findActivity(activityType, input.getStockStatus());
			UserActivityRegisterVo activity = iUserActivityRepository.save(new UserActivityRegisterVo(activityName,
					new Date(), input.getOutlet(), input.getTotalAmountOfItemUpdated(), loginUserId));
			LOGGER.info(" UserActivityRegisterVo from saveLogAndActivity for " + activityName
					+ " activity ====================>" + activity);
			activity.setStockReferenceId(input.getId());
			LOGGER.info("Data saved in UserActivityRegister table == > " + activity);

			// send for approval
			if (!input.getStockStatus().equals("Edit_Draft") && !input.getStockStatus().equals("Draft")) {
				List<StockApprovalVo> editRequest = new ArrayList<StockApprovalVo>();
				List<UserDetailsVo> assignTo = iUserDetailsRepository.findUserForEditApproval(loginUserId);
				for (UserDetailsVo each : assignTo) {
					StockApprovalVo data = new StockApprovalVo(input, "Pending Approval", new Date(), loginUserId,
							new Date(), loginUserId, each);
					editRequest.add(data);
				}
				List<StockApprovalVo> editRequestSaved = iStockApprovalRepository.saveAll(editRequest);
				LOGGER.info("Data saved in Stock Approval table == > " + editRequestSaved);

				// take a snap ================== WORK TO DO HERE +++++++++++++++++++++++++
			}
		} else {
			// create a activity Data
			String activityType = input.getActivityType();
			BA_Activity_Enum activityName = findActivity(activityType, input.getStockStatus());
			UserActivityRegisterVo activity = iUserActivityRepository.save(new UserActivityRegisterVo(activityName,
					new Date(), input.getOutlet(), input.getTotalAmountOfItem(), loginUserId));
			activity.setStockReferenceId(input.getId());
			LOGGER.info("Data saved in UserActivityRegister table == > " + activity);

			if (null != input.getStockStatus() && !input.getStockStatus().equals("Draft")
					&& !input.getStockStatus().equals("Edit_Draft")) {
				// Update pocket MIS
				updatePocketMIS(input, loginUserId, activityName, false);
			}
		}
	}

	// Added on 09/01/24
	private BA_Activity_Enum findActivity(String activityType, String stockStatus) {
		BA_Activity_Enum activityName = null;
		if (null != activityType && "Purchase".equalsIgnoreCase(activityType)) {
			activityName = (stockStatus.equals("Draft")) ? Constants.BA_Activity_Enum.purchase_draft
					: (stockStatus.equals("Edit")) ? Constants.BA_Activity_Enum.purchase_edit
							: (stockStatus.equals("Prev_Adj")) ? Constants.BA_Activity_Enum.prev_purchase_adj
									: (stockStatus.equals("Delete")) ? Constants.BA_Activity_Enum.purchase_delete
											: Constants.BA_Activity_Enum.purchase_entry;
		} else if (null != activityType && "Sale".equalsIgnoreCase(activityType)) {
			activityName = (stockStatus.equals("Edit")) ? Constants.BA_Activity_Enum.sale_edit
					: (stockStatus.equals("Prev_Adj")) ? Constants.BA_Activity_Enum.prev_sale_adj
							: (stockStatus.equals("Delete")) ? Constants.BA_Activity_Enum.sale_delete
									: Constants.BA_Activity_Enum.sale_entry;
		} else if (null != activityType && "Stock".equalsIgnoreCase(activityType)) {
			activityName = (stockStatus.equals("Draft")) ? Constants.BA_Activity_Enum.stock_draft
					: (stockStatus.equals("Edit_Draft")) ? Constants.BA_Activity_Enum.stock_edit_draft
							: (stockStatus.equals("Edit")) ? Constants.BA_Activity_Enum.stock_edit
									: Constants.BA_Activity_Enum.stock_entry;
			// activityName = Constants.BA_Activity_Enum.stock_entry;
		} else if (null != activityType && "Damage".equalsIgnoreCase(activityType)) {
			activityName = Constants.BA_Activity_Enum.damage_entry;
		} else if (null != activityType && "purchase_return".equalsIgnoreCase(activityType)) {
			activityName = Constants.BA_Activity_Enum.purchase_return;
		} else if (null != activityType && "sale_return".equalsIgnoreCase(activityType)) {
			activityName = Constants.BA_Activity_Enum.sale_return;
		}
		return activityName;
	}

	private List<CounterStockManageVo> purchaseEdit(StockEntryVo input, Long loginUserId, boolean validation) {
		List<CounterStockManageVo> dataList = new ArrayList<CounterStockManageVo>();
		List<CounterStockManageVo> existingData = new ArrayList<CounterStockManageVo>();
		Date transDate = input.getTransactionDate();
		// if previous month purchase edit, then search data of previous month
		// boolean ifPrevMonthTransaction = ifPrevMonthTransaction(transDate);

		// existingData =
		// iCounterStockManageRepository.findPocketMISByDateOutlet(input.getOutlet().getId(),
		// new Date());
		// get a collection of all the ids.
		List<Long> products = input.getStockEntryDtlVo().stream().map(e -> e.getProductId().getId())
				.collect(Collectors.toList());
		existingData = iCounterStockManageRepository.findPocketMISByDateOutletAndProducts(input.getOutlet().getId(),
				transDate, products);

		List<CounterStockManageVo> dbData2 = existingData.stream().map(SerializationUtils::clone)
				.collect(Collectors.toList());

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setAdjustedNoOfPcs(dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated());
					mis.setAdjustedAmount(
							Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated()));
					mis.setAdjustedBy(mis.getUpdatedBy());
					mis.setAdjustedOn(new Date());
					mis.setPurchase(mis.getPurchase() - mis.getAdjustedNoOfPcs());
					mis.setPurchaseAmount(
							String.valueOf(Double.parseDouble(mis.getPurchaseAmount()) - (mis.getAdjustedAmount())));
					mis.setClosingStock(mis.getClosingStockCalc());
					mis.setClosingStockAmount(mis.getClosingStockAmountCalc());
					dataList.add(mis);
					match = true;
				}
			}
			if (!match) {
				LOGGER.info("New product - >" + dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L,
						dtl.getNoOfPcsUpdated(), 0L, dtl.getNoOfPcsUpdated(), "0", dtl.getAmountUpdated(), "0",
						dtl.getAmountUpdated(), "user", dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(),
						loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0", input.getTransactionDate());
				dataList.add(each);
			}
		}
		// boolean isNegativeStock = dataList.stream().filter(o -> o.getClosingStock()
		// <= 0).findFirst().isPresent();
		if (validation) {
			boolean isNegativeStock = dataList.stream().anyMatch(o -> o.getClosingStock() <= 0);
			if (isNegativeStock) {
				// only return negative data
				return dataList.stream().filter(o -> o.getClosingStock() <= 0).collect(Collectors.toList());
			}
			return dbData2;
		}
		// If previous month purchase, then stock transfer needed
		if (input.previousMonthTxnCheck()) {
			// call stock update for current month
			List<CounterStockManageVo> prevdata = prevMonthStockAdjust(dataList, products, input.getOutlet().getId(),
					loginUserId, "purchase");
			dataList.addAll(prevdata);
		}
		return dataList;
	}

	private List<CounterStockManageVo> purchaseEntry(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList, boolean stockAdjust) {
		// List<CounterStockManageVo> existingData = iCounterStockManageRepository
		// .findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
		Date transDate = input.getTransactionDate();
		// if previous month purchase edit, then search data of previous month
		// boolean ifPrevMonthTransaction = ifPrevMonthTransaction(transDate);
		// get a collection of all the ids.
		List<Long> products = input.getStockEntryDtlVo().stream().map(e -> e.getProductId().getId())
				.collect(Collectors.toList());
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutletAndProducts(input.getOutlet().getId(), transDate, products);

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setPurchase(
							(null != mis.getPurchase()) ? mis.getPurchase() + dtl.getNoOfPcs() : dtl.getNoOfPcs());
					mis.setPurchaseAmount((null != mis.getPurchaseAmount())
							? String.valueOf(
									Double.parseDouble(mis.getPurchaseAmount()) + Double.parseDouble(dtl.getAmount()))
							: dtl.getAmount());
					if (input.previousMonthTxnCheck() && !stockAdjust) {
						LOGGER.info("+++++++ stockAdjust ===================================>>" + stockAdjust);
						mis.setAdjustedNoOfPcs(dtl.getNoOfPcsUpdated());
						mis.setAdjustedAmount(Double.parseDouble(dtl.getAmountUpdated()));
						mis.setAdjustedBy(mis.getUpdatedBy());
						mis.setAdjustedOn(new Date());
						// mis.setUpdatedOn(input.getTransactionDate()); //For pocket MIS calculation
					}

					mis.setClosingStock(mis.getClosingStockCalc());
					mis.setClosingStockAmount(mis.getClosingStockAmountCalc());
					dataList.add(mis);
					match = true;
					break;
				}
			}
			if (!match) {
				// Date adjustedOn, Long adjustedBy, Long adjustedNoOfPcs, Double adjustedAmount
				LOGGER.info(dtl.getProductId().getId());
				if (input.previousMonthTxnCheck() && !stockAdjust) {
					CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L,
							dtl.getNoOfPcsUpdated(), 0L, dtl.getNoOfPcsUpdated(), "0", dtl.getAmountUpdated(), "0",
							dtl.getAmountUpdated(), "user", dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(),
							loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0", new Date(), loginUserId,
							(dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated()),
							(Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated())),
							input.getTransactionDate());
					dataList.add(each);
				} else {
					CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L,
							dtl.getNoOfPcs(), 0L, dtl.getNoOfPcs(), "0", dtl.getAmount(), "0", dtl.getAmount(), "user",
							dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(), loginUserId, new Date(),
							loginUserId, 0L, 0L, "0", "0", input.getTransactionDate());
					dataList.add(each);
				}

			}
		}

		// If previous month purchase, then stock transfer needed
		if (input.previousMonthTxnCheck()) {
			// call stock update for current month
			List<CounterStockManageVo> currentMonData = prevMonthStockAdjust(dataList, products,
					input.getOutlet().getId(), loginUserId, "purchase");
			dataList.addAll(currentMonData);
		}
		return dataList;
	}

	private List<CounterStockManageVo> prevMonthStockAdjust(List<CounterStockManageVo> dataList, List<Long> products,
			Long outletId, Long loginUserId, String transType) {
		List<CounterStockManageVo> currentStocks = iCounterStockManageRepository
				.findPocketMISByDateOutletAndProducts(outletId, new Date(), products);
		// time complexity O(nlogn) + O(logN)

		currentStocks.sort(Comparator.comparingLong(e -> e.getProductId().getId()));

		List<CounterStockManageVo> finalStocks = dataList.stream().filter(e -> e.getAdjustedNoOfPcs() != 0).map(e -> {
			int index = Util.binarySearch(
					currentStocks.stream().map(m -> m.getProductId().getId()).collect(Collectors.toList()),
					e.getProductId().getId());
			LOGGER.info("index ========== >" + index);
			if (index == -1) {
				CounterStockManageVo each = new CounterStockManageVo(e.getOutlet(), e.getProductId(),
						e.getClosingStock(), 0L, 0L, e.getClosingStock(), e.getClosingStockAmount(), "0", "0",
						e.getClosingStockAmount(), "system_adjust", e.getCategoryName(), e.getSubCategoryName(),
						new Date(), loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0", new Date(), loginUserId,
						e.getClosingStock(), Double.parseDouble(e.getClosingStockAmount()), new Date());
				LOGGER.info("each ========== >" + each);
				return each;
			}

			CounterStockManageVo currentStock = currentStocks.get(index);
			if (transType.equals("sale")) {
				currentStock.setOpeningStock(currentStock.getOpeningStock() + e.getAdjustedNoOfPcs()); // 3 + (-1) = 3-1
																										// = 2
				currentStock.setClosingStock(currentStock.getClosingStock() + e.getAdjustedNoOfPcs());
				currentStock.setOpeningStockAmount(
						(Double.parseDouble(currentStock.getOpeningStockAmount()) + e.getAdjustedAmount()) + "");
				currentStock.setClosingStockAmount(
						(Double.parseDouble(currentStock.getClosingStockAmount()) + e.getAdjustedAmount()) + "");
			} else if (transType.equals("purchase")) {
				currentStock.setOpeningStock(currentStock.getOpeningStock() - e.getAdjustedNoOfPcs()); // 3 - (-1) = 3+1
																										// = 4
				currentStock.setClosingStock(currentStock.getClosingStock() - e.getAdjustedNoOfPcs());
				currentStock.setOpeningStockAmount(
						(Double.parseDouble(currentStock.getOpeningStockAmount()) - e.getAdjustedAmount()) + "");
				currentStock.setClosingStockAmount(
						(Double.parseDouble(currentStock.getClosingStockAmount()) - e.getAdjustedAmount()) + "");
			}
			currentStock.setTransactionDate(new Date());
			currentStock.setUpdateType("system_adjust");
			return currentStock;
		}).collect(Collectors.toList());

		return finalStocks;
		// iCounterStockManageRepository.saveAll(finalStocks);
	}

	// Currently NOT USED ===============
	/*
	 * private List<CounterStockManageVo> stockEdit(StockEntryVo input, Long
	 * loginUserId, boolean validation) {
	 * List<CounterStockManageVo> dataList = new ArrayList<CounterStockManageVo>();
	 * List<CounterStockManageVo> existingData = new
	 * ArrayList<CounterStockManageVo>();
	 * Date transDate = input.getTransactionDate();
	 * // if previous month purchase edit, then search data of previous month
	 * // boolean ifPrevMonthTransaction = ifPrevMonthTransaction(transDate);
	 * 
	 * // existingData =
	 * // iCounterStockManageRepository.findPocketMISByDateOutlet(input.getOutlet().
	 * getId(),
	 * // new Date());
	 * // get a collection of all the ids.
	 * List<Long> products = input.getStockEntryDtlVo().stream().map(e ->
	 * e.getProductId().getId())
	 * .collect(Collectors.toList());
	 * existingData =
	 * iCounterStockManageRepository.findPocketMISByDateOutletAndProducts(input.
	 * getOutlet().getId(),
	 * transDate, products);
	 * 
	 * List<CounterStockManageVo> dbData2 =
	 * existingData.stream().map(SerializationUtils::clone)
	 * .collect(Collectors.toList());
	 * 
	 * for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
	 * boolean match = false;
	 * for (CounterStockManageVo mis : existingData) {
	 * if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
	 * mis.setAdjustedNoOfPcs(dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated()); // 10-15
	 * = -5
	 * LOGGER.info("AdjustedNoOfPcs ========= > for product :: " +
	 * mis.getProductId().getId() + "=="
	 * + mis.getAdjustedNoOfPcs());
	 * mis.setAdjustedAmount(
	 * Double.parseDouble(dtl.getAmount()) -
	 * Double.parseDouble(dtl.getAmountUpdated()));
	 * mis.setAdjustedBy(mis.getUpdatedBy());
	 * mis.setAdjustedOn(new Date());
	 * mis.setOpeningStock(mis.getOpeningStock() - mis.getAdjustedNoOfPcs()); // 10
	 * - (-5) = 15
	 * mis.setOpeningStockAmount(String
	 * .valueOf(Double.parseDouble(mis.getOpeningStockAmount()) -
	 * (mis.getAdjustedAmount())));
	 * mis.setClosingStock(mis.getClosingStockCalc());
	 * mis.setClosingStockAmount(mis.getClosingStockAmountCalc());
	 * dataList.add(mis);
	 * match = true;
	 * }
	 * }
	 * if (!match) {
	 * LOGGER.info("New product - >" + dtl.getProductId().getId());
	 * CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(),
	 * dtl.getProductId(),
	 * dtl.getNoOfPcsUpdated(), 0L, 0L, dtl.getNoOfPcsUpdated(),
	 * dtl.getAmountUpdated(), "0", "0",
	 * dtl.getAmountUpdated(), "user", dtl.getCategoryName(),
	 * dtl.getSubCategoryName(), new Date(),
	 * loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0", new Date(),
	 * loginUserId,
	 * dtl.getNoOfPcsUpdated(), Double.parseDouble(dtl.getAmountUpdated()),
	 * input.getTransactionDate());
	 * dataList.add(each);
	 * }
	 * }
	 * // boolean isNegativeStock = dataList.stream().filter(o ->
	 * o.getClosingStock()
	 * // <= 0).findFirst().isPresent();
	 * if (validation) {
	 * boolean isNegativeStock = dataList.stream().anyMatch(o -> o.getClosingStock()
	 * <= 0);
	 * if (isNegativeStock) {
	 * // only return negative data
	 * return dataList.stream().filter(o -> o.getClosingStock() <=
	 * 0).collect(Collectors.toList());
	 * }
	 * return dbData2;
	 * }
	 * // If previous month purchase, then stock transfer needed
	 * LOGGER.info("previousMonthTxnCheck ==== - >" +
	 * input.previousMonthTxnCheck());
	 * LOGGER.info("previousMonthTxnCheck ==== - >" +
	 * ifPrevMonthTransaction(input.getTransactionDate()));
	 * if (input.previousMonthTxnCheck()) {
	 * // call stock update for current month
	 * List<CounterStockManageVo> prevdata = prevMonthStockAdjust(dataList,
	 * products, input.getOutlet().getId(),
	 * loginUserId);
	 * dataList.addAll(prevdata);
	 * }
	 * return dataList;
	 * }
	 */

	private List<CounterStockManageVo> purchaseEntryPrevAdjust(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {
		// List<CounterStockManageVo> existingData = iCounterStockManageRepository
		// .findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
		Date transDate = input.getTransactionDate();
		// if previous month purchase edit, then search data of previous month
		// boolean ifPrevMonthTransaction = ifPrevMonthTransaction(transDate);
		// get a collection of all the ids.
		List<Long> products = input.getStockEntryDtlVo().stream().map(e -> e.getProductId().getId())
				.collect(Collectors.toList());
		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutletAndProducts(input.getOutlet().getId(), transDate, products);

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setPurchase(
							(null != mis.getPurchase()) ? mis.getPurchase() + dtl.getNoOfPcsUpdated()
									: dtl.getNoOfPcsUpdated());
					mis.setPurchaseAmount((null != mis.getPurchaseAmount())
							? String.valueOf(
									Double.parseDouble(mis.getPurchaseAmount())
											+ Double.parseDouble(dtl.getAmountUpdated()))
							: dtl.getAmount());
					if (input.previousMonthTxnCheck()) {
						mis.setAdjustedNoOfPcs(dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated());
						mis.setAdjustedAmount(
								Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated()));
						mis.setAdjustedBy(mis.getUpdatedBy());
						mis.setAdjustedOn(new Date());
						// mis.setUpdatedOn(input.getTransactionDate()); //For pocket MIS calculation
					}

					mis.setClosingStock(mis.getClosingStockCalc());
					mis.setClosingStockAmount(mis.getClosingStockAmountCalc());
					dataList.add(mis);
					match = true;
					break;
				}
			}
			if (!match) {
				// Date adjustedOn, Long adjustedBy, Long adjustedNoOfPcs, Double adjustedAmount
				LOGGER.info(dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L,
						dtl.getNoOfPcsUpdated(), 0L, dtl.getNoOfPcsUpdated(), "0", dtl.getAmountUpdated(), "0",
						dtl.getAmountUpdated(), "user",
						dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(), loginUserId, new Date(),
						loginUserId, 0L, 0L, "0", "0", input.getTransactionDate());
				dataList.add(each);
				/*
				 * if (input.previousMonthTxnCheck()) {
				 * CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(),
				 * dtl.getProductId(), 0L,
				 * dtl.getNoOfPcsUpdated(), 0L, dtl.getNoOfPcsUpdated(), "0",
				 * dtl.getAmountUpdated(), "0",
				 * dtl.getAmountUpdated(), "user", dtl.getCategoryName(),
				 * dtl.getSubCategoryName(), new Date(),
				 * loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0", new Date(),
				 * loginUserId,
				 * (dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated()),
				 * (Double.parseDouble(dtl.getAmount()) -
				 * Double.parseDouble(dtl.getAmountUpdated())),
				 * input.getTransactionDate());
				 * dataList.add(each);
				 * } else {
				 * CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(),
				 * dtl.getProductId(), 0L,
				 * dtl.getNoOfPcsUpdated(), 0L, dtl.getNoOfPcsUpdated(), "0",
				 * dtl.getAmountUpdated(), "0", dtl.getAmountUpdated(), "user",
				 * dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(), loginUserId, new
				 * Date(),
				 * loginUserId, 0L, 0L, "0", "0", input.getTransactionDate());
				 * dataList.add(each);
				 * }
				 */
			}
		}

		// If previous month purchase, then stock transfer needed
		if (input.previousMonthTxnCheck()) {
			// call stock update for current month
			List<CounterStockManageVo> currentMonData = prevMonthStockAdjust(dataList, products,
					input.getOutlet().getId(), loginUserId, "purchase");
			dataList.addAll(currentMonData);
		}
		return dataList;
	}

	private List<CounterStockManageVo> saleEntryForAdj(StockEntryVo input, Long loginUserId,
			List<CounterStockManageVo> dataList) {

		List<Long> products = input.getStockEntryDtlVo().stream().map(e -> e.getProductId().getId())
				.collect(Collectors.toList());

		List<CounterStockManageVo> existingData = iCounterStockManageRepository
				.findPocketMISByDateOutletAndProducts(input.getOutlet().getId(), input.getTransactionDate(), products);

		for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					LOGGER.info("Sold product ------ > " + dtl.getProductId().getId());

					mis.setSale((null != mis.getSale()) ? mis.getSale() + dtl.getNoOfPcs() : dtl.getNoOfPcs());

					mis.setSaleAmount((null != mis.getSaleAmount())
							? String.valueOf(
									Double.parseDouble(mis.getSaleAmount()) + Double.parseDouble(dtl.getAmount()))
							: dtl.getAmount());
					mis.setClosingStock(mis.getClosingStockCalc());
					mis.setClosingStockAmount(mis.getClosingStockAmountCalc());

					match = true;
					dataList.add(mis);
					break;
				}
			}
			if (!match) {
				CounterStockManageVo each = new CounterStockManageVo().setOutlet(input.getOutlet())
						.setProductId(dtl.getProductId()).setSale(dtl.getNoOfPcs())
						.setSaleAmount(dtl.getAmount()).setCategoryName(dtl.getCategoryName())
						.setSubCategoryName(dtl.getSubCategoryName()).setTransactionDate(input.getTransactionDate())
						.setCreatedBy(loginUserId).setUpdatedBy(loginUserId).setUpdateType("user");

				dataList.add(each);

			}
		}
		return dataList;
	}

	private List<CounterStockManageVo> stockEditMonthYr(StockEntryVo input, Long loginUserId, boolean validation) {
		List<CounterStockManageVo> dataList = new ArrayList<CounterStockManageVo>();
		List<CounterStockManageVo> existingData = new ArrayList<CounterStockManageVo>();
		Date transDate = input.getTransactionDate();
		// Date editReqDate = input.getUpdatedOn();
		// get a collection of all the ids.
		List<Long> products = input.getStockEntryDtlVo().stream()
				.filter(e -> e.getNoOfPcs() != e.getNoOfPcsUpdated())
				.map(e -> e.getProductId().getId())
				.collect(Collectors.toList());
		List<StockEntryDtlVo> changedDataList = input.getStockEntryDtlVo().stream()
				.filter(e -> e.getNoOfPcs() != e.getNoOfPcsUpdated())
				.collect(Collectors.toList());
		existingData = iCounterStockManageRepository.findPocketMISByDateOutletAndProducts(input.getOutlet().getId(),
				transDate, products);

		List<CounterStockManageVo> dbData2 = existingData.stream().map(SerializationUtils::clone)
				.collect(Collectors.toList());

		for (StockEntryDtlVo dtl : changedDataList) {
			boolean match = false;
			for (CounterStockManageVo mis : existingData) {
				if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
					mis.setAdjustedNoOfPcs(dtl.getNoOfPcs() - dtl.getNoOfPcsUpdated()); // 10-15 = -5
					LOGGER.info("AdjustedNoOfPcs ========= > for product :: " + mis.getProductId().getId() + "=="
							+ mis.getAdjustedNoOfPcs());
					mis.setAdjustedAmount(
							Double.parseDouble(dtl.getAmount()) - Double.parseDouble(dtl.getAmountUpdated()));
					mis.setAdjustedBy(mis.getUpdatedBy());
					mis.setAdjustedOn(new Date());
					mis.setOpeningStock(mis.getOpeningStock() - mis.getAdjustedNoOfPcs()); // 10 - (-5) = 15
					mis.setOpeningStockAmount(String
							.valueOf(Double.parseDouble(mis.getOpeningStockAmount()) - (mis.getAdjustedAmount())));
					mis.setClosingStock(mis.getClosingStockCalc());
					mis.setClosingStockAmount(mis.getClosingStockAmountCalc());
					dataList.add(mis);
					match = true;
				}
			}
			if (!match) {
				LOGGER.info("New product - >" + dtl.getProductId().getId());
				CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(),
						dtl.getNoOfPcsUpdated(), 0L, 0L, dtl.getNoOfPcsUpdated(), dtl.getAmountUpdated(), "0", "0",
						dtl.getAmountUpdated(), "user", dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(),
						loginUserId, new Date(), loginUserId, 0L, 0L, "0", "0", new Date(), loginUserId,
						dtl.getNoOfPcsUpdated(), Double.parseDouble(dtl.getAmountUpdated()),
						input.getTransactionDate());
				dataList.add(each);
			}
		}
		// boolean isNegativeStock = dataList.stream().filter(o -> o.getClosingStock()
		// <= 0).findFirst().isPresent();
		if (validation) {
			boolean isNegativeStock = dataList.stream().anyMatch(o -> o.getClosingStock() <= 0);
			if (isNegativeStock) {
				// only return negative data
				return dataList.stream().filter(o -> o.getClosingStock() <= 0).collect(Collectors.toList());
			}
			return dbData2;
		}
		return dataList;
	}
}