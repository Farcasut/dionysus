package com.afarcasi.dionysus.model.entity.user;

import com.afarcasi.dionysus.model.entity.event.Event;
import com.afarcasi.dionysus.model.entity.order.Order;
import com.afarcasi.dionysus.model.entity.venue.Venue;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "User must have a role")
    private UserCategory role;


    @OneToMany(mappedBy = "owner")
    // For Venue Owners
    private List<Venue> ownedVenues;

    @OneToMany(mappedBy = "organizer")
    // For Event Organizers
    private List<Event> organizedEvents;

    @OneToMany(mappedBy = "customer")
    // For Normal Users
    private List<Order> purchases;
}