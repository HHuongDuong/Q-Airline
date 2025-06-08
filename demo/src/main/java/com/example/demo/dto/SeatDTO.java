package com.example.demo.dto;

import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatDTO {
    private Long id;
    private Long flightId;
    private String seatNumber;
    private SeatType seatType;
    private SeatStatus status;
    private Double price;
    private boolean hasExtraLegroom;
    private boolean isEmergencyExit;
    private int row;
    private boolean selected;
} 