package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.dto.common.CustomPage;
import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightService {
    FlightSummaryDto createFlight(RegisterFlightDto flight);
    FlightSummaryDto updateOriginById(Integer id, String origin);
    FlightSummaryDto updateDestinationById(Integer id, String destination);
    FlightSummaryDto updateDateById(Integer id, String date);
    FlightSummaryDto updateSeatsById(Integer id, Integer seats);
    void deleteFlightById(Integer id);
    FlightSummaryDto getFlightById(Integer id);
    CustomPage<FlightSummaryDto> getAllFlights(Pageable pageable);
    CustomPage<FlightSummaryDto> getFlightsByFilter(String origin, String destination, String date, Integer seats, Pageable pageable);
    Integer getAvailableSeatsByFlightId(Integer id);
}
