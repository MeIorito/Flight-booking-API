package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;
import com.melle.flightbooking.model.Flight;

public interface FlightService {
    FlightSummaryDto createFlight(RegisterFlightDto flight);
    FlightSummaryDto updateOriginById(Integer id, String origin);
    FlightSummaryDto updateDestinationById(Integer id, String destination);
    FlightSummaryDto updateDateById(Integer id, String date);
    FlightSummaryDto updateSeatsById(Integer id, Integer seats);
    void deleteFlightById(Integer id);
    FlightSummaryDto getFlightById(Integer id);
    Iterable<FlightSummaryDto> getAllFlights();
}
