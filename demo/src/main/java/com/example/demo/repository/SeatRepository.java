package com.example.demo.repository;

import com.example.demo.dto.SeatSearchCriteria;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Flight;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByFlight(Flight flight);
    List<Seat> findByFlight_Id(Long flightId);
    List<Seat> findByAircraft(Aircraft aircraft);
    List<Seat> findByFlightAndStatus(Flight flight, SeatStatus status);
    List<Seat> findByFlight_IdAndStatus(Long flightId, SeatStatus status);
    List<Seat> findByFlightAndSeatType(Flight flight, SeatType seatType);
    List<Seat> findByFlight_IdAndSeatType(Long flightId, SeatType seatType);
    List<Seat> findByFlightAndSeatNumber(Flight flight, String seatNumber);
    List<Seat> findByFlight_IdAndSeatNumber(Long flightId, String seatNumber);
    List<Seat> findByFlightAndHasExtraLegroom(Flight flight, boolean hasExtraLegroom);
    List<Seat> findByFlight_IdAndHasExtraLegroom(Long flightId, boolean hasExtraLegroom);
    List<Seat> findByFlightAndIsEmergencyExit(Flight flight, boolean isEmergencyExit);
    List<Seat> findByFlight_IdAndIsEmergencyExit(Long flightId, boolean isEmergencyExit);
    List<Seat> findByFlightAndRow(Flight flight, int row);
    List<Seat> findByFlight_IdAndRow(Long flightId, int row);
    List<Seat> findByAircraftAndSeatType(Aircraft aircraft, SeatType seatType);
    List<Seat> findByAircraftAndStatus(Aircraft aircraft, SeatStatus status);
    List<Seat> findByAircraftAndSeatTypeAndStatus(Aircraft aircraft, SeatType seatType, SeatStatus status);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight)")
    List<Seat> findCheapestSeats(@Param("flight") Flight flight);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId)")
    List<Seat> findCheapestSeats(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.seatType = :type AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight AND s2.seatType = :type)")
    List<Seat> findCheapestSeatsByType(@Param("flight") Flight flight, @Param("type") SeatType type);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.seatType = :type AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId AND s2.seatType = :type)")
    List<Seat> findCheapestSeatsByType(@Param("flightId") Long flightId, @Param("type") SeatType type);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.hasExtraLegroom = true AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight AND s2.hasExtraLegroom = true)")
    List<Seat> findCheapestExtraLegroomSeats(@Param("flight") Flight flight);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.hasExtraLegroom = true AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId AND s2.hasExtraLegroom = true)")
    List<Seat> findCheapestExtraLegroomSeats(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.isEmergencyExit = true AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight AND s2.isEmergencyExit = true)")
    List<Seat> findCheapestEmergencyExitSeats(@Param("flight") Flight flight);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.isEmergencyExit = true AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId AND s2.isEmergencyExit = true)")
    List<Seat> findCheapestEmergencyExitSeats(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.seatType = 'WINDOW' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight AND s2.seatType = 'WINDOW')")
    List<Seat> findCheapestWindowSeats(@Param("flight") Flight flight);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.seatType = 'WINDOW' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId AND s2.seatType = 'WINDOW')")
    List<Seat> findCheapestWindowSeats(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.seatType = 'AISLE' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight AND s2.seatType = 'AISLE')")
    List<Seat> findCheapestAisleSeats(@Param("flight") Flight flight);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.seatType = 'AISLE' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId AND s2.seatType = 'AISLE')")
    List<Seat> findCheapestAisleSeats(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.seatType = 'MIDDLE' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight AND s2.seatType = 'MIDDLE')")
    List<Seat> findCheapestMiddleSeats(@Param("flight") Flight flight);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.seatType = 'MIDDLE' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId AND s2.seatType = 'MIDDLE')")
    List<Seat> findCheapestMiddleSeats(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.seatType = 'BULKHEAD' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight AND s2.seatType = 'BULKHEAD')")
    List<Seat> findCheapestBulkheadSeats(@Param("flight") Flight flight);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.seatType = 'BULKHEAD' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId AND s2.seatType = 'BULKHEAD')")
    List<Seat> findCheapestBulkheadSeats(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight = :flight AND s.seatType = 'EXIT_ROW' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight = :flight AND s2.seatType = 'EXIT_ROW')")
    List<Seat> findCheapestExitRowSeats(@Param("flight") Flight flight);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.seatType = 'EXIT_ROW' AND s.price = (SELECT MIN(s2.price) FROM Seat s2 WHERE s2.flight.id = :flightId AND s2.seatType = 'EXIT_ROW')")
    List<Seat> findCheapestExitRowSeats(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId " +
           "AND (:seatType IS NULL OR s.seatType = :seatType) " +
           "AND (:status IS NULL OR s.status = :status) " +
           "AND (:minPrice IS NULL OR s.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR s.price <= :maxPrice) " +
           "AND (:hasExtraLegroom IS NULL OR s.hasExtraLegroom = :hasExtraLegroom) " +
           "AND (:isEmergencyExit IS NULL OR s.isEmergencyExit = :isEmergencyExit)")
    List<Seat> searchSeats(@Param("flightId") Long flightId,
                          @Param("seatType") SeatType seatType,
                          @Param("status") SeatStatus status,
                          @Param("minPrice") Double minPrice,
                          @Param("maxPrice") Double maxPrice,
                          @Param("hasExtraLegroom") Boolean hasExtraLegroom,
                          @Param("isEmergencyExit") Boolean isEmergencyExit);

    boolean existsByFlight_IdAndSeatNumber(Long flightId, String seatNumber);
    Long countByFlight_IdAndStatus(Long flightId, SeatStatus status);
} 