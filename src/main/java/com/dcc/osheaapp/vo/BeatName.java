package com.dcc.osheaapp.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.dcc.osheaapp.ojbso.vo.BeatTypeMst;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "beat_name_mst")
@EntityListeners(AuditingEntityListener.class)
public class BeatName implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "beat_name")
  private String beatName;
  
  @Transient
  private Integer page;
  @Transient
  private Integer size;

  @Column(name = "is_active")
  private  Boolean isActive;

  @OneToOne
  @JoinColumn(name = "company_zone")
  private DropdownMasterVo companyZone;

  @Column(name = "created_on", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  private Date createdOn;

  @Column(name = "created_by", nullable = false, updatable = false)
  private Long createdBy;

  @OneToOne
  @JoinColumn(name="beat_type")
  private BeatTypeMst beatType;

  @OneToOne
  @JoinColumn(name="so_id")
  private UserDetailsVo soId;

  @OneToOne
  @JoinColumn(name = "product_division")
  private DropdownMasterVo productDivision;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_type")
  private UserTypeVo associateDesignation;

  public UserTypeVo getAssociateDesignation() {
    return associateDesignation;
  }

  public void setAssociateDesignation(UserTypeVo associateDesignation) {
    this.associateDesignation = associateDesignation;
  }



  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public String getBeatName() {
    return beatName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBeatName(String beatName) {
    this.beatName = beatName;
  }
  

  public Integer getPage() {
	return page;
}

public void setPage(Integer page) {
	this.page = page;
}

public Integer getSize() {
	return size;
}

public void setSize(Integer size) {
	this.size = size;
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

  public DropdownMasterVo getCompanyZone() {
    return companyZone;
  }

  public void setCompanyZone(DropdownMasterVo companyZone) {
    this.companyZone = companyZone;
  }

  public BeatTypeMst getBeatType() {
    return beatType;
  }

  public void setBeatType(BeatTypeMst beatType) {
    this.beatType = beatType;
  }

  public UserDetailsVo getSoId() {
    return soId;
  }

  public void setSoId(UserDetailsVo soId) {
    this.soId = soId;
  }

  public DropdownMasterVo getProductDivision() {
    return productDivision;
  }

  public BeatName() {
  }

  public void setProductDivision(DropdownMasterVo productDivision) {
    this.productDivision = productDivision;
  }

  @Override
  public String toString() {
    return "BeatName{" +
            "id=" + id +
            ", beatName='" + beatName + '\'' +
            ", page=" + page +
            ", size=" + size +
            ", isActive=" + isActive +
            ", companyZone=" + companyZone +
            ", createdOn=" + createdOn +
            ", createdBy=" + createdBy +
            ", beatType=" + beatType +
            ", soId=" + soId +
            ", productDivision=" + productDivision +
            ", associateDesignation=" + associateDesignation +
            '}';
  }
}
