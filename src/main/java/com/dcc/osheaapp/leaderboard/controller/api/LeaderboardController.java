package com.dcc.osheaapp.leaderboard.controller.api;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.leaderboard.controller.handler.internal.RequestHandler;
import com.dcc.osheaapp.repository.BaPerformanceViewRepo;
import com.dcc.osheaapp.repository.IUserChainRepository;
import com.dcc.osheaapp.vo.views.UserChain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
    originPatterns = {"*"},
    allowedHeaders = "*",
    maxAge = 4800,
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/leaderboard")
@Tag(description = "API related to BA leaderboard", name = "Leaderboard")
public class LeaderboardController {

  @Autowired private RequestHandler handler;


  @Operation(summary = "Top N ranks", description = "Get top N BA ranks")
  @RequestMapping(value = "/top/{count}/{zoneId}/{yearMonth}", method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> topNRanks(
      @PathVariable("count") Integer count,
      @PathVariable("zoneId") Long zoneId,
      @PathVariable("yearMonth") YearMonth yearMonth) {
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(
            200,
            "SUCCESS",
            "Target Fetched Successfully.",
            handler.handleTopNRanksFetch(count, zoneId, yearMonth)),
        HttpStatus.OK);
  }

  @Operation(summary = "Top N ranks", description = "Get top N BA ranks")
  @RequestMapping(value = "/topAndAbove/{userId}/{zoneId}/{yearMonth}", method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> topAndAbove(
      @PathVariable("userId") Long userId,
      @PathVariable("zoneId") Long zoneId,
      @PathVariable("yearMonth") YearMonth yearMonth,
      @RequestParam("top") Integer topCount,
      @RequestParam("above") Integer aboveCount) {
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(
            200,
            "SUCCESS",
            "Target Fetched Successfully.",
            handler.handleTopAndAboveRanksFetch(topCount, aboveCount, zoneId, yearMonth, userId)),
        HttpStatus.OK);
  }

  @Operation(summary = "User's rank details", description = "User's rank details")
  @RequestMapping(value = "/{zoneId}/{yearMonth}/{userId}", method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> leaderboardDetails(
      @PathVariable("userId") Long userId,
      @PathVariable("zoneId") Long zoneId,
      @PathVariable("yearMonth") YearMonth yearMonth) {
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(
            200,
            "SUCCESS",
            "Target Fetched Successfully.",
            handler.handleUserRankFetch(zoneId, yearMonth, userId)),
        HttpStatus.OK);
  }
}
