package com.example.demo.service;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.entity.Flight;
import com.example.demo.enums.TicketStatus;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    Ticket updateTicket(Ticket ticket);
    void deleteTicket(Long id);
    Optional<Ticket> getTicketById(Long id);
    List<Ticket> getAllTickets();
    List<Ticket> getTicketsByUser(User user);
    List<Ticket> getTicketsByFlight(Flight flight);
    List<Ticket> getTicketsByStatus(TicketStatus status);
    List<Ticket> getUserTicketsByStatus(User user, TicketStatus status);
    Ticket cancelTicket(Long id);
} 