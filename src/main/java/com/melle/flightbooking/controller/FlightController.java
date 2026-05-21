package com.melle.flightbooking.controller;

import com.melle.flightbooking.service.FlightServiceImp;
import com.melle.flightbooking.model.Flight;
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
    public Flight createFlight(@RequestBody Flight flight) {
        return flightService.createFlight(flight);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public Flight updateFlightById(@RequestBody Flight flight) { return flightService.updateFlightById(flight); }

    // Should not return Boolean
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public boolean deleteFlight(@PathVariable int id){
        return flightService.deleteFlightById(id);
    }

    @GetMapping()
    public Iterable<Flight> getAllFlights(){
        return flightService.getAllFlights();
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable int id){
        return flightService.getFlightById(id);
    }
}
