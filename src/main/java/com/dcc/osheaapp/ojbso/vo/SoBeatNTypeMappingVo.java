package com.dcc.osheaapp.ojbso.vo;

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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dcc.osheaapp.vo.BeatName;
import com.dcc.osheaapp.vo.UserDetailsVo;

@Entity
@Table(name = "so_Beat_type_mapping")
@EntityListeners(AuditingEntityListener.class)
public class SoBeatNTypeMappingVo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @OneToOne // (cascade = CascadeType.MERGE )
	@JoinColumn(name = "so_id")
    private UserDetailsVo soId;
    
    @OneToOne 
	@JoinColumn(name = "beat_id")
	private BeatName beatId;
    
    @OneToOne 
	@JoinColumn(name = "beat_type_id")
	private BeatTypeMst beatTypeId;
    
    @Column(name = "beat_and_type_name")
	private String beatAndTypeName;
    
    @Column(name = "active_flag")
	private boolean activeFlag = true;

    @Column(name = "created_on", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;
    
    @Column(name = "created_by", nullable = false, updatable = false)
	private Long createdBy;

	@Column(name = "updated_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedOn;

	@Column(name = "updated_by")
	private Long updatedBy;

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

	public BeatName getBeatId() {
		return beatId;
	}

	public void setBeatId(BeatName beatId) {
		this.beatId = beatId;
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

	public BeatTypeMst getBeatTypeId() {
		return beatTypeId;
	}

	public void setBeatTypeId(BeatTypeMst beatTypeId) {
		this.beatTypeId = beatTypeId;
	}

	public String getBeatAndTypeName() {
		return beatAndTypeName;
	}

	public void setBeatAndTypeName(String beatAndTypeName) {
		this.beatAndTypeName = beatAndTypeName;
		if(null == beatAndTypeName) {
			this.beatAndTypeName = this.beatId.getBeatName()+ " - " + this.beatTypeId.getBeatType();
		}
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Override
	public String toString() {
		return "SoBeatNTypeMappingVo [id=" + id + ", soId=" + soId + ", beatId=" + beatId + ", beatTypeId=" + beatTypeId
				+ ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", updatedOn=" + updatedOn + ", updatedBy="
				+ updatedBy + "]";
	}
}
