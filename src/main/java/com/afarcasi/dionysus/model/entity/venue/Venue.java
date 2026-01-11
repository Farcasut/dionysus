package com.afarcasi.dionysus.model.entity.venue;

import com.afarcasi.dionysus.model.entity.event.Event;
import com.afarcasi.dionysus.model.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "venue")
    private List<Event> events;
}