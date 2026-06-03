package com.melle.flightbooking.repository;

import com.melle.flightbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    User findUserByEmail(String email);
    User findUserById(Integer id);
    <T> Iterable<T> findBy(Class<T> type);
}
