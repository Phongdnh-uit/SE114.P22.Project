package com.se114p12.backend.repository;

import com.se114p12.backend.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(@NotBlank String username);
    boolean existsByEmail(@Email String email);
    boolean existsByPhone(String phone);
}
