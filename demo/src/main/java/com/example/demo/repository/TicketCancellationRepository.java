package com.example.demo.repository;

import com.example.demo.entity.TicketCancellation;
import com.example.demo.enums.CancellationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketCancellationRepository extends JpaRepository<TicketCancellation, Long> {
    List<TicketCancellation> findByTicket_Id(Long ticketId);
    List<TicketCancellation> findByUser_Id(Long userId);
    List<TicketCancellation> findByTicket_Flight_Id(Long flightId);
    List<TicketCancellation> findByStatus(CancellationStatus status);
    List<TicketCancellation> findByRequestedAtBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByTicket_Id(Long ticketId);
    
    List<TicketCancellation> findByStatusAndRequestedAtBetween(String status, LocalDateTime start, LocalDateTime end);
    
    // Dashboard statistics methods
    @Query("SELECT COUNT(t) FROM TicketCancellation t WHERE t.requestedAt >= :start AND t.requestedAt < :end")
    Long countByRequestedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT COALESCE(SUM(t.refundAmount), 0.0) FROM TicketCancellation t WHERE t.requestedAt >= :start AND t.requestedAt < :end")
    Double sumRefundAmountByRequestedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<TicketCancellation> findByTicketId(Long ticketId);
    List<TicketCancellation> findByUserId(Long userId);
    List<TicketCancellation> findByTicketIdAndStatus(Long ticketId, CancellationStatus status);
    List<TicketCancellation> findByUserIdAndStatus(Long userId, CancellationStatus status);
} 