package com.afarcasi.dionysus.model.entity.user;

/**
 * Enumerates what roles a user can have.
 */
public enum UserCategory {
    // Normal user -> can just buy tickets
    NORMAL_USER,
    // Can create events and link them to venues.
    EVENT_ORGANIZER,
    // Can register and manage a venue.
    VENUE_OWNER
}
