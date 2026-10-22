package com.exemple.user.service.impl;

import com.exemple.user.dto.UserDTO;
import com.exemple.user.entity.User;
import com.exemple.user.exception.DuplicateResourceException;
import com.exemple.user.exception.ResourceNotFoundException;
import com.exemple.user.repository.UserRepository;
import com.exemple.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (existsByUsername(userDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + userDTO.getUsername());
        }
        if (userDTO.getEmail() != null && existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + userDTO.getEmail());
        }

        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .phoneNumber(userDTO.getPhoneNumber())
                .photoUrl(userDTO.getPhotoUrl())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .status("ACTIVE")
                .role(userDTO.getRole() != null ? userDTO.getRole() : "USER")
                .enabled(true)
                .locked(false)
                .createdAt(LocalDateTime.now())
                .fidelityScore(0.0)
                .fidelityLevel("BRONZE")
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getUsername());
        return mapToUserDTO(savedUser);
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToUserDTO);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::mapToUserDTO);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::mapToUserDTO);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            if (existsByUsername(userDTO.getUsername())) {
                throw new DuplicateResourceException("Username already exists");
            }
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (existsByEmail(userDTO.getEmail())) {
                throw new DuplicateResourceException("Email already exists");
            }
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if (userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getPhotoUrl() != null) user.setPhotoUrl(userDTO.getPhotoUrl());
        if (userDTO.getStatus() != null) user.setStatus(userDTO.getStatus());
        if (userDTO.getRole() != null) user.setRole(userDTO.getRole());

        user.setUpdatedAt(LocalDateTime.now());
        User updated = userRepository.save(user);
        return mapToUserDTO(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void lockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setLocked(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setLocked(false);
        userRepository.save(user);
    }

    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .photoUrl(user.getPhotoUrl())
                .status(user.getStatus())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .locked(user.isLocked())
                .enabled(user.isEnabled())
                .fidelityScore(user.getFidelityScore())
                .fidelityLevel(user.getFidelityLevel())
                .build();
    }
}
