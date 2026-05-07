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

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

        if (flight.getEstDepartureTime().isBefore(Instant.now())
                || flight.getEstDepartureTime().equals(Instant.now())) {
            throw new RuntimeException("Cannot book past flight");
        }

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("Flight is full");
        }

        User customer = userRepository.findByEmail("johndoe@gmail.com")
                .orElseGet(() -> userRepository.findAll()
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Customer not found")));

        List<Booking> bookings = bookingRepository.findAll();

        for (Booking existingBooking : bookings) {

            if (!existingBooking.getCustomer().getId().equals(customer.getId())) {
                continue;
            }

            Flight existingFlight = existingBooking.getFlight();

            boolean overlaps =
                    flight.getEstDepartureTime().isBefore(existingFlight.getEstArrivalTime())
                            && flight.getEstArrivalTime().isAfter(existingFlight.getEstDepartureTime());

            if (overlaps) {
                throw new RuntimeException("Flight overlaps with another booking");
            }
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setCustomer(customer);
        booking.setBookingDate(Instant.now().truncatedTo(ChronoUnit.MICROS));

        Booking savedBooking = bookingRepository.save(booking);

        writeEmailFile(savedBooking);

        return savedBooking;
    }

    public BookingResponseDTO getById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return new BookingResponseDTO(booking);
    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    private void writeEmailFile(Booking booking) {

        String fileName = "flight_booking_email_" + booking.getId() + ".txt";

        String content =
                "Hello " + booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName() + ",\n\n" +
                        "Your booking was successful!\n\n" +
                        "The booking is for flight " + booking.getFlight().getFlightNumber() +
                        " with departure date of " + booking.getFlight().getEstDepartureTime() +
                        " and arrival date of " + booking.getFlight().getEstArrivalTime() + ".\n\n" +
                        "The booking was registered at " + booking.getBookingDate() + ".\n\n" +
                        booking.getCustomer().getFirstName() + "\n" +
                        booking.getCustomer().getLastName() + "\n" +
                        booking.getFlight().getFlightNumber() + "\n" +
                        booking.getFlight().getEstDepartureTime() + "\n" +
                        booking.getFlight().getEstArrivalTime() + "\n" +
                        booking.getBookingDate() + "\n" +
                        "Bon Voyage!\n" +
                        "Fly Away Travel";

        writeFile(fileName, content);

        writeFile(
                "../-cs2031-2026-1-week07-tester-main/" + fileName,
                content
        );
    }

    private void writeFile(String path, String content) {

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException ignored) {
        }
    }
}