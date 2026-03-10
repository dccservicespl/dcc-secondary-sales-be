package com.dcc.osheaapp.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user_credential")
@EntityListeners(AuditingEntityListener.class)
public class UserCredVo implements Serializable, UserDetails {

  private static final long serialVersionUID = 1L;

  public UserCredVo(String password, String username, Boolean isActive) {
    this.password = password;
    this.username = username;
    this.isActive = isActive;
  }

  public UserCredVo() {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @JsonIgnore
  @Column(name = "password", nullable = false, length = 100)
  private String password;

  @Column(name = "username", unique = true, nullable = false, length = 40)
  private String username;

  @Column(name = "is_active")
  private Boolean isActive;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "UserCredVo{"
        + "userId="
        + id
        + ", password='"
        + password
        + '\''
        + ", username='"
        + username
        + '\''
        + '}';
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  public String getCode() {
    return this.username.split("@")[0];
  }

  public void setCode(String code, String contactNo) {
    this.username = code + "@" + contactNo;
  }
}
