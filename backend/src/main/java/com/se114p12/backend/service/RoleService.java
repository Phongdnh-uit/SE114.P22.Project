package com.se114p12.backend.service;

import com.se114p12.backend.domain.Role;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.RoleRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role create(@NotNull Role role) {
        checkRoleNameUniqueness(role.getName());
        role.setActive(true); // Default to active upon creation
        role.setCreatedAt(Instant.now());
        role.setUpdatedAt(Instant.now());
        return roleRepository.save(role);
    }

    public Role update(Long id, Role roleDetails) {
        Role existingRole = findRoleById(id);

        if (!existingRole.getName().equals(roleDetails.getName())) {
            checkRoleNameUniqueness(roleDetails.getName());
        }

        existingRole.setName(roleDetails.getName());
//        existingRole.setDescription(roleDetails.getDescription());
//        existingRole.setActive(roleDetails.getActive());
//        existingRole.setUpdatedAt(Instant.now());
//        existingRole.setUpdatedBy(roleDetails.getUpdatedBy());

        return roleRepository.save(existingRole);
    }

    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return findRoleById(id);
    }

    // Private helper methods

    private Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    private void checkRoleNameUniqueness(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            throw new DataConflictException("Role name already exists");
        }
    }
}