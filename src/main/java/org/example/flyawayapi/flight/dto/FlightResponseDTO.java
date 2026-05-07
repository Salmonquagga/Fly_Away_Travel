package org.example.flyawayapi.flight.dto;

import org.example.flyawayapi.flight.domain.Flight;

import java.time.Instant;

public class FlightResponseDTO {
    public String id;
    public String airlineName;
    public String flightNumber;
    public Instant estDepartureTime;
    public Instant estArrivalTime;
    public Integer availableSeats;

    public FlightResponseDTO(Flight flight) {
        this.id = flight.getId().toString();
        this.airlineName = flight.getAirlineName();
        this.flightNumber = flight.getFlightNumber();
        this.estDepartureTime = flight.getEstDepartureTime();
        this.estArrivalTime = flight.getEstArrivalTime();
        this.availableSeats = flight.getAvailableSeats();
    }
}