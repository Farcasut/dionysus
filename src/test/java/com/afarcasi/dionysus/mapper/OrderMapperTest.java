package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.order.OrderViewDTO;
import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.model.entity.order.Order;
import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private OrderMapper orderMapper;

    private Order order;
    private User customer;
    private Ticket ticket;
    private TicketCategory ticketCategory;
    private TicketViewDTO ticketViewDTO;

    @BeforeEach
    void setUp() {
        customer = new User();
        customer.setId(1L);
        customer.setEmail("customer@example.com");
        customer.setUsername("customer");
        customer.setRole(UserCategory.NORMAL_USER);
        customer.setFirstName("Customer");
        customer.setLastName("User");

        ticketCategory = new TicketCategory();
        ticketCategory.setId(1L);
        ticketCategory.setName("VIP");
        ticketCategory.setPrice(100.0);
        ticketCategory.setAvailableSpots(50);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setQrCode("qr-code-123");
        ticket.setCategory(ticketCategory);
        ticket.setOrder(order);

        ticketViewDTO = new TicketViewDTO();
        ticketViewDTO.setId(1L);
        ticketViewDTO.setQrCode("qr-code-123");

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setTickets(Arrays.asList(ticket));
    }

    @Test
    void toViewDTO_ShouldMapAllFieldsCorrectly() {
        when(ticketMapper.toViewDTO(ticket)).thenReturn(ticketViewDTO);

        OrderViewDTO result = orderMapper.toViewDTO(order);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderDate(), result.getOrderDate());
        assertNotNull(result.getCustomer());
        assertEquals(customer.getId(), result.getCustomer().getId());
        assertEquals(customer.getEmail(), result.getCustomer().getEmail());
        assertEquals(customer.getUsername(), result.getCustomer().getUsername());
        assertEquals(customer.getRole(), result.getCustomer().getRole());
        assertEquals(customer.getFirstName(), result.getCustomer().getFirstName());
        assertEquals(customer.getLastName(), result.getCustomer().getLastName());
        assertNotNull(result.getTickets());
        assertEquals(1, result.getTickets().size());
        verify(ticketMapper).toViewDTO(ticket);
    }

    @Test
    void toViewDTO_WhenCustomerIsNull_ShouldNotSetCustomer() {
        order.setCustomer(null);
        when(ticketMapper.toViewDTO(ticket)).thenReturn(ticketViewDTO);

        OrderViewDTO result = orderMapper.toViewDTO(order);

        assertNotNull(result);
        assertNull(result.getCustomer());
        verify(ticketMapper).toViewDTO(ticket);
    }

    @Test
    void toViewDTO_WhenTicketsIsNull_ShouldNotSetTickets() {
        order.setTickets(null);

        OrderViewDTO result = orderMapper.toViewDTO(order);

        assertNotNull(result);
        assertNull(result.getTickets());
        verify(ticketMapper, never()).toViewDTO(any(Ticket.class));
    }

    @Test
    void toViewDTO_WhenTicketsIsEmpty_ShouldSetEmptyList() {
        order.setTickets(Arrays.asList());

        OrderViewDTO result = orderMapper.toViewDTO(order);

        assertNotNull(result);
        assertNotNull(result.getTickets());
        assertEquals(0, result.getTickets().size());
        verify(ticketMapper, never()).toViewDTO(any(Ticket.class));
    }

    @Test
    void toViewDTO_WhenMultipleTickets_ShouldMapAllTickets() {
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setQrCode("qr-code-456");
        TicketViewDTO ticketViewDTO2 = new TicketViewDTO();
        ticketViewDTO2.setId(2L);
        ticketViewDTO2.setQrCode("qr-code-456");
        order.setTickets(Arrays.asList(ticket, ticket2));
        when(ticketMapper.toViewDTO(ticket)).thenReturn(ticketViewDTO);
        when(ticketMapper.toViewDTO(ticket2)).thenReturn(ticketViewDTO2);

        OrderViewDTO result = orderMapper.toViewDTO(order);

        assertNotNull(result);
        assertNotNull(result.getTickets());
        assertEquals(2, result.getTickets().size());
        verify(ticketMapper).toViewDTO(ticket);
        verify(ticketMapper).toViewDTO(ticket2);
    }
}
