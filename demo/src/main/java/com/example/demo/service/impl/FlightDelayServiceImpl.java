package com.example.demo.service.impl;

import com.example.demo.dto.FlightDelayRequestDTO;
import com.example.demo.dto.FlightDelayResponseDTO;
import com.example.demo.entity.Flight;
import com.example.demo.entity.FlightDelay;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FlightDelayRepository;
import com.example.demo.repository.FlightRepository;
import com.example.demo.service.FlightDelayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightDelayServiceImpl implements FlightDelayService {

    private final FlightDelayRepository flightDelayRepository;
    private final FlightRepository flightRepository;

    @Override
    public List<FlightDelayResponseDTO> getAllFlightDelays() {
        return flightDelayRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FlightDelayResponseDTO> getFlightDelayById(Long id) {
        return flightDelayRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public FlightDelayResponseDTO createFlightDelay(FlightDelayRequestDTO flightDelayRequestDTO) {
        Flight flight = flightRepository.findById(flightDelayRequestDTO.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + flightDelayRequestDTO.getFlightId()));

        FlightDelay flightDelay = new FlightDelay();
        flightDelay.setFlight(flight);
        flightDelay.setDelayDuration(flightDelayRequestDTO.getDelayDuration());
        flightDelay.setReason(flightDelayRequestDTO.getReason());
        flightDelay.setStatus(flightDelayRequestDTO.getStatus());

        FlightDelay savedFlightDelay = flightDelayRepository.save(flightDelay);
        return convertToDto(savedFlightDelay);
    }

    @Override
    public FlightDelayResponseDTO updateFlightDelay(Long id, FlightDelayRequestDTO flightDelayRequestDTO) {
        FlightDelay existingDelay = flightDelayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight Delay not found with id: " + id));

        Flight flight = flightRepository.findById(flightDelayRequestDTO.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + flightDelayRequestDTO.getFlightId()));

        existingDelay.setFlight(flight);
        existingDelay.setDelayDuration(flightDelayRequestDTO.getDelayDuration());
        existingDelay.setReason(flightDelayRequestDTO.getReason());
        existingDelay.setStatus(flightDelayRequestDTO.getStatus());

        FlightDelay updatedFlightDelay = flightDelayRepository.save(existingDelay);
        return convertToDto(updatedFlightDelay);
    }

    @Override
    public void deleteFlightDelay(Long id) {
        if (!flightDelayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flight Delay not found with id: " + id);
        }
        flightDelayRepository.deleteById(id);
    }

    private FlightDelayResponseDTO convertToDto(FlightDelay flightDelay) {
        FlightDelayResponseDTO dto = new FlightDelayResponseDTO();
        dto.setId(flightDelay.getId());
        dto.setFlightNumber(flightDelay.getFlight().getFlightNumber());
        dto.setRoute(flightDelay.getFlight().getDepartureAirport() + " - " + flightDelay.getFlight().getArrivalAirport());
        dto.setDelayDuration(flightDelay.getDelayDuration());
        dto.setReason(flightDelay.getReason());
        dto.setStatus(flightDelay.getStatus());
        dto.setCreatedAt(flightDelay.getCreatedAt());
        dto.setUpdatedAt(flightDelay.getUpdatedAt());
        return dto;
    }
}