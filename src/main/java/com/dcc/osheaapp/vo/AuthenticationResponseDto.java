package com.dcc.osheaapp.vo;

public class AuthenticationResponseDto {
  String username;

  String token;

  String refreshToken;

  public AuthenticationResponseDto(String username, String token, String refreshToken) {
    this.username = username;
    this.token = token;
    this.refreshToken = refreshToken;
  }

  public String getUsername() {
    return username;
  }

  public String getToken() {
    return token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
