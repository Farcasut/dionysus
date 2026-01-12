package com.afarcasi.dionysus.model.dto.ticketCategory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating a ticket category.
 */
@Data
public class TicketCategoryCreationDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be positive")
    private Double price;
    
    @NotNull(message = "Available spots cannot be null")
    @Min(value = 1, message = "Available spots must be at least 1")
    private Integer availableSpots;
    
    @NotNull(message = "Event ID cannot be null")
    private Long eventId;
}
