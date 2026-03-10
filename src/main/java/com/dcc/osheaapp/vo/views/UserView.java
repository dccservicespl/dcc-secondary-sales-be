package com.dcc.osheaapp.vo.views;

import org.hibernate.annotations.Immutable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "user_view")
@Immutable
@Embeddable
public class UserView {
   @Id
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

   private String createdOn;


   public Long getUserId() {
      return userId;
   }

   public UserView setUserId(Long userId) {
      this.userId = userId;
      return this;
   }

   public String getAdhaar() {
      return adhaar;
   }

   public UserView setAdhaar(String adhaar) {
      this.adhaar = adhaar;
      return this;
   }

   public String getAddress() {
      return address;
   }

   public UserView setAddress(String address) {
      this.address = address;
      return this;
   }

   public String getBancAccNo() {
      return bancAccNo;
   }

   public UserView setBancAccNo(String bancAccNo) {
      this.bancAccNo = bancAccNo;
      return this;
   }

   public String getBankBranch() {
      return bankBranch;
   }

   public UserView setBankBranch(String bankBranch) {
      this.bankBranch = bankBranch;
      return this;
   }

   public String getBankName() {
      return bankName;
   }

   public UserView setBankName(String bankName) {
      this.bankName = bankName;
      return this;
   }

   public String getCity() {
      return city;
   }

   public UserView setCity(String city) {
      this.city = city;
      return this;
   }

   public String getContactNo() {
      return contactNo;
   }

   public UserView setContactNo(String contactNo) {
      this.contactNo = contactNo;
      return this;
   }

   public String getCreatedOn() {
      return createdOn;
   }

   public UserView setCreatedOn(String createdOn) {
      this.createdOn = createdOn;
      return this;
   }

   public String getCountry() {
      return country;
   }

   public UserView setCountry(String country) {
      this.country = country;
      return this;
   }

   public LocalDate getDateOfJoining() {
      return dateOfJoining;
   }

   public UserView setDateOfJoining(LocalDate dateOfJoining) {
      this.dateOfJoining = dateOfJoining;
      return this;
   }

   public String getFullName() {
      return fullName;
   }

   public UserView setFullName(String fullName) {
      this.fullName = fullName;
      return this;
   }

   public String getIfsc() {
      return ifsc;
   }

   public UserView setIfsc(String ifsc) {
      this.ifsc = ifsc;
      return this;
   }

   public boolean isActive() {
      return isActive;
   }

   public UserView setActive(boolean active) {
      isActive = active;
      return this;
   }

   public String getPan() {
      return pan;
   }

   public UserView setPan(String pan) {
      this.pan = pan;
      return this;
   }

   public Long getPin() {
      return pin;
   }

   public UserView setPin(Long pin) {
      this.pin = pin;
      return this;
   }

   public LocalDate getReleaseDate() {
      return releaseDate;
   }

   public UserView setReleaseDate(LocalDate releaseDate) {
      this.releaseDate = releaseDate;
      return this;
   }

   public String getSalary() {
      return salary;
   }

   public UserView setSalary(String salary) {
      this.salary = salary;
      return this;
   }

   public String getUserState() {
      return userState;
   }

   public UserView setUserState(String userState) {
      this.userState = userState;
      return this;
   }

   public Long getCompanyZoneId() {
      return companyZoneId;
   }

   public UserView setCompanyZoneId(Long companyZoneId) {
      this.companyZoneId = companyZoneId;
      return this;
   }

   public String getCompanyZone() {
      return companyZone;
   }

   public UserView setCompanyZone(String companyZone) {
      this.companyZone = companyZone;
      return this;
   }

   public Long getDivisionId() {
      return divisionId;
   }

   public UserView setDivisionId(Long divisionId) {
      this.divisionId = divisionId;
      return this;
   }

   public String getDivision() {
      return division;
   }

   public UserView setDivision(String division) {
      this.division = division;
      return this;
   }

   public Long getCredId() {
      return credId;
   }

   public UserView setCredId(Long credId) {
      this.credId = credId;
      return this;
   }

   public String getUsername() {
      return username;
   }

   public UserView setUsername(String username) {
      this.username = username;
      return this;
   }

   public Long getUserTypeId() {
      return userTypeId;
   }

   public UserView setUserTypeId(Long userTypeId) {
      this.userTypeId = userTypeId;
      return this;
   }

   public String getUserType() {
      return userType;
   }

   public UserView setUserType(String userType) {
      this.userType = userType;
      return this;
   }
}
