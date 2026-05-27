package com.melle.flightbooking.interfaces;

import com.melle.flightbooking.dto.booking.BookingSummaryDto;
import com.melle.flightbooking.dto.booking.RegisterBookingDto;
import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;

public interface BookingService {
    BookingSummaryDto createBooking(Integer id, RegisterBookingDto booking);
    BookingSummaryDto updateBookingUser(Integer bookingId, Integer userId);
//    boolean deleteBookingById(Integer id);
//    BookingSummaryDto getBookingById(Integer id);
//    Iterable<BookingSummaryDto> getAllBookings();
}
