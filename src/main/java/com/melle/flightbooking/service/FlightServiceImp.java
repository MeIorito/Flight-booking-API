package com.melle.flightbooking.service;

import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;
import com.melle.flightbooking.dto.user.UserSummaryDto;
import com.melle.flightbooking.exception.FlightNotFoundException;
import com.melle.flightbooking.exception.IdDoesNotExistException;
import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.interfaces.FlightService;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
public class FlightServiceImp implements FlightService {
    private final FlightRepository flightRepository;

    @Autowired
    public FlightServiceImp(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    @Override
    public FlightSummaryDto createFlight(RegisterFlightDto flight) {
        Flight newFlight = new Flight();

        newFlight.setOrigin(flight.getOrigin());
        newFlight.setDestination(flight.getDestination());
        newFlight.setDate(flight.getDate());
        newFlight.setSeats(flight.getSeats());

        Flight savedFlight = flightRepository.save(newFlight);

        return createFlightSummaryDto(savedFlight);
    }

    public FlightSummaryDto updateOriginById(Integer id, String origin) {
        idIsPresent(id);

        Flight newFlight = flightRepository.findFlightById(id);
        newFlight.setOrigin(origin);

        Flight savedFlight = flightRepository.save(newFlight);

        return createFlightSummaryDto(savedFlight);
    }

    public FlightSummaryDto updateDestinationById(Integer id, String destination) {
        idIsPresent(id);

        Flight newFlight = flightRepository.findFlightById(id);
        newFlight.setDestination(destination);

        Flight savedFlight = flightRepository.save(newFlight);

        return createFlightSummaryDto(savedFlight);
    }

    public FlightSummaryDto updateDateById(Integer id, String date) {
        idIsPresent(id);

        Flight newFlight = flightRepository.findFlightById(id);
        newFlight.setDate(date);

        Flight savedFlight = flightRepository.save(newFlight);

        return createFlightSummaryDto(savedFlight);
    }

    public FlightSummaryDto updateSeatsById(Integer id, Integer seats) {
        idIsPresent(id);

        Flight newFlight = flightRepository.findFlightById(id);
        newFlight.setSeats(seats);

        Flight savedFlight = flightRepository.save(newFlight);

        return createFlightSummaryDto(savedFlight);
    }

    @Override
    public void deleteFlightById(Integer id) {
        idIsPresent(id);
        flightRepository.deleteById(id);
    }

    @Override
    public FlightSummaryDto getFlightById(Integer id) {
        Flight newFlight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight with id: " + id + " does not exist"));

        return createFlightSummaryDto(newFlight);
    }

    @Override
    public Iterable<FlightSummaryDto> getAllFlights() {

        return StreamSupport.stream(
                        flightRepository.findAll().spliterator(),
                        false
                )
                .map(this::createFlightSummaryDto)
                .toList();
    }

    /*
    HELPER FUNCTIONS
     */

    private FlightSummaryDto createFlightSummaryDto(Flight flight) {
        return new FlightSummaryDto(flight.getId(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDate(),
                flight.getSeats());
    }

    private void idIsPresent(Integer id) {
        boolean idIsPresent = flightRepository.existsById(id);

        if (!idIsPresent){
            throw new FlightNotFoundException("Flight with id: " + id + " does not exist");
        }
    }
}