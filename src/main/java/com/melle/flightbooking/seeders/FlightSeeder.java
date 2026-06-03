package com.melle.flightbooking.seeders;

import com.melle.flightbooking.model.Flight;
import com.melle.flightbooking.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class FlightSeeder implements CommandLineRunner {

    private final FlightRepository flightRepository;
    private final Random random = new Random();

    public FlightSeeder(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) {

        if (flightRepository.count() > 0) {
            return;
        }

        List<String> cities = List.of(
                "Amsterdam", "Rotterdam", "Eindhoven", "Paris", "London",
                "Berlin", "Rome", "Madrid", "Lisbon", "Barcelona",
                "New York", "Tokyo", "Dubai", "Singapore", "Sydney",
                "Bangkok", "Istanbul", "Athens", "Copenhagen", "Stockholm"
        );

        for (int i = 0; i < 100; i++) {

            String origin = randomCity(cities);
            String destination;

            // ensure origin != destination
            do {
                destination = randomCity(cities);
            } while (destination.equals(origin));

            int seats = random.nextInt(50, 400);

            LocalDate date = LocalDate.now()
                    .plusDays(random.nextInt(1, 365));

            Flight flight = new Flight();
            flight.setOrigin(origin);
            flight.setDestination(destination);
            flight.setSeats(seats);
            flight.setDate(date.toString());

            flightRepository.save(flight);
        }

        System.out.println("100 Flights seeded!");
    }

    private String randomCity(List<String> cities) {
        return cities.get(random.nextInt(cities.size()));
    }
}