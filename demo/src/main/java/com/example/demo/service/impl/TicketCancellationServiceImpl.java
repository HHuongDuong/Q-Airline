package com.example.demo.service.impl;

import com.example.demo.dto.TicketCancellationDTO;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.TicketCancellation;
import com.example.demo.entity.User;
import com.example.demo.enums.CancellationStatus;
import com.example.demo.enums.TicketStatus;
import com.example.demo.repository.TicketCancellationRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.TicketCancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketCancellationServiceImpl implements TicketCancellationService {
    private final TicketCancellationRepository cancellationRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public TicketCancellationDTO requestCancellation(Long ticketId, String reason, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!isCancellationAllowed(ticketId)) {
            throw new RuntimeException("Cancellation is not allowed for this ticket");
        }

        TicketCancellation cancellation = new TicketCancellation();
        cancellation.setTicket(ticket);
        cancellation.setUser(user);
        cancellation.setReason(reason);
        cancellation.setStatus(CancellationStatus.PENDING);
        cancellation.setRequestedAt(LocalDateTime.now());

        return convertToDTO(cancellationRepository.save(cancellation));
    }

    @Override
    @Transactional
    public TicketCancellationDTO approveCancellation(Long cancellationId, String notes) {
        TicketCancellation cancellation = cancellationRepository.findById(cancellationId)
                .orElseThrow(() -> new RuntimeException("Cancellation request not found"));

        if (cancellation.getStatus() != CancellationStatus.PENDING) {
            throw new RuntimeException("Only pending cancellations can be approved");
        }

        cancellation.setStatus(CancellationStatus.APPROVED);
        cancellation.setNotes(notes);
        cancellation.setProcessedAt(LocalDateTime.now());

        // Update ticket status
        Ticket ticket = cancellation.getTicket();
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);

        return convertToDTO(cancellationRepository.save(cancellation));
    }

    @Override
    @Transactional
    public TicketCancellationDTO rejectCancellation(Long cancellationId, String reason) {
        TicketCancellation cancellation = cancellationRepository.findById(cancellationId)
                .orElseThrow(() -> new RuntimeException("Cancellation request not found"));

        if (cancellation.getStatus() != CancellationStatus.PENDING) {
            throw new RuntimeException("Only pending cancellations can be rejected");
        }

        cancellation.setStatus(CancellationStatus.REJECTED);
        cancellation.setNotes(reason);
        cancellation.setProcessedAt(LocalDateTime.now());

        return convertToDTO(cancellationRepository.save(cancellation));
    }

    @Override
    public List<TicketCancellationDTO> getCancellationsByUser(Long userId) {
        return cancellationRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketCancellationDTO> getPendingCancellations() {
        return cancellationRepository.findByStatus(CancellationStatus.PENDING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketCancellationDTO> getCancellationsByDateRange(LocalDateTime start, LocalDateTime end) {
        return cancellationRepository.findByRequestedAtBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketCancellationDTO> getCancellationsByFlight(Long flightId) {
        return cancellationRepository.findByTicket_Flight_Id(flightId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketCancellationDTO> getCancellationsByStatus(CancellationStatus status) {
        return cancellationRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCancellationAllowed(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // Check if ticket is already cancelled
        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            return false;
        }

        // Check if flight has already departed
        if (ticket.getFlight().getDepartureTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Add more business rules here if needed
        return true;
    }

    @Override
    public double calculateRefundAmount(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!isCancellationAllowed(ticketId)) {
            return 0.0;
        }

        // Calculate refund based on time before departure
        LocalDateTime departureTime = ticket.getFlight().getDepartureTime();
        long hoursBeforeDeparture = java.time.Duration.between(LocalDateTime.now(), departureTime).toHours();

        double basePrice = ticket.getPrice();
        if (hoursBeforeDeparture > 48) {
            return basePrice * 0.9; // 90% refund
        } else if (hoursBeforeDeparture > 24) {
            return basePrice * 0.7; // 70% refund
        } else if (hoursBeforeDeparture > 12) {
            return basePrice * 0.5; // 50% refund
        } else {
            return basePrice * 0.3; // 30% refund
        }
    }

    private TicketCancellationDTO convertToDTO(TicketCancellation cancellation) {
        TicketCancellationDTO dto = new TicketCancellationDTO();
        dto.setId(cancellation.getId());
        dto.setTicketId(cancellation.getTicket().getId());
        dto.setUserId(cancellation.getUser().getId());
        dto.setReason(cancellation.getReason());
        dto.setStatus(cancellation.getStatus());
        dto.setRequestedAt(cancellation.getRequestedAt());
        dto.setProcessedAt(cancellation.getProcessedAt());
        dto.setRejectionReason(cancellation.getNotes());
        return dto;
    }
} 