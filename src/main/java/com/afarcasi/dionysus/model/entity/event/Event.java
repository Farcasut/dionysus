package com.afarcasi.dionysus.model.entity.event;

import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.venue.Venue;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data @NoArgsConstructor
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;



    @OneToMany(mappedBy = "event")
    private List<TicketCategory> categories;

}