package com.se114p12.backend.controllers.role;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.role.RoleRequestDTO;
import com.se114p12.backend.dtos.role.RoleResponseDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.services.role.RoleService;
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

@Tag(name = "Role Module", description = "APIs for managing roles")
@RestController
@RequestMapping(AppConstant.API_BASE_PATH + "/roles")
@RequiredArgsConstructor
public class RoleController {
  private final RoleService roleService;

  @Operation(
      summary = "Get all roles",
      description =
          "Retrieve a paginated list of all roles. Only accessible to users with the ADMIN role.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved roles",
      content = @Content(schema = @Schema(implementation = PageVO.class)))
  @ErrorResponse
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<PageVO<RoleResponseDTO>> getAllRoles(
      @ParameterObject Pageable pageable,
      @Filter @Parameter(name = "filter") Specification<Role> specification) {
    pageable = pageable.isPaged() ? pageable : Pageable.unpaged();
    return ResponseEntity.ok(roleService.getAllRoles(specification, pageable));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(
      summary = "Get role by ID",
      description = "Retrieve a role by its ID. Only accessible to users with the ADMIN role.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved role",
      content = @Content(schema = @Schema(implementation = RoleResponseDTO.class)))
  @ErrorResponse
  @GetMapping("/{id}")
  public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(roleService.getRoleById(id));
  }

  @Operation(
      summary = "Create a new role",
      description = "Create a new role. Only accessible to users with the ADMIN role.")
  @ApiResponse(
      responseCode = "201",
      description = "Successfully created role",
      content = @Content(schema = @Schema(implementation = RoleResponseDTO.class)))
  @ErrorResponse
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<RoleResponseDTO> createRole(
      @Valid @RequestBody RoleRequestDTO roleRequestDTO) {
    return ResponseEntity.ok(roleService.create(roleRequestDTO));
  }

  @Operation(
      summary = "Update a role",
      description = "Update an existing role. Only accessible to users with the ADMIN role.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully updated role",
      content = @Content(schema = @Schema(implementation = RoleResponseDTO.class)))
  @ErrorResponse
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<RoleResponseDTO> updateRole(
      @PathVariable("id") Long id, @Valid @RequestBody RoleRequestDTO roleRequestDTO) {
    return ResponseEntity.ok(roleService.update(id, roleRequestDTO));
  }

  @Operation(
      summary = "Delete a role",
      description = "Delete a role by its ID. Only accessible to users with the ADMIN role.")
  @ApiResponse(responseCode = "204", description = "Successfully deleted role")
  @ErrorResponse
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) {
    roleService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
