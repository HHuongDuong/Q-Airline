package com.example.demo.repository;

import com.example.demo.entity.Seat;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByAircraft(Aircraft aircraft);
    List<Seat> findByAircraftAndSeatType(Aircraft aircraft, SeatType seatType);
    List<Seat> findByAircraftAndStatus(Aircraft aircraft, SeatStatus status);
    Optional<Seat> findByAircraftAndSeatNumber(Aircraft aircraft, String seatNumber);
    List<Seat> findByAircraftAndSeatTypeAndStatus(Aircraft aircraft, SeatType seatType, SeatStatus status);
} 