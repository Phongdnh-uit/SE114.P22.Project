package com.se114p12.backend.seeders;

import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.enums.RoleName;
import com.se114p12.backend.repositories.authentication.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RoleInitializer {

  private final RoleRepository roleRepository;

  @Transactional
  public void initializeRoles() {
    List<Role> roles = new ArrayList<>();
    for (RoleName roleName : RoleName.values()) {
      if (!roleRepository.existsByName(roleName.getValue())) {
        Role role = new Role();
        role.setName(roleName.getValue());
        role.setDescription(roleName.getDescription());
        role.setActive(true);
        roles.add(role);
      }
    }
    if (!roles.isEmpty()) {
      roleRepository.saveAll(roles);
      System.out.println("Roles initialized successfully.");
    } else {
      System.out.println("No new roles to initialize.");
    }
  }
}
