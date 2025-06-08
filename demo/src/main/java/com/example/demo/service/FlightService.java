package com.example.demo.service;

import com.example.demo.entity.Flight;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.FlightStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightService {
    Flight createFlight(Flight flight);
    Flight updateFlight(Flight flight);
    void deleteFlight(Long id);
    Optional<Flight> getFlightById(Long id);
    Optional<Flight> getFlightByFlightNumber(String flightNumber);
    List<Flight> getAllFlights();
    Page<Flight> getAllFlights(Pageable pageable);
    List<Flight> getFlightsByAircraft(Aircraft aircraft);
    List<Flight> getFlightsByStatus(FlightStatus status);
    List<Flight> searchFlights(String departureCity, String arrivalCity, LocalDateTime start, LocalDateTime end);
    Flight updateFlightStatus(Long id, FlightStatus status);
} 