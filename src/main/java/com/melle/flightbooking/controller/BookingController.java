package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.CustomUserPrinciple;
import com.melle.flightbooking.dto.booking.*;
import com.melle.flightbooking.service.BookingServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    // Booking service
    private final BookingServiceImp bookingService;


    @Autowired
    public BookingController(BookingServiceImp bookingService) {
        this.bookingService = bookingService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public BookingSummaryDto createBooking(@RequestBody RegisterBookingDto request) {
        CustomUserPrinciple user = getUserPrinciple();

        return bookingService.createBooking(user.getId(), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user")
    public BookingSummaryDto updateBookingUser(@RequestBody UpdateBookingUserDto request) {
        return bookingService.updateBookingUser(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/flight")
    public BookingSummaryDto updateBookingFlight(@RequestBody UpdateBookingFlightDto request){
        return bookingService.updateBookingFlight(request);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping()
    public boolean deleteBooking(@RequestBody DeleteBookingDto request) {
        CustomUserPrinciple user = getUserPrinciple();

        return bookingService.deleteBooking(user.getId(), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookingId}")
    public boolean deleteBookingAdmin(@PathVariable Integer bookingId) {
        return bookingService.deleteBookingAdmin(bookingId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{bookingId}")
    public BookingSummaryDto getBookingById(@PathVariable Integer bookingId) {
        CustomUserPrinciple user = getUserPrinciple();

        return bookingService.getBookingById(user.getId(), bookingId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public Iterable<BookingSummaryDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    /*
    HELPER FUNCTIONS
     */

    private CustomUserPrinciple getUserPrinciple() {
        return (CustomUserPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
