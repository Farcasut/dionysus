package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.event.EventResponseDTO;
import com.afarcasi.dionysus.model.dto.event.EventUpdateDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueViewDTO;
import com.afarcasi.dionysus.model.entity.event.Event;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the mapping of an event to DTOs and vice versa.
 */
@Component
public class EventMapper {

    /**
     * Maps an Event entity to EventResponseDTO.
     */
    public EventResponseDTO toResponseDTO(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setEventDate(event.getEventDate());

        if (event.getOrganizer() != null) {
            UserResponseDTO organizerDTO = new UserResponseDTO();
            organizerDTO.setId(event.getOrganizer().getId());
            organizerDTO.setEmail(event.getOrganizer().getEmail());
            organizerDTO.setUsername(event.getOrganizer().getUsername());
            organizerDTO.setRole(event.getOrganizer().getRole());
            organizerDTO.setFirstName(event.getOrganizer().getFirstName());
            organizerDTO.setLastName(event.getOrganizer().getLastName());
            dto.setOrganizer(organizerDTO);
        }

        if (event.getVenue() != null) {
            VenueViewDTO venueDTO = new VenueViewDTO();
            venueDTO.setId(event.getVenue().getId());
            venueDTO.setName(event.getVenue().getName());
            venueDTO.setCapacity(event.getVenue().getCapacity());
            dto.setVenue(venueDTO);
        }

        if (event.getCategories() != null) {
            List<TicketCategoryViewDTO> categoryDTOs = event.getCategories().stream()
                    .map(this::toTicketCategoryViewDTO)
                    .collect(Collectors.toList());
            dto.setTicketCategories(categoryDTOs);
        }

        return dto;
    }

    /**
     * Updates an Event entity from EventUpdateDTO.
     * Only non-null fields will be applied.
     */
    public void updateEntity(EventUpdateDTO dto, Event event) {
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
    }

    private TicketCategoryViewDTO toTicketCategoryViewDTO(TicketCategory category) {
        TicketCategoryViewDTO dto = new TicketCategoryViewDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setPrice(category.getPrice());
        dto.setAvailableSpots(category.getAvailableSpots());
        return dto;
    }
}
