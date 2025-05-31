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
    List<Flight> findByDepartureCityAndArrivalCity(String departureCity, String arrivalCity);
    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Flight> findByDepartureCityAndArrivalCityAndDepartureTimeBetween(
        String departureCity, 
        String arrivalCity, 
        LocalDateTime start, 
        LocalDateTime end
    );
    
    // Dashboard statistics methods
    Long countByStatusAndDepartureTimeBetween(String status, LocalDateTime start, LocalDateTime end);
} 