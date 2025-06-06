package com.example.demo.dto;

import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class SeatMapDTO {
    private Long id;
    private Long flightId;
    private String seatNumber;
    private SeatType seatType;
    private SeatStatus status;
    private Double price;
    private boolean hasExtraLegroom;
    private boolean isEmergencyExit;
    private int row;
    private int column;
    private boolean selected;
    private String aircraftType;
    private int totalRows;
    private int seatsPerRow;
    private List<SeatDTO> seats;
    private Boolean isBulkhead;
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

    public void setIsEmergencyExit(boolean isEmergencyExit) {
        this.isEmergencyExit = isEmergencyExit;
    }

    public boolean getIsEmergencyExit() {
        return isEmergencyExit;
    }
} 