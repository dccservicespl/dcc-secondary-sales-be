package com.dcc.osheaapp.leaderboard.domain.service;

import com.dcc.osheaapp.leaderboard.domain.LeaderboardTestData;
import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LeaderboardServiceTest {

  @Test
  void updated_ranking_must_care_for_duplicate_scores() {
    List<Leaderboard> l1 =
        LeaderboardService.getUpdatedRanks(LeaderboardTestData.duplicatedBlankLeaderboard());
    Assertions.assertThat(l1).allSatisfy(e -> Assertions.assertThat(e.getRank()).isEqualTo(1));

    List<Leaderboard> l2 =
        LeaderboardService.getUpdatedRanks(LeaderboardTestData.duplicatedLeaderboard());
    Assertions.assertThat(l2.stream().map(Leaderboard::getRank).collect(Collectors.toList()))
        .containsExactly(1, 2, 2, 2, 3, 4, 5, 6, 6);

    List<Leaderboard> l3 = LeaderboardService.getUpdatedRanks(new ArrayList<>());
    Assertions.assertThat(l3).isEmpty();

    List<Leaderboard> l4 =
        LeaderboardService.getUpdatedRanks(LeaderboardTestData.oneLeaderboardData());
    Assertions.assertThat(l4.stream().map(Leaderboard::getRank)).containsExactly(1);
  }
}
