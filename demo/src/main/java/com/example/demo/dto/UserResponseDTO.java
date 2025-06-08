package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import com.example.demo.enums.UserRole;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}