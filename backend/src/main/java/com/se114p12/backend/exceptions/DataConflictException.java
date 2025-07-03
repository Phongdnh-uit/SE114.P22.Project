package com.se114p12.backend.exceptions;

import java.util.Map;
import lombok.Getter;

@Getter
public class DataConflictException extends RuntimeException {
  Map<String, String> errors;

  public DataConflictException(Map<String, String> errors) {
    super("Data conflict occurred");
    this.errors = errors;
  }
}
