package com.afarcasi.dionysus.model.entity.order;

import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import com.afarcasi.dionysus.model.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    // The Normal User buying the ticket
    private User customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}