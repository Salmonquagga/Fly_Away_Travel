package org.example.flyawayapi.flight.infrastructure;

import org.example.flyawayapi.flight.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumber(String flightNumber);

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByFlightNumberContainingIgnoreCaseOrAirlineNameContainingIgnoreCase(
            String flightNumber,
            String airlineName
    );
}