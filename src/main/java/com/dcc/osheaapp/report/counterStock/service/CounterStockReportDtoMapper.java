//package com.dcc.osheaapp.report.counterStock.service;
//
//import com.dcc.osheaapp.report.common.UserChainDto;
//import com.dcc.osheaapp.service.UserChainService;
//import com.dcc.osheaapp.service.UserService;
//import com.dcc.osheaapp.vo.UserDetailsVo;
//import com.dcc.osheaapp.vo.views.BaPerformanceView;
//import com.dcc.osheaapp.vo.views.UserChain;
//import java.time.LocalDate;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//
//@Service
//public class CounterStockReportDtoMapper {
//   private final UserChainService userChainService;
//
//   private final UserService userService;
//
//   @PersistenceContext
//   private EntityManager em;
//
//  public CounterStockReportDtoMapper(UserChainService userChainService, UserService userService) {
//    this.userChainService = userChainService;
//    this.userService = userService;
//  }
//
//
//
//  public CategoryReportDto toDto(BaPerformanceView view) {
//    em.clear();
//    UserChainDto chainDto = userChainService.generateChainFlattened(view.getUserId());
//    CategoryReportDto dto = new CategoryReportDto();
//    return dto
//        .setZone(view.getCompanyZone())
//        .setUserChain(chainDto)
//        .setDistributorCode("")
//        .setDistributorName("")
//        .setRegion(view.getUserState())
//        .setCounterCode(view.getOutletCode())
//        .setCounterCode(view.getOutletName())
//        .setBaErpId(view.getUsername().split("@")[0])
//        .setBaName(view.getFullName())
//        .setBaSalary(view.getSalary().orElse(""))
//        .setDoj(view.getDateOfJoining().toString())
//        .setLeftDate(view.getReleaseDate().map(LocalDate::toString).orElse(""))
//        .setMonth(view.getMonth().toString())
//        .setLastMonthClStock(view.getLastMonthClosingStock().toString())
//        .setOpStock(view.getOpeningStock().toString())
//        .setStockDifference(view.getStockDifference().toString())
//        .setTargetSkin(view.getSkinTargetAmount().toString())
//        .setTargetColor(view.getColorTargetAmount().toString())
//        .setTargetTotal(view.getTotalTargetAmount().toString())
//        .setAchievementSkin(view.getSkinAchieved().toString())
//        .setAchievementColor(view.getColorAchieved().toString())
//        .setAchievementTotal(view.getTotalAchievementAmount().toString())
//        .setPercentageAchievementSkin(view.getSkinAchievementPercentage().toString())
//        .setPercentageAchievementColor(view.getColorAchievementPercentage().toString())
//        .setPercentageAchievementTotal(view.getTotalAchievementPercentage().toString());
//  }
//
//  /**
//   * Populates the userchain e.g., MD > NSM >...>BA etc recursively.
//   *
//   * @param chainMap
//   * @param userId
//   * @param dto
//   * @return
//   */
//  public UserChainDto populateUserChain(
//      Map<Long, UserChain> chainMap, Long userId, UserChainDto dto) {
//
//    UserChain user = chainMap.get(userId);
//    if (user == null) return dto;
//
//    if (user.getUserType().equals("BA")) {
//      UserDetailsVo ba = userService.findUserById(user.getUserId()).orElse(null);
//      dto.setBaName(user.getFullName()).setBaCode(ba == null ? null : ba.getUserCred().getCode());
//    }
//
//    if (user.getUserType().equals("SO")) {
//      dto.setSo(user.getFullName());
//    }
//
//    if (user.getUserType().equals("BDE")) {
//      dto.setBde(user.getFullName());
//    }
//
//    if (user.getUserType().equals("ASM")) {
//      dto.setAsm(user.getFullName());
//    }
//
//    if (user.getUserType().equals("ZSM")) {
//      dto.setZsm(user.getFullName());
//    }
//
//    // if anyone reports to MD... it will not be in the map as there is no association of MD
//    // further.
//    // checking if the user reports to the md.. in that case the md's name would be there in dto
//    if (user.getAssociatedUserType().equals("MD")) dto.setMd(user.getAssociatedUserName());
//
//    return populateUserChain(chainMap, user.getAssociatedUserId(), dto);
//  }
//}
