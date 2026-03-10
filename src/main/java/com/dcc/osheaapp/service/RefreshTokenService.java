package com.dcc.osheaapp.service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.repository.IRefreshTokenRepository;
import com.dcc.osheaapp.repository.IUserCredRepository;
import com.dcc.osheaapp.vo.RefreshToken;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

  @Autowired private IUserCredRepository userCredRepository;
  @Autowired private IRefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.refresh-token.validity}")
  private Long validity;

  public RefreshToken create(String username) {
    Optional<RefreshToken> optionalToken = refreshTokenRepository.findByUsername(username);
    RefreshToken token = null;
    if (optionalToken.isPresent()) token = optionalToken.get().extendValidity(validity);
    else
      token =
          new RefreshToken(
              UUID.randomUUID().toString(), Instant.now().plusMillis(validity), username);
    refreshTokenRepository.save(token);
    return token;
  }

  public RefreshToken validate(String refreshToken) {
    RefreshToken token =
        refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(
                () -> new OjbException(ErrorCode.NOT_FOUND, new String[] {"Refresh Token"}));
    if (token.isExpired()) {
      token.extendValidity(validity);
      refreshTokenRepository.save(token);
    }
    return token;
  }

  public String getRefreshTokenFromCookieHeader(HttpServletRequest request) {
    Cookie refreshCookie =
        Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals("refreshToken"))
            .findFirst()
            .orElseThrow(
                () -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"Refresh Token Cookie"}));
    return refreshCookie.getValue();
  }
}
