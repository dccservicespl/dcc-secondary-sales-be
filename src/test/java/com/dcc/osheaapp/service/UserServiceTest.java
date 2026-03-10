package com.dcc.osheaapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.dcc.osheaapp.repository.BaPerformanceViewRepo;
import com.dcc.osheaapp.repository.BaPerformanceViewSpecifications;
import com.dcc.osheaapp.repository.UserViewRepository;
import com.dcc.osheaapp.repository.UserViewSpecifications;
import com.dcc.osheaapp.vo.views.BaPerformanceView;
import com.dcc.osheaapp.vo.views.UserView;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class UserServiceTest {

  @Autowired UserViewRepository repository;

  @Autowired BaPerformanceViewRepo perfRepo;

  @Test
  public void user_chain_returns_the_chain_of_associations() {}

//  @Test
  public void user_view_specification_test() {
    List<UserView> res = repository.findAll(UserViewSpecifications.ofType("BA"));
    assertThat(res).allSatisfy(e -> assertThat(e.getUserType()).isEqualTo("BA"));

    List<BaPerformanceView> perfs =
        perfRepo.findAll(
            BaPerformanceViewSpecifications.ofUserType("BA")
                .and(
                    BaPerformanceViewSpecifications.fromMonth(YearMonth.now().getMonth())
                        .and(BaPerformanceViewSpecifications.fromYear(YearMonth.now().getYear()))));

    assertThat(perfs).allSatisfy(e -> assertThat(e.getUserType()).isEqualTo("BA"));
    assertThat(perfs).allSatisfy(e -> assertThat(e.getMonth()).isEqualTo(Month.NOVEMBER));
    assertThat(perfs).allSatisfy(e -> assertThat(e.getYear()).isEqualTo(YearMonth.now().getYear()));
  }
}
