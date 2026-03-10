package com.dcc.osheaapp.leaderboard.domain.service;

import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.vo.BaTarget;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeaderboardService {

  /**
   * For calculative score for ranking
   *
   * @param target
   * @return leaderboard score
   */
  public static BigDecimal getScore(BaTarget target) {
    return target.getCumulativeAchievementScore();
  }

  /**
   * ranks the leaderboard using cumulative score. same scorer will have the same rank.
   *
   * @param leaderboards
   * @return newly ranked leaderboard
   */
  public static List<Leaderboard> getUpdatedRanks(List<Leaderboard> leaderboards) {
    List<Leaderboard> cloned = new ArrayList<>(leaderboards);
    cloned.sort(Comparator.comparing(Leaderboard::getScore, Comparator.reverseOrder()));

    if (cloned.isEmpty()) return leaderboards;
    if (cloned.size() == 1) {
      cloned.get(0).setRank(1);
      return cloned;
    }

    cloned.get(0).setRank(1);
    for (int i = 1; i < cloned.size(); i++) {
      Leaderboard prev = cloned.get(i - 1);
      Leaderboard current = cloned.get(i);
      if (prev.getScore().equals(current.getScore())) current.setRank(prev.getRank());
      else current.setRank(prev.getRank() + 1);
    }

    return cloned;
  }
}
