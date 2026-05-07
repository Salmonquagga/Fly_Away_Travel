package org.example.flyawayapi.booking.infrastructure;

import org.example.flyawayapi.booking.application.BookingService;
import org.example.flyawayapi.booking.domain.Booking;
import org.example.flyawayapi.booking.dto.BookingResponseDTO;
import org.example.flyawayapi.booking.dto.FlightBookRequestDTO;
import org.example.flyawayapi.flight.dto.NewIdDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/flights/book")
    public ResponseEntity<NewIdDTO> book(@RequestBody FlightBookRequestDTO requestDTO) {
        Booking booking = bookingService.book(requestDTO);
        return ResponseEntity.ok(new NewIdDTO(booking.getId().toString()));
    }

    @GetMapping("/flights/book/{id}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }
}