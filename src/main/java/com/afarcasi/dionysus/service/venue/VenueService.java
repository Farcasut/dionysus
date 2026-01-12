package com.afarcasi.dionysus.service.venue;

import com.afarcasi.dionysus.exception.UnauthorizedRoleException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.exception.VenueNotFoundException;
import com.afarcasi.dionysus.mapper.VenueMapper;
import com.afarcasi.dionysus.model.dto.venue.VenueCreationDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueUpdateDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueViewDTO;
import com.afarcasi.dionysus.model.entity.user.User;
import com.afarcasi.dionysus.model.entity.user.UserCategory;
import com.afarcasi.dionysus.model.entity.venue.Venue;
import com.afarcasi.dionysus.repository.user.UserRepository;
import com.afarcasi.dionysus.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final VenueMapper venueMapper;

    @Transactional
    public VenueViewDTO createVenue(VenueCreationDTO dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException(dto.getOwnerId()));

        if (owner.getRole() != UserCategory.VENUE_OWNER) {
            throw new UnauthorizedRoleException("Only users with VENUE_OWNER role can create venues");
        }

        Venue venue = new Venue();
        venue.setName(dto.getName());
        venue.setCapacity(dto.getCapacity());
        venue.setOwner(owner);

        venueRepository.save(venue);
        return venueMapper.toViewDTO(venue);
    }

    public Optional<VenueViewDTO> findById(Long id) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (venue.isPresent()) {
            return Optional.of(venueMapper.toViewDTO(venue.get()));
        }
        return Optional.empty();
    }

    public List<VenueViewDTO> findAll() {
        return venueRepository.findAll().stream()
                .map(venueMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<VenueViewDTO> findByOwnerId(Long ownerId) {
        return venueRepository.findByOwnerId(ownerId).stream()
                .map(venueMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<VenueViewDTO> findByNameContaining(String name) {
        return venueRepository.findByNameContainingIgnoreCase(name).stream()
                .map(venueMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VenueViewDTO updateVenue(Long id, VenueUpdateDTO dto) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException(id));

        venueMapper.updateEntity(dto, venue);
        venueRepository.save(venue);

        return venueMapper.toViewDTO(venue);
    }

    @Transactional
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new VenueNotFoundException(id);
        }
        venueRepository.deleteById(id);
    }
}
