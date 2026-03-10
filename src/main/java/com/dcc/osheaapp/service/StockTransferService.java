package com.dcc.osheaapp.service;

import com.dcc.osheaapp.common.event.Events;
import com.dcc.osheaapp.common.event.StockTransferEvent;
import com.dcc.osheaapp.common.model.LogExecutionTime;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.vo.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockTransferService {

	private static final Logger LOGGER = LogManager.getLogger(StockTransferService.class);
	private final StockService stockService;
	private final IStockEntryRepository stockEntryRepository;
	private final IStockEntryDtlRepository stockEntryDtlRepository;
	private final IOutletUserMappingRepository outletUserMappingRepository;
	private final ICounterStockManageRepository counterStockManageRepository;
	private final IOutletRepository outletRepository;
	private final IEventLogRepository eventLogRepository;
	private final StockAdjustmentService stockAdjustmentService;

	private final ApplicationEventPublisher publisher;

	@Autowired
	public StockTransferService(StockService stockService, IStockEntryRepository stockEntryRepository,
			IStockEntryDtlRepository stockEntryDtlRepository, IOutletUserMappingRepository outletUserMappingRepository,
			ICounterStockManageRepository counterStockManageRepository, IOutletRepository outletRepository,
			IEventLogRepository eventLogRepository, StockAdjustmentService stockAdjustmentService,
			ApplicationEventPublisher publisher) {
		this.stockService = stockService;
		this.stockEntryRepository = stockEntryRepository;
		this.stockEntryDtlRepository = stockEntryDtlRepository;
		this.outletUserMappingRepository = outletUserMappingRepository;
		this.counterStockManageRepository = counterStockManageRepository;
		this.outletRepository = outletRepository;
		this.eventLogRepository = eventLogRepository;
		this.stockAdjustmentService = stockAdjustmentService;
		this.publisher = publisher;
	}

	@LogExecutionTime
	public List<StockEntryVo> transferAllOutletStocksForCurrentMonth() {

		YearMonth targetMonth = YearMonth.now(ZoneId.of("Asia/Kolkata"));
		YearMonth sourceMonth = targetMonth.minusMonths(1L);

		LOGGER.info("[Transferring Stocks]:: " + targetMonth);
		EventLog log = eventLogRepository
				.findByEventTimeAndEventName(targetMonth.toString(), Events.STOCKS_TRANSFERRED.name()).orElse(null);
		if (log != null) {
			LOGGER.info("Stocks has already been transferred:: " + targetMonth);
			return null;
		}

		List<Long> activeOutlets = counterStockManageRepository.findAllMonthlyOutletStock(sourceMonth.toString())
				.stream().map(e -> e.getOutlet().getId()).distinct().collect(Collectors.toList());
		LOGGER.info("Stocks transferred for outlets:: " + activeOutlets.size());
		List<StockEntryVo> stocks = activeOutlets.parallelStream().map(e -> transferStocks(sourceMonth, targetMonth, e))
				.collect(Collectors.toList());
		publisher.publishEvent(new StockTransferEvent(this, LocalDateTime.now(ZoneId.of("Asia/Kolkata"))));
		return stocks;
	}

	public StockEntryVo transferStocks(YearMonth sourceMonth, YearMonth targetMonth, Long outlet) {
		long monthInterval = ChronoUnit.MONTHS.between(sourceMonth, targetMonth);
		if (monthInterval != 1L) {
			throw new IllegalArgumentException("Transfer of stocks only allowed for previous 1 month.");
		}

		LOGGER.info("[Transferring Stocks]:: " + "Outlet::  " + outlet);
		List<CounterStockManageVo> oldStocks = counterStockManageRepository
				.findMonthlyOutletStockOfAnOutlet(sourceMonth.toString(), outlet);
		List<OutletUserMappingVo> outletMapping = outletUserMappingRepository
				.findActiveExistingAssociatedOutlet(outlet);
		Long user = outletMapping.isEmpty() ? 1L : outletMapping.get(0).getAssotiatedUser();
		List<CounterStockManageVo> counterStocks = calculateOutletOpeningStock(targetMonth, oldStocks, outlet);
		StockEntryVo newEntry = createNewStockEntry(counterStocks, targetMonth, outlet, user);
		StockEntryVo stocks = saveNewStocks(counterStocks, newEntry);
		stockAdjustmentService.inactivateDrafts(outlet, sourceMonth);
		return stocks;
	}

	@Transactional
	private StockEntryVo saveNewStocks(List<CounterStockManageVo> counterStocks, StockEntryVo newEntry) {
		counterStockManageRepository.saveAll(counterStocks);
		List<StockEntryDtlVo> details = newEntry.getStockEntryDtlVo();
		details.forEach(e -> e.setStockEntryVo(newEntry));
		return stockEntryRepository.save(newEntry);
	}

	private StockEntryVo createNewStockEntry(List<CounterStockManageVo> oldStocks, YearMonth targetMonth, Long outlet,
			Long user) {
		LocalDate firstDayOfYearMonth = targetMonth.atDay(1);
		Date currentDate = java.sql.Date.valueOf(firstDayOfYearMonth);
		List<StockEntryDtlVo> newStockDtlEntries = createNewStockDtlEntries(oldStocks);
		double totalClosingStockAmount = newStockDtlEntries.stream().map(StockEntryDtlVo::getAmount)
				.mapToDouble(Double::parseDouble).reduce(0L, Double::sum);

		Long totalClosingStocks = newStockDtlEntries.stream().map(StockEntryDtlVo::getNoOfPcs).reduce(0L, Long::sum);

		return new StockEntryVo().setActivityType("stock").setCreatedBy(1L).setCreatedOn(currentDate)
				.setStockStatus("Submitted").setTotalAmountOfItem(Double.toString(totalClosingStockAmount))
				.setTotalNoOfItem(totalClosingStocks.toString()).setTransactionType("system").setUpdatedBy(1L)
				.setUpdatedOn(currentDate).setOutlet(new OutletVo().setId(outlet))
				.setUserId(new UserDetailsVo().setId(user)).setTransactionDate(currentDate)
				.setStockEntryDtlVo(newStockDtlEntries);

	}

	private List<StockEntryDtlVo> createNewStockDtlEntries(List<CounterStockManageVo> stocks) {
		return stocks.stream()
				.map(e -> new StockEntryDtlVo().setNoOfPcs(getClosingStock.apply(e.getOpeningStock()))
						.setNoOfPcsUpdated(0L).setAmount(getClosingStockAmount.apply(e.getOpeningStockAmount()))
						.setAmountUpdated("0.0").setProductId(e.getProductId()).setCategoryName(e.getCategoryName())
						.setSubCategoryName(e.getSubCategoryName()))
				.collect(Collectors.toList());
	}

	private List<CounterStockManageVo> calculateOutletOpeningStock(YearMonth yearMonth,
			List<CounterStockManageVo> stocks, Long outletId) {
		LocalDate firstDayOfYearMonth = yearMonth.atDay(1);
		Date currentDate = java.sql.Date.valueOf(firstDayOfYearMonth);

		// NOTE: closing stock is transferred as 0 if negative.
		return stocks.stream()
				.map(e -> CounterStockManageVo.builder().outlet(new OutletVo().setId(outletId))
						.productId(e.getProductId()).openingStock(getClosingStock.apply(e.getClosingStock()))
						.openingStockAmount(getClosingStockAmount.apply(e.getClosingStockAmount()))
						.closingStock(getClosingStock.apply(e.getClosingStock()))
						.closingStockAmount(getClosingStockAmount.apply(e.getClosingStockAmount())).purchase(0L)
						.purchaseAmount("0.0").purchaseReturn(0L).purchaseReturnAmount("0.0").sale(0L).saleAmount("0.0")
						.saleReturn(0L).saleReturnAmount("0.0").damage(0L).damageAmount("0.0")
						.categoryName(e.getCategoryName()).subCategoryName(e.getSubCategoryName()).createdBy(1L)
						.createdOn(currentDate).updatedBy(1L).updatedOn(currentDate).updateType("system")
						.transactionDate(currentDate).build())
				.collect(Collectors.toList());
	}

	private final Function<Long, Long> getClosingStock = e -> e.compareTo(0L) < 0 ? 0L : e;
	private final Function<String, String> getClosingStockAmount = e -> Double.compare(Double.parseDouble(e), 0.0) < 0
			? "0.0"
			: e;

	public Boolean isStocksTransferred(YearMonth yearMonth) {
		return eventLogRepository.findByEventTimeAndEventName(yearMonth.toString(), Events.STOCKS_TRANSFERRED.name())
				.isPresent();
	}
}
