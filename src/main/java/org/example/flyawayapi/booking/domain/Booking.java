package org.example.flyawayapi.booking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.flyawayapi.flight.domain.Flight;
import org.example.flyawayapi.user.domain.User;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant bookingDate;

    @ManyToOne
    private Flight flight;

    @ManyToOne
    private User customer;
}