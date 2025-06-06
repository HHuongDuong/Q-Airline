package com.example.demo.repository;

import com.example.demo.entity.Flight;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);
    List<Flight> findByAircraft(Aircraft aircraft);
    List<Flight> findByStatus(FlightStatus status);
    List<Flight> findByDepartureAirportAndArrivalAirport(String departureAirport, String arrivalAirport);
    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Flight> findByDepartureAirportAndArrivalAirportAndDepartureTimeBetween(
        String departureAirport, 
        String arrivalAirport, 
        LocalDateTime start, 
        LocalDateTime end
    );
    
    // Dashboard statistics methods
    Long countByStatusAndDepartureTimeBetween(String status, LocalDateTime start, LocalDateTime end);
} 