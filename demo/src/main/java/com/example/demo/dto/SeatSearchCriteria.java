package com.example.demo.dto;

import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import com.example.demo.exception.BadRequestException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatSearchCriteria {

    private Long flightId;

    private Double minPrice;

    private Double maxPrice;

    private SeatType seatType;

    private Boolean hasExtraLegroom;

    private Boolean isEmergencyExit;

    private Boolean isBulkhead;

    private Integer row;

    private Integer column;

    private SeatStatus status;

    public void validate() {
        if (flightId == null) {
            throw new IllegalArgumentException("Flight ID is required");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        if (minPrice != null && minPrice < 0) {
            throw new BadRequestException("Minimum price cannot be negative");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new BadRequestException("Maximum price cannot be negative");
        }
    }

    public boolean hasPriceRange() {
        return minPrice != null || maxPrice != null;
    }

    public boolean hasSeatTypePreference() {
        return seatType != null;
    }

    public boolean hasExtraLegroomPreference() {
        return hasExtraLegroom != null;
    }

    public boolean hasEmergencyExitPreference() {
        return isEmergencyExit != null;
    }

    public boolean hasRowPreference() {
        return row != null;
    }

    public boolean hasStatusPreference() {
        return status != null;
    }

    public boolean hasAnyPreference() {
        return hasPriceRange() || hasSeatTypePreference() || hasExtraLegroomPreference() 
            || hasEmergencyExitPreference() || hasRowPreference() || hasStatusPreference();
    }
} 