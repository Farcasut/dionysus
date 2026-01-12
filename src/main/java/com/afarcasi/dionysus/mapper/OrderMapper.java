package com.afarcasi.dionysus.mapper;

import com.afarcasi.dionysus.model.dto.order.OrderViewDTO;
import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.model.dto.user.UserResponseDTO;
import com.afarcasi.dionysus.model.entity.order.Order;
import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the mapping of orders to DTOs.
 */
@Component
public class OrderMapper {

    /**
     * Maps an Order entity to OrderViewDTO.
     */
    public OrderViewDTO toViewDTO(Order order) {
        OrderViewDTO dto = new OrderViewDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());

        if (order.getCustomer() != null) {
            UserResponseDTO customerDTO = new UserResponseDTO();
            customerDTO.setId(order.getCustomer().getId());
            customerDTO.setEmail(order.getCustomer().getEmail());
            customerDTO.setUsername(order.getCustomer().getUsername());
            customerDTO.setRole(order.getCustomer().getRole());
            customerDTO.setFirstName(order.getCustomer().getFirstName());
            customerDTO.setLastName(order.getCustomer().getLastName());
            dto.setCustomer(customerDTO);
        }

        if (order.getTickets() != null) {
            List<TicketViewDTO> ticketDTOs = order.getTickets().stream()
                    .map(this::toTicketViewDTO)
                    .collect(Collectors.toList());
            dto.setTickets(ticketDTOs);
        }

        return dto;
    }

    private TicketViewDTO toTicketViewDTO(Ticket ticket) {
        TicketViewDTO dto = new TicketViewDTO();
        dto.setId(ticket.getId());
        dto.setQrCode(ticket.getQrCode());

        if (ticket.getCategory() != null) {
            com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO categoryDTO =
                    new com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO();
            categoryDTO.setId(ticket.getCategory().getId());
            categoryDTO.setName(ticket.getCategory().getName());
            categoryDTO.setPrice(ticket.getCategory().getPrice());
            categoryDTO.setAvailableSpots(ticket.getCategory().getAvailableSpots());
            dto.setCategory(categoryDTO);
        }

        return dto;
    }
}
