package com.dcc.osheaapp.vo.views;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity(name = "ba_performance_view")
@Immutable
public class BaPerformanceView {



  @Id private Long id;
  private Long outletId;
  private String outletCode;
  private String outletName;
  private String outletType;

  private BigDecimal colorAchieved = BigDecimal.ZERO;
  private BigDecimal colorTargetAmount = BigDecimal.ZERO;
  private BigDecimal skinAchieved = BigDecimal.ZERO;
  private BigDecimal skinTargetAmount = BigDecimal.ZERO;

  @Enumerated(EnumType.STRING)
  private Month month;

  private int year;

  private Long userId;

  private String adhaar;
  private String address;
  private String bancAccNo;
  private String bankBranch;
  private String bankName;
  private String city;
  private String contactNo;
  private String country;
  private LocalDate dateOfJoining;
  private String fullName;
  private String ifsc;
  private boolean isActive;
  private String pan;
  private Long pin;
  private LocalDate releaseDate;
  private String salary;
  private String userState;
  private Long companyZoneId;
  private String companyZone;
  private Long divisionId;
  private String division;
  private Long credId;
  private String username;
  private Long userTypeId;
  private String userType;

  private BigDecimal openingStock = BigDecimal.ZERO;
  private BigDecimal lastMonthClosingStock = BigDecimal.ZERO;

  @Transient
  private String rank;



  @Transient
  private List<UserChain> chains = new ArrayList<>();

  public List<UserChain> getChains() {
    return chains;
  }

  public BaPerformanceView setChains(List<UserChain> chains) {
    this.chains = chains;
    return this;
  }

  public Long getId() {
    return id;
  }

  public BaPerformanceView setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getOutletId() {
    return outletId;
  }

  public BaPerformanceView setOutletId(Long outletId) {
    this.outletId = outletId;
    return this;
  }

  public String getOutletCode() {
    return outletCode;
  }

  public BaPerformanceView setOutletCode(String outletCode) {
    this.outletCode = outletCode;
    return this;
  }

  public String getOutletName() {
    return outletName;
  }

  public BaPerformanceView setOutletName(String outletName) {
    this.outletName = outletName;
    return this;
  }

  public String getOutletType() {
    return outletType;
  }

  public BaPerformanceView setOutletType(String outletType) {
    this.outletType = outletType;
    return this;
  }

  public BigDecimal getColorAchieved() {
    return colorAchieved;
  }

  public BaPerformanceView setColorAchieved(BigDecimal colorAchieved) {
    this.colorAchieved = colorAchieved;
    return this;
  }

  public BigDecimal getColorTargetAmount() {
    return colorTargetAmount;
  }

  public BaPerformanceView setColorTargetAmount(BigDecimal colorTargetAmount) {
    this.colorTargetAmount = colorTargetAmount;
    return this;
  }

  public BigDecimal getSkinAchieved() {
    return skinAchieved;
  }

  public BaPerformanceView setSkinAchieved(BigDecimal skinAchieved) {
    this.skinAchieved = skinAchieved;
    return this;
  }

  public BigDecimal getSkinTargetAmount() {
    return skinTargetAmount;
  }

  public BaPerformanceView setSkinTargetAmount(BigDecimal skinTargetAmount) {
    this.skinTargetAmount = skinTargetAmount;
    return this;
  }

  public Month getMonth() {
    return month;
  }

  public BaPerformanceView setMonth(Month month) {
    this.month = month;
    return this;
  }

  public int getYear() {
    return year;
  }

  public BaPerformanceView setYear(int year) {
    this.year = year;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public BaPerformanceView setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Optional<String> getAdhaar() {
    return Optional.ofNullable(adhaar);
  }

  public BaPerformanceView setAdhaar(String adhaar) {
    this.adhaar = adhaar;
    return this;
  }

  public Optional<String> getAddress() {
    return Optional.ofNullable(address);
  }

  public BaPerformanceView setAddress(String address) {
    this.address = address;
    return this;
  }

  public Optional<String> getBancAccNo() {
    return Optional.ofNullable(bancAccNo);
  }

  public BaPerformanceView setBancAccNo(String bancAccNo) {
    this.bancAccNo = bancAccNo;
    return this;
  }

  public Optional<String> getBankBranch() {
    return Optional.ofNullable(bankBranch);
  }

  public BaPerformanceView setBankBranch(String bankBranch) {
    this.bankBranch = bankBranch;
    return this;
  }

  public Optional<String> getBankName() {
    return Optional.ofNullable(bankName);
  }

  public BaPerformanceView setBankName(String bankName) {
    this.bankName = bankName;
    return this;
  }

  public Optional<String> getCity() {
    return Optional.ofNullable(city);
  }

  public BaPerformanceView setCity(String city) {
    this.city = city;
    return this;
  }

  public String getContactNo() {
    return contactNo;
  }

  public BaPerformanceView setContactNo(String contactNo) {
    this.contactNo = contactNo;
    return this;
  }

  public Optional<String> getCountry() {
    return Optional.ofNullable(country);
  }

  public BaPerformanceView setCountry(String country) {
    this.country = country;
    return this;
  }

  public LocalDate getDateOfJoining() {
    return dateOfJoining;
  }

  public BaPerformanceView setDateOfJoining(LocalDate dateOfJoining) {
    this.dateOfJoining = dateOfJoining;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public BaPerformanceView setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  public Optional<String> getIfsc() {
    return Optional.ofNullable(ifsc);
  }

  public BaPerformanceView setIfsc(String ifsc) {
    this.ifsc = ifsc;
    return this;
  }

  public boolean isActive() {
    return isActive;
  }

  public BaPerformanceView setActive(boolean active) {
    isActive = active;
    return this;
  }

  public Optional<String> getPan() {
    return Optional.ofNullable(pan);
  }

  public BaPerformanceView setPan(String pan) {
    this.pan = pan;
    return this;
  }

  public Optional<Long> getPin() {
    return Optional.ofNullable(pin);
  }

  public BaPerformanceView setPin(Long pin) {
    this.pin = pin;
    return this;
  }

  public Optional<LocalDate> getReleaseDate() {
    return Optional.ofNullable(releaseDate);
  }

  public BaPerformanceView setReleaseDate(LocalDate releaseDate) {
    this.releaseDate = releaseDate;
    return this;
  }

  public Optional<String> getSalary() {
    return Optional.ofNullable(salary);
  }

  public BaPerformanceView setSalary(String salary) {
    this.salary = salary;
    return this;
  }

  public String getUserState() {
    return userState;
  }

  public BaPerformanceView setUserState(String userState) {
    this.userState = userState;
    return this;
  }

  public Long getCompanyZoneId() {
    return companyZoneId;
  }

  public BaPerformanceView setCompanyZoneId(Long companyZoneId) {
    this.companyZoneId = companyZoneId;
    return this;
  }

  public String getCompanyZone() {
    return companyZone;
  }

  public BaPerformanceView setCompanyZone(String companyZone) {
    this.companyZone = companyZone;
    return this;
  }

  public Long getDivisionId() {
    return divisionId;
  }

  public BaPerformanceView setDivisionId(Long divisionId) {
    this.divisionId = divisionId;
    return this;
  }

  public String getDivision() {
    return division;
  }

  public BaPerformanceView setDivision(String division) {
    this.division = division;
    return this;
  }

  public Long getCredId() {
    return credId;
  }

  public BaPerformanceView setCredId(Long credId) {
    this.credId = credId;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public BaPerformanceView setUsername(String username) {
    this.username = username;
    return this;
  }

  public Long getUserTypeId() {
    return userTypeId;
  }

  public BaPerformanceView setUserTypeId(Long userTypeId) {
    this.userTypeId = userTypeId;
    return this;
  }

  public String getUserType() {
    return userType;
  }

  public BaPerformanceView setUserType(String userType) {
    this.userType = userType;
    return this;
  }

  public BigDecimal getTotalTargetAmount() {
    return colorTargetAmount.add(skinTargetAmount);
  }

  public BigDecimal getTotalAchievementAmount() {
    return colorAchieved.add(skinAchieved);
  }

  public BigDecimal getColorAchievementPercentage() {
    return colorAchieved
        .divide(
            colorTargetAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : colorTargetAmount,
            RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }

  public BigDecimal getSkinAchievementPercentage() {
    return skinAchieved
        .divide(
            skinTargetAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : skinTargetAmount,
            RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }

  public BigDecimal getTotalAchievementPercentage() {
    return (getSkinAchievementPercentage()
        .add(getColorAchievementPercentage())
        .divide(BigDecimal.valueOf(200), RoundingMode.HALF_UP));
  }

  public BigDecimal getOpeningStock() {
    return openingStock;
  }

  public BaPerformanceView setOpeningStock(BigDecimal openingStock) {
    this.openingStock = openingStock;
    return this;
  }

  public BigDecimal getLastMonthClosingStock() {
    return lastMonthClosingStock;
  }

  public BaPerformanceView setLastMonthClosingStock(BigDecimal lastMonthClosingStock) {
    this.lastMonthClosingStock = lastMonthClosingStock;
    return this;
  }

  public BigDecimal getStockDifference() {
    return openingStock.subtract(lastMonthClosingStock);
  }

  public String  getRank() {
    return rank;
  }

  public BaPerformanceView setRank(String rank) {
    this.rank = rank;
    return  this;
  }


}



