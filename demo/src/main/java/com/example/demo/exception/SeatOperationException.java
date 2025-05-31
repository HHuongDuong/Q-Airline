package com.example.demo.exception;

public class SeatOperationException extends RuntimeException {
    public SeatOperationException(String message) {
        super(message);
    }

    public SeatOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 