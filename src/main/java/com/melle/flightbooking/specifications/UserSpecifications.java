package com.melle.flightbooking.specifications;

import com.melle.flightbooking.model.RoleEnum;
import com.melle.flightbooking.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"
                );
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"
                );
    }

    public static Specification<User> hasRole(RoleEnum role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role);
    }
}