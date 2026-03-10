package com.dcc.osheaapp.leaderboard.domain.model;

import com.dcc.osheaapp.vo.BaTarget;
import com.dcc.osheaapp.vo.CompanyZoneVo;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Leaderboard {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private UserDetailsVo user;

  @OneToOne
  @JoinColumn(name = "achievement_id")
  private BaTarget achievement;

  private BigDecimal score;

  private Integer rank;

  @OneToOne
  @JoinColumn(name = "zone")
  private DropdownMasterVo zone;

  private String yearMonth;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdOn;

  @Column(nullable = false)
  @UpdateTimestamp
  private Date updatedOn;

  public Leaderboard() {}

  public Long getId() {
    return id;
  }

  public BigDecimal getScore() {
    return score;
  }

  public Integer getRank() {
    return rank;
  }

  public DropdownMasterVo getZone() {
    return zone;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public Leaderboard setId(Long id) {
    this.id = id;
    return this;
  }

  public BaTarget getAchievement() {
    return achievement;
  }

  public Leaderboard setAchievement(BaTarget achievement) {
    this.achievement = achievement;
    return this;
  }

  public Leaderboard setScore(BigDecimal score) {
    this.score = score;
    return this;
  }

  public Leaderboard setRank(Integer rank) {
    this.rank = rank;
    return this;
  }

  public Leaderboard setZone(DropdownMasterVo zone) {
    this.zone = zone;
    return this;
  }

  public YearMonth getYearMonth() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    return YearMonth.parse(this.yearMonth, formatter);
  }

  public Leaderboard setYearMonth(String yearMonth) {
    this.yearMonth = yearMonth;
    return this;
  }

  public UserDetailsVo getUser() {
    return user;
  }

  public Leaderboard setUser(UserDetailsVo user) {
    this.user = user;
    return this;
  }
}
