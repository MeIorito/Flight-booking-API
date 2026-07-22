package com.melle.flightbooking.service;

import com.melle.flightbooking.dto.common.CustomPage;
import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;
import com.melle.flightbooking.exception.FlightNotFoundException;
import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceImpTest {

    @Mock
    FlightRepository flightRepository;

    @InjectMocks
    FlightServiceImp flightServiceImp;

    @Test
    public void createFlight_returnsCreatedFlight_whenFlightIsCreated(){
        RegisterFlightDto registerFlightDto = new RegisterFlightDto("Amsterdam", "New York", "06-05-2004", 150);
        Flight flight = new Flight(1, "Amsterdam", "New York", "06-05-2004", 150);

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);


        FlightSummaryDto result = flightServiceImp.createFlight(registerFlightDto);

        assertEquals(flight.getId(), result.id());
        assertEquals(flight.getOrigin(), result.origin());
        assertEquals(flight.getDestination(), result.destination());
        assertEquals(flight.getDate(), result.date());
        assertEquals(flight.getSeats(), result.seats());
        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    public void deleteFlightById_throwsFlightNotFoundException_whenFlightNotFound(){
        int id = 1;

        when(flightRepository.existsById(id)).thenReturn(false);

        assertThrows(
                FlightNotFoundException.class,
                () -> flightServiceImp.deleteFlightById(id)
        );
        verify(flightRepository, never()).deleteById(any());
    }

    @Test
    public void deleteFlightById_returnsNothing_whenFlightIsDeleted(){
        int id = 1;

        when(flightRepository.existsById(id)).thenReturn(true);

        flightServiceImp.deleteFlightById(id);

        verify(flightRepository).deleteById(id);
    }

    @Test
    public void findFlightById_throwsFlightNotFoundException_whenFlightNotFound(){
        int id = 1;

        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                FlightNotFoundException.class,
                () -> flightServiceImp.getFlightById(id)
        );
        verify(flightRepository).findById(id);
    }

    @Test
    public void findFlightById_returnsFlightSummeryDto_whenFlightFound(){
        int id = 1;
        Flight flight = new Flight(1, "Amsterdam", "New York", "06-05-2004", 150);


        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));

        FlightSummaryDto result = flightServiceImp.getFlightById(id);

        assertEquals(id, result.id());
        verify(flightRepository).findById(id);
    }

    @Test
    void getAllFlights_returnsPageOfFlights() {
        Flight flight1 = new Flight(1, "Amsterdam", "London", "2025-06-01", 120);
        Flight flight2 = new Flight(2, "Rotterdam", "Parijs", "2025-06-02", 80);
        Page<Flight> flightPage = new PageImpl<>(List.of(flight1, flight2), PageRequest.of(0, 10), 2);

        when(flightRepository.findAll(any(Pageable.class))).thenReturn(flightPage);

        CustomPage<FlightSummaryDto> result = flightServiceImp.getAllFlights(PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
        assertEquals("Amsterdam", result.getContent().get(0).origin());
        assertEquals("Rotterdam", result.getContent().get(1).origin());
    }

    @Test
    void getFlightsByFilter_withOrigin_returnsFilteredFlights() {
        Flight flight = new Flight(1, "Amsterdam", "London", "2025-06-01", 120);
        Page<Flight> flightPage = new PageImpl<>(List.of(flight), PageRequest.of(0, 10), 1);

        when(flightRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(flightPage);

        CustomPage<FlightSummaryDto> result = flightServiceImp.getFlightsByFilter("Amsterdam", null, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Amsterdam", result.getContent().get(0).origin());
    }

    @Test
    void getFlightsByFilter_withNoFilters_returnsAllFlights() {
        Flight flight1 = new Flight(1, "Amsterdam", "London", "2025-06-01", 120);
        Flight flight2 = new Flight(2, "Rotterdam", "Parijs", "2025-06-02", 80);
        Page<Flight> flightPage = new PageImpl<>(List.of(flight1, flight2), PageRequest.of(0, 10), 2);

        when(flightRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(flightPage);

        CustomPage<FlightSummaryDto> result = flightServiceImp.getFlightsByFilter(null, null, null, null, PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void getAvailableSeatsByFlightId_whenFlightExists_returnsSeats() {
        Flight flight = new Flight(1, "Amsterdam", "London", "2025-06-01", 120);

        when(flightRepository.existsById(1)).thenReturn(true);
        when(flightRepository.findFlightById(1)).thenReturn(flight);

        Integer result = flightServiceImp.getAvailableSeatsByFlightId(1);

        assertEquals(120, result);
    }

    @Test
    void getAvailableSeatsByFlightId_whenFlightNotFound_throwsFlightNotFoundException() {
        when(flightRepository.existsById(99)).thenReturn(false);

        assertThrows(FlightNotFoundException.class,
                () -> flightServiceImp.getAvailableSeatsByFlightId(99));

        verify(flightRepository, never()).findFlightById(any());
    }
}
