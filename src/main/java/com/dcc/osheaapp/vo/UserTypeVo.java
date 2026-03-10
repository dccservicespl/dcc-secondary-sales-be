package com.dcc.osheaapp.vo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_type_mst")
@EntityListeners(AuditingEntityListener.class)
public class UserTypeVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_type")
  private String userType;

  @Column(name = "form_type", length = 10)
  private String formType;

  @Column(name = "user_priority", length = 10)
  private Long userPriority;

  @Column(name = "is_active")
  private Boolean isActive;

  public String getUserType() {
    return userType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getFormType() {
    return formType;
  }

  public void setFormType(String formType) {
    this.formType = formType;
  }

  public Long getUserPriority() {
    return userPriority;
  }

  public void setUserPriority(Long userPriority) {
    this.userPriority = userPriority;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public String toString() {
    return "UserTypeVo [id="
        + id
        + ", userType="
        + userType
        + ", formType="
        + formType
        + ", userPriority="
        + userPriority
        + ", isActive="
        + isActive
        + "]";
  }
}
