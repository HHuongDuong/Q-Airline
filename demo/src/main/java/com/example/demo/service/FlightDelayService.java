package com.example.demo.service;

import com.example.demo.dto.FlightDelayRequestDTO;
import com.example.demo.dto.FlightDelayResponseDTO;
import java.util.List;
import java.util.Optional;

public interface FlightDelayService {
    List<FlightDelayResponseDTO> getAllFlightDelays();
    Optional<FlightDelayResponseDTO> getFlightDelayById(Long id);
    FlightDelayResponseDTO createFlightDelay(FlightDelayRequestDTO flightDelayRequestDTO);
    FlightDelayResponseDTO updateFlightDelay(Long id, FlightDelayRequestDTO flightDelayRequestDTO);
    void deleteFlightDelay(Long id);
}