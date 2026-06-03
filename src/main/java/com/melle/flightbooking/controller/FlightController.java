package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.flight.*;
import com.melle.flightbooking.interfaces.FlightService;
import com.melle.flightbooking.service.FlightServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }


    @Operation(summary = "Create a new flight")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Admin role required")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public FlightSummaryDto createFlight(@Valid @RequestBody RegisterFlightDto flight) {
        return flightService.createFlight(flight);
    }

    @Operation(summary = "Update flight origin by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight origin successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Admin role required"),
            @ApiResponse(responseCode = "404", description = "Invalid flight id")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/origin")
    public FlightSummaryDto updateOriginById(@Valid @RequestBody UpdateOriginDto request) {
        return this.flightService.updateOriginById(request.getId(), request.getOrigin());
    }

    @Operation(summary = "Update flight destination by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight destination successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Admin role required"),
            @ApiResponse(responseCode = "404", description = "Invalid flight id")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/destination")
    public FlightSummaryDto updateDestinationById(@Valid @RequestBody UpdateDestinationDto request) {
        return this.flightService.updateDestinationById(request.getId(), request.getDestination());
    }

    @Operation(summary = "Update flight date by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight date successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Admin role required"),
            @ApiResponse(responseCode = "404", description = "Invalid flight id")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/date")
    public FlightSummaryDto updateDateById(@Valid @RequestBody UpdateDateDto request) {
        return this.flightService.updateDateById(request.getId(), request.getDate());
    }

    @Operation(summary = "Update flight seats by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight seats successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Admin role required"),
            @ApiResponse(responseCode = "404", description = "Invalid flight id")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/seats")
    public FlightSummaryDto updateSeatsById(@Valid @RequestBody UpdateSeatsDto request) {
        return this.flightService.updateSeatsById(request.getId(), request.getSeats());
    }

    // Should not return Boolean
    @Operation(summary = "Delete flight by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Flight successfully deleted, no response body"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Admin role required"),
            @ApiResponse(responseCode = "404", description = "Invalid flight id")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable int id){
        flightService.deleteFlightById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all flights")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully gotten all flights"),
            @ApiResponse(responseCode = "401", description = "Valid jwt required"),
    })
    @GetMapping()
    public Iterable<FlightSummaryDto> getAllFlights(){
        return flightService.getAllFlights();
    }


    @Operation(summary = "Get 1 flight by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully gotten 1 flight"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Valid jwt required"),
            @ApiResponse(responseCode = "404", description = "Invalid flight id")
    })
    @GetMapping("/{id}")
    public FlightSummaryDto getFlightById(@PathVariable int id){
        return flightService.getFlightById(id);
    }

    @Operation(summary = "Getting all flights based on origin, destination, date and seats filters")
    @GetMapping("/search")
    public Iterable<FlightSummaryDto> getFlightsByFilters(
            @RequestParam (required = false) String origin,
            @RequestParam (required = false) String destination,
            @RequestParam (required = false) String date,
            @RequestParam (required = false) Integer seats
    ) {
        return flightService.getFlightsByFilter(origin, destination, date, seats);
    }
}