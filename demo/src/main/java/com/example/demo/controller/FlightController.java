package com.example.demo.controller;

import com.example.demo.entity.Flight;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.FlightStatus;
import com.example.demo.service.FlightService;
import com.example.demo.dto.FlightResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    // Helper method to convert Flight to FlightResponseDTO
    private FlightResponseDTO convertToDto(Flight flight) {
        FlightResponseDTO dto = new FlightResponseDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setDepartureAirport(flight.getDepartureAirport());
        dto.setArrivalAirport(flight.getArrivalAirport());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setTotalSeats(flight.getTotalSeats());
        dto.setAvailableSeats(flight.getAvailableSeats());
        dto.setPrice(flight.getPrice());
        dto.setStatus(flight.getStatus());
        if (flight.getAircraft() != null) {
            dto.setAircraftId(flight.getAircraft().getId());
            dto.setAircraftCode(flight.getAircraft().getCode());
            dto.setAircraftModel(flight.getAircraft().getModel());
        }
        return dto;
    }

    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.createFlight(flight));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        flight.setId(id);
        return ResponseEntity.ok(flightService.updateFlight(flight));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{flightNumber}")
    public ResponseEntity<FlightResponseDTO> getFlightByFlightNumber(@PathVariable String flightNumber) {
        return flightService.getFlightByFlightNumber(flightNumber)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        List<FlightResponseDTO> dtos = flights.stream()
                                                .map(this::convertToDto)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/aircraft/{aircraftId}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByAircraft(@PathVariable Long aircraftId) {
        Aircraft aircraft = new Aircraft();
        aircraft.setId(aircraftId);
        List<Flight> flights = flightService.getFlightsByAircraft(aircraft);
        List<FlightResponseDTO> dtos = flights.stream()
                                                .map(this::convertToDto)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByStatus(@PathVariable String status) {
        List<Flight> flights = flightService.getFlightsByStatus(FlightStatus.valueOf(status.toUpperCase()));
        List<FlightResponseDTO> dtos = flights.stream()
                                                .map(this::convertToDto)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(
            @RequestParam String departureAirport,
            @RequestParam String arrivalAirport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Flight> flights = flightService.searchFlights(departureAirport, arrivalAirport, start, end);
        List<FlightResponseDTO> dtos = flights.stream()
                                                .map(this::convertToDto)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Flight> updateFlightStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(flightService.updateFlightStatus(id, FlightStatus.valueOf(status.toUpperCase())));
    }
} 