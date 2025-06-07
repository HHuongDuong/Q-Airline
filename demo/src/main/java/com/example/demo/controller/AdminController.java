package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin.html")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "admin.html"; // Trả về tên file HTML (sẽ được tìm trong src/main/resources/static/)
    }

    @GetMapping("/admin/dashboard.html")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboardPage() {
        return "admin/dashboard.html"; // Trả về tên file HTML (sẽ được tìm trong src/main/resources/static/admin/)
    }

    @GetMapping("/admin/delays.html")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDelaysPage() {
        return "admin/delays.html";
    }

    @GetMapping("/admin/flightsflights.html")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminFlightsFlightsPage() {
        return "admin/flightsflights.html";
    }
} 
