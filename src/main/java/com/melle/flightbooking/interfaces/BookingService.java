package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.dto.booking.BookingSummaryDto;
import com.melle.flightbooking.dto.booking.RegisterBookingDto;
import com.melle.flightbooking.dto.booking.UpdateBookingFlightDto;
import com.melle.flightbooking.dto.booking.UpdateBookingUserDto;
import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;

public interface BookingService {
    BookingSummaryDto createBooking(Integer id, RegisterBookingDto request);
    BookingSummaryDto updateBookingUser(UpdateBookingUserDto request);
    BookingSummaryDto updateBookingFlight(UpdateBookingFlightDto request);
//    boolean deleteBookingById(Integer id);
//    BookingSummaryDto getBookingById(Integer id);
//    Iterable<BookingSummaryDto> getAllBookings();
}
