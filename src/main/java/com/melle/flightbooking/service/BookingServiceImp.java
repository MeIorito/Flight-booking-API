package com.melle.flightbooking.service;

import com.melle.flightbooking.dto.booking.*;
import com.melle.flightbooking.dto.flight.FlightSummaryDto;
import com.melle.flightbooking.dto.flight.RegisterFlightDto;
import com.melle.flightbooking.exception.BookingOwnershipException;
import com.melle.flightbooking.exception.FlightNotFoundException;
import com.melle.flightbooking.exception.IdDoesNotExistException;
import com.melle.flightbooking.interfaces.BookingService;
import com.melle.flightbooking.interfaces.FlightService;
import com.melle.flightbooking.model.Booking;
import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.BookingRepository;
import com.melle.flightbooking.repository.FlightRepository;
import com.melle.flightbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    @Autowired
    public BookingServiceImp(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            FlightRepository flightRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public BookingSummaryDto createBooking(Integer userId, RegisterBookingDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdDoesNotExistException("User with id: " + userId + " does not exist"));

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new FlightNotFoundException("Flight with id: " + request.getFlightId() + " does not exist"));

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        return createBookingSummaryDto(savedBooking);
    }

    @Override
    public BookingSummaryDto updateBookingUser(UpdateBookingUserDto request) {
        Integer bookingId = request.getBookingId();
        Integer userId = request.getUserId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new FlightNotFoundException("Booking with id: " + bookingId + " does not exist"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdDoesNotExistException("User with id: " + userId + " does not exist"));

        booking.setUser(user);
        booking.setBookedAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);

        return createBookingSummaryDto(savedBooking);
    }

    @Override
    public BookingSummaryDto updateBookingFlight(UpdateBookingFlightDto request) {
        Integer bookingId = request.getBookingId();
        Integer flightId = request.getFlightId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new FlightNotFoundException("Booking with id: " + bookingId + " does not exist"));

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight with id: " + flightId + " does not exist"));

        booking.setFlight(flight);
        booking.setBookedAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);

        return createBookingSummaryDto(savedBooking);
    }

    @Override
    public void deleteBooking(Integer userId, DeleteBookingDto request) {
        Integer bookingId = request.getBookingId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new FlightNotFoundException("Booking with id: " + bookingId + " does not exist"));

        if(!Objects.equals(userId, booking.getUser().getId())) {
            throw new BookingOwnershipException("You are not allowed to delete this booking");
        }

        bookingRepository.delete(booking);
    }

    @Override
    public void deleteBookingAdmin(Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new FlightNotFoundException("Booking with id: " + bookingId + " does not exist"));

        bookingRepository.delete(booking);
    }

    @Override
    public BookingSummaryDto getBookingById(Integer userId, Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new FlightNotFoundException("Booking with id: " + bookingId + " does not exist"));

        if(!Objects.equals(userId, booking.getUser().getId())) {
            throw new BookingOwnershipException("You are not allowed to delete this booking");
        }

        return createBookingSummaryDto(booking);
    }

    @Override
    public Iterable<BookingSummaryDto> getAllBookings() {
        return StreamSupport.stream(
                        bookingRepository.findAll().spliterator(),
                        false
                )
                .map(this::createBookingSummaryDto)
                .toList();
    }

    /*
    HELPER FUNCTIONS
     */

    private BookingSummaryDto createBookingSummaryDto (Booking booking) {
        return new BookingSummaryDto(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getUsername(),
                booking.getFlight().getId(),
                booking.getFlight().getOrigin(),
                booking.getFlight().getDestination(),
                booking.getBookedAt()
        );
    }
}