package com.example.demo.service.impl;

import com.example.demo.dto.BookingRequestDTO;
import com.example.demo.dto.BookingResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.BookingService;
import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.BookingStatus;
import com.example.demo.enums.TicketStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Find flight
        Flight flight = flightRepository.findById(bookingRequest.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        // Find seat
        Seat seat = seatRepository.findByFlightAndSeatNumber(flight, bookingRequest.getSeatNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        // Check if seat is available
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new BadRequestException("Seat is not available");
        }

        // Create passenger
        Passenger passenger = passengerRepository.findByEmail(bookingRequest.getPassenger().getEmail())
                .orElseGet(() -> {
                    Passenger newPassenger = new Passenger();
                    newPassenger.setFirstName(bookingRequest.getPassenger().getFirstName());
                    newPassenger.setLastName(bookingRequest.getPassenger().getLastName());
                    newPassenger.setEmail(bookingRequest.getPassenger().getEmail());
                    newPassenger.setPhone(bookingRequest.getPassenger().getPhone());
                    return passengerRepository.save(newPassenger);
                });

        // Create booking
        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setSeat(seat);
        booking.setPassenger(passenger);
        booking.setUser(currentUser);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);

        // Update seat status
        seat.setStatus(SeatStatus.OCCUPIED);
        seatRepository.save(seat);

        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        ticket.setFlight(flight);
        ticket.setSeat(seat);
        ticket.setUser(currentUser);
        ticket.setPrice(seat.getPrice());
        ticket.setStatus(TicketStatus.CONFIRMED);
        ticketRepository.save(ticket);

        // Create response
        BookingResponseDTO response = new BookingResponseDTO();
        response.setId(booking.getId());
        response.setFlightNumber(flight.getFlightNumber());
        response.setDepartureAirport(flight.getDepartureAirport());
        response.setArrivalAirport(flight.getArrivalAirport());
        response.setDepartureTime(flight.getDepartureTime().toString());
        response.setArrivalTime(flight.getArrivalTime().toString());
        response.setSeatNumber(seat.getSeatNumber());
        response.setSeatType(seat.getSeatType().toString());
        response.setStatus(booking.getStatus().toString());
        response.setPassenger(bookingRequest.getPassenger());

        return response;
    }
}