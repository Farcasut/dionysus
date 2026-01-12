package com.afarcasi.dionysus.model.dto.venue;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * DTO for updating venue information.
 */
@Data
public class VenueUpdateDTO {
    private String name;
    
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
}
