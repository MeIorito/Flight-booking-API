package com.melle.flightbooking.repository;

import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Integer> {
    Flight findFlightById(Integer id);
}
