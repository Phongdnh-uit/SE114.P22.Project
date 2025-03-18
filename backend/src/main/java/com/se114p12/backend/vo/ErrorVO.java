package com.se114p12.backend.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.se114p12.backend.domain.enums.ErrorType;

import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ErrorVO {
    private ErrorType type;
    private Object details;
}
