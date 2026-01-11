package com.afarcasi.dionysus.model.entity.ticketCategory;

import com.afarcasi.dionysus.model.entity.event.Event;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TicketCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // VIP, General
    private Double price;
    private Integer availableSpots;

    @ManyToOne
    private Event event;
}