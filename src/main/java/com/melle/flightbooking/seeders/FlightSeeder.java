package com.melle.flightbooking.seeders;

import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FlightSeeder implements CommandLineRunner {

    private final FlightRepository flightRepository;

    public FlightSeeder(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) {

        if (flightRepository.count() > 0) {
            return;
        }

        Flight f1 = new Flight();
        f1.setOrigin("Amsterdam");
        f1.setDestination("Barcelona");
        f1.setDate("2026-06-01");
        f1.setSeats(180);

        Flight f2 = new Flight();
        f2.setOrigin("Amsterdam");
        f2.setDestination("Tokyo");
        f2.setDate("2026-07-15");
        f2.setSeats(250);

        Flight f3 = new Flight();
        f3.setOrigin("Rotterdam");
        f3.setDestination("London");
        f3.setDate("2026-05-30");
        f3.setSeats(120);

        flightRepository.save(f1);
        flightRepository.save(f2);
        flightRepository.save(f3);

        System.out.println("Flights seeded!");
    }
}