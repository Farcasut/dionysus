package com.afarcasi.dionysus.model.dto.ticket;

import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import lombok.Data;

/**
 * DTO for viewing ticket information.
 */
@Data
public class TicketViewDTO {
    private Long id;
    private String qrCode;
    private TicketCategoryViewDTO category;
}
