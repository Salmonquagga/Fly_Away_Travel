package org.example.flyawayapi.flight.dto;

import java.util.List;

public class FlightSearchResponseDTO {
    public List<FlightResponseDTO> items;

    public FlightSearchResponseDTO(List<FlightResponseDTO> flights) {
        this.items = flights;
    }
}