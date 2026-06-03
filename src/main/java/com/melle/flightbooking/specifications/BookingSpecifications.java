package com.melle.flightbooking.specifications;

import com.melle.flightbooking.model.Booking;
import org.springframework.data.jpa.domain.Specification;

public class BookingSpecifications {

    public static Specification<Booking> hasUserId(Integer id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), id);
    }

    public static Specification<Booking> hasFlightId(Integer id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("flight").get("id"), id);
    }
}