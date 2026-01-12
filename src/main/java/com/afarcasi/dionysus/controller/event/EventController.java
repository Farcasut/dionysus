package com.afarcasi.dionysus.controller.event;

import com.afarcasi.dionysus.exception.EventNotFoundException;
import com.afarcasi.dionysus.exception.UnauthorizedRoleException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.exception.VenueNotFoundException;
import com.afarcasi.dionysus.model.dto.event.EventCreationDTO;
import com.afarcasi.dionysus.model.dto.event.EventResponseDTO;
import com.afarcasi.dionysus.model.dto.event.EventUpdateDTO;
import com.afarcasi.dionysus.service.event.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event Management", description = "Endpoints for event handling.")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @Operation(summary = "Create a new event")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody @NotNull EventCreationDTO dto) {
        try {
            EventResponseDTO event = eventService.createEvent(dto);
            return new ResponseEntity<>(event, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (VenueNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UnauthorizedRoleException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event details by ID")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        Optional<EventResponseDTO> event = eventService.findById(id);
        if (event.isPresent()) {
            return new ResponseEntity<>(event.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @Operation(summary = "Get all events")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.findAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/organizer/{organizerId}")
    @Operation(summary = "Get all events by organizer ID")
    public ResponseEntity<List<EventResponseDTO>> getEventsByOrganizer(@PathVariable Long organizerId) {
        List<EventResponseDTO> events = eventService.findByOrganizerId(organizerId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/venue/{venueId}")
    @Operation(summary = "Get all events by venue ID")
    public ResponseEntity<List<EventResponseDTO>> getEventsByVenue(@PathVariable Long venueId) {
        List<EventResponseDTO> events = eventService.findByVenueId(venueId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search events by title")
    public ResponseEntity<List<EventResponseDTO>> searchEventsByTitle(@RequestParam String title) {
        List<EventResponseDTO> events = eventService.findByTitleContaining(title);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update event details")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody @NotNull EventUpdateDTO dto) {
        try {
            EventResponseDTO updatedEvent = eventService.updateEvent(id, dto);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        } catch (EventNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (VenueNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EventNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
