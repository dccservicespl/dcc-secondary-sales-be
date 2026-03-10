package com.dcc.osheaapp.service;

import com.dcc.osheaapp.report.baPerformance.controller.PerformanceReqHandler;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.repository.BaPerformanceViewRepo;
import com.dcc.osheaapp.vo.views.BaPerformanceView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Month;
import java.util.List;

import static com.dcc.osheaapp.repository.BaPerformanceViewSpecifications.*;

@SpringBootTest
class UserChainServiceTest {
  @Autowired UserChainService userChainService;

  @Autowired
  BaPerformanceViewRepo repo;

  @Autowired
  PerformanceReqHandler handler;

//  @Test
  public void expecting_a_chain_of_associated_user() {
    List<BaPerformanceView> data =
            repo.findAll(
                    ofUserType("BA").and(fromMonth(Month.NOVEMBER)).and(fromYear(2023)));
    for (BaPerformanceView view : data) {
      UserChainFlat chain = userChainService.generateChainFlattened(view.getUserId());
      System.out.println(chain.getMd());
    }
  }


//  @Test
  public void testing_api_handler() {

// List<UserChainDto> chain =    handler.export(new CategoryReportInputDto(11, 2023), null);
    System.out.println("done");
  }

  @Test
  void getAllActiveUserChain() {
//    userChainService.getAllActiveUserChain();
  }
}
