package com.example.demo.service;

import com.example.demo.entity.DashboardStats;
import com.example.demo.repository.DashboardStatsRepository;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.TicketCancellationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardStatsRepository statsRepository;
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final TicketCancellationRepository cancellationRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    @Transactional
    public void generateDailyStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        DashboardStats stats = new DashboardStats();
        stats.setDate(startOfDay);

        // Calculate total bookings
        stats.setTotalBookings(ticketRepository.countByCreatedAtBetween(startOfDay, endOfDay));

        // Calculate total cancellations
        stats.setTotalCancellations(cancellationRepository.countByRequestedAtBetween(startOfDay, endOfDay));

        // Calculate total revenue
        stats.setTotalRevenue(ticketRepository.sumPriceByCreatedAtBetween(startOfDay, endOfDay));

        // Calculate total refunds
        stats.setTotalRefunds(cancellationRepository.sumRefundAmountByRequestedAtBetween(startOfDay, endOfDay));

        // Calculate flight statistics
        stats.setActiveFlights(flightRepository.countByStatusAndDepartureTimeBetween("ACTIVE", startOfDay, endOfDay));
        stats.setCompletedFlights(flightRepository.countByStatusAndDepartureTimeBetween("COMPLETED", startOfDay, endOfDay));
        stats.setDelayedFlights(flightRepository.countByStatusAndDepartureTimeBetween("DELAYED", startOfDay, endOfDay));

        // Calculate passenger statistics
        stats.setTotalPassengers(ticketRepository.countByCreatedAtBetween(startOfDay, endOfDay));

        // Calculate average ticket price
        double avgPrice = ticketRepository.findByCreatedAtBetween(startOfDay, endOfDay).stream()
            .mapToDouble(ticket -> ticket.getPrice())
            .average()
            .orElse(0.0);
        stats.setAverageTicketPrice(avgPrice);

        // Calculate occupancy rate
        double totalSeats = flightRepository.findByDepartureTimeBetween(startOfDay, endOfDay).stream()
            .mapToInt(flight -> flight.getTotalSeats())
            .sum();
        double occupiedSeats = ticketRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        stats.setOccupancyRate(totalSeats > 0 ? (occupiedSeats / totalSeats) * 100 : 0.0);

        statsRepository.save(stats);
    }

    public DashboardStats getLatestStats() {
        return statsRepository.findLatestStats();
    }

    public List<DashboardStats> getStatsByDateRange(LocalDateTime start, LocalDateTime end) {
        return statsRepository.findByDateBetween(start, end);
    }

    public List<DashboardStats> getStatsFromDate(LocalDateTime date) {
        return statsRepository.findStatsFromDate(date);
    }

    public Double getTotalRevenueByDateRange(LocalDateTime start, LocalDateTime end) {
        return ticketRepository.sumPriceByCreatedAtBetween(start, end);
    }

    public Double getTotalRevenue() {
        return ticketRepository.sumPriceByCreatedAtBetween(
            LocalDateTime.now().minusDays(30),
            LocalDateTime.now()
        );
    }

    public Long getTotalBookingsByDateRange(LocalDateTime start, LocalDateTime end) {
        return ticketRepository.countByCreatedAtBetween(start, end);
    }

    public Long getTotalBookings() {
        return ticketRepository.countByCreatedAtBetween(
            LocalDateTime.now().minusDays(30),
            LocalDateTime.now()
        );
    }

    public Long getTotalCancellationsByDateRange(LocalDateTime start, LocalDateTime end) {
        return cancellationRepository.countByRequestedAtBetween(start, end);
    }

    public Long getTotalCancellations() {
        return cancellationRepository.countByRequestedAtBetween(
            LocalDateTime.now().minusDays(30),
            LocalDateTime.now()
        );
    }

    public Map<String, Long> getFlightStatsByDateRange(LocalDateTime start, LocalDateTime end) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("active", flightRepository.countByStatusAndDepartureTimeBetween("ACTIVE", start, end));
        stats.put("completed", flightRepository.countByStatusAndDepartureTimeBetween("COMPLETED", start, end));
        stats.put("delayed", flightRepository.countByStatusAndDepartureTimeBetween("DELAYED", start, end));
        return stats;
    }

    public Map<String, Long> getFlightStats() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return getFlightStatsByDateRange(thirtyDaysAgo, LocalDateTime.now());
    }

    public Map<String, Object> getPassengerStatsByDateRange(LocalDateTime start, LocalDateTime end) {
        Map<String, Object> stats = new HashMap<>();
        long totalPassengers = ticketRepository.countByCreatedAtBetween(start, end);
        double avgPrice = ticketRepository.findByCreatedAtBetween(start, end).stream()
            .mapToDouble(ticket -> ticket.getPrice())
            .average()
            .orElse(0.0);
        
        stats.put("totalPassengers", totalPassengers);
        stats.put("averageTicketPrice", avgPrice);
        return stats;
    }

    public Map<String, Object> getPassengerStats() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return getPassengerStatsByDateRange(thirtyDaysAgo, LocalDateTime.now());
    }
} 