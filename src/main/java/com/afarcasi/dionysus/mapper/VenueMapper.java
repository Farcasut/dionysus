package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.venue.VenueUpdateDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueViewDTO;
import com.afarcasi.dionysus.model.entity.venue.Venue;
import org.springframework.stereotype.Component;

/**
 * Handles the mapping of venues to DTOs.
 */
@Component
public class VenueMapper {

    /**
     * Maps a Venue entity to VenueViewDTO.
     */
    public VenueViewDTO toViewDTO(Venue venue) {
        VenueViewDTO dto = new VenueViewDTO();
        dto.setId(venue.getId());
        dto.setName(venue.getName());
        dto.setCapacity(venue.getCapacity());
        return dto;
    }

    /**
     * Updates a Venue entity from VenueUpdateDTO.
     * Only non-null fields will be applied.
     */
    public void updateEntity(VenueUpdateDTO dto, Venue venue) {
        if (dto.getName() != null) {
            venue.setName(dto.getName());
        }
        if (dto.getCapacity() != null) {
            venue.setCapacity(dto.getCapacity());
        }
    }
}
