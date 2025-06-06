package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "dashboard_stats")
public class DashboardStats extends BaseEntity {
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
} 