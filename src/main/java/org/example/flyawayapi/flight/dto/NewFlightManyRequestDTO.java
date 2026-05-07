package org.example.flyawayapi.flight.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewFlightManyRequestDTO {
    private List<NewFlightRequestDTO> inputs;
}