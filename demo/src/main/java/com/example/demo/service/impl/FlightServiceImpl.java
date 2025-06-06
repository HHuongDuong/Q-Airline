package com.example.demo.service.impl;

import com.example.demo.entity.Flight;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.FlightStatus;
import com.example.demo.repository.FlightRepository;
import com.example.demo.service.FlightService;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight updateFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flight> getFlightByFlightNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByAircraft(Aircraft aircraft) {
        return flightRepository.findByAircraft(aircraft);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByStatus(FlightStatus status) {
        return flightRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> searchFlights(String departureAirport, String arrivalAirport, LocalDateTime start, LocalDateTime end) {
        return flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureTimeBetween(
            departureAirport, arrivalAirport, start, end);
    }

    @Override
    @Transactional
    public Flight updateFlightStatus(Long id, FlightStatus status) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        flight.setStatus(status);
        return flightRepository.save(flight);
    }
} 