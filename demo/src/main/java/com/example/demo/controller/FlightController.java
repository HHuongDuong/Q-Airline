package com.example.demo.controller;

import com.example.demo.entity.Flight;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.FlightStatus;
import com.example.demo.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

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
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{flightNumber}")
    public ResponseEntity<Flight> getFlightByFlightNumber(@PathVariable String flightNumber) {
        return flightService.getFlightByFlightNumber(flightNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/aircraft/{aircraftId}")
    public ResponseEntity<List<Flight>> getFlightsByAircraft(@PathVariable Long aircraftId) {
        Aircraft aircraft = new Aircraft();
        aircraft.setId(aircraftId);
        return ResponseEntity.ok(flightService.getFlightsByAircraft(aircraft));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Flight>> getFlightsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(flightService.getFlightsByStatus(FlightStatus.valueOf(status.toUpperCase())));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam String departureAirport,
            @RequestParam String arrivalAirport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(flightService.searchFlights(departureAirport, arrivalAirport, start, end));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Flight> updateFlightStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(flightService.updateFlightStatus(id, FlightStatus.valueOf(status.toUpperCase())));
    }
} 