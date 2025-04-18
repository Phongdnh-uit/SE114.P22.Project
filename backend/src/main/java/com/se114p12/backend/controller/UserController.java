package com.se114p12.backend.controller;

import com.se114p12.backend.domain.User;
import com.se114p12.backend.service.UserService;
import com.se114p12.backend.vo.PageVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Module")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<PageVO<?>> getAllUsers(@ParameterObject Pageable pageable) {
    return ResponseEntity.ok(userService.getAllUsers(pageable));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{search}")
  public ResponseEntity<List<User>> searchUsers(@PathVariable("id") String search) {
    return ResponseEntity.ok(userService.searchUsers(search));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    return ResponseEntity.ok(userService.create(user));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(
      @PathVariable("id") Long id, @Validated @RequestBody User userDetails) {
    return ResponseEntity.ok(userService.update(id, userDetails));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
