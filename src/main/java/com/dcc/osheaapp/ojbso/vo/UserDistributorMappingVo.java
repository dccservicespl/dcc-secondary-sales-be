package com.dcc.osheaapp.ojbso.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dcc.osheaapp.vo.DistributorVo;
import com.dcc.osheaapp.vo.UserDetailsVo;

@Entity
@Table(name = "so_distributor_mapping")
@EntityListeners(AuditingEntityListener.class)
public class UserDistributorMappingVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    
    @OneToOne // (cascade = CascadeType.MERGE )
	@JoinColumn(name = "so_id")
    private UserDetailsVo soId;
    
    @OneToOne // (cascade = CascadeType.MERGE )
	@JoinColumn(name = "distributor_id")
    private DistributorVo distributorId;
    
    @Column(name = "is_active")
	private Boolean isActive;

    @Column(name = "updated_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedOn;

	@Column(name = "updated_by")
	private Long updatedBy;
	
	public UserDistributorMappingVo() {
		
	}

	public UserDistributorMappingVo(UserDetailsVo soId, DistributorVo distributorId, Boolean isActive, Date updatedOn,
			Long updatedBy) {
		this.soId = soId;
		this.distributorId = distributorId;
		this.isActive = isActive;
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDetailsVo getSoId() {
		return soId;
	}

	public void setSoId(UserDetailsVo soId) {
		this.soId = soId;
	}

	public DistributorVo getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(DistributorVo distributorId) {
		this.distributorId = distributorId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
		return "UserDistributorMappingVo [id=" + id + ", soId=" + soId + ", distributorId=" + distributorId
				+ ", isActive=" + isActive + "]";
	}
}
