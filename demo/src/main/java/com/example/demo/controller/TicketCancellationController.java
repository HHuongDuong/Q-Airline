package com.example.demo.controller;

import com.example.demo.dto.TicketCancellationDTO;
import com.example.demo.entity.User;
import com.example.demo.enums.CancellationStatus;
import com.example.demo.service.TicketCancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cancellations")
@RequiredArgsConstructor
public class TicketCancellationController {
    private final TicketCancellationService cancellationService;

    @PostMapping("/request")
    public ResponseEntity<TicketCancellationDTO> requestCancellation(
            @RequestParam Long ticketId,
            @RequestParam String reason,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cancellationService.requestCancellation(ticketId, reason, user));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<TicketCancellationDTO> approveCancellation(
            @PathVariable Long id,
            @RequestParam String notes) {
        return ResponseEntity.ok(cancellationService.approveCancellation(id, notes));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<TicketCancellationDTO> rejectCancellation(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(cancellationService.rejectCancellation(id, reason));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketCancellationDTO>> getCancellationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cancellationService.getCancellationsByUser(userId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TicketCancellationDTO>> getPendingCancellations() {
        return ResponseEntity.ok(cancellationService.getPendingCancellations());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TicketCancellationDTO>> getCancellationsByStatus(
            @PathVariable CancellationStatus status) {
        return ResponseEntity.ok(cancellationService.getCancellationsByStatus(status));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TicketCancellationDTO>> getCancellationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(cancellationService.getCancellationsByDateRange(start, end));
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<TicketCancellationDTO>> getCancellationsByFlight(@PathVariable Long flightId) {
        return ResponseEntity.ok(cancellationService.getCancellationsByFlight(flightId));
    }

    @GetMapping("/check/{ticketId}")
    public ResponseEntity<Boolean> isCancellationAllowed(@PathVariable Long ticketId) {
        return ResponseEntity.ok(cancellationService.isCancellationAllowed(ticketId));
    }

    @GetMapping("/refund/{ticketId}")
    public ResponseEntity<Double> calculateRefundAmount(@PathVariable Long ticketId) {
        return ResponseEntity.ok(cancellationService.calculateRefundAmount(ticketId));
    }
} 