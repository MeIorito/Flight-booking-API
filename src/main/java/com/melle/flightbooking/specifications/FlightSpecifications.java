package com.melle.flightbooking.specifications;

import com.melle.flightbooking.model.Flight;
import org.springframework.data.jpa.domain.Specification;

public class FlightSpecifications {

    public static Specification<Flight> hasOrigin(String origin) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("origin")),
                        "%" + origin.toLowerCase() + "%"
                );
    }

    public static Specification<Flight> hasDestination(String destination) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("destination")),
                        "%" + destination.toLowerCase() + "%"
                );
    }

    public static Specification<Flight> hasDate(String date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("date"), date);
    }

    public static Specification<Flight> hasSeats(Integer seats) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("seats"), seats);
    }
}
