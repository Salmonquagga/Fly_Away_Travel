package org.example.flyawayapi.flight.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class NewFlightRequestDTO {

    private String airlineName;
    private String flightNumber;
    private Instant estDepartureTime;
    private Instant estArrivalTime;
    private Integer availableSeats;
}