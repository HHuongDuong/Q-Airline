package com.example.demo.service.impl;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.entity.Flight;
import com.example.demo.enums.TicketStatus;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Ticket createTicket(Ticket ticket) {
        ticket.setStatus(TicketStatus.CONFIRMED);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByFlight(Flight flight) {
        return ticketRepository.findByFlight(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getUserTicketsByStatus(User user, TicketStatus status) {
        return ticketRepository.findByUserAndStatus(user, status);
    }

    @Override
    public Ticket cancelTicket(Long id) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            ticket.setStatus(TicketStatus.CANCELLED);
            return ticketRepository.save(ticket);
        }
        throw new RuntimeException("Ticket not found with id: " + id);
    }
} 