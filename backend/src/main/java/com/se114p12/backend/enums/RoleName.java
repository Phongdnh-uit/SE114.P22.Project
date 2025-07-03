package com.se114p12.backend.enums;

import lombok.Getter;

@Getter
public enum RoleName {
  USER("USER", "Normal user with limited access"),
  ADMIN("ADMIN", "Administrator with full access");

  private String value;
  private String description;

  RoleName(String value, String description) {
    this.value = value;
    this.description = description;
  }
}
