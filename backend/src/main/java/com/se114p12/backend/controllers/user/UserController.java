package com.se114p12.backend.controllers.user;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.user.UserRequestDTO;
import com.se114p12.backend.dtos.user.UserResponseDTO;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.services.user.UserService;
import com.se114p12.backend.vo.PageVO;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Module", description = "APIs for managing users")
@RestController
@RequestMapping(AppConstant.API_BASE_PATH + "/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved list of users",
      content = @Content(schema = @Schema(implementation = PageVO.class)))
  @ErrorResponse
  @GetMapping
  public ResponseEntity<PageVO<UserResponseDTO>> getAllUsers(
      @ParameterObject Pageable pageable,
      @Filter @Parameter(name = "filter") Specification<User> specification) {
    pageable = pageable.isPaged() ? pageable : Pageable.unpaged();
    return ResponseEntity.ok(userService.getAllUsers(specification, pageable));
  }

  @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved user",
      content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
  @ErrorResponse
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @Operation(summary = "Update user", description = "Update an existing user by their ID")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully updated user",
      content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
  @ErrorResponse
  @PutMapping(value = "/{id}", consumes = "multipart/form-data")
  public ResponseEntity<UserResponseDTO> updateUser(
      @PathVariable("id") Long id, @Valid @ModelAttribute UserRequestDTO userRequestDTO) {
    return ResponseEntity.ok(userService.update(id, userRequestDTO));
  }

  @Operation(summary = "Delete user", description = "Delete a user by their ID")
  @ApiResponse(
      responseCode = "204",
      description = "Successfully deleted user",
      content = @Content(schema = @Schema(implementation = Void.class)))
  @ErrorResponse
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Assign role to user", description = "Assign a role to a user by their ID")
  @ApiResponse(
          responseCode = "204",
          description = "Successfully assigned role to user",
          content = @Content(schema = @Schema(implementation = Void.class)))
  @ErrorResponse
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/{id}/assign-role/{roleId}")
  public ResponseEntity<Void> assignRoleToUser(
          @PathVariable("id") Long userId, @PathVariable("roleId") Long roleId) {
    userService.assignRoleToUser(userId, roleId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update user status", description = "Update the status of a user by their ID")
  @ApiResponse(
          responseCode = "204",
          description = "Successfully updated user status",
          content = @Content(schema = @Schema(implementation = Void.class)))
  @ErrorResponse
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/{id}/status")
  public ResponseEntity<Void> updateStatus(
          @PathVariable("id") Long userId, @RequestParam("status") String status) {
    userService.updateUserStatus(userId, UserStatus.valueOf(status));
      return ResponseEntity.noContent().build();
  }
}
