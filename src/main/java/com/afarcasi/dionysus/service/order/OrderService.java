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
import com.afarcasi.dionysus.repository.order.OrderRepository;
import com.afarcasi.dionysus.repository.ticket.TicketRepository;
import com.afarcasi.dionysus.repository.ticketCategory.TicketCategoryRepository;
import com.afarcasi.dionysus.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final TicketCategoryRepository ticketCategoryRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final TicketMapper ticketMapper;

    @Transactional
    public OrderViewDTO sellTickets(TicketSaleDTO dto) {
        User customer = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new UserNotFoundException(dto.getCustomerId()));

        TicketCategory category = ticketCategoryRepository.findById(dto.getTicketCategoryId())
                .orElseThrow(() -> new TicketCategoryNotFoundException(dto.getTicketCategoryId()));

        if (category.getAvailableSpots() < dto.getQuantity()) {
            throw new InsufficientTicketsException(dto.getQuantity(), category.getAvailableSpots());
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < dto.getQuantity(); i++) {
            Ticket ticket = new Ticket();
            ticket.setOrder(order);
            ticket.setCategory(category);
            ticket.setQrCode(UUID.randomUUID().toString());
            tickets.add(ticket);
        }

        ticketRepository.saveAll(tickets);

        category.setAvailableSpots(category.getAvailableSpots() - dto.getQuantity());
        ticketCategoryRepository.save(category);

        order.setTickets(tickets);
        return orderMapper.toViewDTO(order);
    }

    public Optional<OrderViewDTO> findById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return Optional.of(orderMapper.toViewDTO(order.get()));
        }
        return Optional.empty();
    }

    public List<OrderViewDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toViewDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<OrderViewDTO> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(orderMapper::toViewDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<TicketViewDTO> findTicketsByOrderId(Long orderId) {
        return ticketRepository.findByOrderId(orderId).stream()
                .map(ticketMapper::toViewDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}
