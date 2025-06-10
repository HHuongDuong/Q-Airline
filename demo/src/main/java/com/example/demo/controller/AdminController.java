package com.example.demo.controller;

import com.example.demo.entity.Flight;
import com.example.demo.entity.News;
import com.example.demo.entity.Aircraft;
import com.example.demo.service.FlightService;
import com.example.demo.service.AircraftService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.dto.FlightRequestDTO;
import com.example.demo.dto.FlightResponseDTO;
import com.example.demo.dto.TicketResponseDTO;
import com.example.demo.entity.Ticket;
import com.example.demo.service.TicketService;
import com.example.demo.service.FlightDelayService;
import com.example.demo.dto.FlightDelayRequestDTO;
import com.example.demo.dto.FlightDelayResponseDTO;
import com.example.demo.entity.Seat;
import com.example.demo.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.example.demo.service.UserService;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.service.NewsService;
import com.example.demo.dto.NewsRequestDTO;
import com.example.demo.dto.NewsResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final FlightService flightService;
    private final AircraftService aircraftService;
    private final TicketService ticketService;
    private final FlightDelayService flightDelayService;
    private final UserService userService;
    private final NewsService newsService;
    private final SeatService seatService;

    @GetMapping("admin/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "admin/admin";
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/delays")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDelays() {
        return "admin/delays";
    }

    @GetMapping("/admin/admin-tickets")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTickets() {
        return "admin/admin-tickets";
    }

    @GetMapping("/admin/admin-users")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminUsers() {
        return "admin/admin-users";
    }

    @GetMapping("/admin/admin-news")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminNews() {
        return "admin/admin-news";
    }

    @GetMapping("/admin/admin-aircraft")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAircraft() {
        return "admin/admin-aircraft";
    }

    @GetMapping("/admin/admin-flights")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminFlightsFlightsPage() {
        return "admin/admin-flights";
    }

    @GetMapping("/admin/admin-seats")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminSeats() {
        return "admin/admin-seats";
    }

    @GetMapping("/api/admin/aircraft")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Aircraft>> getAllAircraft() {
        return ResponseEntity.ok(aircraftService.getAllAircraft());
    }

    @PostMapping("/api/admin/aircraft")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
        return ResponseEntity.ok(aircraftService.createAircraft(aircraft));
    }

    @GetMapping("/api/admin/aircraft/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        return aircraftService.getAircraftById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/admin/aircraft/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @RequestBody Aircraft aircraft) {
        aircraft.setId(id);
        return ResponseEntity.ok(aircraftService.updateAircraft(aircraft));
    }

    @DeleteMapping("/api/admin/aircraft/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/admin/flights")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FlightResponseDTO>> getAllFlights(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Flight> flightPage = flightService.getAllFlights(pageable);
        Page<FlightResponseDTO> dtoPage = flightPage.map(flight -> {
            FlightResponseDTO dto = new FlightResponseDTO();
            dto.setId(flight.getId());
            dto.setFlightNumber(flight.getFlightNumber());
            dto.setDepartureAirport(flight.getDepartureAirport());
            dto.setArrivalAirport(flight.getArrivalAirport());
            dto.setDepartureTime(flight.getDepartureTime());
            dto.setArrivalTime(flight.getArrivalTime());
            dto.setTotalSeats(flight.getTotalSeats());
            dto.setAvailableSeats(flight.getAvailableSeats());
            dto.setPrice(flight.getPrice());
            dto.setStatus(flight.getStatus());
            if (flight.getAircraft() != null) {
                dto.setAircraftId(flight.getAircraft().getId());
                dto.setAircraftCode(flight.getAircraft().getCode());
                dto.setAircraftModel(flight.getAircraft().getModel());
            }
            return dto;
        });
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/api/admin/flights/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightResponseDTO> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.getFlightById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        FlightResponseDTO dto = new FlightResponseDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setDepartureAirport(flight.getDepartureAirport());
        dto.setArrivalAirport(flight.getArrivalAirport());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setTotalSeats(flight.getTotalSeats());
        dto.setAvailableSeats(flight.getAvailableSeats());
        dto.setPrice(flight.getPrice());
        dto.setStatus(flight.getStatus());
        if (flight.getAircraft() != null) {
            dto.setAircraftId(flight.getAircraft().getId());
            dto.setAircraftCode(flight.getAircraft().getCode());
            dto.setAircraftModel(flight.getAircraft().getModel());
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/api/admin/flights")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> createFlight(@RequestBody FlightRequestDTO flightRequestDTO) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightRequestDTO.getFlightNumber());
        flight.setDepartureAirport(flightRequestDTO.getDepartureAirport());
        flight.setArrivalAirport(flightRequestDTO.getArrivalAirport());
        flight.setDepartureTime(flightRequestDTO.getDepartureTime());
        flight.setArrivalTime(flightRequestDTO.getArrivalTime());
        flight.setTotalSeats(flightRequestDTO.getTotalSeats());
        flight.setAvailableSeats(flightRequestDTO.getAvailableSeats());
        flight.setPrice(flightRequestDTO.getPrice());
        flight.setStatus(flightRequestDTO.getStatus());

        Aircraft aircraft = aircraftService.getAircraftById(flightRequestDTO.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + flightRequestDTO.getAircraftId()));
        flight.setAircraft(aircraft);
        return ResponseEntity.ok(flightService.createFlight(flight));
    }

    @PutMapping("/api/admin/flights/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightResponseDTO> updateFlight(@PathVariable Long id, @RequestBody FlightRequestDTO flightRequestDTO) {
        Flight flight = flightService.getFlightById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        
        flight.setFlightNumber(flightRequestDTO.getFlightNumber());
        flight.setDepartureAirport(flightRequestDTO.getDepartureAirport());
        flight.setArrivalAirport(flightRequestDTO.getArrivalAirport());
        flight.setDepartureTime(flightRequestDTO.getDepartureTime());
        flight.setArrivalTime(flightRequestDTO.getArrivalTime());
        flight.setTotalSeats(flightRequestDTO.getTotalSeats());
        flight.setAvailableSeats(flightRequestDTO.getAvailableSeats());
        flight.setPrice(flightRequestDTO.getPrice());
        flight.setStatus(flightRequestDTO.getStatus());

        Aircraft aircraft = aircraftService.getAircraftById(flightRequestDTO.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + flightRequestDTO.getAircraftId()));
        flight.setAircraft(aircraft);
        
        Flight updatedFlight = flightService.updateFlight(flight);

        FlightResponseDTO dto = new FlightResponseDTO();
        dto.setId(updatedFlight.getId());
        dto.setFlightNumber(updatedFlight.getFlightNumber());
        dto.setDepartureAirport(updatedFlight.getDepartureAirport());
        dto.setArrivalAirport(updatedFlight.getArrivalAirport());
        dto.setDepartureTime(updatedFlight.getDepartureTime());
        dto.setArrivalTime(updatedFlight.getArrivalTime());
        dto.setTotalSeats(updatedFlight.getTotalSeats());
        dto.setAvailableSeats(updatedFlight.getAvailableSeats());
        dto.setPrice(updatedFlight.getPrice());
        dto.setStatus(updatedFlight.getStatus());
        if (updatedFlight.getAircraft() != null) {
            dto.setAircraftId(updatedFlight.getAircraft().getId());
            dto.setAircraftCode(updatedFlight.getAircraft().getCode());
            dto.setAircraftModel(updatedFlight.getAircraft().getModel());
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/api/admin/flights/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok().build();
    }

    // Ticket endpoints
    @GetMapping("/api/admin/tickets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TicketResponseDTO>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> ticketPage = ticketService.getAllTickets(pageable);
        Page<TicketResponseDTO> dtoPage = ticketPage.map(ticket -> {
            TicketResponseDTO dto = new TicketResponseDTO();
            dto.setId(ticket.getId());
            dto.setTicketNumber(ticket.getTicketNumber());
            dto.setPrice(ticket.getPrice());
            dto.setStatus(ticket.getStatus());

            if (ticket.getFlight() != null) {
                dto.setFlightId(ticket.getFlight().getId());
                dto.setFlightNumber(ticket.getFlight().getFlightNumber());
                dto.setDepartureAirport(ticket.getFlight().getDepartureAirport());
                dto.setArrivalAirport(ticket.getFlight().getArrivalAirport());
                dto.setDepartureTime(ticket.getFlight().getDepartureTime());
                dto.setArrivalTime(ticket.getFlight().getArrivalTime());
            }

            if (ticket.getSeat() != null) {
                dto.setSeatId(ticket.getSeat().getId());
                dto.setSeatNumber(ticket.getSeat().getSeatNumber());
                dto.setSeatRow(ticket.getSeat().getRow());
                dto.setSeatColumn(ticket.getSeat().getColumn());
            }

            if (ticket.getUser() != null) {
                dto.setUserId(ticket.getUser().getId());
                dto.setUsername(ticket.getUser().getUsername());
                dto.setFullName(ticket.getUser().getFullName());
            }

            return dto;
        });
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/api/admin/tickets/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delay endpoints
    @GetMapping("/api/admin/delays")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FlightDelayResponseDTO>> getAllDelays() {
        return ResponseEntity.ok(flightDelayService.getAllFlightDelays());
    }

    @GetMapping("/api/admin/delays/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightDelayResponseDTO> getDelayById(@PathVariable Long id) {
        return flightDelayService.getFlightDelayById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/admin/delays")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightDelayResponseDTO> createDelay(@RequestBody FlightDelayRequestDTO flightDelayRequestDTO) {
        return ResponseEntity.ok(flightDelayService.createFlightDelay(flightDelayRequestDTO));
    }

    @PutMapping("/api/admin/delays/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightDelayResponseDTO> updateDelay(@PathVariable Long id, @RequestBody FlightDelayRequestDTO flightDelayRequestDTO) {
        return ResponseEntity.ok(flightDelayService.updateFlightDelay(id, flightDelayRequestDTO));
    }

    @DeleteMapping("/api/admin/delays/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDelay(@PathVariable Long id) {
        flightDelayService.deleteFlightDelay(id);
        return ResponseEntity.ok().build();
    }

    // User endpoints
    @GetMapping("/api/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOs = users.stream().map(user -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setFullName(user.getFullName());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setCreatedAt(user.getCreatedAt());
            dto.setUpdatedAt(user.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOs);
    }

    @GetMapping("/api/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/api/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());
        user.setFullName(userRequestDTO.getFullName());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        User createdUser = userService.createUser(user);

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(createdUser.getId());
        dto.setUsername(createdUser.getUsername());
        dto.setEmail(createdUser.getEmail());
        dto.setRole(createdUser.getRole());
        dto.setFullName(createdUser.getFullName());
        dto.setPhoneNumber(createdUser.getPhoneNumber());
        dto.setCreatedAt(createdUser.getCreatedAt());
        dto.setUpdatedAt(createdUser.getUpdatedAt());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/api/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        User user = userService.getUserById(id);
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        // Password should be handled separately or not updated here if empty
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            user.setPassword(userRequestDTO.getPassword());
        }
        user.setRole(userRequestDTO.getRole());
        user.setFullName(userRequestDTO.getFullName());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        User updatedUser = userService.updateUser(id, user);

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(updatedUser.getId());
        dto.setUsername(updatedUser.getUsername());
        dto.setEmail(updatedUser.getEmail());
        dto.setRole(updatedUser.getRole());
        dto.setFullName(updatedUser.getFullName());
        dto.setPhoneNumber(updatedUser.getPhoneNumber());
        dto.setCreatedAt(updatedUser.getCreatedAt());
        dto.setUpdatedAt(updatedUser.getUpdatedAt());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/api/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // News endpoints
    @GetMapping("/api/admin/news")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NewsResponseDTO>> getAllNews() {
        List<News> newsList = newsService.getAllNews();
        List<NewsResponseDTO> dtoList = newsList.stream()
            .map(news -> {
                NewsResponseDTO dto = new NewsResponseDTO();
                dto.setId(news.getId());
                dto.setTitle(news.getTitle());
                dto.setContent(news.getContent());
                dto.setImageUrl(news.getImageUrl());
                dto.setCreatedAt(news.getCreatedAt());
                dto.setUpdatedAt(news.getUpdatedAt());
                return dto;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/api/admin/news/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsResponseDTO> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @PostMapping("/api/admin/news")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsResponseDTO> createNews(@RequestBody NewsRequestDTO newsRequestDTO) {
        return ResponseEntity.ok(newsService.createNews(newsRequestDTO));
    }

    @PutMapping("/api/admin/news/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsResponseDTO> updateNews(@PathVariable Long id, @RequestBody NewsRequestDTO newsRequestDTO) {
        return ResponseEntity.ok(newsService.updateNews(id, newsRequestDTO));
    }

    @DeleteMapping("/api/admin/news/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin-create-seat")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminCreateSeatPage() {
        return "admin/admin-create-seat";
    }

    @GetMapping("/admin/admin-edit-seat")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminEditSeatPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Accessing /admin/admin-edit-seat. Authentication: " + authentication);
        if (authentication != null) {
            System.out.println("Authorities: " + authentication.getAuthorities());
        }
        return "admin/admin-edit-seat";
    }

    @GetMapping("/api/admin/flight-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, List<?>>> getFlightStats() {
        Map<String, List<?>> stats = new HashMap<>();
        // Placeholder data for demonstration. Replace with actual flight stats logic.
        stats.put("labels", List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun"));
        stats.put("values", List.of(10, 20, 15, 25, 22, 30));
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/api/admin/revenue-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, List<?>>> getRevenueStats() {
        Map<String, List<?>> stats = new HashMap<>();
        // Placeholder data for demonstration. Replace with actual revenue stats logic.
        stats.put("labels", List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun"));
        stats.put("values", List.of(50000, 75000, 60000, 90000, 85000, 100000));
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/api/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getOverallStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFlights", flightService.countAllFlights());
        stats.put("activeTickets", ticketService.countAllTickets());
        stats.put("totalUsers", userService.countAllUsers());
        stats.put("totalRevenue", ticketService.getTotalRevenue());
        return ResponseEntity.ok(stats);
    }
} 
