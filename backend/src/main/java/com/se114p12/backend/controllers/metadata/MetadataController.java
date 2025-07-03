package com.se114p12.backend.controllers.metadata;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.enums.EnumType;
import com.se114p12.backend.enums.OTPAction;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.enums.PaymentMethod;
import com.se114p12.backend.enums.RoleName;
import com.se114p12.backend.enums.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "Metadata",
    description = "Endpoints for retrieving metadata such as enums and other static data.")
@RequestMapping(AppConstant.API_BASE_PATH + "/metadata")
@RestController
public class MetadataController {

  @Operation(
      summary = "Get all enums",
      description =
          "Retrieve all enums available in the application. Optionally filter by types. \n"
              + "Available types: OTP_ACTION, ROLE_NAME, USER_STATUS, ORDER_STATUS, PAYMENT_METHOD")
  @GetMapping
  public ResponseEntity<Map<String, List<String>>> getEnums(
      @RequestParam(value = "types", required = false) List<EnumType> types) {
    Map<String, List<String>> enums = new HashMap<>();
    for (EnumType type : types) {
      enums.put(type.name(), fetchEnumByType(type));
    }
    return ResponseEntity.ok(enums);
  }

  private List<String> fetchEnumByType(EnumType type) {
    return switch (type) {
      case OTP_ACTION -> Arrays.stream(OTPAction.values()).map(OTPAction::name).toList();
      case ROLE_NAME -> Arrays.stream(RoleName.values()).map(RoleName::name).toList();
      case USER_STATUS -> Arrays.stream(UserStatus.values()).map(UserStatus::name).toList();
      case ORDER_STATUS -> Arrays.stream(OrderStatus.values()).map(OrderStatus::name).toList();
      case PAYMENT_METHOD ->
          Arrays.stream(PaymentMethod.values()).map(PaymentMethod::name).toList();
    };
  }
}
