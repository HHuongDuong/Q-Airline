package com.example.demo.exception;

public class TicketCancellationException extends RuntimeException {
    public TicketCancellationException(String message) {
        super(message);
    }

    public TicketCancellationException(String message, Throwable cause) {
        super(message, cause);
    }
} 