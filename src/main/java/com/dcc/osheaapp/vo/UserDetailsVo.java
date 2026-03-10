package com.dcc.osheaapp.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_details")
@EntityListeners(AuditingEntityListener.class)
public class UserDetailsVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "contact_number", nullable = false, length = 10)
	private String contactNumber;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_type")
	private UserTypeVo userType;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_cred")
	private UserCredVo userCred;

	@Transient
	private List<OutletUserMappingVo> assotiateOutletList; // different mapping table

	@Transient
	private List<OutletVo> assotiateOutlet;

	@OneToOne
	@JoinColumn(name = "company_zone")
	private DropdownMasterVo companyZone;

	// @Column(name = "employee_type")
	@OneToOne
	@JoinColumn(name = "employee_type")
	private DropdownMasterVo employeeType; // BA Non Payroll / Payroll (NP/P) // diff master table

	// @Column(name = "reporting_to")
	@Transient
	private UserDetailsVo reportingTo; // different mapping table

	@Column(name = "user_address")
	private String address;

	@Column(name = "user_city")
	private String city;

	@Column(name = "user_state")
	private String state; // different master table

	@Column(name = "user_country")
	private String country; // different master table

	@Column(name = "user_pin")
	private Long pin;

	@Column(name = "date_of_joining", nullable = false)
	@Temporal(TemporalType.DATE)
	@CreatedDate
	private Date dateOfJoining;

	@Column(name = "release_date")
	@Temporal(TemporalType.DATE)
	private Date releaseDate;

	@Column(name = "user_salary")
	private String salary;

	@OneToOne
	@JoinColumn(name = "product_division")
	private DropdownMasterVo productDivision;

	@Column(name = "user_aadhar", length = 20)
	private String aadhar;

	@Column(name = "user_pan", length = 20)
	private String pan;

	@Column(name = "bank_name", length = 100)
	private String bankName;

	@Column(name = "bank_branch_name", length = 100)
	private String bankBranchName;

	@Column(name = "bank_account_no", length = 20)
	private String bankAccountNo;

	@Column(name = "ifsc_code")
	private String ifscCode;

	@Column(name = "is_active")
	private Boolean isActive;

	// Added by kousik for XL BACODE
	@Transient
	private String baCodeXl;

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

	@Transient
	private String userCode;


//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	@Transient
	private List<UserBeatsAssociation> beat;

	public UserDetailsVo() {
		super();
	}

	public UserDetailsVo(Long id) {
		this.id = id;
	}

	public UserDetailsVo(String fullName, String contactNumber, UserTypeVo userType, UserCredVo userCred,
			DropdownMasterVo companyZone, Date dateOfJoining, DropdownMasterVo productDivision, Boolean isActive,
			Date createdOn, Date updatedOn) {
		super();
		this.fullName = fullName;
		this.contactNumber = contactNumber;
		this.companyZone = companyZone;
		this.dateOfJoining = dateOfJoining;
		this.productDivision = productDivision;
		this.isActive = isActive;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
	}

	public Long getId() {
		return id;
	}

	public UserDetailsVo setId(Long id) {
		this.id = id;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public UserDetailsVo setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public UserDetailsVo setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
		return this;
	}

	public UserTypeVo getUserType() {
		return userType;
	}

	public UserDetailsVo setUserType(UserTypeVo userType) {
		this.userType = userType;
		return this;
	}

	public UserCredVo getUserCred() {
		return userCred;
	}

	public UserDetailsVo setUserCred(UserCredVo userCred) {
		this.userCred = userCred;
		return this;
	}

	public List<OutletUserMappingVo> getAssotiateOutletList() {
		return assotiateOutletList;
	}

	public UserDetailsVo setAssotiateOutletList(List<OutletUserMappingVo> assotiateOutletList) {
		this.assotiateOutletList = assotiateOutletList;
		return this;
	}

	public List<OutletVo> getAssotiateOutlet() {
		return assotiateOutlet;
	}

	public UserDetailsVo setAssotiateOutlet(List<OutletVo> assotiateOutlet) {
		this.assotiateOutlet = assotiateOutlet;
		return this;
	}

	public DropdownMasterVo getCompanyZone() {
		return companyZone;
	}

	public UserDetailsVo setCompanyZone(DropdownMasterVo companyZone) {
		this.companyZone = companyZone;
		return this;
	}

	public DropdownMasterVo getEmployeeType() {
		return employeeType;
	}

	public UserDetailsVo setEmployeeType(DropdownMasterVo employeeType) {
		this.employeeType = employeeType;
		return this;
	}

	public UserDetailsVo getReportingTo() {
		return reportingTo;
	}

	public UserDetailsVo setReportingTo(UserDetailsVo reportingTo) {
		this.reportingTo = reportingTo;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public UserDetailsVo setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getCity() {
		return city;
	}

	public UserDetailsVo setCity(String city) {
		this.city = city;
		return this;
	}

	public String getState() {
		return state;
	}

	public UserDetailsVo setState(String state) {
		this.state = state;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public UserDetailsVo setCountry(String country) {
		this.country = country;
		return this;
	}

	public Long getPin() {
		return pin;
	}

	public UserDetailsVo setPin(Long pin) {
		this.pin = pin;
		return this;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public UserDetailsVo setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
		return this;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public UserDetailsVo setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
		return this;
	}

	public String getSalary() {
		return salary;
	}

	public UserDetailsVo setSalary(String salary) {
		this.salary = salary;
		return this;
	}

	public DropdownMasterVo getProductDivision() {
		return productDivision;
	}

	public UserDetailsVo setProductDivision(DropdownMasterVo productDivision) {
		this.productDivision = productDivision;
		return this;
	}

	public String getAadhar() {
		return aadhar;
	}

	public UserDetailsVo setAadhar(String aadhar) {
		this.aadhar = aadhar;
		return this;
	}

	public String getPan() {
		return pan;
	}

	public UserDetailsVo setPan(String pan) {
		this.pan = pan;
		return this;
	}

	public String getBankName() {
		return bankName;
	}

	public UserDetailsVo setBankName(String bankName) {
		this.bankName = bankName;
		return this;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public UserDetailsVo setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
		return this;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public UserDetailsVo setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
		return this;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public UserDetailsVo setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
		return this;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public UserDetailsVo setIsActive(Boolean active) {
		isActive = active;
		return this;
	}

	public String getBaCodeXl() {
		return baCodeXl;
	}

	public UserDetailsVo setBaCodeXl(String baCodeXl) {
		this.baCodeXl = baCodeXl;
		return this;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public UserDetailsVo setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public UserDetailsVo setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public UserDetailsVo setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
		return this;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public UserDetailsVo setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
		return this;
	}

	public String getUserCode() {
		return userCode;
	}

	public UserDetailsVo setUserCode(String userCode) {
		this.userCode = userCode;
		return this;
	}



	public boolean ifBA() {
		return userType != null && userType.getUserType().equalsIgnoreCase("ba");
	}


	public boolean ifFloater() {
		return userType != null && userType.getUserType().equalsIgnoreCase("floater");
	}

	public List<UserBeatsAssociation> getBeat() {
		return beat;
	}

	public void setBeat(List<UserBeatsAssociation> beat) {
		this.beat = beat;
	}
}
