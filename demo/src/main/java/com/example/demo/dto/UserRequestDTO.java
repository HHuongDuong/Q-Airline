package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import com.example.demo.enums.UserRole;

@Getter
@Setter
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private String fullName;
    private String phoneNumber;
}