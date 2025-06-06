package com.example.demo.service;

import com.example.demo.dto.SeatMapDTO;
import com.example.demo.dto.SeatSearchCriteria;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Flight;
import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface SeatService {
    List<Seat> getAllSeats();
    Seat getSeatById(Long id);
    Seat createSeat(Seat seat);
    Seat updateSeat(Long id, Seat seat);
    void deleteSeat(Long id);
    List<Seat> getSeatsByFlight(Long flightId);
    List<Seat> getAvailableSeatsByFlight(Long flightId);
    List<Seat> getSeatsByType(Long flightId, SeatType seatType);
    Seat getSeatByNumber(Long flightId, String seatNumber);
    List<Seat> getExtraLegroomSeats(Long flightId);
    List<Seat> getEmergencyExitSeats(Long flightId);
    List<Seat> getSeatsByRow(Long flightId, String row);
    List<SeatMapDTO> getSeatMap(Long flightId);
    List<Seat> searchSeats(SeatSearchCriteria criteria);
    List<Seat> getRecommendedSeats(Long flightId, SeatType seatType, Boolean hasExtraLegroom, Boolean emergencyExit);
    List<Seat> findCheapestSeats(Long flightId);
    List<Seat> findCheapestSeatsByType(Long flightId, SeatType type);
    List<Seat> findCheapestExtraLegroomSeats(Long flightId);
    List<Seat> findCheapestEmergencyExitSeats(Long flightId);
    List<Seat> findCheapestWindowSeats(Long flightId);
    List<Seat> findCheapestAisleSeats(Long flightId);
    List<Seat> findCheapestMiddleSeats(Long flightId);
    List<Seat> findCheapestBulkheadSeats(Long flightId);
    List<Seat> findCheapestExitRowSeats(Long flightId);
    List<SeatMapDTO> getSeatMapWithSelectedSeats(Long flightId, List<String> selectedSeatIds);
    Seat blockSeat(Long seatId);
    Seat unblockSeat(Long seatId);
    Seat updateSeatStatus(Long seatId, SeatStatus status);
    Seat reserveSeat(Long seatId);
    Seat occupySeat(Long seatId);
    Seat releaseSeat(Long seatId);
    Seat putSeatInMaintenance(Long seatId);
}

