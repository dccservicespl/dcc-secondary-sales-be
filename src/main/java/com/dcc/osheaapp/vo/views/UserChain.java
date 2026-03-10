package com.dcc.osheaapp.vo.views;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Immutable
public class UserChain implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private Long userId;
  private String fullName;
  private String userType;
  private Long associatedUserId;
  private String associatedUserName;
  private String associatedUserType;


  public Long getId() {
    return id;
  }

  public Long getUserId() {
    return userId;
  }

  public String getFullName() {
    return fullName;
  }

  public Long getAssociatedUserId() {
    return associatedUserId;
  }

  public String getUserType() {
    return userType;
  }

  public String getAssociatedUserName() {
    return associatedUserName;
  }

  public String getAssociatedUserType() {
    return associatedUserType;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public void setAssociatedUserId(Long associatedUserId) {
    this.associatedUserId = associatedUserId;
  }

  public void setAssociatedUserName(String associatedUserName) {
    this.associatedUserName = associatedUserName;
  }

  public void setAssociatedUserType(String associatedUserType) {
    this.associatedUserType = associatedUserType;
  }
}
