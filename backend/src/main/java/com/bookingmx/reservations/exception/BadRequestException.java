package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * BadRequestException
 *
 * This custom exception is used to indicate that the client has sent
 * an invalid or malformed request. When this exception is thrown,
 * Spring automatically returns an HTTP 400 BAD REQUEST response because
 * of the @ResponseStatus annotation.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructor for BadRequestException.
     *
     * @param m The error message describing why the request is invalid.
     *
     * This message is included in the HTTP response body and helps
     * the client understand what went wrong.
     */

    public BadRequestException(String m) { super(m); }
}
