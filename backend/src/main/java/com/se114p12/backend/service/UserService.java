package com.se114p12.backend.service;

import com.se114p12.backend.domain.User;
import com.se114p12.backend.domain.enums.UserStatus;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        validateUserUniqueness(user, null);
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