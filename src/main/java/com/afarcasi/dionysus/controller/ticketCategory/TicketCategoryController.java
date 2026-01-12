package com.afarcasi.dionysus.controller.ticketCategory;

import com.afarcasi.dionysus.exception.EventNotFoundException;
import com.afarcasi.dionysus.exception.TicketCategoryNotFoundException;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryCreationDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryUpdateDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import com.afarcasi.dionysus.service.ticketCategory.TicketCategoryService;
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
@RequestMapping("/api/ticket-categories")
@RequiredArgsConstructor
@Tag(name = "Ticket Category Management", description = "Endpoints for ticket category handling.")
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;

    @PostMapping
    @Operation(summary = "Create a new ticket category")
    public ResponseEntity<TicketCategoryViewDTO> createTicketCategory(
            @Valid @RequestBody @NotNull TicketCategoryCreationDTO dto) {
        try {
            TicketCategoryViewDTO category = ticketCategoryService.createTicketCategory(dto);
            return new ResponseEntity<>(category, HttpStatus.CREATED);
        } catch (EventNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket category by ID")
    public ResponseEntity<TicketCategoryViewDTO> getTicketCategoryById(@PathVariable Long id) {
        Optional<TicketCategoryViewDTO> category = ticketCategoryService.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @Operation(summary = "Get all ticket categories")
    public ResponseEntity<List<TicketCategoryViewDTO>> getAllTicketCategories() {
        List<TicketCategoryViewDTO> categories = ticketCategoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Get all ticket categories for an event")
    public ResponseEntity<List<TicketCategoryViewDTO>> getTicketCategoriesByEvent(@PathVariable Long eventId) {
        List<TicketCategoryViewDTO> categories = ticketCategoryService.findByEventId(eventId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/organizer/{organizerId}")
    @Operation(summary = "Get all ticket categories for events organized by a user")
    public ResponseEntity<List<TicketCategoryViewDTO>> getTicketCategoriesByOrganizer(@PathVariable Long organizerId) {
        List<TicketCategoryViewDTO> categories = ticketCategoryService.findByOrganizerId(organizerId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a ticket category")
    public ResponseEntity<TicketCategoryViewDTO> updateTicketCategory(
            @PathVariable Long id,
            @Valid @RequestBody @NotNull TicketCategoryUpdateDTO dto) {
        try {
            TicketCategoryViewDTO category = ticketCategoryService.updateTicketCategory(id, dto);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (TicketCategoryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket category")
    public ResponseEntity<Void> deleteTicketCategory(@PathVariable Long id) {
        try {
            ticketCategoryService.deleteTicketCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (TicketCategoryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
