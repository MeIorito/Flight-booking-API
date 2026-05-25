package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.flight.*;
import com.melle.flightbooking.service.FlightServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightServiceImp flightService;

    @Autowired
    public FlightController(FlightServiceImp flightService) {
        this.flightService = flightService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public FlightSummaryDto createFlight(@Valid @RequestBody RegisterFlightDto flight) {
        return flightService.createFlight(flight);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/origin")
    public FlightSummaryDto updateOriginById(@Valid @RequestBody UpdateOriginDto request) {
        return this.flightService.updateOriginById(request.getId(), request.getOrigin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/destination")
    public FlightSummaryDto updateDestinationById(@Valid @RequestBody UpdateDestinationDto request) {
        return this.flightService.updateDestinationById(request.getId(), request.getDestination());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/date")
    public FlightSummaryDto updateDateById(@Valid @RequestBody UpdateDateDto request) {
        return this.flightService.updateDateById(request.getId(), request.getDate());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/seats")
    public FlightSummaryDto updateSeatsById(@Valid @RequestBody UpdateSeatsDto request) {
        return this.flightService.updateSeatsById(request.getId(), request.getSeats());
    }

    // Should not return Boolean
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public boolean deleteFlight(@PathVariable int id){
        return flightService.deleteFlightById(id);
    }

    @GetMapping()
    public Iterable<FlightSummaryDto> getAllFlights(){
        return flightService.getAllFlights();
    }

    @GetMapping("/{id}")
    public FlightSummaryDto getFlightById(@PathVariable int id){
        return flightService.getFlightById(id);
    }
}
