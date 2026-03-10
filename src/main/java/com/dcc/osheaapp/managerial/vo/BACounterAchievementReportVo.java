package com.dcc.osheaapp.managerial.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

@Entity
public class BACounterAchievementReportVo {

    @Id
    private Long target_id;

    private String outlet_name;

    private String outlet_code;

    private String ba_name;

    private String ba_code;

    private BigDecimal colorTargetAmount = BigDecimal.ZERO;

    private BigDecimal skinTargetAmount = BigDecimal.ZERO;

    private BigDecimal skinAchieved = BigDecimal.ZERO;
    private BigDecimal colorAchieved = BigDecimal.ZERO;

    private BigDecimal cumulativePercentage;

    @Transient
    private BigDecimal totalTargetPerDay = BigDecimal.ZERO;



    public BACounterAchievementReportVo() {
		super();
	}

	public BACounterAchievementReportVo(Long target_id, String outlet_name, String outlet_code, String ba_name,
			String ba_code, BigDecimal colorTargetAmount, BigDecimal skinTargetAmount, BigDecimal skinAchieved,
			BigDecimal colorAchieved, BigDecimal cumulativePercentage, BigDecimal totalTargetPerDay) {
		super();
		this.target_id = target_id;
		this.outlet_name = outlet_name;
		this.outlet_code = outlet_code;
		this.ba_name = ba_name;
		this.ba_code = ba_code;
		this.colorTargetAmount = colorTargetAmount;
		this.skinTargetAmount = skinTargetAmount;
		this.skinAchieved = skinAchieved;
		this.colorAchieved = colorAchieved;
		this.cumulativePercentage = cumulativePercentage;
		this.totalTargetPerDay = totalTargetPerDay;
		this.init();
	}
    
    @PostLoad
    private void init() {
      this.calculateTargetsPerDay();
    }

	public void calculateTargetsPerDay() {
        BigDecimal delColorTarget = this.getColorTargetAmount().subtract(this.getColorAchieved());
        BigDecimal delSkinAchieved = this.getSkinTargetAmount().subtract(this.getSkinAchieved());
        int period =
                Period.between(LocalDate.now(), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))
                        .getDays();
        BigDecimal daysRemaining = BigDecimal.valueOf(++period);
        BigDecimal divisor = daysRemaining.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : daysRemaining;
        this.totalTargetPerDay = delColorTarget.divide(divisor,RoundingMode.HALF_DOWN).add(delSkinAchieved.divide(divisor, RoundingMode.HALF_DOWN));
//        this.skinTargetPerDay = delSkinAchieved.divide(divisor, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getColorAchieved() {
        return colorAchieved;
    }

    public BigDecimal getSkinAchieved() {
        return skinAchieved;
    }

    public BigDecimal getColorTargetAmount() {
        return colorTargetAmount;
    }

    public BigDecimal getSkinTargetAmount() {
        return skinTargetAmount;
    }

    public Long getTarget_id() {
        return target_id;
    }

    public void setTarget_id(Long target_id) {
        this.target_id = target_id;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getOutlet_code() {
        return outlet_code;
    }

    public void setOutlet_code(String outlet_code) {
        this.outlet_code = outlet_code;
    }

    public String getBa_name() {
        return ba_name;
    }

    public void setBa_name(String ba_name) {
        this.ba_name = ba_name;
    }

    public String getBa_code() {
        return ba_code;
    }

    public void setBa_code(String ba_code) {
        this.ba_code = ba_code;
    }

    public void setColorTargetAmount(BigDecimal colorTargetAmount) {
        this.colorTargetAmount = colorTargetAmount;
    }

    public void setSkinTargetAmount(BigDecimal skinTargetAmount) {
        this.skinTargetAmount = skinTargetAmount;
    }

    public void setSkinAchieved(BigDecimal skinAchieved) {
        this.skinAchieved = skinAchieved;
    }

    public void setColorAchieved(BigDecimal colorAchieved) {
        this.colorAchieved = colorAchieved;
    }

    public BigDecimal getCumulativePercentage() {
        return cumulativePercentage;
    }

    public void setCumulativePercentage(BigDecimal cumulativePercentage) {
        this.cumulativePercentage = cumulativePercentage;
    }

    public BigDecimal getTotalTargetPerDay() {
        return totalTargetPerDay;
    }

    public void setTotalTargetPerDay(BigDecimal totalTargetPerDay) {
        this.totalTargetPerDay = totalTargetPerDay;
    }
}
