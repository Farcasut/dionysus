package com.afarcasi.dionysus.model.dto.order;

import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for viewing order information.
 */
@Data
public class OrderViewDTO {
    private Long id;
    private LocalDateTime orderDate;
    private UserResponseDTO customer;
    private List<TicketViewDTO> tickets;
}
