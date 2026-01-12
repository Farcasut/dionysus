package com.afarcasi.dionysus.model.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO handling the event creation.
 */
@Data
public class EventCreationDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Event date cannot be null")
    private LocalDateTime eventDate;

    @NotNull(message = "Organizer ID cannot be null")
    private Long organizerId;

    @NotNull(message = "Venue ID cannot be null")
    private Long venueId;
}
