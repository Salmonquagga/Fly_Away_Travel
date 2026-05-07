package org.example.flyawayapi.cleanup;

import org.example.flyawayapi.booking.application.BookingService;
import org.example.flyawayapi.flight.application.FlightService;
import org.example.flyawayapi.user.application.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CleanupController {

    private final UserService userService;
    private final FlightService flightService;
    private final BookingService bookingService;

    public CleanupController(
            UserService userService,
            FlightService flightService,
            BookingService bookingService
    ) {
        this.userService = userService;
        this.flightService = flightService;
        this.bookingService = bookingService;
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> cleanup() {

        bookingService.deleteAll();
        flightService.deleteAll();
        userService.deleteAll();

        return ResponseEntity.ok().build();
    }
}