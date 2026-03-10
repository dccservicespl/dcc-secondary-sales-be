package com.dcc.osheaapp.config;

import com.dcc.osheaapp.common.model.Password;
import com.dcc.osheaapp.service.UserService;
import com.dcc.osheaapp.vo.UserCredVo;
import java.util.Collection;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private static final Logger LOGGER = LogManager.getLogger(CustomAuthenticationProvider.class);
  private UserService userServices;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    LOGGER.info("CustomAuthenticationProvider:: authenticate:: Entering");

    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    boolean checkPassword = false;
    UserCredVo user = userServices.findByUsername(username);

    if (user == null || !user.getUsername().equalsIgnoreCase(username)) {
      throw new BadCredentialsException("Username not found.");
    }

    String passwordfromui = new String(Base64.decodeBase64(password));

    LOGGER.info("CustomAuthenticationProvider:: authenticate:: Password::" + passwordfromui);

    try {

      checkPassword = Password.check(password, user.getPassword());

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    if (!checkPassword) {
      throw new BadCredentialsException("Wrong password.");
    }

    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

    return new UsernamePasswordAuthenticationToken(user, password, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return false;
  }
}
