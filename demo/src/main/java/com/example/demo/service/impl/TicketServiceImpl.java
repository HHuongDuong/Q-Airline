package com.example.demo.service.impl;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.entity.Flight;
import com.example.demo.enums.TicketStatus;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.TicketService;
import com.example.demo.dto.TicketResponseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(Ticket ticket) {
        if (!ticketRepository.existsById(ticket.getId())) {
            throw new ResourceNotFoundException("Ticket not found");
        }
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public Optional<TicketResponseDTO> getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(this::convertToTicketResponseDTO);
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToTicketResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    @Override
    public List<TicketResponseDTO> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user).stream()
                .map(this::convertToTicketResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDTO> getTicketsByFlight(Flight flight) {
        return ticketRepository.findByFlight(flight).stream()
                .map(this::convertToTicketResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDTO> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status).stream()
                .map(this::convertToTicketResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDTO> getUserTicketsByStatus(User user, TicketStatus status) {
        return ticketRepository.findByUserAndStatus(user, status).stream()
                .map(this::convertToTicketResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Ticket cancelTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setStatus(TicketStatus.CANCELLED);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicketStatus(Long ticketId, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    @Override
    public boolean isTicketAvailable(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return ticket.getStatus() == TicketStatus.CONFIRMED;
    }

    private TicketResponseDTO convertToTicketResponseDTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setPrice(ticket.getPrice());
        dto.setStatus(ticket.getStatus());

        // Safely set Flight details
        if (ticket.getFlight() != null) {
            dto.setFlightId(ticket.getFlight().getId());
            dto.setFlightNumber(ticket.getFlight().getFlightNumber());
            dto.setDepartureAirport(ticket.getFlight().getDepartureAirport());
            dto.setArrivalAirport(ticket.getFlight().getArrivalAirport());
            dto.setDepartureTime(ticket.getFlight().getDepartureTime());
            dto.setArrivalTime(ticket.getFlight().getArrivalTime());
        } else {
            // Set default/null values if flight is null to prevent NullPointerException
            dto.setFlightId(null);
            dto.setFlightNumber("N/A");
            dto.setDepartureAirport("N/A");
            dto.setArrivalAirport("N/A");
            dto.setDepartureTime(null);
            dto.setArrivalTime(null);
        }

        // Safely set Seat details
        if (ticket.getSeat() != null) {
            dto.setSeatId(ticket.getSeat().getId());
            dto.setSeatNumber(ticket.getSeat().getSeatNumber());
            dto.setSeatRow(ticket.getSeat().getRow());
            dto.setSeatColumn(ticket.getSeat().getColumn());
        } else {
            // Set default/null values if seat is null
            dto.setSeatId(null);
            dto.setSeatNumber("N/A");
            dto.setSeatRow(0);
            dto.setSeatColumn(0);
        }

        // Safely set User details
        if (ticket.getUser() != null) {
            dto.setUserId(ticket.getUser().getId());
            dto.setUsername(ticket.getUser().getUsername());
            dto.setFullName(ticket.getUser().getFullName());
        } else {
            // Set default/null values if user is null
            dto.setUserId(null);
            dto.setUsername("N/A");
            dto.setFullName("N/A");
        }

        return dto;
    }

    @Override
    public long countAllTickets() {
        return ticketRepository.count();
    }

    @Override
    public double getTotalRevenue() {
        return ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.CONFIRMED)
                .mapToDouble(Ticket::getPrice)
                .sum();
    }

} 