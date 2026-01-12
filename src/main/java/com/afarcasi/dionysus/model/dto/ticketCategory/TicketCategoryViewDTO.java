package com.afarcasi.dionysus.model.dto.ticketCategory;

import lombok.Data;

/**
 * DTO for viewing ticket category information.
 */
@Data
public class TicketCategoryViewDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer availableSpots;
}
