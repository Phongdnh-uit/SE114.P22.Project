package com.se114p12.backend.services.authentication;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Accessors(chain = true)
public class CustomUserDetails implements UserDetails {
  private Long id;
  private String username;
  private String password;
  private boolean isAccountNonLocked;
  private Set<? extends GrantedAuthority> authorities;

  @Override
  public boolean isAccountNonLocked() {
    return isAccountNonLocked;
  }
}
