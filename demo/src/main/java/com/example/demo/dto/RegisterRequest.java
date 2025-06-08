package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    public String username;
    public String password;
    public String email;
    public String fullName;
    public String phoneNumber;
    // getter, setter, constructor nếu cần
} 