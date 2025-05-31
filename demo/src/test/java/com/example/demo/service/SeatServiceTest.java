package com.example.demo.service;

import com.example.demo.entity.Seat;
import com.example.demo.entity.Flight;
import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private SeatServiceImpl seatService;

    private Flight flight;
    private Seat seat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("FL123");

        seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("A1");
        seat.setFlight(flight);
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setType(SeatType.WINDOW);
        seat.setPrice(100.0);
    }

    @Test
    void createSeat_ShouldReturnCreatedSeat() {
        when(flightRepository.findById(flight.getId())).thenReturn(Optional.of(flight));
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        Seat createdSeat = seatService.createSeat(seat);

        assertNotNull(createdSeat);
        assertEquals(seat.getSeatNumber(), createdSeat.getSeatNumber());
        assertEquals(seat.getStatus(), createdSeat.getStatus());
        assertEquals(seat.getType(), createdSeat.getType());
        assertEquals(seat.getPrice(), createdSeat.getPrice());
        verify(seatRepository).save(any(Seat.class));
    }

    @Test
    void createSeat_WithInvalidFlight_ShouldThrowException() {
        when(flightRepository.findById(flight.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> seatService.createSeat(seat));
        verify(seatRepository, never()).save(any(Seat.class));
    }

    @Test
    void getSeatById_ShouldReturnSeat() {
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));

        Seat foundSeat = seatService.getSeatById(seat.getId());

        assertNotNull(foundSeat);
        assertEquals(seat.getId(), foundSeat.getId());
        assertEquals(seat.getSeatNumber(), foundSeat.getSeatNumber());
    }

    @Test
    void getSeatById_WithInvalidId_ShouldThrowException() {
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> seatService.getSeatById(seat.getId()));
    }

    @Test
    void getSeatsByFlight_ShouldReturnListOfSeats() {
        List<Seat> seats = Arrays.asList(seat);
        when(seatRepository.findByFlightId(flight.getId())).thenReturn(seats);

        List<Seat> foundSeats = seatService.getSeatsByFlight(flight.getId());

        assertNotNull(foundSeats);
        assertEquals(1, foundSeats.size());
        assertEquals(seat.getSeatNumber(), foundSeats.get(0).getSeatNumber());
    }

    @Test
    void updateSeatStatus_ShouldUpdateStatus() {
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        Seat updatedSeat = seatService.updateSeatStatus(seat.getId(), SeatStatus.OCCUPIED);

        assertNotNull(updatedSeat);
        assertEquals(SeatStatus.OCCUPIED, updatedSeat.getStatus());
        verify(seatRepository).save(any(Seat.class));
    }

    @Test
    void updateSeatStatus_WithInvalidId_ShouldThrowException() {
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> seatService.updateSeatStatus(seat.getId(), SeatStatus.OCCUPIED));
        verify(seatRepository, never()).save(any(Seat.class));
    }

    @Test
    void deleteSeat_ShouldDeleteSeat() {
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
        doNothing().when(seatRepository).delete(seat);

        seatService.deleteSeat(seat.getId());

        verify(seatRepository).delete(seat);
    }

    @Test
    void deleteSeat_WithInvalidId_ShouldThrowException() {
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> seatService.deleteSeat(seat.getId()));
        verify(seatRepository, never()).delete(any(Seat.class));
    }
} 