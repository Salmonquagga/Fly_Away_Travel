package org.example.flyawayapi.flight.infrastructure;

import org.example.flyawayapi.flight.application.FlightService;
import org.example.flyawayapi.flight.domain.Flight;
import org.example.flyawayapi.flight.dto.FlightSearchResponseDTO;
import org.example.flyawayapi.flight.dto.NewFlightManyRequestDTO;
import org.example.flyawayapi.flight.dto.NewFlightRequestDTO;
import org.example.flyawayapi.flight.dto.NewIdDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/create")
    public ResponseEntity<NewIdDTO> create(@RequestBody NewFlightRequestDTO newFlight) {
        Flight flight = flightService.create(newFlight);
        return ResponseEntity.status(201).body(new NewIdDTO(flight.getId().toString()));
    }

    @PostMapping("/create-many")
    public ResponseEntity<NewIdDTO> createMany(@RequestBody NewFlightManyRequestDTO requestDTO) {
        flightService.createMany(requestDTO);
        return ResponseEntity.status(201).body(new NewIdDTO("ok"));
    }

    @GetMapping("/search")
    public ResponseEntity<FlightSearchResponseDTO> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo
    ) {
        return ResponseEntity.ok(
                flightService.search(flightNumber, airlineName, estDepartureTimeFrom, estDepartureTimeTo)
        );
    }
}