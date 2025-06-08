package com.example.demo.dto;

import com.example.demo.enums.CancellationStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
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