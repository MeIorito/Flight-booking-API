package com.melle.flightbooking.repository;

import com.melle.flightbooking.model.Booking;
import com.melle.flightbooking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    Booking findBookingById(Integer id);
    <T> Iterable<T> findBy(Class<T> type);

}
