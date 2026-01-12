package com.afarcasi.dionysus.controller.ticket;

import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.service.ticket.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Management", description = "Endpoints for ticket handling.")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID")
    public ResponseEntity<TicketViewDTO> getTicketById(@PathVariable Long id) {
        Optional<TicketViewDTO> ticket = ticketService.findById(id);
        if (ticket.isPresent()) {
            return new ResponseEntity<>(ticket.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @Operation(summary = "Get all tickets")
    public ResponseEntity<List<TicketViewDTO>> getAllTickets() {
        List<TicketViewDTO> tickets = ticketService.findAll();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}
