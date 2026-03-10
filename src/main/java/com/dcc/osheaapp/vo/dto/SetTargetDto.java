package com.dcc.osheaapp.vo.dto;

import java.math.BigDecimal;
import java.time.Month;

public class SetTargetDto {

  private Long zoneId;
  private String zone;
  private Long baId;

  private Long outletId;

  private Month month;

  private int year;

  private BigDecimal colorTargetAmount;

  private BigDecimal skinTargetAmount;

  private BigDecimal colorAchieved;

  private BigDecimal skinAchieved;

  public SetTargetDto() {}

  public SetTargetDto(Long zoneId, String zone, Long baId, Long outletId, Month month, int year, BigDecimal colorTargetAmount, BigDecimal skinTargetAmount, BigDecimal colorAchieved, BigDecimal skinAchieved) {
    this.zoneId = zoneId;
    this.zone = zone;
    this.baId = baId;
    this.outletId = outletId;
    this.month = month;
    this.year = year;
    this.colorTargetAmount = colorTargetAmount;
    this.skinTargetAmount = skinTargetAmount;
    this.colorAchieved = colorAchieved;
    this.skinAchieved = skinAchieved;
  }

  public Long getZoneId() {
    return zoneId;
  }

  public Long getBaId() {
    return baId;
  }

  public Long getOutletId() {
    return outletId;
  }

  public Month getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }

  public BigDecimal getColorTargetAmount() {
    return colorTargetAmount;
  }

  public BigDecimal getSkinTargetAmount() {
    return skinTargetAmount;
  }

  public BigDecimal getColorAchieved() {
    return colorAchieved;
  }

  public BigDecimal getSkinAchieved() {
    return skinAchieved;
  }

  public void setBaId(Long baId) {
    this.baId = baId;
  }

  public void setOutletId(Long outletId) {
    this.outletId = outletId;
  }

  public void setMonth(Month month) {
    this.month = month;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setColorTargetAmount(BigDecimal colorTargetAmount) {
    this.colorTargetAmount = colorTargetAmount;
  }

  public void setSkinTargetAmount(BigDecimal skinTargetAmount) {
    this.skinTargetAmount = skinTargetAmount;
  }

  public void setColorAchieved(BigDecimal colorAchieved) {
    this.colorAchieved = colorAchieved;
  }

  public void setSkinAchieved(BigDecimal skinAchieved) {
    this.skinAchieved = skinAchieved;
  }

  public void setZoneId(Long zoneId) {
    this.zoneId = zoneId;
  }

  public String getZone() {
    return zone;
  }

  public SetTargetDto setZone(String zone) {
    this.zone = zone;
    return this;
  }

  @Override
  public String toString() {
    return "SetTargetDto{" +
            "zoneId=" + zoneId +
            ", zone='" + zone + '\'' +
            ", baId=" + baId +
            ", outletId=" + outletId +
            ", month=" + month +
            ", year=" + year +
            ", colorTargetAmount=" + colorTargetAmount +
            ", skinTargetAmount=" + skinTargetAmount +
            ", colorAchieved=" + colorAchieved +
            ", skinAchieved=" + skinAchieved +
            '}';
  }
}
