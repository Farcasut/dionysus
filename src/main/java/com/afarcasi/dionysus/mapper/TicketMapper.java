package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import org.springframework.stereotype.Component;

/**
 * Handles the mapping of tickets to DTOs.
 */
@Component
public class TicketMapper {

    /**
     * Maps a Ticket entity to TicketViewDTO.
     */
    public TicketViewDTO toViewDTO(Ticket ticket) {
        TicketViewDTO dto = new TicketViewDTO();
        dto.setId(ticket.getId());
        dto.setQrCode(ticket.getQrCode());

        if (ticket.getCategory() != null) {
            TicketCategoryViewDTO categoryDTO = new TicketCategoryViewDTO();
            categoryDTO.setId(ticket.getCategory().getId());
            categoryDTO.setName(ticket.getCategory().getName());
            categoryDTO.setPrice(ticket.getCategory().getPrice());
            categoryDTO.setAvailableSpots(ticket.getCategory().getAvailableSpots());
            dto.setCategory(categoryDTO);
        }

        return dto;
    }
}
