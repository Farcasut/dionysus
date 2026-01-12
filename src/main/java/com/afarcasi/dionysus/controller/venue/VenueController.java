package com.afarcasi.dionysus.controller.venue;

import com.afarcasi.dionysus.exception.UnauthorizedRoleException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.exception.VenueNotFoundException;
import com.afarcasi.dionysus.model.dto.venue.VenueCreationDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueUpdateDTO;
import com.afarcasi.dionysus.model.dto.venue.VenueViewDTO;
import com.afarcasi.dionysus.service.venue.VenueService;
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
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Tag(name = "Venue Management", description = "Endpoints for venue handling.")
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    @Operation(summary = "Create a new venue")
    public ResponseEntity<VenueViewDTO> createVenue(@Valid @RequestBody @NotNull VenueCreationDTO dto) {
        try {
            VenueViewDTO venue = venueService.createVenue(dto);
            return new ResponseEntity<>(venue, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UnauthorizedRoleException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get venue by ID")
    public ResponseEntity<VenueViewDTO> getVenueById(@PathVariable Long id) {
        Optional<VenueViewDTO> venue = venueService.findById(id);
        if (venue.isPresent()) {
            return new ResponseEntity<>(venue.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @Operation(summary = "Get all venues")
    public ResponseEntity<List<VenueViewDTO>> getAllVenues() {
        List<VenueViewDTO> venues = venueService.findAll();
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get all venues by owner ID")
    public ResponseEntity<List<VenueViewDTO>> getVenuesByOwner(@PathVariable Long ownerId) {
        List<VenueViewDTO> venues = venueService.findByOwnerId(ownerId);
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search venues by name")
    public ResponseEntity<List<VenueViewDTO>> searchVenuesByName(@RequestParam String name) {
        List<VenueViewDTO> venues = venueService.findByNameContaining(name);
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update venue details")
    public ResponseEntity<VenueViewDTO> updateVenue(
            @PathVariable Long id,
            @Valid @RequestBody @NotNull VenueUpdateDTO dto) {
        try {
            VenueViewDTO updatedVenue = venueService.updateVenue(id, dto);
            return new ResponseEntity<>(updatedVenue, HttpStatus.OK);
        } catch (VenueNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a venue")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        try {
            venueService.deleteVenue(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (VenueNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
