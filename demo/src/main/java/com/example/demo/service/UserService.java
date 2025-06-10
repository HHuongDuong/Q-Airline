package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.dto.UserDetailsDTO;
import com.example.demo.dto.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> getAllUsers();
    UserResponseDTO getCurrentUser();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void updateUserRole(Long userId, UserRole newRole);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    List<User> getUsersByRole(String role);
    User updateUserDetails(Long userId, UserDetailsDTO userDetails);
    long countAllUsers();
} 