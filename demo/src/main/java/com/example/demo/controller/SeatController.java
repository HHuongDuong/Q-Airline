package com.example.demo.controller;

import com.example.demo.dto.SeatMapDTO;
import com.example.demo.dto.SeatRequestDTO;
import com.example.demo.dto.SeatSearchCriteria;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Flight;
import com.example.demo.entity.Aircraft;
import com.example.demo.enums.SeatStatus;
import com.example.demo.enums.SeatType;
import com.example.demo.service.SeatService;
import com.example.demo.service.FlightService;
import com.example.demo.service.AircraftService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@Tag(name = "Seat Management", description = "APIs for managing flight seats")
public class SeatController {

    private final SeatService seatService;

    private final FlightService flightService;

    private final AircraftService aircraftService;

    @GetMapping
    @Operation(summary = "Get all seats", description = "Retrieves a list of all seats")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Seat>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get seat by ID", description = "Retrieves a specific seat by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the seat",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Seat not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Seat> getSeatById(
            @Parameter(description = "ID of the seat to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new seat", description = "Creates a new seat with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created the seat",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> createSeat(
            @Parameter(description = "Seat details", required = true)
            @Valid @RequestBody SeatRequestDTO seatRequestDTO) {
        Flight flight = flightService.getFlightById(seatRequestDTO.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + seatRequestDTO.getFlightId()));
        
        Aircraft aircraft = aircraftService.getAircraftById(seatRequestDTO.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + seatRequestDTO.getAircraftId()));
        
        int columnNumber;
        try {
            columnNumber = Integer.parseInt(seatRequestDTO.getColumn());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Column number must be a valid integer");
        }
        
        Seat seat = new Seat();
        seat.setSeatNumber(seatRequestDTO.getSeatNumber());
        seat.setSeatType(seatRequestDTO.getSeatType());
        seat.setStatus(seatRequestDTO.getStatus());
        seat.setPrice(seatRequestDTO.getPrice());
        seat.setHasExtraLegroom(seatRequestDTO.getHasExtraLegroom());
        seat.setIsEmergencyExit(seatRequestDTO.getIsEmergencyExit());
        seat.setIsBulkhead(seatRequestDTO.getIsBulkhead());
        seat.setRow(seatRequestDTO.getRow());
        seat.setColumn(columnNumber);
        seat.setNotes(seatRequestDTO.getNotes());
        seat.setFlight(flight);
        seat.setAircraft(aircraft);
        
        return new ResponseEntity<>(seatService.createSeat(seat), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a seat", description = "Updates an existing seat with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the seat",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Seat not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> updateSeat(
            @Parameter(description = "ID of the seat to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated seat details", required = true)
            @Valid @RequestBody SeatRequestDTO seatRequestDTO) {
        Flight flight = flightService.getFlightById(seatRequestDTO.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + seatRequestDTO.getFlightId()));
        
        Aircraft aircraft = aircraftService.getAircraftById(seatRequestDTO.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + seatRequestDTO.getAircraftId()));
        
        int columnNumber;
        try {
            columnNumber = Integer.parseInt(seatRequestDTO.getColumn());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Column number must be a valid integer");
        }
        
        Seat seat = new Seat();
        seat.setSeatNumber(seatRequestDTO.getSeatNumber());
        seat.setSeatType(seatRequestDTO.getSeatType());
        seat.setStatus(seatRequestDTO.getStatus());
        seat.setPrice(seatRequestDTO.getPrice());
        seat.setHasExtraLegroom(seatRequestDTO.getHasExtraLegroom());
        seat.setIsEmergencyExit(seatRequestDTO.getIsEmergencyExit());
        seat.setIsBulkhead(seatRequestDTO.getIsBulkhead());
        seat.setRow(seatRequestDTO.getRow());
        seat.setColumn(columnNumber);
        seat.setNotes(seatRequestDTO.getNotes());
        seat.setFlight(flight);
        seat.setAircraft(aircraft);
        
        return ResponseEntity.ok(seatService.updateSeat(id, seat));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a seat", description = "Deletes a seat by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the seat"),
        @ApiResponse(responseCode = "404", description = "Seat not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSeat(
            @Parameter(description = "ID of the seat to delete", required = true)
            @PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "Get seats by flight", description = "Retrieves all seats for a specific flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getSeatsByFlight(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.getSeatsByFlight(flightId));
    }

    @GetMapping("/flight/{flightId}/map")
    @Operation(summary = "Get seat map", description = "Retrieves the seat map for a specific flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved seat map",
            content = @Content(schema = @Schema(implementation = SeatMapDTO.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<SeatMapDTO>> getSeatMap(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.getSeatMap(flightId));
    }

    @PostMapping("/search")
    @Operation(summary = "Search seats", description = "Searches for seats based on provided criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved matching seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> searchSeats(
            @Parameter(description = "Search criteria", required = true)
            @Valid @RequestBody SeatSearchCriteria criteria) {
        return ResponseEntity.ok(seatService.searchSeats(criteria));
    }

    @GetMapping("/flight/{flightId}/recommended")
    @Operation(summary = "Get recommended seats", description = "Retrieves recommended seats for a flight based on preferences")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved recommended seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getRecommendedSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId,
            @Parameter(description = "Seat type preference")
            @RequestParam(required = false) SeatType seatType,
            @Parameter(description = "Extra legroom preference")
            @RequestParam(required = false) Boolean hasExtraLegroom,
            @Parameter(description = "Emergency exit preference")
            @RequestParam(required = false) Boolean emergencyExit) {
        return ResponseEntity.ok(seatService.getRecommendedSeats(flightId, seatType, hasExtraLegroom, emergencyExit));
    }

    @GetMapping("/flight/{flightId}/cheapest")
    @Operation(summary = "Get cheapest seats", description = "Retrieves the cheapest available seats for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.findCheapestSeats(flightId));
    }

    @GetMapping("/flight/{flightId}/cheapest/{type}")
    @Operation(summary = "Get cheapest seats by type", description = "Retrieves the cheapest available seats of a specific type for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestSeatsByType(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId,
            @Parameter(description = "Type of seat", required = true)
            @PathVariable SeatType type) {
        return ResponseEntity.ok(seatService.findCheapestSeatsByType(flightId, type));
    }

    @GetMapping("/flight/{flightId}/cheapest/extra-legroom")
    @Operation(summary = "Get cheapest extra legroom seats", description = "Retrieves the cheapest available extra legroom seats for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest extra legroom seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestExtraLegroomSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.findCheapestExtraLegroomSeats(flightId));
    }

    @GetMapping("/flight/{flightId}/cheapest/emergency-exit")
    @Operation(summary = "Get cheapest emergency exit seats", description = "Retrieves the cheapest available emergency exit seats for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest emergency exit seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestEmergencyExitSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.findCheapestEmergencyExitSeats(flightId));
    }

    @GetMapping("/flight/{flightId}/cheapest/window")
    @Operation(summary = "Get cheapest window seats", description = "Retrieves the cheapest available window seats for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest window seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestWindowSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.findCheapestWindowSeats(flightId));
    }

    @GetMapping("/flight/{flightId}/cheapest/aisle")
    @Operation(summary = "Get cheapest aisle seats", description = "Retrieves the cheapest available aisle seats for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest aisle seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestAisleSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.findCheapestAisleSeats(flightId));
    }

    @GetMapping("/flight/{flightId}/cheapest/middle")
    @Operation(summary = "Get cheapest middle seats", description = "Retrieves the cheapest available middle seats for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest middle seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestMiddleSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.findCheapestMiddleSeats(flightId));
    }

    @GetMapping("/flight/{flightId}/cheapest/bulkhead")
    @Operation(summary = "Get cheapest bulkhead seats", description = "Retrieves the cheapest available bulkhead seats for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest bulkhead seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestBulkheadSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.findCheapestBulkheadSeats(flightId));
    }

    @GetMapping("/flight/{flightId}/cheapest/exit-row")
    @Operation(summary = "Get cheapest exit row seats", description = "Retrieves the cheapest available exit row seats for a flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved cheapest exit row seats",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<Seat>> getCheapestExitRowSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.findCheapestExitRowSeats(flightId));
    }

    @GetMapping("/flight/{flightId}/map/selected")
    @Operation(summary = "Get seat map with selected seats", description = "Retrieves the seat map with specific seats marked as selected")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved seat map with selected seats",
            content = @Content(schema = @Schema(implementation = SeatMapDTO.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<SeatMapDTO>> getSeatMapWithSelectedSeats(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable Long flightId,
            @Parameter(description = "List of selected seat IDs", required = true)
            @RequestParam List<String> selectedSeatIds) {
        return ResponseEntity.ok(seatService.getSeatMapWithSelectedSeats(flightId, selectedSeatIds));
    }

    @PostMapping("/{id}/block")
    @Operation(summary = "Block a seat", description = "Blocks a seat from being booked")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully blocked the seat",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Seat not found"),
        @ApiResponse(responseCode = "400", description = "Seat cannot be blocked"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> blockSeat(
            @Parameter(description = "ID of the seat to block", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(seatService.blockSeat(id));
    }

    @PostMapping("/{id}/unblock")
    @Operation(summary = "Unblock a seat", description = "Unblocks a previously blocked seat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully unblocked the seat",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Seat not found"),
        @ApiResponse(responseCode = "400", description = "Seat cannot be unblocked"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> unblockSeat(
            @Parameter(description = "ID of the seat to unblock", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(seatService.unblockSeat(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update seat status", description = "Updates the status of a seat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated seat status",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "404", description = "Seat not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> updateSeatStatus(
            @Parameter(description = "ID of the seat to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status", required = true)
            @RequestParam SeatStatus status) {
        return ResponseEntity.ok(seatService.updateSeatStatus(id, status));
    }

    @PostMapping("/{id}/maintenance")
    @Operation(summary = "Put seat in maintenance", description = "Puts a seat in maintenance mode")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully put seat in maintenance",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Seat not found"),
        @ApiResponse(responseCode = "400", description = "Seat cannot be put in maintenance"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> putSeatInMaintenance(
            @Parameter(description = "ID of the seat to put in maintenance", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(seatService.putSeatInMaintenance(id));
    }

    @PostMapping("/{id}/release")
    @Operation(summary = "Release a seat", description = "Releases a seat from maintenance or blocked status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully released the seat",
            content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Seat not found"),
        @ApiResponse(responseCode = "400", description = "Seat cannot be released"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> releaseSeat(
            @Parameter(description = "ID of the seat to release", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(seatService.releaseSeat(id));
    }
} 