package com.dcc.osheaapp.leaderboard.application.useCases;

import static org.springframework.data.jpa.domain.Specification.where;

import com.dcc.osheaapp.common.service.Util;
import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.leaderboard.domain.repository.LeaderBoardRepository;
import com.dcc.osheaapp.leaderboard.domain.repository.LeaderboardSpecifications;
import com.dcc.osheaapp.leaderboard.domain.service.LeaderboardService;
import com.dcc.osheaapp.repository.BaTargetSpecifications;
import com.dcc.osheaapp.repository.IBaTargetRepository;
import com.dcc.osheaapp.service.TargetService;
import com.dcc.osheaapp.vo.BaTarget;
import com.dcc.osheaapp.vo.CompanyZoneVo;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RefreshLeaderboardService {
  private static final Logger LOGGER = LogManager.getLogger(RefreshLeaderboardService.class);
  private final LeaderboardService _leaderboardService;
  private final LeaderBoardRepository _leaderBoardRepository;
  private final IBaTargetRepository _targetRepository;

  public RefreshLeaderboardService(
      LeaderboardService _leaderboardService,
      LeaderBoardRepository _leaderBoardRepository,
      IBaTargetRepository _targetRepository) {
    this._leaderboardService = _leaderboardService;
    this._leaderBoardRepository = _leaderBoardRepository;
    this._targetRepository = _targetRepository;
  }

  public List<Leaderboard> refresh(DropdownMasterVo zone, YearMonth yearMonth) {
    List<Leaderboard> rankings = getExistingLeaderboards(zone, yearMonth);
    List<BaTarget> targets = getExistingTargets(zone, yearMonth);
    LOGGER.info("====>> refresh Leaderboard ==>>zone ==>"+zone+" yearMonth== >"+yearMonth +" targets size=="+targets.size());
    List<Leaderboard> prepersistedLeaderboard = new ArrayList<>();
    rankings.sort(Comparator.comparingLong(e -> e.getUser().getId()));
    for (BaTarget target : targets) {
      int index =
          Util.binarySearch(
              rankings.stream().map(e -> e.getUser().getId()).collect(Collectors.toList()),
              target.getBaId());

      if (index == -1) {
        Leaderboard rank = getFreshLeaderboard(zone, yearMonth, target);
        prepersistedLeaderboard.add(rank);
        continue;
      }
      ;
      Leaderboard rank = rankings.get(index);
      rank.setScore(LeaderboardService.getScore(target));
      prepersistedLeaderboard.add(rank);
    }
    return _leaderBoardRepository.saveAll(
        LeaderboardService.getUpdatedRanks(prepersistedLeaderboard));
  }

  private List<BaTarget> getExistingTargets(DropdownMasterVo zone, YearMonth yearMonth) {
    return _targetRepository.findAll(
        where(BaTargetSpecifications.withZoneAlt(zone))
            .and(
                BaTargetSpecifications.fromYear(yearMonth.getYear())
                    .and(BaTargetSpecifications.fromMonth(yearMonth.getMonth()))));
  }

  private List<Leaderboard> getExistingLeaderboards(DropdownMasterVo zone, YearMonth yearMonth) {
    return _leaderBoardRepository.findAll(
        where(LeaderboardSpecifications.fromZone(zone))
            .and(LeaderboardSpecifications.fromYearMonth(yearMonth)),
        Sort.by(Sort.Direction.ASC, "score"));
  }

  private Leaderboard getFreshLeaderboard(
          DropdownMasterVo zone, YearMonth yearMonth, BaTarget target) {
    Leaderboard rank = new Leaderboard();
    UserDetailsVo user = new UserDetailsVo();
    user.setId(target.getBaId());
    return rank.setUser(user)
        .setId(null)
        .setAchievement(target)
        .setYearMonth(yearMonth.toString())
        .setZone(zone)
        .setScore(target.getCumulativeAchievementScore())
        .setRank(null);
  }
}
