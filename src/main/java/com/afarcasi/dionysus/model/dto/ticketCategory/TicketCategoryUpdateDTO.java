package com.afarcasi.dionysus.model.dto.ticketCategory;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * DTO for updating ticket category information.
 */
@Data
public class TicketCategoryUpdateDTO {
    private String name;
    
    @Min(value = 0, message = "Price must be positive")
    private Double price;
    
    @Min(value = 0, message = "Available spots must be non-negative")
    private Integer availableSpots;
}
