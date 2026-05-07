package org.example.flyawayapi.booking.infrastructure;

import org.example.flyawayapi.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}