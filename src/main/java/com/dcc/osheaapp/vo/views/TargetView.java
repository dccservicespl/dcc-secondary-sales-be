package com.dcc.osheaapp.vo.views;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

public interface TargetView {

  Long getId();

  Long getZoneId();
  String getZone();
  Long getBaId();

  String getBaName();

  String getBaCode();

  String getDivision();

  Long getOutletId();

  String getOutletName();

  String getOutletCode();

  Month getMonth();

  int getYear();

  BigDecimal getColorTargetAmount();

  BigDecimal getSkinTargetAmount();

  BigDecimal getColorsAchieved();

  BigDecimal getSkinAchieved();

  LocalDateTime getCreatedOn();

  LocalDateTime getUpdatedOn();
}
