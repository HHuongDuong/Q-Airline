package com.example.demo.dto;

import com.example.demo.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketResponseDTO {
    private Long id;
    private String ticketNumber;
    private Double price;
    private TicketStatus status;

    // Flight details
    private Long flightId;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    // Seat details
    private Long seatId;
    private String seatNumber;
    private int seatRow;
    private int seatColumn;

    // User details
    private Long userId;
    private String username;
    private String fullName;
} 