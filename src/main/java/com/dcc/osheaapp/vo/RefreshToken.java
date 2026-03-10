package com.dcc.osheaapp.vo;

import java.time.Instant;
import javax.persistence.*;

@Entity(name = "refresh_token")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String token;
  Instant expiry;

  @Column(unique = true)
  String username;

  public RefreshToken() {}

  public RefreshToken(String token, Instant expiry, String username) {
    this.token = token;
    this.expiry = expiry;
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public String getToken() {
    return token;
  }

  public Instant getExpiry() {
    return expiry;
  }

  public String getUsername() {
    return username;
  }

  public boolean isExpired() {
    return Instant.now().compareTo(expiry) > 0;
  }

  public RefreshToken extendValidity(Long extension) {
    this.expiry = Instant.now().plusMillis(extension);
    return this;
  }
}
