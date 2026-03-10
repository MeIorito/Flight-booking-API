package com.melle.flightbooking.controller;

import com.melle.flightbooking.interfaces.FlightService;
import com.melle.flightbooking.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/flights")
    public Flight createFlight(@RequestBody Flight flight) {
        return flightService.createFlight(flight);
    }

    @DeleteMapping("/flights/{id}")
    public boolean deleteFlight(@PathVariable int id){
        return flightService.deleteFlightById(id);
    }

    @GetMapping("/flights")
    public Iterable<Flight> getAllFlights(){
        return flightService.getAllFlights();
    }

    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable int id){
        return flightService.getFlightById(id);
    }


}
