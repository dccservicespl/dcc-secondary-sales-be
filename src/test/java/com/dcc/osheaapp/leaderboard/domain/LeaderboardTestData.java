package com.dcc.osheaapp.leaderboard.domain;

import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardTestData {

  public static List<Leaderboard> oneLeaderboardData() {
    return new ArrayList<>(List.of(new Leaderboard().setScore(BigDecimal.TEN)));
  }

  public static List<Leaderboard> duplicatedBlankLeaderboard() {
    return new ArrayList<>(
        List.of(
            new Leaderboard[] {
              new Leaderboard().setId(1L).setScore(BigDecimal.ZERO),
              new Leaderboard().setId(2L).setScore(BigDecimal.ZERO),
              new Leaderboard().setId(3L).setScore(BigDecimal.ZERO),
              new Leaderboard().setId(4L).setScore(BigDecimal.ZERO)
            }));
  }

  public static List<Leaderboard> duplicatedLeaderboard() {
    return new ArrayList<>(
        List.of(
            new Leaderboard[] {
              new Leaderboard().setId(1L).setScore(BigDecimal.valueOf(76.4)),
              new Leaderboard().setId(2L).setScore(BigDecimal.valueOf(87.4)),
              new Leaderboard().setId(3L).setScore(BigDecimal.valueOf(100)),
              new Leaderboard().setId(3L).setScore(BigDecimal.valueOf(90)),
              new Leaderboard().setId(4L).setScore(BigDecimal.valueOf(90)),
              new Leaderboard().setId(5L).setScore(BigDecimal.valueOf(90)),
              new Leaderboard().setId(6L).setScore(BigDecimal.valueOf(65)),
              new Leaderboard().setId(7L).setScore(BigDecimal.valueOf(65)),
              new Leaderboard().setId(8L).setScore(BigDecimal.valueOf(85)),
            }));
  }
}
