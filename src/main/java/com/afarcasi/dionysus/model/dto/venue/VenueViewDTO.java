package com.afarcasi.dionysus.model.dto.venue;

import lombok.Data;

/**
 * DTO for viewing venue information.
 */
@Data
public class VenueViewDTO {
    private Long id;
    private String name;
    private int capacity;
}
