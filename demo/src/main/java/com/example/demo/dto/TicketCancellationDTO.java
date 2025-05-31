package com.example.demo.dto;

import com.example.demo.enums.CancellationStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketCancellationDTO {
    private Long id;
    private Long ticketId;
    private Long userId;
    private String reason;
    private CancellationStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private Double refundAmount;
    private String rejectionReason;
} 