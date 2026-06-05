package com.melle.flightbooking.service;

import com.melle.flightbooking.dto.booking.*;
import com.melle.flightbooking.exception.BookingOwnershipException;
import com.melle.flightbooking.exception.FlightNotFoundException;
import com.melle.flightbooking.exception.IdDoesNotExistException;
import com.melle.flightbooking.interfaces.BookingService;
import com.melle.flightbooking.model.Booking;
import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.BookingRepository;
import com.melle.flightbooking.repository.FlightRepository;
import com.melle.flightbooking.repository.UserRepository;
import com.melle.flightbooking.specifications.BookingSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Slf4j
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
        log.info("Creating booking for user with id: {} for flight with id: {}", userId, request.getFlightId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id: {} not found", userId);
                    return new IdDoesNotExistException("User with id: " + userId + " does not exist")
                });

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> {
                    log.warn("Flight with id: {} not found", request.getFlightId());
                    return new FlightNotFoundException("Flight with id: " + request.getFlightId() + " does not exist")
                });

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created successfully with id: {}", savedBooking.getId());

        return createBookingSummaryDto(savedBooking);
    }

    @Override
    public BookingSummaryDto updateBookingUser(UpdateBookingUserDto request) {
        log.info("Updating user of booking with id: {} to user with id: {}", request.getBookingId(), request.getUserId());

        Integer bookingId = request.getBookingId();
        Integer userId = request.getUserId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id: {} not found", bookingId);
                    return new FlightNotFoundException("Booking with id: " + bookingId + " does not exist")
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id: {} not found", userId);
                    return new IdDoesNotExistException("User with id: " + userId + " does not exist")
                });

        booking.setUser(user);
        booking.setBookedAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        log.info("User of booking with id: {} successfully updated to user with id: {}", savedBooking.getId(), savedBooking.getUser().getId());

        return createBookingSummaryDto(savedBooking);
    }

    // Nested db searches, need to look into this
    @Override
    public Iterable<BookingSummaryDto> getBookingsByUserId(Integer id) {
        log.info("Fetching bookings for user with id: {}", id);

        List<BookingSummaryDto> response = bookingRepository.findAll(BookingSpecifications.hasUserId(id))
                .stream()
                .map(this::createBookingSummaryDto)
                .toList();

        log.info("Found {} bookings for user with id: {}", response.size(), id);
        return response;
    }

    @Override
    public BookingSummaryDto updateBookingFlight(UpdateBookingFlightDto request) {
        Integer bookingId = request.getBookingId();
        Integer flightId = request.getFlightId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id: {} not found", bookingId);
                    return new FlightNotFoundException("Booking with id: " + bookingId + " does not exist")
                });

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    log.warn("Flight with id: {} not found", flightId);
                    return new FlightNotFoundException("Flight with id: " + flightId + " does not exist")
                });

        booking.setFlight(flight);
        booking.setBookedAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Flight of booking with id: {} successfully updated to flight with id: {}", savedBooking.getId(), savedBooking.getFlight().getId());

        return createBookingSummaryDto(savedBooking);
    }

    @Override
    public void deleteBooking(Integer userId, DeleteBookingDto request) {
        log.info("Deleting booking with id: {} by user with id: {}", request.getBookingId(), userId);

        Integer bookingId = request.getBookingId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id: {} not found", bookingId);
                    return new FlightNotFoundException("Booking with id: " + bookingId + " does not exist")
                });

        if(!Objects.equals(userId, booking.getUser().getId())) {
            log.warn("User with id: {} is not allowed to delete booking with id: {}", userId, bookingId);
            throw new BookingOwnershipException("You are not allowed to delete this booking");
        }

        bookingRepository.delete(booking);
        log.info("Booking with id: {} successfully deleted by user with id: {}", bookingId, userId);
    }

    @Override
    public void deleteBookingAdmin(Integer bookingId) {
        log.info("Admin deleting booking with id: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id: {} not found", bookingId);
                    return new FlightNotFoundException("Booking with id: " + bookingId + " does not exist")
                });

        bookingRepository.delete(booking);
        log.info("Booking with id: {} successfully deleted by admin", bookingId);
    }

    @Override
    public BookingSummaryDto getBookingById(Integer userId, Integer bookingId) {
        log.info("Fetching booking with id: {} for user with id: {}", bookingId, userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id: {} not found", bookingId);
                    return new FlightNotFoundException("Booking with id: " + bookingId + " does not exist")
                });

        if(!Objects.equals(userId, booking.getUser().getId())) {
            log.warn("User with id: {} is not allowed to view booking with id: {}", userId, bookingId);
            throw new BookingOwnershipException("You are not allowed to view this booking");
        }

        return createBookingSummaryDto(booking);
    }

    @Override
    public Iterable<BookingSummaryDto> getAllBookings() {
        log.info("Fetching all bookings");
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