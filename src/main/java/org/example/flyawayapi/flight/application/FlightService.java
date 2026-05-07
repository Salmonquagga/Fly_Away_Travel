package org.example.flyawayapi.flight.application;

import org.example.flyawayapi.flight.domain.Flight;
import org.example.flyawayapi.flight.dto.FlightResponseDTO;
import org.example.flyawayapi.flight.dto.FlightSearchResponseDTO;
import org.example.flyawayapi.flight.dto.NewFlightManyRequestDTO;
import org.example.flyawayapi.flight.dto.NewFlightRequestDTO;
import org.example.flyawayapi.flight.infrastructure.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight create(NewFlightRequestDTO dto) {

        if (dto.getAirlineName() == null || dto.getAirlineName().isBlank()) {
            throw new RuntimeException("Airline name is mandatory");
        }

        if (dto.getFlightNumber() == null || dto.getFlightNumber().isBlank()) {
            throw new RuntimeException("Flight number is mandatory");
        }

        if (!dto.getFlightNumber().matches("^[A-Z]{2,3}[0-9]{3}$")) {
            throw new RuntimeException("Invalid flight number");
        }

        if (dto.getEstDepartureTime() == null) {
            throw new RuntimeException("Departure time is mandatory");
        }

        if (dto.getEstArrivalTime() == null) {
            throw new RuntimeException("Arrival time is mandatory");
        }

        if (!dto.getEstDepartureTime().isBefore(dto.getEstArrivalTime())) {
            throw new RuntimeException("Departure must be before arrival");
        }

        if (dto.getAvailableSeats() == null || dto.getAvailableSeats() <= 0) {
            throw new RuntimeException("Available seats must be greater than 0");
        }

        if (flightRepository.existsByFlightNumber(dto.getFlightNumber())) {
            throw new RuntimeException("Flight number already exists");
        }

        Flight flight = new Flight();
        flight.setAirlineName(dto.getAirlineName());
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setEstDepartureTime(dto.getEstDepartureTime());
        flight.setEstArrivalTime(dto.getEstArrivalTime());
        flight.setAvailableSeats(dto.getAvailableSeats());

        return flightRepository.save(flight);
    }

    public void createMany(NewFlightManyRequestDTO dto) {
        if (dto.getInputs() == null || dto.getInputs().isEmpty()) {
            throw new RuntimeException("Inputs are mandatory");
        }

        for (NewFlightRequestDTO input : dto.getInputs()) {
            create(input);
        }
    }

    public FlightSearchResponseDTO search(
            String flightNumber,
            String airlineName,
            String estDepartureTimeFrom,
            String estDepartureTimeTo
    ) {
        List<Flight> flights = flightRepository.findAll();

        if (flightNumber != null && !flightNumber.isBlank()) {
            flights = flights.stream()
                    .filter(f -> f.getFlightNumber().toLowerCase().contains(flightNumber.toLowerCase()))
                    .toList();
        }

        if (airlineName != null && !airlineName.isBlank()) {
            flights = flights.stream()
                    .filter(f -> f.getAirlineName().toLowerCase().contains(airlineName.toLowerCase()))
                    .toList();
        }

        if (estDepartureTimeFrom != null && !estDepartureTimeFrom.isBlank()) {
            Instant from = Instant.parse(estDepartureTimeFrom);
            flights = flights.stream()
                    .filter(f -> !f.getEstDepartureTime().isBefore(from))
                    .toList();
        }

        if (estDepartureTimeTo != null && !estDepartureTimeTo.isBlank()) {
            Instant to = Instant.parse(estDepartureTimeTo);
            flights = flights.stream()
                    .filter(f -> !f.getEstDepartureTime().isAfter(to))
                    .toList();
        }

        List<FlightResponseDTO> items = flights.stream()
                .map(FlightResponseDTO::new)
                .toList();

        return new FlightSearchResponseDTO(items);
    }

    public void deleteAll() {
        flightRepository.deleteAll();
    }
}