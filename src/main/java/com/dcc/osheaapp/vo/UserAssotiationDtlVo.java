package com.dcc.osheaapp.vo;

import java.io.Serializable;
import javax.persistence.CascadeType;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_assotiation_details")
@EntityListeners(AuditingEntityListener.class)
public class UserAssotiationDtlVo implements Serializable {

  private static final long serialVersionUID = 1L;

  public UserAssotiationDtlVo() {}

  public UserAssotiationDtlVo(
      UserDetailsVo userId, UserDetailsVo assotiatedUser, Boolean isActive) {
    this.userId = userId;
    this.assotiatedUser = assotiatedUser;
    this.isActive = isActive;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // @Column(name = "user_id")
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "user_id")
  private UserDetailsVo userId;

  // @Column(name = "assotiated_user_id")
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "assotiated_user_id")
  private UserDetailsVo assotiatedUser;

  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "release_date")
  private String releaseDate;

  @Column (name = "date_of_joining")
  private String dateOfJoining;

  public UserDetailsVo getUserId() {
    return userId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUserId(UserDetailsVo userId) {
    this.userId = userId;
  }

  public UserDetailsVo getAssotiatedUser() {
    return assotiatedUser;
  }

  public void setAssotiatedUser(UserDetailsVo assotiatedUser) {
    this.assotiatedUser = assotiatedUser;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getDateOfJoining() {
    return dateOfJoining;
  }

  public void setDateOfJoining(String dateOfJoining) {
    this.dateOfJoining = dateOfJoining;
  }

  @Override
  public String toString() {
    return "UserAssotiationDtlVo [Id="
        + id
        + ", userId="
        + userId
        + ", assotiatedUser="
        + assotiatedUser
        + ", isActive="
        + isActive
            + ", releaseDate="
            + releaseDate
            +", dateOfJoining= "
            + dateOfJoining
        + "]";
  }
}
