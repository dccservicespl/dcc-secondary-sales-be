package com.dcc.osheaapp.leaderboard.application.useCases;

import static org.springframework.data.jpa.domain.Specification.where;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.leaderboard.domain.repository.LeaderBoardRepository;
import com.dcc.osheaapp.leaderboard.domain.repository.LeaderboardSpecifications;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FetchRankingsService {

  private final LeaderBoardRepository _leaderboardRepository;

  @Autowired
  public FetchRankingsService(LeaderBoardRepository _leaderboardRepository) {
    this._leaderboardRepository = _leaderboardRepository;
  }

  /**
   * Top N number of ranked BA from a particular zone and month.
   *
   * @param count
   * @param zone
   * @param yearMonth
   * @return list of top N number of ba.
   */
  public List<Leaderboard> topN(Integer count, DropdownMasterVo zone, YearMonth yearMonth) {
    Pageable page = PageRequest.of(0, count);
    Page<Leaderboard> result =
        _leaderboardRepository.findAll(
            where(LeaderboardSpecifications.fromZone(zone))
                .and(LeaderboardSpecifications.fromYearMonth(yearMonth))
                .and(LeaderboardSpecifications.orderBy("rank", "asc")),
            page);
    return result.getContent();
  }

  /**
   * Get above N number of ranked people from a certain user.
   *
   * @param count
   * @param zone
   * @param yearMonth
   * @param user
   * @return
   */
  public List<Leaderboard> immediateTopN(
      Integer count, DropdownMasterVo zone, YearMonth yearMonth, UserDetailsVo user) {

    Leaderboard rank = getUserRank(zone, yearMonth, user);
    Pageable page = PageRequest.of(0, count);
    Page<Leaderboard> result =
        _leaderboardRepository.findAll(
            where(
                LeaderboardSpecifications.fromZone(rank.getZone())
                    .and(LeaderboardSpecifications.fromYearMonth(rank.getYearMonth()))
                    .and(
                        LeaderboardSpecifications.fetchNrowsAbove(
                            count, "rank", rank.getRank(), yearMonth, zone))
                    .and(LeaderboardSpecifications.orderBy("rank", "desc"))),
            page);

    return result.getContent().stream()
        .sorted(Comparator.comparingLong(Leaderboard::getRank))
        .collect(Collectors.toList());
  }

  public Leaderboard getUserRank(DropdownMasterVo zone, YearMonth yearMonth, UserDetailsVo user) {
    List<Leaderboard> ranks =
        _leaderboardRepository.findAll(
            where(
                LeaderboardSpecifications.fromZone(zone)
                    .and(LeaderboardSpecifications.fromYearMonth(yearMonth))
                    .and(LeaderboardSpecifications.withUser(user))));
    if (ranks.size() != 1) throw new OjbException(ErrorCode.NOT_FOUND, new Object[] {"Rank"});
    return ranks.get(0);
  }

  public BigDecimal getAmountToTop(DropdownMasterVo zone, YearMonth yearMonth, UserDetailsVo user) {
    List<Leaderboard> ranks = topN(1, zone, yearMonth);
    if (ranks.size() != 1) throw new OjbException(ErrorCode.NOT_FOUND, new Object[] {"Top Ranker"});
    Leaderboard topRanker = ranks.get(0);
    Leaderboard currentRanker = getUserRank(zone, yearMonth, user);
    return topRanker
        .getAchievement()
        .getCumulativeAchievement()
        .subtract(currentRanker.getAchievement().getCumulativeAchievement());
  }
}
