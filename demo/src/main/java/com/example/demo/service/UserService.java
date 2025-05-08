package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    List<User> getUsersByRole(UserRole role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 