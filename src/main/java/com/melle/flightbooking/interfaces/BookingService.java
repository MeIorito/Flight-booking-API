package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.dto.booking.*;
import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;

public interface BookingService {
    BookingSummaryDto createBooking(Integer id, RegisterBookingDto request);
    BookingSummaryDto updateBookingUser(UpdateBookingUserDto request);
    BookingSummaryDto updateBookingFlight(UpdateBookingFlightDto request);
    void deleteBooking(Integer userId, DeleteBookingDto request);
    void deleteBookingAdmin(Integer bookingId);
    BookingSummaryDto getBookingById(Integer userId, Integer bookingId);
    Iterable<BookingSummaryDto> getAllBookings();
}
