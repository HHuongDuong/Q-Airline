package com.example.demo.service;

import com.example.demo.dto.TicketCancellationDTO;
import com.example.demo.entity.User;
import com.example.demo.enums.CancellationStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketCancellationService {
    TicketCancellationDTO requestCancellation(Long ticketId, String reason, User user);
    TicketCancellationDTO approveCancellation(Long cancellationId, String notes);
    TicketCancellationDTO rejectCancellation(Long cancellationId, String reason);
    List<TicketCancellationDTO> getCancellationsByUser(Long userId);
    List<TicketCancellationDTO> getPendingCancellations();
    List<TicketCancellationDTO> getCancellationsByDateRange(LocalDateTime start, LocalDateTime end);
    List<TicketCancellationDTO> getCancellationsByFlight(Long flightId);
    List<TicketCancellationDTO> getCancellationsByStatus(CancellationStatus status);
    boolean isCancellationAllowed(Long ticketId);
    double calculateRefundAmount(Long ticketId);
} 