package com.afarcasi.dionysus.exception;

public class TicketCategoryNotFoundException extends RuntimeException {
    public TicketCategoryNotFoundException(String message) {
        super(message);
    }

    public TicketCategoryNotFoundException(Long id) {
        super("Ticket category not found with id: " + id);
    }
}
