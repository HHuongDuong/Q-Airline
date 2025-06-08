package com.example.demo.dto;

import com.example.demo.enums.FlightStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FlightResponseDTO {
    private Long id;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double price;
    private FlightStatus status;
    private Long aircraftId;
    private String aircraftCode;
    private String aircraftModel;
} 