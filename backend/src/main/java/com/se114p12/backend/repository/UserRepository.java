package com.se114p12.backend.repository;

import com.se114p12.backend.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(@NotBlank String username);
    boolean existsByEmail(@Email String email);
    boolean existsByPhone(String phone);
    List<User> findUserByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(@NotBlank String username, @Email String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String username);
    Optional<User> findByUsername(String username);
}
