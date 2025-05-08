package com.se114p12.backend.controllers.user;

import com.se114p12.backend.entities.authentication.User;
import com.se114p12.backend.services.user.UserService;
import com.se114p12.backend.vo.PageVO;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Module")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageVO<?>> getAllUsers(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

//    @GetMapping("/{search}")
//    public ResponseEntity<List<User>> searchUsers(@PathVariable("id") String search) {
//        return ResponseEntity.ok(userService.searchUsers(search));
//    }

//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        return ResponseEntity.ok(userService.create(user));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Validated @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.update(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign-role/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable("id") Long userId, @PathVariable("roleId") Long roleId) {
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }

}
