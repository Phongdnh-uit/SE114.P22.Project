package com.se114p12.backend.exception;

import com.se114p12.backend.domain.enums.ErrorType;
import com.se114p12.backend.vo.ErrorVO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DataConflictException.class)
  public ResponseEntity<?> handleDataConflictException(DataConflictException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(
            ErrorVO.builder()
                .type(ErrorType.RESOURCE_CONFLICT_ERROR)
                .details(e.getMessage())
                .build());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            ErrorVO.builder()
                .type(ErrorType.RESOURCE_NOT_FOUND_ERROR)
                .details(e.getMessage())
                .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    var errors = e.getBindingResult().getFieldErrors();
    Map<String, String> errorMap = new HashMap<>();
    for (var error : errors) {
      errorMap.put(error.getField(), error.getDefaultMessage());
    }
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(ErrorVO.builder().type(ErrorType.VALIDATION_ERROR).details(errorMap).build());
  }
}
