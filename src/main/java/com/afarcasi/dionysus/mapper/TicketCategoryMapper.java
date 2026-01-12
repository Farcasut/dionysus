package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryUpdateDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import org.springframework.stereotype.Component;

/**
 * Handles the mapping of ticket categories to DTOs.
 */
@Component
public class TicketCategoryMapper {

    /**
     * Maps a TicketCategory entity to TicketCategoryViewDTO.
     */
    public TicketCategoryViewDTO toViewDTO(TicketCategory category) {
        TicketCategoryViewDTO dto = new TicketCategoryViewDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setPrice(category.getPrice());
        dto.setAvailableSpots(category.getAvailableSpots());
        return dto;
    }

    /**
     * Updates a TicketCategory entity from TicketCategoryUpdateDTO.
     * Only non-null fields will be applied.
     */
    public void updateEntity(TicketCategoryUpdateDTO dto, TicketCategory category) {
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        if (dto.getPrice() != null) {
            category.setPrice(dto.getPrice());
        }
        if (dto.getAvailableSpots() != null) {
            category.setAvailableSpots(dto.getAvailableSpots());
        }
    }
}
