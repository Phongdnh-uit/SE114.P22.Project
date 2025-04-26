package com.se114p12.backend.config;

import com.se114p12.backend.domains.authentication.Role;
import com.se114p12.backend.domains.authentication.User;
import com.se114p12.backend.repository.authentication.RoleRepository;
import com.se114p12.backend.repository.authentication.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    if (!roleRepository.existsByName("ADMIN")) {
      Role adminRole = new Role();
      adminRole.setName("ADMIN");
      adminRole.setActive(true);
      adminRole = roleRepository.save(adminRole);

      if (!userRepository.existsByUsername("admin")) {
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setFullname("admin");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(adminRole);
        userRepository.save(admin);
      }
    }

    if (!roleRepository.existsByName("USER")) {
      Role userRole = new Role();
      userRole.setName("USER");
      userRole.setActive(true);
      roleRepository.save(userRole);
    }
  }
}
