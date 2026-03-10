//package com.dcc.osheaapp.service;
//
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.dcc.osheaapp.common.model.ApiResponse;
//import com.dcc.osheaapp.common.model.Constants.BA_Activity_Enum;
//import com.dcc.osheaapp.repository.ICounterStockManageSanpRepository;
//import com.dcc.osheaapp.repository.IStockEntryRepository;
//import com.dcc.osheaapp.vo.CounterStockManageSnapVo;
//import com.dcc.osheaapp.vo.StockEntryVo;
//import com.dcc.osheaapp.vo.dto.PocketMISInputVo;
//
//@Service
//public class StockAdjustmentServiceSnap {
//
//	private static final Logger LOGGER = LogManager.getLogger(StockAdjustmentServiceSnap.class);
//
//	private final StockService stockService;
//	private final IStockEntryRepository iStockEntryRepository;
//	// private final ICounterStockManageSnapRepository
//	// iCounterStockManageRepository;
//	private final ICounterStockManageSanpRepository snap;
//
//	@Autowired
//	public StockAdjustmentServiceSnap(StockService stockService, IStockEntryRepository iStockEntryRepository,
//			ICounterStockManageSanpRepository snap) {
//		this.stockService = stockService;
//		this.iStockEntryRepository = iStockEntryRepository;
//		this.snap = snap;
//		// this.iCounterStockManageRepository = iCounterStockManageRepository;
//	}
//
//	public ResponseEntity<ApiResponse> callForAdjustment(PocketMISInputVo input) {
//		String monYr = input.getMonthYr();
//		Long outletId = input.getOutletId();
//		List<String> stockStatus = input.getStockStatus();
//		List<StockEntryVo> entryForAdjustment = iStockEntryRepository.entryForAdjustment(monYr, outletId, stockStatus);
//		for (StockEntryVo each : entryForAdjustment) {
//			LOGGER.info(" ========== each entry ======>> " + each.getId() + " ========== getActivityType ======>> "
//					+ each.getActivityType());
//			if (each.getActivityType().equals("stock")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.stock_entry, true);
//			} else if (each.getActivityType().equals("Purchase")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.purchase_entry, true);
//			} else if (each.getActivityType().equals("Sale")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_for_adj, true);
//			} else if (each.getActivityType().equalsIgnoreCase("Damage")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.damage_entry, true);
//			} else if (each.getActivityType().equalsIgnoreCase("purchase_return")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.purchase_return, true);
//			} else if (each.getActivityType().equalsIgnoreCase("sale_return")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_return, true);
//			}
//		}
//
//		// then insert data for next month-year
//		if (previousMonthTxnCheck(monYr)) {
//			// Transfer total stock to next month
//			List<CounterStockManageSnapVo> stocks = snap.findStockOfOutlet(outletId, monYr);
//			LOGGER.info("Previuos stock : " + stocks.size());
//			saveStocksData(calculateOutletOpeningStock(YearMonth.now(), stocks, outletId));
//		}
//		List<StockEntryVo> entryForAdjustmentNext = iStockEntryRepository.entryForAdjustment(YearMonth.now().toString(),
//				outletId, stockStatus);
//		for (StockEntryVo each : entryForAdjustmentNext) {
//			LOGGER.info("Next Year month" + YearMonth.now().toString() + " ========== each entry ======>> "
//					+ each.getId() + " ========== getActivityType ======>> " + each.getActivityType());
//			if (each.getActivityType().equals("stock")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.stock_entry, true);
//			} else if (each.getActivityType().equals("Purchase")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.purchase_entry, true);
//			} else if (each.getActivityType().equals("Sale")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_for_adj, true);
//			} else if (each.getActivityType().equalsIgnoreCase("Damage")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.damage_entry, true);
//			} else if (each.getActivityType().equalsIgnoreCase("purchase_return")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.purchase_return, true);
//			} else if (each.getActivityType().equalsIgnoreCase("sale_return")) {
//				LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//				stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_return, true);
//			}
//		}
//		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Valdation OK", null), HttpStatus.OK);
//	}
//
//	// ===================== For Snap Repo ========
//
//	public static boolean previousMonthTxnCheck(String currMonYr) {
//		YearMonth input = YearMonth.parse(currMonYr);
//		int com = input.compareTo(YearMonth.now());
//		System.out.println("input==>" + input + "dateToBeConsidered  =>" + com);
//		if (com == -1) {
//			return true;
//		}
//		return false;
//	}
//
//	public static List<CounterStockManageSnapVo> calculateOutletOpeningStock(YearMonth yearMonth,
//			List<CounterStockManageSnapVo> stocks, Long outletId) {
//		List<CounterStockManageSnapVo> desiredOutletStocks = stocks.stream()
//				.filter(e -> e.getOutlet().getId().equals(outletId)).collect(Collectors.toList());
//		LocalDate firstDayOfYearMonth = yearMonth.atDay(1);
//		Date currentDate = java.sql.Date.valueOf(firstDayOfYearMonth);
//		return desiredOutletStocks.stream()
//				.map(e -> CounterStockManageSnapVo.builder().outlet(e.getOutlet()).productId(e.getProductId())
//						.openingStock(e.getClosingStock()).openingStockAmount(e.getClosingStockAmount())
//						.closingStock(e.getClosingStock()).closingStockAmount(e.getClosingStockAmount()).purchase(0L)
//						.purchaseAmount("0.0").purchaseReturn(0L).purchaseReturnAmount("0.0").sale(0L).saleAmount("0.0")
//						.saleReturn(0L).saleReturnAmount("0.0").damage(0L).damageAmount("0.0")
//						.categoryName(e.getCategoryName()).subCategoryName(e.getSubCategoryName()).createdBy(1L)
//						.createdOn(currentDate).updatedBy(1L).updatedOn(currentDate).updateType("system")
//						.transactionDate(currentDate).build())
//				.collect(Collectors.toList());
//	}
//
//	public void saveStocksData(List<CounterStockManageSnapVo> stocks) {
//		snap.saveAll(stocks);
//	}
//
//}