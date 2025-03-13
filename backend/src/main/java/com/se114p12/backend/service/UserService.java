package com.se114p12.backend.service;

import com.se114p12.backend.domain.User;
import com.se114p12.backend.domain.enums.UserStatus;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DataConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DataConflictException("Email already exists");
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new DataConflictException("Phone already exists");
        }

        user.setStatus(UserStatus.PENDING);
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getUsername().equals(existingUser.getUsername())
                && userRepository.existsByUsername(user.getUsername())) {
            throw new DataConflictException("Username already exists");
        }

        if (!user.getEmail().equals(existingUser.getEmail())
                && userRepository.existsByEmail(user.getEmail())) {
            throw new DataConflictException("Email already exists");
        }

        if (!user.getPhone().equals(existingUser.getPhone())
                && userRepository.existsByPhone(user.getPhone())) {
            throw new DataConflictException("Phone already exists");
        }

        existingUser.setFullname(user.getFullname());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAvatarUrl(user.getAvatarUrl());
        existingUser.setStatus(user.getStatus());

        return userRepository.save(existingUser);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
