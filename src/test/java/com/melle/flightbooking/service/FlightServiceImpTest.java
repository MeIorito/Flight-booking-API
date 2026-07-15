package com.melle.flightbooking.service;

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
        verify(flightRepository).save(flight);
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
//
//    @Test
//    public void findFlightById_returnsFlight_whenFlightFound(){
//        int id = 1;
//        Flight flight = new Flight();
//        flight.setId(id);
//
//        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));
//
//        Flight result = flightServiceImp.getFlightById(id);
//
//        assertEquals(id, result.getId());
//        verify(flightRepository).findById(id);
//    }
//
//    @Test
//    public void getAllFlights_returnsFlightIterable_whenIterableIsFound(){
//        Flight flight1 = new Flight();
//        flight1.setId(1);
//
//        Flight flight2 = new Flight();
//        flight2.setId(2);
//
//        Iterable<Flight> allFlights = List.of(flight1, flight2);
//
//        when(flightRepository.findAll()).thenReturn(allFlights);
//
//        Iterable<Flight> result = flightServiceImp.getAllFlights();
//
//        assertEquals(2, ((List<Flight>) result).size());
//        verify(flightRepository).findAll();
//    }
//
//    public RegisterFlightDto createRegisterFlightDto() {
//        return new RegisterFlightDto("Amsterdam", "New York", "06-05-2004", 150);
//    }
}
