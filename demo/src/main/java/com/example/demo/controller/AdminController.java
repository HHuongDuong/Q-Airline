package com.example.demo.controller;

import com.example.demo.entity.Flight;
import com.example.demo.entity.Aircraft;
import com.example.demo.service.FlightService;
import com.example.demo.service.AircraftService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.dto.FlightRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final FlightService flightService;
    private final AircraftService aircraftService;

    @GetMapping("/admin/admin.html")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "admin/admin.html"; 
    }

    @GetMapping("/admin/dashboard.html")
    public String adminDashboard() {
        return "admin/dashboard.html";
    }

    @GetMapping("/admin/flights.html")
    public String adminFlights() {
        return "admin/admin-flights.html";
    }

    @GetMapping("/admin/delays.html")
    public String adminDelays() {
        return "admin/delays.html";
    }

    @GetMapping("/admin/admin-tickets.html")
    public String adminTickets() {
        return "admin/admin-tickets.html";
    }

    @GetMapping("/admin/admin-users.html")
    public String adminUsers() {
        return "admin/admin-users.html";
    }

    @GetMapping("/admin/admin-news.html")
    public String adminNews() {
        return "admin/admin-news.html";
    }

    @GetMapping("/admin/admin-flights.html")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminFlightsFlightsPage() {
        return "admin/admin-flights.html";
    }

    @GetMapping("/api/admin/aircraft")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Aircraft>> getAllAircraft() {
        return ResponseEntity.ok(aircraftService.getAllAircraft());
    }

    @PostMapping("/api/admin/aircraft")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
        return ResponseEntity.ok(aircraftService.createAircraft(aircraft));
    }

    @PostMapping("/api/admin/flights")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> createFlight(@RequestBody FlightRequestDTO flightRequestDTO) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightRequestDTO.getFlightNumber());
        flight.setDepartureAirport(flightRequestDTO.getDepartureAirport());
        flight.setArrivalAirport(flightRequestDTO.getArrivalAirport());
        flight.setDepartureTime(flightRequestDTO.getDepartureTime());
        flight.setArrivalTime(flightRequestDTO.getArrivalTime());
        flight.setTotalSeats(flightRequestDTO.getTotalSeats());
        flight.setAvailableSeats(flightRequestDTO.getAvailableSeats());
        flight.setPrice(flightRequestDTO.getPrice());
        flight.setStatus(flightRequestDTO.getStatus());

        Aircraft aircraft = aircraftService.getAircraftById(flightRequestDTO.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + flightRequestDTO.getAircraftId()));
        flight.setAircraft(aircraft);
        return ResponseEntity.ok(flightService.createFlight(flight));
    }

    @PutMapping("/api/admin/flights/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody FlightRequestDTO flightRequestDTO) {
        Flight flight = flightService.getFlightById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        
        flight.setFlightNumber(flightRequestDTO.getFlightNumber());
        flight.setDepartureAirport(flightRequestDTO.getDepartureAirport());
        flight.setArrivalAirport(flightRequestDTO.getArrivalAirport());
        flight.setDepartureTime(flightRequestDTO.getDepartureTime());
        flight.setArrivalTime(flightRequestDTO.getArrivalTime());
        flight.setTotalSeats(flightRequestDTO.getTotalSeats());
        flight.setAvailableSeats(flightRequestDTO.getAvailableSeats());
        flight.setPrice(flightRequestDTO.getPrice());
        flight.setStatus(flightRequestDTO.getStatus());

        Aircraft aircraft = aircraftService.getAircraftById(flightRequestDTO.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + flightRequestDTO.getAircraftId()));
        flight.setAircraft(aircraft);
        return ResponseEntity.ok(flightService.updateFlight(flight));
    }
} 
