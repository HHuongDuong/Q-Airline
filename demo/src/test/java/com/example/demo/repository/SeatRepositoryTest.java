package com.example.demo.repository;

import com.example.demo.entity.Seat;
import com.example.demo.entity.Flight;
import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SeatRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SeatRepository seatRepository;

    private Flight flight;
    private Seat seat;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setFlightNumber("FL123");
        flight = entityManager.persist(flight);

        seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setFlight(flight);
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setType(SeatType.WINDOW);
        seat.setPrice(100.0);
        seat = entityManager.persist(seat);
    }

    @Test
    void findByFlightId_ShouldReturnListOfSeats() {
        // Arrange
        Seat seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setSeatType(SeatType.ECONOMY);
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setFlightId(1L);
        entityManager.persist(seat);
        entityManager.flush();

        // Act
        List<Seat> foundSeats = seatRepository.findByFlightId(1L);

        // Assert
        assertNotNull(foundSeats);
        assertFalse(foundSeats.isEmpty());
        assertEquals(1, foundSeats.size());
        assertEquals("A1", foundSeats.get(0).getSeatNumber());
    }

    @Test
    void findByFlightIdAndSeatNumber_ShouldReturnSeat() {
        Optional<Seat> foundSeat = seatRepository.findByFlightIdAndSeatNumber(flight.getId(), seat.getSeatNumber());

        assertThat(foundSeat).isPresent();
        assertThat(foundSeat.get().getSeatNumber()).isEqualTo(seat.getSeatNumber());
        assertThat(foundSeat.get().getFlight().getId()).isEqualTo(flight.getId());
    }

    @Test
    void findByFlightIdAndStatus_ShouldReturnListOfSeats() {
        // Arrange
        Seat seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setSeatType(SeatType.ECONOMY);
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setFlightId(1L);
        entityManager.persist(seat);
        entityManager.flush();

        // Act
        List<Seat> foundSeats = seatRepository.findByFlightIdAndStatus(1L, SeatStatus.AVAILABLE);

        // Assert
        assertNotNull(foundSeats);
        assertFalse(foundSeats.isEmpty());
        assertEquals(1, foundSeats.size());
        assertEquals(SeatStatus.AVAILABLE, foundSeats.get(0).getStatus());
    }

    @Test
    void findByFlightIdAndSeatType_ShouldReturnListOfSeats() {
        // Arrange
        Seat seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setSeatType(SeatType.ECONOMY);
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setFlightId(1L);
        entityManager.persist(seat);
        entityManager.flush();

        // Act
        List<Seat> foundSeats = seatRepository.findByFlightIdAndSeatType(1L, SeatType.ECONOMY);

        // Assert
        assertNotNull(foundSeats);
        assertFalse(foundSeats.isEmpty());
        assertEquals(1, foundSeats.size());
        assertEquals(SeatType.ECONOMY, foundSeats.get(0).getSeatType());
    }

    @Test
    void countByFlightIdAndStatus_ShouldReturnCount() {
        long count = seatRepository.countByFlightIdAndStatus(flight.getId(), SeatStatus.AVAILABLE);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void save_ShouldPersistSeat() {
        Seat newSeat = new Seat();
        newSeat.setSeatNumber("A2");
        newSeat.setFlight(flight);
        newSeat.setStatus(SeatStatus.AVAILABLE);
        newSeat.setType(SeatType.AISLE);
        newSeat.setPrice(90.0);

        Seat savedSeat = seatRepository.save(newSeat);

        assertThat(savedSeat.getId()).isNotNull();
        assertThat(savedSeat.getSeatNumber()).isEqualTo(newSeat.getSeatNumber());
        assertThat(savedSeat.getFlight().getId()).isEqualTo(flight.getId());
    }

    @Test
    void delete_ShouldRemoveSeat() {
        seatRepository.delete(seat);
        Optional<Seat> deletedSeat = seatRepository.findById(seat.getId());

        assertThat(deletedSeat).isEmpty();
    }
} 