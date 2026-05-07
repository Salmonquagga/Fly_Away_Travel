package org.example.flyawayapi.booking.dto;

import org.example.flyawayapi.booking.domain.Booking;

import java.time.Instant;

public class BookingResponseDTO {

    public String id;
    public Instant bookingDate;

    public String flightId;
    public String flightNumber;

    public Instant estDepartureTime;
    public Instant estArrivalTime;

    public String customerId;
    public String customerFirstName;
    public String customerLastName;

    public BookingResponseDTO(Booking booking) {

        this.id = booking.getId().toString();

        this.bookingDate = booking.getBookingDate();

        this.flightId = booking.getFlight().getId().toString();
        this.flightNumber = booking.getFlight().getFlightNumber();

        this.estDepartureTime = booking.getFlight().getEstDepartureTime();
        this.estArrivalTime = booking.getFlight().getEstArrivalTime();

        this.customerId = booking.getCustomer().getId().toString();
        this.customerFirstName = booking.getCustomer().getFirstName();
        this.customerLastName = booking.getCustomer().getLastName();
    }
}