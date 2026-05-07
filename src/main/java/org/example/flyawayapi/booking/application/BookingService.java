package org.example.flyawayapi.booking.application;

import org.example.flyawayapi.booking.domain.Booking;
import org.example.flyawayapi.booking.dto.BookingResponseDTO;
import org.example.flyawayapi.booking.dto.FlightBookRequestDTO;
import org.example.flyawayapi.booking.infrastructure.BookingRepository;
import org.example.flyawayapi.flight.domain.Flight;
import org.example.flyawayapi.flight.infrastructure.FlightRepository;
import org.example.flyawayapi.user.domain.User;
import org.example.flyawayapi.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            FlightRepository flightRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    public Booking book(FlightBookRequestDTO dto) {

        if (dto.getFlightId() == null || dto.getFlightId().isBlank()) {
            throw new RuntimeException("Flight id is mandatory");
        }

        Long flightId = Long.parseLong(dto.getFlightId());

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("Flight is full");
        }

        // Temporal: usamos el primer usuario registrado.
        // Luego lo cambiaremos para leerlo desde el token.
        User customer = userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setCustomer(customer);
        booking.setBookingDate(Instant.now());

        return bookingRepository.save(booking);
    }

    public BookingResponseDTO getById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return new BookingResponseDTO(booking);
    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }
}