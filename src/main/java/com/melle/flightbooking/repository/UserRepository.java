package com.melle.flightbooking.repository;

import com.melle.flightbooking.dto.UserSummaryDto;
import com.melle.flightbooking.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(String email);
    User findUserByEmail(String email);
    User findUserById(Integer id);
    <T> Iterable<T> findBy(Class<T> type);
}
