package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.entity.Flight;
import com.example.demo.entity.Seat;
import com.example.demo.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);
    List<Ticket> findByFlight(Flight flight);
    List<Ticket> findBySeat(Seat seat);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByUserAndStatus(User user, TicketStatus status);
    List<Ticket> findByFlightAndStatus(Flight flight, TicketStatus status);
} 