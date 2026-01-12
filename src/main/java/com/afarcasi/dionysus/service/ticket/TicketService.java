package com.afarcasi.dionysus.service.ticket;

import com.afarcasi.dionysus.exception.TicketNotFoundException;
import com.afarcasi.dionysus.mapper.TicketMapper;
import com.afarcasi.dionysus.model.dto.ticket.TicketViewDTO;
import com.afarcasi.dionysus.model.entity.ticket.Ticket;
import com.afarcasi.dionysus.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public Optional<TicketViewDTO> findById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            return Optional.of(ticketMapper.toViewDTO(ticket.get()));
        }
        return Optional.empty();
    }

    public List<TicketViewDTO> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<TicketViewDTO> findByOrderId(Long orderId) {
        return ticketRepository.findByOrderId(orderId).stream()
                .map(ticketMapper::toViewDTO)
                .collect(Collectors.toList());
    }
}
