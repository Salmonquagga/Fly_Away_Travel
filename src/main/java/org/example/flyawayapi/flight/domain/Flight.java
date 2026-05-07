package org.example.flyawayapi.flight.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String airlineName;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    @Column(nullable = false)
    private Instant estDepartureTime;

    @Column(nullable = false)
    private Instant estArrivalTime;

    @Column(nullable = false)
    private Integer availableSeats;
}