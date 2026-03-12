package com.melle.flightbooking.service;

import com.melle.flightbooking.exception.FlightNotFoundException;
import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
