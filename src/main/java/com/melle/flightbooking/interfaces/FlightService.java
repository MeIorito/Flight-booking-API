package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.model.Flight;

public interface FlightService {
    Flight createFlight(Flight flight);
    boolean deleteFlightById(Integer id);
    Flight getFlightById(Integer id);
    Iterable<Flight> getAllFlights();
}
