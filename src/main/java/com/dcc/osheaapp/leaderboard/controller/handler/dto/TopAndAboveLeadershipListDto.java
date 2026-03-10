package com.dcc.osheaapp.leaderboard.controller.handler.dto;

import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.vo.CompanyZoneVo;
import java.time.YearMonth;
import java.util.List;

public class TopAndAboveLeadershipListDto {
  public CompanyZoneVo zone;
  public YearMonth yearMonth;
  public List<Leaderboard> top;
  public List<Leaderboard> above;

  public TopAndAboveLeadershipListDto(
      CompanyZoneVo zone, YearMonth yearMonth, List<Leaderboard> top, List<Leaderboard> above) {
    this.zone = zone;
    this.yearMonth = yearMonth;
    this.top = top;
    this.above = above;
  }
}
