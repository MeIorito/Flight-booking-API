package com.melle.flightbooking.service;

import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;
import com.melle.flightbooking.exception.FlightNotFoundException;
import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.interfaces.FlightService;
import com.melle.flightbooking.repository.FlightRepository;
import com.melle.flightbooking.specifications.FlightSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class FlightServiceImp implements FlightService {
    private final FlightRepository flightRepository;

    @Autowired
    public FlightServiceImp(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    @Override
    public FlightSummaryDto createFlight(RegisterFlightDto flight) {
        log.info("Creating flight from {} to {} on {}", flight.getOrigin(), flight.getDestination(), flight.getDate());
        Flight newFlight = new Flight();

        newFlight.setOrigin(flight.getOrigin());
        newFlight.setDestination(flight.getDestination());
        newFlight.setDate(flight.getDate());
        newFlight.setSeats(flight.getSeats());

        Flight savedFlight = flightRepository.save(newFlight);
        log.info("Flight created successfully with id: {}", savedFlight.getId());

        return createFlightSummaryDto(savedFlight);
    }

    public FlightSummaryDto updateOriginById(Integer id, String origin) {
        log.info("Updating origin of flight with id: {} to: {}", id, origin);
        idIsPresent(id);

        Flight newFlight = flightRepository.findFlightById(id);
        newFlight.setOrigin(origin);

        Flight savedFlight = flightRepository.save(newFlight);
        log.info("Origin of flight with id: {} successfully updated to: {}", savedFlight.getId(), savedFlight.getOrigin());

        return createFlightSummaryDto(savedFlight);
    }

    public FlightSummaryDto updateDestinationById(Integer id, String destination) {
        log.info("Updating destination of flight with id: {} to: {}", id, destination);
        idIsPresent(id);

        Flight newFlight = flightRepository.findFlightById(id);
        newFlight.setDestination(destination);

        Flight savedFlight = flightRepository.save(newFlight);
        log.info("Destination of flight with id: {} successfully updated to: {}", savedFlight.getId(), savedFlight.getDestination());

        return createFlightSummaryDto(savedFlight);
    }

    public FlightSummaryDto updateDateById(Integer id, String date) {
        log.info("Updating date of flight with id: {} to: {}", id, date);
        idIsPresent(id);

        Flight newFlight = flightRepository.findFlightById(id);
        newFlight.setDate(date);

        Flight savedFlight = flightRepository.save(newFlight);
        log.info("Date of flight with id: {} successfully updated to: {}", savedFlight.getId(), savedFlight.getDate());

        return createFlightSummaryDto(savedFlight);
    }

    public FlightSummaryDto updateSeatsById(Integer id, Integer seats) {
        log.info("Updating seats of flight with id: {} to: {}", id, seats);
        idIsPresent(id);

        Flight newFlight = flightRepository.findFlightById(id);
        newFlight.setSeats(seats);

        Flight savedFlight = flightRepository.save(newFlight);
        log.info("Seats of flight with id: {} successfully updated to: {}", savedFlight.getId(), savedFlight.getSeats());

        return createFlightSummaryDto(savedFlight);
    }

    @Override
    public void deleteFlightById(Integer id) {
        log.info("Deleting flight with id: {}", id);

        idIsPresent(id);

        flightRepository.deleteById(id);
        log.info("Flight with id: {} successfully deleted", id);
    }

    @Override
    @Cacheable(value = "flightCache")
    public FlightSummaryDto getFlightById(Integer id) {
        log.info("Fetching flight with id: {}", id);

        Flight newFlight = flightRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Flight with id: {} not found", id);
                    return new FlightNotFoundException("Flight with id: " + id + " does not exist");
                });

        return createFlightSummaryDto(newFlight);
    }

    @Override
    @Cacheable(value = "flightCache")
    public Page<FlightSummaryDto> getAllFlights(Pageable pageable) {
        log.info("Fetching all flights");

        return flightRepository.findAll(pageable)
                .map(this::createFlightSummaryDto);
    }

    @Cacheable(value = "flightCache")
    public Page<FlightSummaryDto> getFlightsByFilter(String origin, String destination, String date, Integer seats, Pageable pageable) {
        log.info("Fetching flights with filters - origin: {}, destination: {}, date: {}, seats: {}", origin, destination, date, seats);

        Specification<Flight> spec = Specification.where(null);

        if (origin != null && !origin.isBlank()) {
            spec = spec.and(FlightSpecifications.hasOrigin(origin));
        }

        if (destination != null && !destination.isBlank()) {
            spec = spec.and(FlightSpecifications.hasDestination(destination));
        }

        if (date != null && !date.isBlank()) {
            spec = spec.and(FlightSpecifications.hasDate(date));
        }

        if (seats != null) {
            spec = spec.and(FlightSpecifications.hasSeats(seats));
        }

        Page<Flight> filteredFlights = flightRepository.findAll(spec, pageable);
        log.info("Found {} flights matching filters", filteredFlights.getSize());

        return filteredFlights
                        .map(this::createFlightSummaryDto);
    }

    @Cacheable(value = "flightSeatCache")
    public Integer getAvailableSeatsByFlightId(Integer id) {
        log.info("Getting available seats from flight with id: {}", id);
        idIsPresent(id);

        return flightRepository.findFlightById(id).getSeats();
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
            log.warn("Flight with id: {} does not exist", id);
            throw new FlightNotFoundException("Flight with id: " + id + " does not exist");
        }
    }
}