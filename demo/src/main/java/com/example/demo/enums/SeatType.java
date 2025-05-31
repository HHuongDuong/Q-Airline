package com.example.demo.enums;

public enum SeatType {
    ECONOMY,
    PREMIUM_ECONOMY,
    BUSINESS,
    FIRST,
    WINDOW,
    AISLE,
    MIDDLE,
    BULKHEAD,
    EXIT_ROW;

    public boolean isEconomyClass() {
        return this == ECONOMY;
    }

    public boolean isPremiumEconomyClass() {
        return this == PREMIUM_ECONOMY;
    }

    public boolean isBusinessClass() {
        return this == BUSINESS;
    }

    public boolean isFirstClass() {
        return this == FIRST;
    }

    public boolean isPremiumSeat() {
        return this == FIRST || this == BUSINESS;
    }

    public boolean isStandardSeat() {
        return this == ECONOMY || this == PREMIUM_ECONOMY;
    }

    public boolean isSpecialSeat() {
        return this == WINDOW || this == AISLE || this == MIDDLE || 
               this == BULKHEAD || this == EXIT_ROW;
    }

    public boolean isWindow() {
        return this == WINDOW;
    }

    public boolean isAisle() {
        return this == AISLE;
    }

    public boolean isMiddle() {
        return this == MIDDLE;
    }

    public boolean isBulkhead() {
        return this == BULKHEAD;
    }

    public boolean isExitRow() {
        return this == EXIT_ROW;
    }
} 