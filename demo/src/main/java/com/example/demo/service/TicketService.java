package com.example.demo.service;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.entity.Flight;
import com.example.demo.enums.TicketStatus;
import com.example.demo.dto.TicketResponseDTO; // Make sure this import is present
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    Ticket updateTicket(Ticket ticket);
    void deleteTicket(Long id);
    Optional<TicketResponseDTO> getTicketById(Long id); // Changed return type here
    List<TicketResponseDTO> getAllTickets(); // Changed return type here
    Page<Ticket> getAllTickets(Pageable pageable);
    List<TicketResponseDTO> getTicketsByUser(User user); // Changed return type here
    List<TicketResponseDTO> getTicketsByFlight(Flight flight); // Changed return type here
    List<TicketResponseDTO> getTicketsByStatus(TicketStatus status); // Changed return type here
    List<TicketResponseDTO> getUserTicketsByStatus(User user, TicketStatus status); // Changed return type here
    Ticket cancelTicket(Long id);
    Ticket updateTicketStatus(Long ticketId, TicketStatus status);
    boolean isTicketAvailable(Long ticketId);
}