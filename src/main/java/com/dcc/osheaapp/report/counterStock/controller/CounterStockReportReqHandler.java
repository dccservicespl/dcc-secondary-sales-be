package com.dcc.osheaapp.report.counterStock.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.report.counterStock.service.CounterStockReportExportService;
import com.dcc.osheaapp.repository.CounterStockReportViewRepo;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.IOutletUserMappingRepository;
import com.dcc.osheaapp.service.UserChainService;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.OutletUserMappingVo;
import com.dcc.osheaapp.vo.views.CounterStockReportView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dcc.osheaapp.vo.views.Outlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CounterStockReportReqHandler {

  private static final Logger LOGGER = LogManager.getLogger(CounterStockReportReqHandler.class);
  private final CounterStockReportViewRepo counterStockReportViewRepo;
  private final CounterStockReportExportService counterStockReportExportService;
  private final UserChainService userChainService;
  private final ReportWSHandler wsHandler;

  private final IDropdownMastereRepository dropdownMastereRepository;
  private final IOutletUserMappingRepository outletUserMappingRepository;
  @PersistenceContext private EntityManager em;

  public CounterStockReportReqHandler(
          CounterStockReportViewRepo counterStockReportViewRepo,
          CounterStockReportExportService counterStockReportExportService,
          UserChainService userChainService,
          ReportWSHandler wsHandler, IDropdownMastereRepository dropdownMastereRepository,
          IOutletUserMappingRepository outletUserMappingRepository) {
    this.counterStockReportViewRepo = counterStockReportViewRepo;
    this.counterStockReportExportService = counterStockReportExportService;
    this.userChainService = userChainService;
    this.wsHandler = wsHandler;
      this.dropdownMastereRepository = dropdownMastereRepository;
      this.outletUserMappingRepository = outletUserMappingRepository;
  }

  public void exportReportThroughWs(CounterStockReportInputDto dto) {
    CompletableFuture.supplyAsync(
            () -> {
              try {
                return new ReportWSPayload(
                    "counter_stock_report_" + dto.getMonthYr(),
                    ((ByteArrayOutputStream) export(dto, new ByteArrayOutputStream()))
                        .toByteArray());
              } catch (IOException e) {
                wsHandler.sendReportErrNotification(
                    new ApiResponse(
                        500, "INTERNAL_SERVER_ERROR", "Error while generating report", null, 0));
                throw new RuntimeException(e);
              }
            })
        .thenAccept(wsHandler::sendReportReadyNotification)
        .exceptionally(
            (e) -> {
              e.printStackTrace();
              wsHandler.sendReportErrNotification(
                  new ApiResponse(
                      500, "INTERNAL_SERVER_ERROR", "Error while generating report", null, 0));
              return null;
            });
  }


  @Transactional(readOnly = true)
  public OutputStream export(CounterStockReportInputDto dto, OutputStream os) throws IOException {

    String whereClause = "";
    if (null != dto.getCompanyZone() && !dto.getCompanyZone().isEmpty()) {
      whereClause = " and o.company_zone = " + dto.getCompanyZone();
    }

    final long start1 = System.currentTimeMillis();
    List<CounterStockReportView> data = generateParallel(dto, whereClause);

    LOGGER.info("[Counter Stock Report] Data ingestion:: " + (System.currentTimeMillis() - start1));

    List<Long> distinctUser = data.stream().map(CounterStockReportView::getUserId).distinct().collect(Collectors.toList());
    Map<Long, UserChainFlat> chains = new HashMap<>();

    final long start = System.currentTimeMillis();
    Map<Long, UserChainFlat>  flattenedChains = userChainService.retrieveChains(distinctUser).stream().collect(Collectors.toMap(UserChainFlat::getBaId, e -> e));
    for(Long user: distinctUser) {
      UserChainFlat userChain = flattenedChains.get(user);
      if(userChain == null) {
        chains.put(user, new UserChainFlat());
      } else
        chains.put(user, flattenedChains.get(user));
    }
    final long elapsed = System.currentTimeMillis() - start;
    LOGGER.info("Elapsed::Chain Gen:: " + elapsed);

    for (CounterStockReportView each : data) {
      each.setUserChain(chains.get(each.getUserId()));
    }

    return counterStockReportExportService.export(data, os);
  }

  @Transactional(readOnly = true)
  public OutputStream export(ReportInputDto dto, OutputStream os) throws IOException {
    String whereClause = "";
    if (null != dto.getZone()) {
      whereClause += " and o.company_zone = " + dto.getZone();
    }
    if (dto.getStatus() != null) {
      whereClause += " and o.is_active = " + dto.getStatus();
    }

    final long start1 = System.currentTimeMillis();
    List<CounterStockReportView> data = generateParallel(dto, whereClause);

    LOGGER.info("[Counter Stock Report] Data ingestion:: " + (System.currentTimeMillis() - start1));

    List<Long> distinctUser = data.stream().map(CounterStockReportView::getUserId).distinct().collect(Collectors.toList());

    //Below code for get outlet id from List of data that user not associated.
//    List<String> nonAssociatedOutletId = data.stream()
//            .filter(i -> i.getUserId() == null) // Only include items where userId is null
//            .map(CounterStockReportView::getOutletId) // Extract outletId
//            .distinct()
//            .collect(Collectors.toList());

    List<Long> nonAssociatedOutletUserIds = data.stream()
            .filter(item -> item.getUserId() == null) // Filter where userId is null
            .map(item -> Long.valueOf(item.getOutletId())) // Convert outletId from String to Long
            .distinct()
            .collect(Collectors.toList());

    List<Long> userId = outletUserMappingRepository.nonAssociatedOutletUserId(nonAssociatedOutletUserIds);

    LOGGER.info("DistinctUser of mapped outlet---->", distinctUser);
    LOGGER.info("UserIds of non mapped outlet--->", userId);

    Set<Long> allUsersToFetchChainsFor = new HashSet<>();
    allUsersToFetchChainsFor.addAll(distinctUser);
    allUsersToFetchChainsFor.addAll(userId);

    LOGGER.info("All ba ids---> ", allUsersToFetchChainsFor);


    Map<Long, UserChainFlat> chains = new HashMap<>();

    final long start = System.currentTimeMillis();
//    Map<Long, UserChainFlat>  flattenedChains = userChainService.retrieveChains(distinctUser).stream().collect(Collectors.toMap(UserChainFlat::getBaId, e -> e));
//    for(Long user: distinctUser) {
//      UserChainFlat userChain = flattenedChains.get(user);
//      if(userChain == null) {
//        chains.put(user, new UserChainFlat());
//      } else
//        chains.put(user, flattenedChains.get(user));
//    }


    Map<Long, UserChainFlat> flattenedChains = userChainService.retrieveChains(new ArrayList<>(allUsersToFetchChainsFor))
            .stream()
            .collect(Collectors.toMap(UserChainFlat::getBaId, e -> e));

// Store chains for users, handle missing values
    for (Long user : allUsersToFetchChainsFor) {
      UserChainFlat userChain = flattenedChains.get(user);
      chains.put(user, userChain != null ? userChain : new UserChainFlat());
    }

    final long elapsed = System.currentTimeMillis() - start;
    LOGGER.info("Elapsed::Chain Gen:: " + elapsed);

    for (CounterStockReportView each : data) {
      if(each.getUserId() == null){
        Long outletId =Long.valueOf(each.getOutletId());
        Long  setUserId = outletUserMappingRepository.setChainOutletUserId(outletId);
        each.setUserChain(chains.get(setUserId));
      }
      else{
        each.setUserChain(chains.get(each.getUserId()));
      }

    }
    return counterStockReportExportService.export(data, os);
  }
  @Transactional(readOnly = true)
  public List<CounterStockReportView> generateParallel(CounterStockReportInputDto dto, String whereClause) {
    long start1 = System.currentTimeMillis();
    List<CounterStockReportView> data = new ArrayList<>();
    try (Stream<CounterStockReportView> employees = counterStockReportViewRepo.findReportStr(whereClause, dto.getMonthYr())) {
      employees.forEach(data::add);
    }
    return data;
  }

  @Transactional(readOnly = true)
  public List<CounterStockReportView> generateParallel(ReportInputDto dto, String whereClause) {
    long start1 = System.currentTimeMillis();
    List<CounterStockReportView> data = new ArrayList<>();
    try (Stream<CounterStockReportView> employees = counterStockReportViewRepo.findReportStr(whereClause, dto.getYearMonth())) {
      employees.forEach(data::add);
    }
    return data;
  }

  public String getCounterStockReportFilename(ReportInputDto dto) {
    DropdownMasterVo dropdownMasterVo = dropdownMastereRepository.findById(dto.getZone()).orElse(new DropdownMasterVo());
    return "counter_stock_report_" + dto.getYearMonth() + "_" + dropdownMasterVo.getFieldName() + ".xlsx";
  }
}
