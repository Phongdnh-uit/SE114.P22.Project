package com.se114p12.backend.service;

import com.se114p12.backend.domain.User;
import com.se114p12.backend.domain.enums.UserStatus;
import com.se114p12.backend.dto.request.RegisterRequestDTO;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.UserRepository;
import com.se114p12.backend.util.TypeUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User create(User user) {
        validateUserUniqueness(user, null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.PENDING);
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        User existingUser = findUserById(id);
        validateUserUniqueness(user, existingUser);
        updateUserDetails(existingUser, user);
        return userRepository.save(existingUser);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return findUserById(id);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.findUserByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }

    public User register(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setFullname(registerRequestDTO.getFullname());
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setEmail(registerRequestDTO.getEmail());
        user.setPhone(registerRequestDTO.getPhone());
        user.setStatus(UserStatus.PENDING);
        return userRepository.save(user);
    }

    public void setUserRefreshToken(String username, String token) {
        if (TypeUtil.checkUsernameType(username) == 1) {
            userRepository.findByPhone(username)
                    .ifPresentOrElse(user -> {
                        user.setRefreshToken(token);
                        userRepository.save(user);
                    }, () -> {
                        throw new ResourceNotFoundException("User not found");
                    });
        } else if (TypeUtil.checkUsernameType(username) == 2) {
            userRepository.findByEmail(username)
                    .ifPresentOrElse(user -> {
                        user.setRefreshToken(token);
                        userRepository.save(user);
                    }, () -> {
                        throw new ResourceNotFoundException("User not found");
                    });
        } else {
            userRepository.findByUsername(username)
                    .ifPresentOrElse(user -> {
                        user.setRefreshToken(token);
                        userRepository.save(user);
                    }, () -> {
                        throw new ResourceNotFoundException("User not found");
                    });
        }
    }

    // Private helper methods

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateUserUniqueness(User user, User existingUser) {
        if ((existingUser == null || !user.getUsername().equals(existingUser.getUsername()))
                && userRepository.existsByUsername(user.getUsername())) {
            throw new DataConflictException("Username already exists");
        }

        if ((existingUser == null || !user.getEmail().equals(existingUser.getEmail()))
                && userRepository.existsByEmail(user.getEmail())) {
            throw new DataConflictException("Email already exists");
        }

        if ((existingUser == null || !user.getPhone().equals(existingUser.getPhone()))
                && userRepository.existsByPhone(user.getPhone())) {
            throw new DataConflictException("Phone already exists");
        }
    }

    private void updateUserDetails(User existingUser, User newUser) {
        existingUser.setFullname(newUser.getFullname());
        existingUser.setUsername(newUser.getUsername());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setPhone(newUser.getPhone());
        existingUser.setAvatarUrl(newUser.getAvatarUrl());
        existingUser.setStatus(newUser.getStatus());
    }
}