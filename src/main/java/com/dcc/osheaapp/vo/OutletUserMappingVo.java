package com.dcc.osheaapp.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "outlet_user_mapping")
@EntityListeners(AuditingEntityListener.class)
public class OutletUserMappingVo implements Serializable {

  private static final long serialVersionUID = 1L;

  public OutletUserMappingVo() {}

  public OutletUserMappingVo(
      OutletVo outlet, Long assotiatedUser, Date assotiatedOn, Date leftOn, Boolean isActive) {
    this.outlet = outlet;
    this.assotiatedUser = assotiatedUser;
    this.assotiatedOn = assotiatedOn;
    this.leftOn = leftOn;
    this.isActive = isActive;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // @Column(name = "outlet_id")
  @OneToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "outlet_id")
  private OutletVo outlet;

  @Column(name = "assotiated_user_id") // BA_id
  private Long assotiatedUser;

  @Column(name = "assotiated_on")
  @Temporal(TemporalType.DATE)
  private Date assotiatedOn;

  @Column(name = "left_on")
  @Temporal(TemporalType.DATE)
  private Date leftOn;

  @Column(name = "is_active")
  private Boolean isActive;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OutletVo getOutlet() {
    return outlet;
  }

  public void setOutlet(OutletVo outlet) {
    this.outlet = outlet;
  }

  public Long getAssotiatedUser() {
    return assotiatedUser;
  }

  public void setAssotiatedUser(Long assotiatedUser) {
    this.assotiatedUser = assotiatedUser;
  }

  public Date getAssotiatedOn() {
    return assotiatedOn;
  }

  public void setAssotiatedOn(Date assotiatedOn) {
    this.assotiatedOn = assotiatedOn;
  }

  public Date getLeftOn() {
    return leftOn;
  }

  public void setLeftOn(Date leftOn) {
    this.leftOn = leftOn;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public String toString() {
    return "OutletUserMappingVo [id="
        + id
        + ", outlet="
        + outlet
        + ", assotiatedUser="
        + assotiatedUser
        + ", assotiatedOn="
        + assotiatedOn
        + ", leftOn="
        + leftOn
        + ", isActive="
        + isActive
        + "]";
  }

    public OutletUserMappingVo dissociate() {
      this.setIsActive(false);
      this.setLeftOn(new Date());
      return this;
    }
}
