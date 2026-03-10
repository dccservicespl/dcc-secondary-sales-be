package com.dcc.osheaapp.vo.views;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Entity
//public class SyncAchievementVo {
//
//    @Id
//    private Long id;
//    private LocalDateTime createdOn;
//    private LocalDateTime updatedOn;
//    private Long zoneId;
//    private  String zone;
//    private  Long baId;
//    private  String baName;
//    private  String baCode;
//    private Long divisionId;
//    private  String division;
//    private Long outletId;
//    private String outletName;
//    private String outletCode;
//    private String month;
//    private int year;
//    private BigDecimal colorTargetAmount;
//    private BigDecimal skinTargetAmount;
//    private BigDecimal colorsAchieved;
//    private BigDecimal skinAchieved;
//}

public interface SyncAchievementVo {
    Long getId();
    LocalDateTime getCreatedOn();
    LocalDateTime getUpdatedOn();
    Long getZoneId();
    String getZone();
    Long getBaId();
    String getBaName();
    String getBaCode();
    Long getDivisionId();
    String getDivision();
    Long getOutletId();
    String getOutletName();
    String getOutletCode();
    String getMonth();
    int getYear();
    BigDecimal getColorTargetAmount();
    BigDecimal getSkinTargetAmount();
    BigDecimal getColorsAchieved();
    BigDecimal getSkinAchieved();
}
