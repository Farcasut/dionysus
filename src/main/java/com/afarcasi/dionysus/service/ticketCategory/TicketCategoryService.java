package com.afarcasi.dionysus.service.ticketCategory;

import com.afarcasi.dionysus.exception.EventNotFoundException;
import com.afarcasi.dionysus.exception.TicketCategoryNotFoundException;
import com.afarcasi.dionysus.mapper.TicketCategoryMapper;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryCreationDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryUpdateDTO;
import com.afarcasi.dionysus.model.dto.ticketCategory.TicketCategoryViewDTO;
import com.afarcasi.dionysus.model.entity.event.Event;
import com.afarcasi.dionysus.model.entity.ticketCategory.TicketCategory;
import com.afarcasi.dionysus.repository.event.EventRepository;
import com.afarcasi.dionysus.repository.ticketCategory.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;
    private final EventRepository eventRepository;
    private final TicketCategoryMapper ticketCategoryMapper;

    @Transactional
    public TicketCategoryViewDTO createTicketCategory(TicketCategoryCreationDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EventNotFoundException(dto.getEventId()));

        TicketCategory category = new TicketCategory();
        category.setName(dto.getName());
        category.setPrice(dto.getPrice());
        category.setAvailableSpots(dto.getAvailableSpots());
        category.setEvent(event);

        ticketCategoryRepository.save(category);
        return ticketCategoryMapper.toViewDTO(category);
    }

    public Optional<TicketCategoryViewDTO> findById(Long id) {
        Optional<TicketCategory> category = ticketCategoryRepository.findById(id);
        if (category.isPresent()) {
            return Optional.of(ticketCategoryMapper.toViewDTO(category.get()));
        }
        return Optional.empty();
    }

    public List<TicketCategoryViewDTO> findAll() {
        return ticketCategoryRepository.findAll().stream()
                .map(ticketCategoryMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<TicketCategoryViewDTO> findByEventId(Long eventId) {
        return ticketCategoryRepository.findByEventId(eventId).stream()
                .map(ticketCategoryMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<TicketCategoryViewDTO> findByOrganizerId(Long organizerId) {
        List<Event> events = eventRepository.findByOrganizerId(organizerId);
        return events.stream()
                .flatMap(event -> ticketCategoryRepository.findByEventId(event.getId()).stream())
                .map(ticketCategoryMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketCategoryViewDTO updateTicketCategory(Long id, TicketCategoryUpdateDTO dto) {
        TicketCategory category = ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new TicketCategoryNotFoundException(id));

        ticketCategoryMapper.updateEntity(dto, category);
        ticketCategoryRepository.save(category);

        return ticketCategoryMapper.toViewDTO(category);
    }

    @Transactional
    public void deleteTicketCategory(Long id) {
        if (!ticketCategoryRepository.existsById(id)) {
            throw new TicketCategoryNotFoundException(id);
        }
        ticketCategoryRepository.deleteById(id);
    }
}
