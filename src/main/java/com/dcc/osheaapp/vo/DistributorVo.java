package com.dcc.osheaapp.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "distributor_mst")
@EntityListeners(AuditingEntityListener.class)
public class DistributorVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "distributor_name")
	private String distributorName;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "region")
	private String region;

	@Column(name = "address")
	private String address;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "pan_no")
	private String pan;

	@Column(name = "gstin")
	private String gstin;

	@Column(name = "credit_duration")
	private Long creditDu;

	@Column(name = "stockist_type")
	private String stockist;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_type")
	private UserTypeVo userType;

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserDetailsVo userId;

	@OneToOne
	@JoinColumn(name = "company_zone")
	private DropdownMasterVo companyZone;

	@Column(name = "user_pinCode")
	private Long pinCode;
	
	@Transient
	private List<Long> userIds;
	
	@Column(name = "created_on", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdOn;

	@Column(name = "created_by", updatable = false)
	private Long createdBy;

	@Column(name = "updated_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedOn;

	@Column(name = "updated_by")
	private Long updatedBy;

	public DistributorVo() {
	}

	public DistributorVo(String distributorName, Boolean isActive, String contactNumber, UserTypeVo userType,
			UserDetailsVo userId, DropdownMasterVo companyZone) {
		super();
		this.distributorName = distributorName;
		this.isActive = isActive;
		this.contactNumber = contactNumber;
		this.userType = userType;
		this.userId = userId;
		this.companyZone = companyZone;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public Long getCreditDu() {
		return creditDu;
	}

	public void setCreditDu(Long creditDu) {
		this.creditDu = creditDu;
	}

	public String getStockist() {
		return stockist;
	}

	public void setStockist(String stockist) {
		this.stockist = stockist;
	}

	public UserTypeVo getUserType() {
		return userType;
	}

	public void setUserType(UserTypeVo userType) {
		this.userType = userType;
	}

	public DropdownMasterVo getCompanyZone() {
		return companyZone;
	}

	public void setCompanyZone(DropdownMasterVo companyZone) {
		this.companyZone = companyZone;
	}

	public Long getPinCode() {
		return pinCode;
	}

	public void setPinCode(Long pinCode) {
		this.pinCode = pinCode;
	}

	public UserDetailsVo getUserId() {
		return userId;
	}

	public void setUserId(UserDetailsVo userId) {
		this.userId = userId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "DistributorVo [id=" + id + ", distributorName=" + distributorName + ", isActive=" + isActive + "]";
	}
}
