package com.dcc.osheaapp.report.baPerformance.service;

import com.dcc.osheaapp.report.common.model.UserChainFlat;

public class PerformanceDto {
    private String zone;
    private UserChainFlat userChain;
    private String distributorName;
    private String distributorCode;
    private String region;
    private String counterCode;
    private String counterName;
    private String counterType;
    private String baName;
    private String baErpId;
    private String baSalary;
    private String doj;
    private String leftDate;
    private String month;
    private String lastMonthClStock;
    private String opStock;
    private String stockDifference;
    private String targetSkin;
    private String targetColor;
    private String targetTotal;
    private String achievementSkinSkin;
    private String achievementColor;
    private String achievementTotal;
    private String percentageAchievementSkin;
    private String percentageAchievementColor;
    private String percentageAchievementTotal;

    private String  rank;

    public String getRank() {
        return rank;
    }

    public PerformanceDto setRank(String rank) {
        this.rank = rank;
        return  this;
    }

    public String getZone() {
        return zone;
    }

    public PerformanceDto setZone(String zone) {
        this.zone = zone;
        return this;
    }

    public UserChainFlat getUserChain() {
        return userChain;
    }

    public PerformanceDto setUserChain(UserChainFlat userChain) {
        this.userChain = userChain;
        return this;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public PerformanceDto setDistributorName(String distributorName) {
        this.distributorName = distributorName;
        return this;
    }

    public String getDistributorCode() {
        return distributorCode;
    }

    public PerformanceDto setDistributorCode(String distributorCode) {
        this.distributorCode = distributorCode;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public PerformanceDto setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public PerformanceDto setCounterCode(String counterCode) {
        this.counterCode = counterCode;
        return this;
    }

    public String getCounterName() {
        return counterName;
    }

    public PerformanceDto setCounterName(String counterName) {
        this.counterName = counterName;
        return this;
    }

    public String getCounterType() {
        return counterType;
    }

    public PerformanceDto setCounterType(String counterType) {
        this.counterType = counterType;
        return this;
    }

    public String getAchievementSkinSkin() {
        return achievementSkinSkin;
    }

    public void setAchievementSkinSkin(String achievementSkinSkin) {
        this.achievementSkinSkin = achievementSkinSkin;
    }

    public String getBaName() {
        return baName;
    }

    public PerformanceDto setBaName(String baName) {
        this.baName = baName;
        return this;
    }

    public String getBaErpId() {
        return baErpId;
    }

    public PerformanceDto setBaErpId(String baErpId) {
        this.baErpId = baErpId;
        return this;
    }

    public String getBaSalary() {
        return baSalary;
    }

    public PerformanceDto setBaSalary(String baSalary) {
        this.baSalary = baSalary;
        return this;
    }

    public String getDoj() {
        return doj;
    }

    public PerformanceDto setDoj(String doj) {
        this.doj = doj;
        return this;
    }

    public String getLeftDate() {
        return leftDate;
    }

    public PerformanceDto setLeftDate(String leftDate) {
        this.leftDate = leftDate;
        return this;
    }

    public String getMonth() {
        return month;
    }

    public PerformanceDto setMonth(String month) {
        this.month = month;
        return this;
    }

    public String getLastMonthClStock() {
        return lastMonthClStock;
    }

    public PerformanceDto setLastMonthClStock(String lastMonthClStock) {
        this.lastMonthClStock = lastMonthClStock;
        return this;
    }

    public String getOpStock() {
        return opStock;
    }

    public PerformanceDto setOpStock(String opStock) {
        this.opStock = opStock;
        return this;
    }

    public String getStockDifference() {
        return stockDifference;
    }

    public PerformanceDto setStockDifference(String stockDifference) {
        this.stockDifference = stockDifference;
        return this;
    }

    public String getTargetSkin() {
        return targetSkin;
    }

    public PerformanceDto setTargetSkin(String targetSkin) {
        this.targetSkin = targetSkin;
        return this;
    }

    public String getTargetColor() {
        return targetColor;
    }

    public PerformanceDto setTargetColor(String targetColor) {
        this.targetColor = targetColor;
        return this;
    }

    public String getTargetTotal() {
        return targetTotal;
    }

    public PerformanceDto setTargetTotal(String targetTotal) {
        this.targetTotal = targetTotal;
        return this;
    }

    public String getAchievementSkin() {
        return achievementSkinSkin;
    }

    public PerformanceDto setAchievementSkin(String achievementSkinSkin) {
        this.achievementSkinSkin = achievementSkinSkin;
        return this;
    }

    public String getAchievementColor() {
        return achievementColor;
    }

    public PerformanceDto setAchievementColor(String achievementColor) {
        this.achievementColor = achievementColor;
        return this;
    }

    public String getAchievementTotal() {
        return achievementTotal;
    }

    public PerformanceDto setAchievementTotal(String achievementTotal) {
        this.achievementTotal = achievementTotal;
        return this;
    }

    public String getPercentageAchievementSkin() {
        return percentageAchievementSkin;
    }

    public PerformanceDto setPercentageAchievementSkin(String percentageAchievementSkin) {
        this.percentageAchievementSkin = percentageAchievementSkin;
        return this;
    }

    public String getPercentageAchievementColor() {
        return percentageAchievementColor;
    }

    public PerformanceDto setPercentageAchievementColor(String percentageAchievementColor) {
        this.percentageAchievementColor = percentageAchievementColor;
        return this;
    }

    public String getPercentageAchievementTotal() {
        return percentageAchievementTotal;
    }

    public PerformanceDto setPercentageAchievementTotal(String percentageAchievementTotal) {
        this.percentageAchievementTotal = percentageAchievementTotal;
        return this;
    }
}
