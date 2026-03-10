package com.dcc.osheaapp.vo.views;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "user_chain_category_report_view")
@Immutable
public class UserChainCategoryReport implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String fullName;
	private String userType;
	private Long companyZone;
	private String companyZoneName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getCompanyZone() {
		return companyZone;
	}

	public void setCompanyZone(Long companyZone) {
		this.companyZone = companyZone;
	}

	public String getCompanyZoneName() {
		return companyZoneName;
	}

	public void setCompanyZoneName(String companyZoneName) {
		this.companyZoneName = companyZoneName;
	}
}
