package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.MessageResponse;
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
        System.out.println("Đăng ký với username: " + request.username);
        System.out.println("Đăng ký với password: " + request.password.trim());

        // Thêm log kiểm tra byte của mật khẩu
        try {
            byte[] passwordBytes = request.password.trim().getBytes("UTF-8");
            StringBuilder sb = new StringBuilder();
            for (byte b : passwordBytes) {
                sb.append(String.format("%02X ", b));
            }
            System.out.println("Password (bytes UTF-8): " + sb.toString().trim());
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (userService.existsByUsername(request.username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already exists!"));
        }
        if (userService.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already exists!"));
        }
        User user = new User();
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password.trim()));
        user.setEmail(request.email);
        user.setFullName(request.fullName);
        user.setRole(com.example.demo.enums.UserRole.USER);
        userService.createUser(user);
        return ResponseEntity.ok(new MessageResponse("Đăng ký thành công!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("Password nhận được: [" + request.password + "]");

        User user = userService.findByUsername(request.username)
                .orElse(null);

        if (user == null) {
            System.out.println("User không tìm thấy với username: " + request.username);
            return ResponseEntity.status(401).body(new MessageResponse("Invalid username or password!"));
        }

        System.out.println("User tìm thấy: " + user.getUsername());
        System.out.println("Hash trong DB (trước matches): " + user.getPassword());
        System.out.println("Mật khẩu nhập vào (trước matches): [" + request.password.trim() + "]");

        boolean passwordMatches = passwordEncoder.matches(request.password.trim(), user.getPassword());
        System.out.println("Kết quả passwordEncoder.matches(): " + passwordMatches);

        if (!passwordMatches) {
            return ResponseEntity.status(401).body(new MessageResponse("Invalid username or password!"));
        }

        System.out.println("Hash trong DB: " + user.getPassword());
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public static class JwtResponse {
        public String token;
        public JwtResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }
} 