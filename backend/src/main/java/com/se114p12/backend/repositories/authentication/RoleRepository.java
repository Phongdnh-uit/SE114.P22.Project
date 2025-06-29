package com.se114p12.backend.repositories.authentication;

import com.se114p12.backend.entities.authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>,
        JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);
    Optional<Role> findByName(String name);
}
