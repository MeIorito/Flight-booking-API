package com.melle.flightbooking.service;

import com.melle.flightbooking.exception.FlightNotFoundException;
import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.interfaces.FlightService;
import com.melle.flightbooking.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightServiceImp implements FlightService {
    private final FlightRepository flightRepository;

    @Autowired
    public FlightServiceImp(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    @Override
    public Flight createFlight(Flight newFlight) {
        return flightRepository.save(newFlight);
    }

    @Override
    public boolean deleteFlightById(Integer id) {
        boolean idIsPresent = flightRepository.existsById(id);

        if (!idIsPresent){
            throw new FlightNotFoundException("Flight with id: " + id + " does not exist");
        }
        flightRepository.deleteById(id);
        return idIsPresent;
    }

    @Override
    public Flight getFlightById(Integer id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight with id: " + id + " does not exist"));
    }

    @Override
    public Iterable<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}