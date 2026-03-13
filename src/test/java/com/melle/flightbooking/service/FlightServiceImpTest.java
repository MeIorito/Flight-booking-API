package com.melle.flightbooking.service;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightServiceImpTest {

    @Mock
    FlightRepository flightRepository;

    @InjectMocks
    FlightServiceImp flightServiceImp;

    @Test
    public void createFlight_returnsCreatedFlight_whenFlightIsCreated(){
        Flight flight = new Flight();
        flight.setId(1);

        when(flightRepository.save(flight)).thenReturn(flight);


        Flight result = flightServiceImp.createFlight(flight);


        assertEquals(flight.getId(), result.getId());
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
    }

    @Test
    public void deleteFlightById_returnsTrue_whenFlightIsDeleted(){
        int id = 1;

        when(flightRepository.existsById(id)).thenReturn(true);

        boolean result = flightServiceImp.deleteFlightById(id);

        assertTrue(result);
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
    public void findFlightById_returnsFlight_whenFlightFound(){
        int id = 1;
        Flight flight = new Flight();
        flight.setId(id);

        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));

        Flight result = flightServiceImp.getFlightById(id);

        assertEquals(id, result.getId());
        verify(flightRepository).findById(id);
    }

    @Test
    public void getAllFlights_returnsFlightIterable_whenIterableIsFound(){
        Flight flight1 = new Flight();
        flight1.setId(1);

        Flight flight2 = new Flight();
        flight2.setId(2);

        Iterable<Flight> allFlights = List.of(flight1, flight2);

        when(flightRepository.findAll()).thenReturn(allFlights);

        Iterable<Flight> result = flightServiceImp.getAllFlights();

        assertEquals(2, ((List<Flight>) result).size());
        verify(flightRepository).findAll();
    }
}
