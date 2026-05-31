package com.melle.flightbooking.controller;

import com.melle.flightbooking.dto.CustomUserPrinciple;
import com.melle.flightbooking.dto.booking.*;
import com.melle.flightbooking.interfaces.BookingService;
import com.melle.flightbooking.service.BookingServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    // Booking service
    private final BookingService bookingService;


    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public BookingSummaryDto createBooking(@Valid @RequestBody RegisterBookingDto request) {
        CustomUserPrinciple user = getUserPrinciple();

        return bookingService.createBooking(user.getId(), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user")
    public BookingSummaryDto updateBookingUser(@Valid @RequestBody UpdateBookingUserDto request) {
        return bookingService.updateBookingUser(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/flight")
    public BookingSummaryDto updateBookingFlight(@Valid @RequestBody UpdateBookingFlightDto request){
        return bookingService.updateBookingFlight(request);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping()
    public boolean deleteBooking(@Valid @RequestBody DeleteBookingDto request) {
        CustomUserPrinciple user = getUserPrinciple();

        return bookingService.deleteBooking(user.getId(), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookingId}")
    public boolean deleteBookingAdmin(@Valid @PathVariable Integer bookingId) {
        return bookingService.deleteBookingAdmin(bookingId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{bookingId}")
    public BookingSummaryDto getBookingById(@Valid @PathVariable Integer bookingId) {
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
