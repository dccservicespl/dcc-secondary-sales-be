package com.dcc.osheaapp.scheduled;

import com.dcc.osheaapp.service.StockAdjustmentService;
import com.dcc.osheaapp.service.StockTransferService;
import com.dcc.osheaapp.service.UserService;

import java.time.YearMonth;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class Workers {

	private static final Logger LOGGER = LogManager.getLogger(Workers.class);

	@Autowired
	UserService userService;

  @Autowired StockTransferService stockTransferService;

  // runs every day at 23:45.
  @Scheduled(cron = "0 58 23 * * ?", zone = "Asia/Kolkata")
  public void storeLogoutUsers() {
    LOGGER.info("Running scheduled task:: Logging out users ==============================>");

		CompletableFuture.supplyAsync(() -> {
			userService.storeLogoutLoggedInUsers(new Date());
			return null;
		}).thenAccept((e) -> {
			LOGGER.info("scheduled task:: LOGGED out users ==============================>");
		}).join();
	}

	@Scheduled(cron = "0 30 0 1 * ?", zone = "Asia/Kolkata")
	public void runMonthlyTask() {
		LOGGER.info("Running scheduled task:: Transferring stocks ==============================>");
		CompletableFuture.supplyAsync(() -> {
			LOGGER.info("[Transferring Stocks]:: " + YearMonth.now().minusMonths(1L).getMonth().name());
			return stockTransferService.transferAllOutletStocksForCurrentMonth();
		}).thenAccept((e) -> LOGGER.info("[Stocks Transferred] :: " + YearMonth.now())).join();
	}
}
