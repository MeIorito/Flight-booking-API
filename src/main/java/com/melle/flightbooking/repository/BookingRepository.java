package com.melle.flightbooking.repository;

import com.melle.flightbooking.model.Booking;
import com.melle.flightbooking.model.Flight;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    Booking findBookingById(Integer id);
    <T> Iterable<T> findBy(Class<T> type);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user", "flight"})
    Page<Booking> findAll(Specification<Booking> spec, @NonNull Pageable pageable);
}
