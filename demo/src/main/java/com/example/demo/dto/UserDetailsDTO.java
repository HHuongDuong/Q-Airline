package com.example.demo.dto;

import com.example.demo.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
    private String phoneNumber;
    private String address;
    private Boolean isActive;
} 