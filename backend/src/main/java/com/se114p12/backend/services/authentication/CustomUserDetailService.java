package com.se114p12.backend.services.authentication;

import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.exceptions.BadRequestException;
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

  private final LoginAttemptService loginAttemptService;
  private final LoginUtil loginUtil;

  @Override
  public UserDetails loadUserByUsername(String credentialId) throws UsernameNotFoundException {
    User user = loginUtil.getUserByCredentialId(credentialId);
    if (!user.getLoginProvider().equals(LoginProvider.LOCAL)) {
      throw new BadRequestException(
          "Account provided by another provider: " + user.getLoginProvider());
    }
    String role = "ROLE_ " + user.getRole().getName();
    return new CustomUserDetails()
        .setId(user.getId())
        .setUsername(credentialId)
        .setPassword(user.getPassword())
        .setAccountNonLocked(!loginAttemptService.isBlocked(user.getId()))
        .setAuthorities(Collections.singleton(new SimpleGrantedAuthority(role)));
  }
}
