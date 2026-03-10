package com.dcc.osheaapp.leaderboard.application.eventHandler;

import com.dcc.osheaapp.leaderboard.application.useCases.RefreshLeaderboardService;
import com.dcc.osheaapp.common.event.AchievementsSyncedEvent;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.concurrent.CompletableFuture;

import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
class AchievementSyncedEventHandler {

  private static final Logger LOGGER = LogManager.getLogger(AchievementSyncedEventHandler.class);
  private final RefreshLeaderboardService _refreshLeaderboardService;

  @Autowired
  public AchievementSyncedEventHandler(RefreshLeaderboardService _refreshLeaderboardService) {
    this._refreshLeaderboardService = _refreshLeaderboardService;
  }

  @EventListener
  private void handle(AchievementsSyncedEvent event) {
    CompletableFuture.supplyAsync(() ->  {refreshleaderboard(event); return null;}).thenAccept((e) -> LOGGER.info("Leaderboard Refreshed:: " + LocalDateTime.now()));
  }

  private void refreshleaderboard(AchievementsSyncedEvent event) {
    for(DropdownMasterVo zone: event.getZone()) {
      _refreshLeaderboardService.refresh(zone, YearMonth.now());
    }
  }
}
