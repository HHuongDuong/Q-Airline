package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Kiểm tra trùng username/email
        if (userService.existsByUsername(request.username)) {
            return ResponseEntity.badRequest().body("Username already exists!");
        }
        if (userService.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }
        // Tạo user mới
        User user = new User();
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setEmail(request.email);
        user.setFullName(request.fullName);
        // Gán role mặc định nếu cần, ví dụ: user.setRole(UserRole.USER);
        userService.createUser(user);
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.username)
                .orElse(null);
        if (user == null || !passwordEncoder.matches(request.password, user.getPassword())) {
            return ResponseEntity.status(401).body("Sai tài khoản hoặc mật khẩu!");
        }
        // Tạo JWT token
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // DTO trả về token
    public static class JwtResponse {
        public String token;
        public JwtResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }
} 