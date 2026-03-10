package com.dcc.osheaapp.service;

import com.dcc.osheaapp.repository.IUserCredRepository;
import com.dcc.osheaapp.vo.UserCredVo;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired private IUserCredRepository userRepository;

  @Autowired private PasswordEncoder bcryptEncoder;

  @Autowired private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws AuthenticationException {
    UserCredVo user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), new ArrayList<>());
  }

  public UserDetails loadUser(HttpServletRequest request, String jwtToken) {
    UserCredVo user = null;
    try {
      user = userService.getUserDetails(request, jwtToken);
      if (null == user) {
        throw new UsernameNotFoundException("User not found.");
      }
    } catch (ExpiredJwtException exp) {
      exp.printStackTrace();
      throw new AccountExpiredException("ACCOUNT_EXPIRED");
    }
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), new ArrayList<>());
  }

  public UserCredVo save(UserCredVo user) {
    return userRepository.save(user);
  }
}
