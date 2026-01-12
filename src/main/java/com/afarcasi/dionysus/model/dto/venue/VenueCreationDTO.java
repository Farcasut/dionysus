package com.afarcasi.dionysus.model.dto.venue;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating a venue.
 */
@Data
public class VenueCreationDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotNull(message = "Capacity cannot be null")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
    
    @NotNull(message = "Owner ID cannot be null")
    private Long ownerId;
}
