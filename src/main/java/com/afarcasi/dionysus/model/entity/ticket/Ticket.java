package com.afarcasi.dionysus.model.entity.ticket;

import com.afarcasi.dionysus.model.entity.order.Order;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String qrCode;

    @ManyToOne
    private Order order;

    @ManyToOne
    private TicketCategory category;
}