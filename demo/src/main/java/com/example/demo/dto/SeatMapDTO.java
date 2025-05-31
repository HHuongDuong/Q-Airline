package com.example.demo.dto;

import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import lombok.Data;
import java.util.List;

@Data
public class SeatMapDTO {
    private Long flightId;
    private String aircraftType;
    private int totalRows;
    private int seatsPerRow;
    private List<SeatDTO> seats;
    private Long id;
    private String seatNumber;
    private SeatType seatType;
    private SeatStatus status;
    private Double price;
    private Boolean hasExtraLegroom;
    private Boolean isEmergencyExit;
    private Boolean isBulkhead;
    private Integer row;
    private Integer column;
    private Boolean selected;
    private String notes;

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public boolean isReserved() {
        return status == SeatStatus.RESERVED;
    }

    public boolean isOccupied() {
        return status == SeatStatus.OCCUPIED;
    }

    public boolean isInMaintenance() {
        return status == SeatStatus.MAINTENANCE;
    }

    public boolean isBlocked() {
        return status == SeatStatus.BLOCKED;
    }

    public boolean isPremiumSeat() {
        return seatType == SeatType.BUSINESS || seatType == SeatType.FIRST || seatType == SeatType.PREMIUM_ECONOMY;
    }

    public boolean isStandardSeat() {
        return seatType == SeatType.ECONOMY;
    }

    public boolean isSpecialSeat() {
        return hasExtraLegroom || isEmergencyExit;
    }
} 