package com.dcc.osheaapp.leaderboard.controller.handler.dto;

import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import java.math.BigDecimal;

public class LeaderboardDto {
  public Leaderboard rank;
  public BigDecimal achievementForTheTop;

  public LeaderboardDto(Leaderboard rank, BigDecimal achievementForTheTop) {
    this.rank = rank;
    this.achievementForTheTop = achievementForTheTop;
  }

  private BigDecimal getAchievementForTheTop() {
    return achievementForTheTop;
  }

  private Leaderboard getRank() {
    return rank;
  }
}
