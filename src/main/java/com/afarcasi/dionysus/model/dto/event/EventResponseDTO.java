package com.afarcasi.dionysus.model.dto.event;

import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueViewDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO handling the event response.
 */
@Data
public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime eventDate;
    private UserResponseDTO organizer;
    private VenueViewDTO venue;
    private List<TicketCategoryViewDTO> ticketCategories;
}
