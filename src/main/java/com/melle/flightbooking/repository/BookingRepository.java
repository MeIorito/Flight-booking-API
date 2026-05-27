package com.melle.flightbooking.repository;

import com.melle.flightbooking.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {
    Booking findBookingById(Integer id);
}
