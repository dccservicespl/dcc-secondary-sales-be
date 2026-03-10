package com.dcc.osheaapp.leaderboard.controller.handler.internal;

import com.dcc.osheaapp.leaderboard.controller.handler.dto.LeaderboardDto;
import com.dcc.osheaapp.leaderboard.controller.handler.dto.TopAndAboveLeadershipListDto;
import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.leaderboard.application.useCases.FetchRankingsService;
import com.dcc.osheaapp.vo.CompanyZoneVo;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RequestHandler {

  private final FetchRankingsService _fetchRankingsService;

  public RequestHandler(FetchRankingsService _fetchRankingsService) {
    this._fetchRankingsService = _fetchRankingsService;
  }

  public List<Leaderboard> handleTopNRanksFetch(Integer count, Long zoneId, YearMonth yearMonth) {
    return _fetchRankingsService.topN(count, new DropdownMasterVo(zoneId, null,null,null), yearMonth);
  }

  public TopAndAboveLeadershipListDto handleTopAndAboveRanksFetch(
      Integer topCount, Integer aboveCount, Long zoneId, YearMonth yearMonth, Long userId) {

    UserDetailsVo user = new UserDetailsVo();
    user.setId(userId);
    List<Leaderboard> top =
        _fetchRankingsService.topN(topCount, new DropdownMasterVo(zoneId, null,null,null), yearMonth);
    List<Leaderboard> above =
        _fetchRankingsService.immediateTopN(
            aboveCount, new DropdownMasterVo(zoneId, null,null,null), yearMonth, user);
    return new TopAndAboveLeadershipListDto(new CompanyZoneVo(zoneId, null), yearMonth, top, above);
  }

  public LeaderboardDto handleUserRankFetch(Long zoneId, YearMonth yearMonth, Long userId) {
    UserDetailsVo user = new UserDetailsVo();
    user.setId(userId);
    DropdownMasterVo zone = new DropdownMasterVo(zoneId, null,null,null);
    Leaderboard rank = _fetchRankingsService.getUserRank(zone, yearMonth, user);
    BigDecimal amountForTheTop = _fetchRankingsService.getAmountToTop(zone, yearMonth, user);
    return new LeaderboardDto(rank, amountForTheTop);
  }

  public BigDecimal handlePendingAmountFetch(Long zoneId, YearMonth yearMonth, Long userId) {
    UserDetailsVo user = new UserDetailsVo();
    user.setId(userId);
    return _fetchRankingsService.getAmountToTop(new DropdownMasterVo(zoneId, null,null,null), yearMonth, user);
  }
}
