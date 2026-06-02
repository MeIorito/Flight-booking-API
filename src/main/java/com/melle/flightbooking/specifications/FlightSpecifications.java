package com.melle.flightbooking.specifications;

import com.melle.flightbooking.model.Flight;
import org.springframework.data.jpa.domain.Specification;

public class FlightSpecifications {

    public static Specification<Flight> hasOrigin(String origin) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("origin"), origin);
    }

    public static Specification<Flight> hasDestination(String destination) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("destination"), destination);
    }

    public static Specification<Flight> hasDate(String date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("date"), date);
    }

    public static Specification<Flight> hasSeats(Integer seats) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("seats"), seats);
    }
}
