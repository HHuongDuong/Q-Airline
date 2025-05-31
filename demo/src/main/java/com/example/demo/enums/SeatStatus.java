package com.example.demo.enums;

public enum SeatStatus {
    AVAILABLE,
    RESERVED,
    OCCUPIED,
    BLOCKED,
    MAINTENANCE;

    public boolean isAvailable() {
        return this == AVAILABLE;
    }

    public boolean isReserved() {
        return this == RESERVED;
    }

    public boolean isOccupied() {
        return this == OCCUPIED;
    }

    public boolean isInMaintenance() {
        return this == MAINTENANCE;
    }

    public boolean isBlocked() {
        return this == BLOCKED;
    }

    public boolean canBeReserved() {
        return this == AVAILABLE;
    }

    public boolean canBeOccupied() {
        return this == RESERVED;
    }

    public boolean canBeReleased() {
        return this == RESERVED || this == BLOCKED;
    }

    public boolean canBePutInMaintenance() {
        return this == AVAILABLE || this == RESERVED || this == BLOCKED;
    }

    public boolean canChangeStatus(SeatStatus newStatus) {
        switch (this) {
            case AVAILABLE:
                return newStatus == RESERVED || newStatus == BLOCKED || newStatus == MAINTENANCE;
            case RESERVED:
                return newStatus == OCCUPIED || newStatus == AVAILABLE || newStatus == MAINTENANCE;
            case OCCUPIED:
                return newStatus == AVAILABLE || newStatus == MAINTENANCE;
            case BLOCKED:
                return newStatus == AVAILABLE || newStatus == MAINTENANCE;
            case MAINTENANCE:
                return newStatus == AVAILABLE;
            default:
                return false;
        }
    }
} 