package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NotFoundException
 * This custom exception is thrown when a requested resource cannot be found.
 * The @ResponseStatus annotation ensures that Spring returns an HTTP 404 NOT FOUND
 * response whenever this exception is triggered.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    /**
     * Constructor for NotFoundException.
     *
     * @param m A descriptive error message indicating which resource was not found.
     * This message is included in the HTTP response body so the client
     * can understand the cause of the error.
     */
    public NotFoundException(String m) { super(m); }
}
