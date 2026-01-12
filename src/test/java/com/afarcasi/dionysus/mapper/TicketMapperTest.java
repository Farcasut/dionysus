package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketMapperTest {

    private TicketMapper ticketMapper;

    private Ticket ticket;
    private TicketCategory ticketCategory;

    @BeforeEach
    void setUp() {
        ticketMapper = new TicketMapper();

        ticketCategory = new TicketCategory();
        ticketCategory.setId(1L);
        ticketCategory.setName("VIP");
        ticketCategory.setPrice(100.0);
        ticketCategory.setAvailableSpots(50);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setQrCode("qr-code-123");
        ticket.setCategory(ticketCategory);
    }

    @Test
    void toViewDTO_ShouldMapAllFieldsCorrectly() {
        TicketViewDTO result = ticketMapper.toViewDTO(ticket);

        assertNotNull(result);
        assertEquals(ticket.getId(), result.getId());
        assertEquals(ticket.getQrCode(), result.getQrCode());
        assertNotNull(result.getCategory());
        assertEquals(ticketCategory.getId(), result.getCategory().getId());
        assertEquals(ticketCategory.getName(), result.getCategory().getName());
        assertEquals(ticketCategory.getPrice(), result.getCategory().getPrice());
        assertEquals(ticketCategory.getAvailableSpots(), result.getCategory().getAvailableSpots());
    }

    @Test
    void toViewDTO_WhenCategoryIsNull_ShouldNotSetCategory() {
        ticket.setCategory(null);

        TicketViewDTO result = ticketMapper.toViewDTO(ticket);

        assertNotNull(result);
        assertEquals(ticket.getId(), result.getId());
        assertEquals(ticket.getQrCode(), result.getQrCode());
        assertNull(result.getCategory());
    }

    @Test
    void toViewDTO_WhenCategoryHasAllFields_ShouldMapAllCategoryFields() {
        TicketViewDTO result = ticketMapper.toViewDTO(ticket);

        assertNotNull(result);
        assertNotNull(result.getCategory());
        assertEquals(ticketCategory.getId(), result.getCategory().getId());
        assertEquals(ticketCategory.getName(), result.getCategory().getName());
        assertEquals(ticketCategory.getPrice(), result.getCategory().getPrice());
        assertEquals(ticketCategory.getAvailableSpots(), result.getCategory().getAvailableSpots());
    }
}
