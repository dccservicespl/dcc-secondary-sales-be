package com.dcc.osheaapp.vo;

import com.dcc.osheaapp.common.model.BaseEntity;
import com.dcc.osheaapp.service.TargetService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Entity(name = "sync_achievement")
public class SyncAchievement extends BaseEntity {

    @Transient private final int TOTAL_POSSIBLE_SCORE = 200;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "ba_id")
    private Long baId;
    @Column(nullable = false, name = "outlet_id")
    private Long outletId;
    @Column(nullable = false, name = "zone_id")
    private Long zoneId;
    @Column(nullable = false, name = "zone" )
    private String zone;
    @Column(nullable = false, name = "color_target_amount")
    private BigDecimal colorTargetAmount = BigDecimal.ZERO;
    @Column(nullable = false, name = "skin_target_amount")
    private BigDecimal skinTargetAmount = BigDecimal.ZERO;
    @Column(name = "color_achieved")
    private BigDecimal colorAchieved = BigDecimal.ZERO;
    @Column(name = "skin_achieved")
    private BigDecimal skinAchieved = BigDecimal.ZERO;

    @Column(nullable = false , name ="month")
//    @Enumerated(EnumType.STRING)
    private String month;
    @Column(nullable = false, name="year")
    private int year;


    @Transient private BigDecimal colorTargetPerDay = BigDecimal.ZERO;

    @Transient private BigDecimal skinTargetPerDay = BigDecimal.ZERO;
    @Transient private BigDecimal pendingSkinAchievementAmount = BigDecimal.ZERO;

    @Transient private BigDecimal pendingColorAchievementAmount = BigDecimal.ZERO;

    @Transient private BigDecimal colorAchievementTillToday = BigDecimal.ZERO;
    @Transient private BigDecimal skinAchievementTillToday = BigDecimal.ZERO;
    @Transient private BigDecimal colorAchievementScore = BigDecimal.ZERO;
    @Transient private BigDecimal skinAchievementScore = BigDecimal.ZERO;
    @Transient private BigDecimal cumulativeAchievement;
    @Transient private BigDecimal cumulativeAchievementScore = BigDecimal.ZERO;
    private static final Logger LOGGER = LogManager.getLogger(TargetService.class);



    public SyncAchievement() {
    }

    public SyncAchievement(Long id, Long baId, Long outletId, Long zoneId, String zone,
                           BigDecimal colorTargetAmount, BigDecimal skinTargetAmount, BigDecimal colorAchieved,
                           BigDecimal skinAchieved, String month, int year) {
        this.id = id;
        this.baId = baId;
        this.outletId = outletId;
        this.zoneId = zoneId;
        this.zone = zone;
        this.colorTargetAmount = colorTargetAmount;
        this.skinTargetAmount = skinTargetAmount;
        this.colorAchieved = colorAchieved;
        this.skinAchieved = skinAchieved;
        this.month = month;
        this.year = year;
        this.init();
    }

    @PostLoad
    private void init() {
        this.calculateTargetsPerDay();
//        this.calculatePendingAchievements();
        this.calculateColorAchievementScore();
        this.calculateSkinAchievementScore();
        this.calculateCumulativeAchievementAmount();
        this.calculateCumulativeAchievementScore();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBaId() {
        return baId;
    }

    public void setBaId(Long baId) {
        this.baId = baId;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public BigDecimal getColorTargetAmount() {
        return colorTargetAmount;
    }

    public void setColorTargetAmount(BigDecimal colorTargetAmount) {
        if (colorTargetAmount == null)
            throw new IllegalArgumentException("Color Target Amount cannot be null.");
        if (colorTargetAmount.signum() == -1)
            throw new IllegalArgumentException("Color Target Amount cannot be negative.");
        this.colorTargetAmount = colorTargetAmount;
    }

    public BigDecimal getSkinTargetAmount() {
        return skinTargetAmount;
    }

    public void setSkinTargetAmount(BigDecimal skinTargetAmount) {
        if (skinTargetAmount != null && skinTargetAmount.signum() == -1)
            throw new IllegalArgumentException("Skin Target Amount cannot be negative.");
        this.skinTargetAmount = skinTargetAmount == null ? BigDecimal.ZERO : skinTargetAmount;
    }

    public BigDecimal getColorAchieved() {
        return colorAchieved;
    }

    public void setColorAchieved(BigDecimal colorAchieved) {
        if (colorAchieved != null && colorAchieved.signum() == -1)
            throw new IllegalArgumentException("Achieved Color target amount cannot be negative");
        this.colorAchieved = colorAchieved == null ? BigDecimal.ZERO : colorAchieved;
    }

    public BigDecimal getSkinAchieved() {
        return skinAchieved;
    }

    public void setSkinAchieved(BigDecimal skinAchieved) {
        if (skinAchieved != null && skinAchieved.signum() == -1)
            throw new IllegalArgumentException("Achieved Skin target amount cannot be negative");
        this.skinAchieved = skinAchieved == null ? BigDecimal.ZERO : skinAchieved;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private void calculateCumulativeAchievementAmount() {
        this.cumulativeAchievement = this.getSkinAchieved().add(this.getColorAchieved());
    }

    public void calculateColorAchievementScore() {
        BigDecimal target = this.colorTargetAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : this.colorTargetAmount;
        this.colorAchievementScore =
                this.getColorAchieved()
                        .divide(
                                target,
                                RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
    }
    public void calculateSkinAchievementScore() {

        BigDecimal target = this.skinTargetAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : this.skinTargetAmount;
        this.skinAchievementScore =
                this.getSkinAchieved()
                        .divide(
                                target,
                                RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
    }
    public void calculateCumulativeAchievementScore() {
        this.cumulativeAchievementScore =
                (this.skinAchievementScore.add(this.colorAchievementScore))
                        .divide(new BigDecimal(TOTAL_POSSIBLE_SCORE), RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100));
    }

    public void calculateTargetsPerDay() {
        BigDecimal delColorTarget = this.getColorTargetAmount().subtract(this.getColorAchieved()).abs();
        BigDecimal delSkinAchieved = this.getSkinTargetAmount().subtract(this.getSkinAchieved()).abs();
        int period =
                Period.between(LocalDate.now(), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))
                        .getDays();
        BigDecimal daysRemaining = BigDecimal.valueOf(++period);
        BigDecimal divisor = daysRemaining.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : daysRemaining;
        this.colorTargetPerDay = delColorTarget.divide(divisor,2, RoundingMode.HALF_DOWN);
        this.skinTargetPerDay = delSkinAchieved.divide(divisor,2, RoundingMode.HALF_DOWN);
    }

    public void calculatePendingAchievements() {
//        BigDecimal pendingSkin = this.getSkinTargetAmount().subtract(this.getSkinAchieved());
//        //Changes for value mis match use for abs()
//        this.pendingSkinAchievementAmount = (pendingSkin.signum() < 0) ?
//                pendingSkin.negate() : pendingSkin;
//        BigDecimal pendingColor =  this.getColorTargetAmount().subtract(this.getColorAchieved());
//
//        this.pendingColorAchievementAmount =
////        new BigDecimal(this.getColorTargetAmount().subtract(this.getColorAchieved()).toString());
//                (pendingColor.signum() < 0) ?
//                        pendingColor.negate() : pendingColor;
    }

    public void setColorTargetPerDay(BigDecimal colorTargetPerDay) {
        this.colorTargetPerDay = colorTargetPerDay;
    }

    public void setSkinTargetPerDay(BigDecimal skinTargetPerDay) {
        this.skinTargetPerDay = skinTargetPerDay;
    }

    public void setPendingSkinAchievementAmount(BigDecimal pendingSkinAchievementAmount) {
        this.pendingSkinAchievementAmount = pendingSkinAchievementAmount;
    }

    public void setPendingColorAchievementAmount(BigDecimal pendingColorAchievementAmount) {
        this.pendingColorAchievementAmount = pendingColorAchievementAmount;
    }

    public BigDecimal getPendingSkinAchievementAmount() {
        return pendingSkinAchievementAmount;
    }

    public BigDecimal getPendingColorAchievementAmount() {
        return pendingColorAchievementAmount;
    }

    public BigDecimal getColorAchievementTillToday() {
        return colorAchievementTillToday;
    }
    public BigDecimal getSkinAchievementTillToday() {
        return skinAchievementTillToday;
    }

    public void setColorAchievementTillToday(BigDecimal colorAchievementTillToday) {
        this.colorAchievementTillToday = colorAchievementTillToday;
    }
    public void setSkinAchievementTillToday(BigDecimal skinAchievementTillToday) {
        this.skinAchievementTillToday = skinAchievementTillToday;
    }
    public BigDecimal getColorTargetPerDay() {
        return colorTargetPerDay;
    }

    public BigDecimal getSkinTargetPerDay() {
        return skinTargetPerDay;
    }

    @Override
    public String toString() {
        return "SyncAchievement{" +
                "id=" + id +
                ", baId=" + baId +
                ", outletId=" + outletId +
                ", zoneId=" + zoneId +
                ", zone='" + zone + '\'' +
                ", colorTargetAmount=" + colorTargetAmount +
                ", skinTargetAmount=" + skinTargetAmount +
                ", colorAchieved=" + colorAchieved +
                ", skinAchieved=" + skinAchieved +
                ", month=" + month +
                ", year=" + year +
                '}';
    }


}
