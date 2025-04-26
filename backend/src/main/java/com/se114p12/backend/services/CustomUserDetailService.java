package com.se114p12.backend.services;

import com.se114p12.backend.domains.authentication.User;
import com.se114p12.backend.util.LoginUtil;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final LoginUtil loginUtil;

  @Override
  public UserDetails loadUserByUsername(String credentialId) throws UsernameNotFoundException {
    User user = loginUtil.getUserByCredentialId(credentialId);
    String role = "ROLE_ " + user.getRole().getName();
    return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
        .password(user.getPassword())
        .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
        .build();
  }
}
