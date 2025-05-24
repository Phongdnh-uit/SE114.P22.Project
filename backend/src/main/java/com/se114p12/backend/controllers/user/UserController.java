package com.se114p12.backend.controllers.user;

import com.se114p12.backend.dto.user.UserRequestDTO;
import com.se114p12.backend.dto.user.UserResponseDTO;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.services.user.UserService;
import com.se114p12.backend.vo.PageVO;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Module", description = "APIs for managing users")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
      })
  @GetMapping
  public ResponseEntity<PageVO<UserResponseDTO>> getAllUsers(
      @ParameterObject Pageable pageable,
      @Filter @Parameter(name = "filter") Specification<User> specification) {
    return ResponseEntity.ok(userService.getAllUsers(specification, pageable));
  }

  @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
      })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @Operation(summary = "Update user", description = "Update an existing user by their ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated user"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "User not found"),
      })
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(
      @PathVariable("id") Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
    return ResponseEntity.ok(userService.update(id, userRequestDTO));
  }

  @Operation(summary = "Delete user", description = "Delete a user by their ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Assign role to user", description = "Assign a role to a user by their ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Successfully assigned role to user"),
        @ApiResponse(responseCode = "404", description = "User or role not found"),
      })
  @PostMapping("/{id}/assign-role/{roleId}")
  public ResponseEntity<Void> assignRoleToUser(
      @PathVariable("id") Long userId, @PathVariable("roleId") Long roleId) {
    userService.assignRoleToUser(userId, roleId);
    return ResponseEntity.noContent().build();
  }
}
