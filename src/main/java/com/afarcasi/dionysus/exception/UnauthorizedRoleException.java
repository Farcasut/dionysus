package com.afarcasi.dionysus.exception;

public class UnauthorizedRoleException extends RuntimeException {
    public UnauthorizedRoleException(String message) {
        super(message);
    }

    public UnauthorizedRoleException() {
        super("User does not have the required role for this operation");
    }
}
