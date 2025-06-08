package com.example.demo.dto;

import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class SeatRequestDTO {

    @NotNull(message = "Flight ID cannot be null")
    private Long flightId;

    @NotNull(message = "Aircraft ID cannot be null")
    private Long aircraftId;

    @NotBlank(message = "Seat number cannot be blank")
    private String seatNumber;

    @NotNull(message = "Seat type cannot be null")
    private SeatType seatType;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Status cannot be null")
    private SeatStatus status;

    private Boolean hasExtraLegroom = false;
    private Boolean isEmergencyExit = false;
    private Boolean isBulkhead = false;
    private Integer row;
    private String column;
    private String notes;
} 