package com.se114p12.backend.controller;

import com.se114p12.backend.domain.Role;
import com.se114p12.backend.service.RoleService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Role Module")
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) { this.roleService = roleService; }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(Pageable pageable) {
        pageable = pageable.isPaged() ? pageable : Pageable.unpaged();
        return ResponseEntity.ok(roleService.getAllRoles(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleService.create(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Long id, @Valid @RequestBody Role roleDetails) {
        return ResponseEntity.ok(roleService.update(id, roleDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
