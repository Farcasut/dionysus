package com.afarcasi.dionysus.service.ticket;

import com.afarcasi.dionysus.mapper.TicketMapper;
import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.model.entity.order.Order;
import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import com.afarcasi.dionysus.repository.ticket.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private TicketViewDTO ticketViewDTO;
    private TicketCategory ticketCategory;
    private Order order;

    @BeforeEach
    void setUp() {
        ticketCategory = new TicketCategory();
        ticketCategory.setId(1L);
        ticketCategory.setName("VIP");
        ticketCategory.setPrice(100.0);
        ticketCategory.setAvailableSpots(50);

        order = new Order();
        order.setId(1L);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setQrCode("qr-code-123");
        ticket.setCategory(ticketCategory);
        ticket.setOrder(order);

        ticketViewDTO = new TicketViewDTO();
        ticketViewDTO.setId(1L);
        ticketViewDTO.setQrCode("qr-code-123");
    }

    @Test
    void findById_WhenTicketExists_ShouldReturnTicketViewDTO() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toViewDTO(ticket)).thenReturn(ticketViewDTO);

        Optional<TicketViewDTO> result = ticketService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(ticketViewDTO.getId(), result.get().getId());
        assertEquals(ticketViewDTO.getQrCode(), result.get().getQrCode());
        verify(ticketRepository).findById(1L);
        verify(ticketMapper).toViewDTO(ticket);
    }

    @Test
    void findById_WhenTicketDoesNotExist_ShouldReturnEmpty() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TicketViewDTO> result = ticketService.findById(1L);

        assertTrue(result.isEmpty());
        verify(ticketRepository).findById(1L);
        verify(ticketMapper, never()).toViewDTO(any(Ticket.class));
    }

    @Test
    void findAll_ShouldReturnListOfTicketViewDTOs() {
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setQrCode("qr-code-456");
        List<Ticket> tickets = Arrays.asList(ticket, ticket2);
        TicketViewDTO ticketViewDTO2 = new TicketViewDTO();
        ticketViewDTO2.setId(2L);
        ticketViewDTO2.setQrCode("qr-code-456");
        when(ticketRepository.findAll()).thenReturn(tickets);
        when(ticketMapper.toViewDTO(ticket)).thenReturn(ticketViewDTO);
        when(ticketMapper.toViewDTO(ticket2)).thenReturn(ticketViewDTO2);

        List<TicketViewDTO> result = ticketService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ticketRepository).findAll();
        verify(ticketMapper, times(2)).toViewDTO(any(Ticket.class));
    }

    @Test
    void findByOrderId_ShouldReturnListOfTicketViewDTOs() {
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setQrCode("qr-code-456");
        ticket2.setOrder(order);
        List<Ticket> tickets = Arrays.asList(ticket, ticket2);
        TicketViewDTO ticketViewDTO2 = new TicketViewDTO();
        ticketViewDTO2.setId(2L);
        ticketViewDTO2.setQrCode("qr-code-456");
        when(ticketRepository.findByOrderId(1L)).thenReturn(tickets);
        when(ticketMapper.toViewDTO(ticket)).thenReturn(ticketViewDTO);
        when(ticketMapper.toViewDTO(ticket2)).thenReturn(ticketViewDTO2);

        List<TicketViewDTO> result = ticketService.findByOrderId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ticketRepository).findByOrderId(1L);
        verify(ticketMapper, times(2)).toViewDTO(any(Ticket.class));
    }
}
