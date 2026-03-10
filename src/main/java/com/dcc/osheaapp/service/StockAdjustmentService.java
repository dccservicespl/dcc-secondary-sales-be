package com.dcc.osheaapp.service;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants.BA_Activity_Enum;
import com.dcc.osheaapp.repository.ICounterStockManageRepository;
import com.dcc.osheaapp.repository.IStockEntryRepository;
import com.dcc.osheaapp.vo.StockEntryVo;
import com.dcc.osheaapp.vo.dto.PocketMISInputVo;

@Service
public class StockAdjustmentService {

	private static final Logger LOGGER = LogManager.getLogger(StockAdjustmentService.class);

	private final StockService stockService;
	private final IStockEntryRepository iStockEntryRepository;
	private final ICounterStockManageRepository iCounterStockManageRepository;

	@Autowired
	public StockAdjustmentService(StockService stockService, IStockEntryRepository iStockEntryRepository,
			ICounterStockManageRepository iCounterStockManageRepository) {
		this.stockService = stockService;
		this.iStockEntryRepository = iStockEntryRepository;
		this.iCounterStockManageRepository = iCounterStockManageRepository;
	}

	public ResponseEntity<ApiResponse> callForAdjustment(PocketMISInputVo input) {
		// First Need to delete existing stock of that month year ===============
		// Pending ???????????
		
//		List<Long> outlets = iStockEntryRepository.outletList(input.getMonthYr());
//		LOGGER.info("callForAdjustment no of outlet == > "+outlets.size());
//		outlets.forEach(e -> {
		input.getOutlets().forEach(e -> {
			String monYr = input.getMonthYr();
			List<String> stockStatus = input.getStockStatus();
			List<StockEntryVo> entryForAdjustment = iStockEntryRepository.entryForAdjustment(monYr, e, stockStatus);
			for (StockEntryVo each : entryForAdjustment) {
				LOGGER.info(" ========== each entry ======>> " + each.getId() + " ========== getActivityType ======>> "
						+ each.getActivityType());
				if (each.getActivityType().equals("stock")) {
					LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
//					stockService.updatePocketMISAlt(each, 1L, BA_Activity_Enum.stock_entry, true );
					stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.stock_entry, true );
				} else if (each.getActivityType().equals("Purchase")) {
					LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
					stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.purchase_entry, true);
				} else if (each.getActivityType().equals("Sale")) {
					LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
					//stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_for_adj, true);
					stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_for_adj, true);
				} else if (each.getActivityType().equalsIgnoreCase("Damage")) {
					LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
					stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.damage_entry, true);
				} else if (each.getActivityType().equalsIgnoreCase("purchase_return")) {
					LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
					stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.purchase_return, true);
				} else if (each.getActivityType().equalsIgnoreCase("sale_return")) {
					LOGGER.info(" ========== getActivityType ======>> " + each.getActivityType());
					stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_return, true);
				}

			}
		});

		// then insert data for next month-year
		/*
		 * if(previousMonthTxnCheck(monYr)) { //Transfer total stock to next month
		 * List<CounterStockManageVo> stocks =
		 * iCounterStockManageRepository.findStockOfOutlet(outletId, monYr);
		 * LOGGER.info("Previuos stock : " + stocks.size());
		 * stockService.saveStocksData(StockTransferService.calculateOutletOpeningStock(
		 * YearMonth.now(), stocks, outletId)); } List<StockEntryVo>
		 * entryForAdjustmentNext =
		 * iStockEntryRepository.entryForAdjustment(YearMonth.now().toString(),
		 * outletId, stockStatus); for (StockEntryVo each : entryForAdjustmentNext) {
		 * LOGGER.info("Next Year month"+YearMonth.now().toString()+
		 * " ========== each entry ======>> "+each.getId()
		 * +" ========== getActivityType ======>> "+each.getActivityType()); if
		 * (each.getActivityType().equals("stock")) {
		 * LOGGER.info(" ========== getActivityType ======>> "+each.getActivityType());
		 * stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.stock_entry, true); }
		 * else if (each.getActivityType().equals("Purchase")) {
		 * LOGGER.info(" ========== getActivityType ======>> "+each.getActivityType());
		 * stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.purchase_entry,
		 * true); } else if (each.getActivityType().equals("Sale")) {
		 * LOGGER.info(" ========== getActivityType ======>> "+each.getActivityType());
		 * stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_for_adj, true);
		 * } else if (each.getActivityType().equalsIgnoreCase("Damage")) {
		 * LOGGER.info(" ========== getActivityType ======>> "+each.getActivityType());
		 * stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.damage_entry, true);
		 * } else if (each.getActivityType().equalsIgnoreCase("purchase_return")) {
		 * LOGGER.info(" ========== getActivityType ======>> "+each.getActivityType());
		 * stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.purchase_return,
		 * true); } else if (each.getActivityType().equalsIgnoreCase("sale_return")) {
		 * LOGGER.info(" ========== getActivityType ======>> "+each.getActivityType());
		 * stockService.updatePocketMIS(each, 1L, BA_Activity_Enum.sale_return, true); }
		 * }
		 */
		return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Valdation OK", null), HttpStatus.OK);
	}

	public static boolean previousMonthTxnCheck(String currMonYr) {
		YearMonth input = YearMonth.parse(currMonYr);
		int com = input.compareTo(YearMonth.now());
		System.out.println("input==>" + input + "dateToBeConsidered  =>" + com);
		if (com == -1) {
			return true;
		}
		return false;
	}

	public List<StockEntryVo> inactivateDrafts(Long outletId, YearMonth month) {
		List<StockEntryVo> existingEntries = iStockEntryRepository.allEntryForAdjustment(month.toString(),
				List.of("Draft"), outletId);
		List<StockEntryVo> inactiveEntries = existingEntries.stream()
				.map(e -> e.setStockStatus("Draft_Inactive").setActivityType("Stock_Inactive"))
				.collect(Collectors.toList());
		return iStockEntryRepository.saveAll(inactiveEntries);
	}
}