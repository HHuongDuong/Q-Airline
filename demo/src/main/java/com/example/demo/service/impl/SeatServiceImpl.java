package com.example.demo.service.impl;

import com.example.demo.dto.SeatMapDTO;
import com.example.demo.dto.SeatSearchCriteria;
import com.example.demo.entity.Flight;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.AircraftRepository;
import com.example.demo.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatServiceImpl implements SeatService {

    private static final String SEAT_NOT_FOUND = "Seat not found";
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;

    @Override
    public Seat blockSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
        seat.setStatus(SeatStatus.BLOCKED);
        return seatRepository.save(seat);
    }

    @Override
    public Seat unblockSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
        seat.setStatus(SeatStatus.AVAILABLE);
        return seatRepository.save(seat);
    }

    @Override
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    @Override
    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
    }

    @Override
    public Seat createSeat(Seat seat) {
        // Fetch Flight and Aircraft entities based on IDs provided in the seat object
        Flight flight = flightRepository.findById(seat.getFlight().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + seat.getFlight().getId()));
        Aircraft aircraft = aircraftRepository.findById(seat.getAircraft().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with ID: " + seat.getAircraft().getId()));

        seat.setFlight(flight);
        seat.setAircraft(aircraft);
        return seatRepository.save(seat);
    }

    @Override
    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));

        // Update fields
        seat.setSeatNumber(seatDetails.getSeatNumber());
        seat.setSeatType(seatDetails.getSeatType());
        seat.setStatus(seatDetails.getStatus());
        seat.setPrice(seatDetails.getPrice());
        seat.setHasExtraLegroom(seatDetails.getHasExtraLegroom());
        seat.setIsEmergencyExit(seatDetails.getIsEmergencyExit());
        seat.setIsBulkhead(seatDetails.getIsBulkhead());
        seat.setRow(seatDetails.getRow());
        seat.setColumn(seatDetails.getColumn());
        seat.setNotes(seatDetails.getNotes());

        // Update Flight and Aircraft if provided
        if (seatDetails.getFlight() != null && seatDetails.getFlight().getId() != null) {
            Flight flight = flightRepository.findById(seatDetails.getFlight().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + seatDetails.getFlight().getId()));
            seat.setFlight(flight);
        }
        if (seatDetails.getAircraft() != null && seatDetails.getAircraft().getId() != null) {
            Aircraft aircraft = aircraftRepository.findById(seatDetails.getAircraft().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with ID: " + seatDetails.getAircraft().getId()));
            seat.setAircraft(aircraft);
        }

        return seatRepository.save(seat);
    }

    @Override
    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
        seatRepository.delete(seat);
    }

    @Override
    public List<Seat> getSeatsByFlight(Long flightId) {
        return seatRepository.findByFlight_Id(flightId);
    }

    @Override
    public List<Seat> getAvailableSeatsByFlight(Long flightId) {
        return seatRepository.findByFlight_IdAndStatus(flightId, SeatStatus.AVAILABLE);
    }

    @Override
    public List<Seat> getSeatsByType(Long flightId, SeatType seatType) {
        return seatRepository.findByFlight_IdAndSeatType(flightId, seatType);
    }

    @Override
    public Seat getSeatByNumber(Long flightId, String seatNumber) {
        return seatRepository.findByFlight_IdAndSeatNumber(flightId, seatNumber).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
    }

    @Override
    public List<Seat> getExtraLegroomSeats(Long flightId) {
        return seatRepository.findByFlight_IdAndHasExtraLegroom(flightId, true);
    }

    @Override
    public List<Seat> getEmergencyExitSeats(Long flightId) {
        return seatRepository.findByFlight_IdAndIsEmergencyExit(flightId, true);
    }

    @Override
    public List<Seat> getSeatsByRow(Long flightId, String row) {
        try {
            int rowNumber = Integer.parseInt(row);
            return seatRepository.findByFlight_IdAndRow(flightId, rowNumber);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid row number format");
        }
    }

    @Override
    public List<SeatMapDTO> getSeatMap(Long flightId) {
        List<Seat> seats = seatRepository.findByFlight_Id(flightId);
        return seats.stream()
                .map(this::convertToSeatMapDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeatMapDTO> getSeatMapWithSelectedSeats(Long flightId, List<String> selectedSeatNumbers) {
        List<SeatMapDTO> seatMap = getSeatMap(flightId);
        seatMap.forEach(seat ->
            seat.setSelected(selectedSeatNumbers.contains(seat.getSeatNumber()))
        );
        return seatMap;
    }

    @Override
    public List<Seat> getRecommendedSeats(Long flightId, SeatType seatType, Boolean hasExtraLegroom, Boolean isEmergencyExit) {
        return seatRepository.findByFlight_Id(flightId).stream()
                .filter(seat -> seatType == null || seat.getSeatType() == seatType)
                .filter(seat -> hasExtraLegroom == null || seat.getHasExtraLegroom() == hasExtraLegroom)
                .filter(seat -> isEmergencyExit == null || seat.getIsEmergencyExit() == isEmergencyExit)
                .toList();
    }

    @Override
    public List<Seat> findCheapestSeats(Long flightId) {
        return seatRepository.findCheapestSeats(flightId);
    }

    @Override
    public List<Seat> findCheapestSeatsByType(Long flightId, SeatType seatType) {
        return seatRepository.findCheapestSeatsByType(flightId, seatType);
    }

    @Override
    public List<Seat> findCheapestExtraLegroomSeats(Long flightId) {
        return seatRepository.findCheapestExtraLegroomSeats(flightId);
    }

    @Override
    public List<Seat> findCheapestEmergencyExitSeats(Long flightId) {
        return seatRepository.findCheapestEmergencyExitSeats(flightId);
    }

    @Override
    public List<Seat> findCheapestWindowSeats(Long flightId) {
        return seatRepository.findCheapestWindowSeats(flightId);
    }

    @Override
    public List<Seat> findCheapestAisleSeats(Long flightId) {
        return seatRepository.findCheapestAisleSeats(flightId);
    }

    @Override
    public List<Seat> findCheapestMiddleSeats(Long flightId) {
        return seatRepository.findCheapestMiddleSeats(flightId);
    }

    @Override
    public List<Seat> findCheapestBulkheadSeats(Long flightId) {
        return seatRepository.findCheapestBulkheadSeats(flightId);
    }

    @Override
    public List<Seat> findCheapestExitRowSeats(Long flightId) {
        return seatRepository.findCheapestExitRowSeats(flightId);
    }

    @Override
    public Seat reserveSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
        seat.setStatus(SeatStatus.RESERVED);
        return seatRepository.save(seat);
    }

    @Override
    public Seat occupySeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
        seat.setStatus(SeatStatus.OCCUPIED);
        return seatRepository.save(seat);
    }

    @Override
    public Seat releaseSeat(Long seatId) {
        return unblockSeat(seatId);
    }

    @Override
    public Seat updateSeatStatus(Long seatId, SeatStatus status) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
        seat.setStatus(status);
        return seatRepository.save(seat);
    }

    @Override
    public Seat putSeatInMaintenance(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(SEAT_NOT_FOUND));
        seat.setStatus(SeatStatus.MAINTENANCE);
        return seatRepository.save(seat);
    }

    @Override
    public List<Seat> searchSeats(SeatSearchCriteria criteria) {
        return seatRepository.searchSeats(
                criteria.getFlightId(),
                criteria.getSeatType(),
                criteria.getStatus(),
                criteria.getMinPrice(),
                criteria.getMaxPrice(),
                criteria.getHasExtraLegroom(),
                criteria.getIsEmergencyExit()
        );
    }

    private SeatMapDTO convertToSeatMapDTO(Seat seat) {
        return new SeatMapDTO(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getSeatType(),
                seat.getStatus(),
                seat.getPrice(),
                seat.getHasExtraLegroom(),
                seat.getIsEmergencyExit(),
                seat.getIsBulkhead(),
                seat.getRow(),
                seat.getColumn(),
                seat.getNotes(),
                seat.getFlight() != null ? seat.getFlight().getId() : null,
                seat.getAircraft() != null ? seat.getAircraft().getId() : null,
                false // selected field, default to false
        );
    }
}