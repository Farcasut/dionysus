package com.afarcasi.dionysus.model.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for updating event information.
 */
@Data
public class EventUpdateDTO {
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private Long venueId;
}
