package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
        user.setPhoneNumber(request.phoneNumber);
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
        String jwt = jwtTokenProvider.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(24 * 60 * 60) // 24 hours
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new JwtResponse(jwt, user.getRole().name()));
    }

    @GetMapping("/test-bcrypt")
    public ResponseEntity<?> testBcrypt() {
        String testPassword = "Duong2003";
        // Lấy hash chính xác mà bạn đã thấy trong log khi đăng ký tài khoản 'anhduong'
        String testHashFromDB = "$2a$10$mmbH2NkIApgX5MceBWXGv.J175VG3gc8u.zWLqe50UNW/wim.iDKm"; 

        System.out.println("--- BCrypt Test Start ---");
        System.out.println("Test Password: [" + testPassword + "]");
        System.out.println("Test Hash From DB (expected to match): " + testHashFromDB);

        // 1. Kiểm tra encode và matches ngay lập tức
        String newlyEncodedHash = passwordEncoder.encode(testPassword);
        System.out.println("Newly Encoded Hash: " + newlyEncodedHash);
        boolean matchesNewlyEncoded = passwordEncoder.matches(testPassword, newlyEncodedHash);
        System.out.println("Matches (Test Password vs Newly Encoded Hash): " + matchesNewlyEncoded);

        // 2. Kiểm tra Test Password với Test Hash From DB
        boolean matchesWithDbHash = passwordEncoder.matches(testPassword, testHashFromDB);
        System.out.println("Matches (Test Password vs Test Hash From DB): " + matchesWithDbHash);
        System.out.println("--- BCrypt Test End ---");

        if (matchesWithDbHash) {
            return ResponseEntity.ok(new MessageResponse("BCrypt test successful! Password matches stored hash."));
        } else {
            return ResponseEntity.status(500).body(new MessageResponse("BCrypt test failed! Password does NOT match stored hash."));
        }
    }

    public static class JwtResponse {
        public String token;
        public String role;

        public JwtResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }

        public String getToken() { return token; }
    }
} 