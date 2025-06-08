package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.entity.Flight;
import com.example.demo.entity.Seat;
import com.example.demo.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @EntityGraph(attributePaths = {"user", "flight", "seat"})
    Optional<Ticket> findById(Long id);

    List<Ticket> findByUser(User user);
    List<Ticket> findByFlight(Flight flight);
    List<Ticket> findBySeat(Seat seat);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByUserAndStatus(User user, TicketStatus status);
    List<Ticket> findByFlightAndStatus(Flight flight, TicketStatus status);
    
    // Dashboard statistics methods
    List<Ticket> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COALESCE(SUM(t.price), 0.0) FROM Ticket t WHERE t.createdAt BETWEEN ?1 AND ?2")
    Double sumPriceByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
} 