package com.example.demo.controller;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.entity.Flight;
import com.example.demo.enums.TicketStatus;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.createTicket(ticket));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        ticket.setId(id);
        return ResponseEntity.ok(ticketService.updateTicket(ticket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(ticketService.getTicketsByUser(user));
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<Ticket>> getTicketsByFlight(@PathVariable Long flightId) {
        Flight flight = new Flight();
        flight.setId(flightId);
        return ResponseEntity.ok(ticketService.getTicketsByFlight(flight));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(TicketStatus.valueOf(status.toUpperCase())));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Ticket>> getUserTicketsByStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(ticketService.getUserTicketsByStatus(user, TicketStatus.valueOf(status.toUpperCase())));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Ticket> cancelTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.cancelTicket(id));
    }
} 