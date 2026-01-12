package com.afarcasi.dionysus.exception;

public class InsufficientTicketsException extends RuntimeException {
    public InsufficientTicketsException(String message) {
        super(message);
    }

    public InsufficientTicketsException(Integer requested, Integer available) {
        super("Insufficient tickets available. Requested: " + requested + ", Available: " + available);
    }
}
