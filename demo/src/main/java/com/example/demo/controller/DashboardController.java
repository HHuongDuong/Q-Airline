package com.example.demo.controller;

import com.example.demo.entity.DashboardStats;
import com.example.demo.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats/latest")
    public ResponseEntity<DashboardStats> getLatestStats() {
        return ResponseEntity.ok(dashboardService.getLatestStats());
    }

    @GetMapping("/stats/date-range")
    public ResponseEntity<List<DashboardStats>> getStatsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getStatsByDateRange(start, end));
    }

    @GetMapping("/stats/from-date")
    public ResponseEntity<List<DashboardStats>> getStatsFromDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(dashboardService.getStatsFromDate(date));
    }

    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        if (start != null && end != null) {
            return ResponseEntity.ok(dashboardService.getTotalRevenueByDateRange(start, end));
        }
        return ResponseEntity.ok(dashboardService.getTotalRevenue());
    }

    @GetMapping("/bookings")
    public ResponseEntity<Long> getTotalBookings(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        if (start != null && end != null) {
            return ResponseEntity.ok(dashboardService.getTotalBookingsByDateRange(start, end));
        }
        return ResponseEntity.ok(dashboardService.getTotalBookings());
    }

    @GetMapping("/cancellations")
    public ResponseEntity<Long> getTotalCancellations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        if (start != null && end != null) {
            return ResponseEntity.ok(dashboardService.getTotalCancellationsByDateRange(start, end));
        }
        return ResponseEntity.ok(dashboardService.getTotalCancellations());
    }

    @GetMapping("/flights")
    public ResponseEntity<Object> getFlightStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        if (start != null && end != null) {
            return ResponseEntity.ok(dashboardService.getFlightStatsByDateRange(start, end));
        }
        return ResponseEntity.ok(dashboardService.getFlightStats());
    }

    @GetMapping("/passengers")
    public ResponseEntity<Object> getPassengerStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        if (start != null && end != null) {
            return ResponseEntity.ok(dashboardService.getPassengerStatsByDateRange(start, end));
        }
        return ResponseEntity.ok(dashboardService.getPassengerStats());
    }
} 