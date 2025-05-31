package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dashboard_stats")
public class DashboardStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Long totalBookings;

    @Column(nullable = false)
    private Long totalCancellations;

    @Column(nullable = false)
    private double totalRevenue;

    @Column(nullable = false)
    private double totalRefunds;

    @Column(nullable = false)
    private Long activeFlights;

    @Column(nullable = false)
    private Long completedFlights;

    @Column(nullable = false)
    private Long delayedFlights;

    @Column(nullable = false)
    private Long totalPassengers;

    @Column(nullable = false)
    private double averageTicketPrice;

    @Column(nullable = false)
    private double occupancyRate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 