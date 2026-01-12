package com.afarcasi.dionysus.service.order;

import com.afarcasi.dionysus.exception.InsufficientTicketsException;
import com.afarcasi.dionysus.exception.TicketCategoryNotFoundException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.mapper.OrderMapper;
import com.afarcasi.dionysus.mapper.TicketMapper;
import com.afarcasi.dionysus.model.dto.order.OrderViewDTO;
import com.afarcasi.dionysus.model.dto.order.TicketSaleDTO;
import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.model.entity.order.Order;
import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import com.afarcasi.dionysus.repository.order.OrderRepository;
import com.afarcasi.dionysus.repository.ticket.TicketRepository;
import com.afarcasi.dionysus.repository.ticketCategory.TicketCategoryRepository;
import com.afarcasi.dionysus.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketCategoryRepository ticketCategoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private OrderService orderService;

    private User customer;
    private TicketCategory ticketCategory;
    private TicketSaleDTO ticketSaleDTO;
    private Order order;
    private OrderViewDTO orderViewDTO;
    private Ticket ticket;
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

        ticketSaleDTO = new TicketSaleDTO();
        ticketSaleDTO.setCustomerId(1L);
        ticketSaleDTO.setTicketCategoryId(1L);
        ticketSaleDTO.setQuantity(2);

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setQrCode("qr-code-123");
        ticket.setCategory(ticketCategory);
        ticket.setOrder(order);

        order.setTickets(Arrays.asList(ticket));

        orderViewDTO = new OrderViewDTO();
        orderViewDTO.setId(1L);
        orderViewDTO.setOrderDate(order.getOrderDate());

        ticketViewDTO = new TicketViewDTO();
        ticketViewDTO.setId(1L);
        ticketViewDTO.setQrCode("qr-code-123");
    }

    @Test
    void sellTickets_WhenValid_ShouldReturnOrderViewDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(ticketCategoryRepository.findById(1L)).thenReturn(Optional.of(ticketCategory));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(ticketRepository.saveAll(anyList())).thenReturn(Arrays.asList(ticket));
        when(ticketCategoryRepository.save(ticketCategory)).thenReturn(ticketCategory);
        when(orderMapper.toViewDTO(any(Order.class))).thenReturn(orderViewDTO);

        OrderViewDTO result = orderService.sellTickets(ticketSaleDTO);

        assertNotNull(result);
        assertEquals(orderViewDTO.getId(), result.getId());
        verify(userRepository).findById(1L);
        verify(ticketCategoryRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
        verify(ticketRepository).saveAll(anyList());
        verify(ticketCategoryRepository).save(ticketCategory);
        assertEquals(48, ticketCategory.getAvailableSpots()); // 50 - 2 = 48
    }

    @Test
    void sellTickets_WhenCustomerDoesNotExist_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.sellTickets(ticketSaleDTO));
        verify(userRepository).findById(1L);
        verify(ticketCategoryRepository, never()).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void sellTickets_WhenTicketCategoryDoesNotExist_ShouldThrowTicketCategoryNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(ticketCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketCategoryNotFoundException.class, () -> orderService.sellTickets(ticketSaleDTO));
        verify(userRepository).findById(1L);
        verify(ticketCategoryRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void sellTickets_WhenInsufficientTickets_ShouldThrowInsufficientTicketsException() {
        ticketCategory.setAvailableSpots(1);
        ticketSaleDTO.setQuantity(2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(ticketCategoryRepository.findById(1L)).thenReturn(Optional.of(ticketCategory));

        assertThrows(InsufficientTicketsException.class, () -> orderService.sellTickets(ticketSaleDTO));
        verify(userRepository).findById(1L);
        verify(ticketCategoryRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
        verify(ticketRepository, never()).saveAll(anyList());
    }

    @Test
    void findById_WhenOrderExists_ShouldReturnOrderViewDTO() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toViewDTO(order)).thenReturn(orderViewDTO);

        Optional<OrderViewDTO> result = orderService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(orderViewDTO.getId(), result.get().getId());
        verify(orderRepository).findById(1L);
        verify(orderMapper).toViewDTO(order);
    }

    @Test
    void findById_WhenOrderDoesNotExist_ShouldReturnEmpty() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<OrderViewDTO> result = orderService.findById(1L);

        assertTrue(result.isEmpty());
        verify(orderRepository).findById(1L);
        verify(orderMapper, never()).toViewDTO(any(Order.class));
    }

    @Test
    void findAll_ShouldReturnListOfOrderViewDTOs() {
        Order order2 = new Order();
        order2.setId(2L);
        List<Order> orders = Arrays.asList(order, order2);
        OrderViewDTO orderViewDTO2 = new OrderViewDTO();
        orderViewDTO2.setId(2L);
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toViewDTO(order)).thenReturn(orderViewDTO);
        when(orderMapper.toViewDTO(order2)).thenReturn(orderViewDTO2);

        List<OrderViewDTO> result = orderService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository).findAll();
        verify(orderMapper, times(2)).toViewDTO(any(Order.class));
    }

    @Test
    void findByCustomerId_ShouldReturnListOfOrderViewDTOs() {
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByCustomerId(1L)).thenReturn(orders);
        when(orderMapper.toViewDTO(order)).thenReturn(orderViewDTO);

        List<OrderViewDTO> result = orderService.findByCustomerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository).findByCustomerId(1L);
        verify(orderMapper).toViewDTO(order);
    }

    @Test
    void findTicketsByOrderId_ShouldReturnListOfTicketViewDTOs() {
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        List<Ticket> tickets = Arrays.asList(ticket, ticket2);
        TicketViewDTO ticketViewDTO2 = new TicketViewDTO();
        ticketViewDTO2.setId(2L);
        when(ticketRepository.findByOrderId(1L)).thenReturn(tickets);
        when(ticketMapper.toViewDTO(ticket)).thenReturn(ticketViewDTO);
        when(ticketMapper.toViewDTO(ticket2)).thenReturn(ticketViewDTO2);

        List<TicketViewDTO> result = orderService.findTicketsByOrderId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ticketRepository).findByOrderId(1L);
        verify(ticketMapper, times(2)).toViewDTO(any(Ticket.class));
    }
}
