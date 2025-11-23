package com.bookingmx.reservations.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;

/**
 * GlobalExceptionHandler
 *
 * This class provides centralized exception handling for all REST controllers.
 * Using @RestControllerAdvice, any exception thrown in the application can be intercepted here,
 * allowing consistent and controlled error responses for the client.
 */



@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NotFoundException errors.
     *
     * @param ex The exception thrown, containing the error message.
     * @return ResponseEntity<String> with:
     *         - HTTP status 404 NOT FOUND
     *         - Response body containing the exception message
     *
     * This method is triggered when a requested resource does not exist.
     */

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles various Bad Request-related exceptions.
     * This method covers:
     * - BadRequestException: custom business validation error.
     * - MethodArgumentNotValidException: validation failure when using @Valid.
     * - HttpMessageNotReadableException: malformed or unreadable JSON in the request body.
     *
     * @param ex The thrown exception (any of the supported types).
     * @return ResponseEntity<String> with:
     *         - HTTP status 400 BAD REQUEST
     *         - Response body containing the exception message
     */

    @ExceptionHandler({ BadRequestException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<String> handleBadRequest(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles any other unexpected exception not explicitly covered above.
     * Acts as a fallback to prevent the application from exposing stack traces or internal details.
     *
     * @param ex The unexpected exception.
     * @return ResponseEntity<String> with:
     *         - HTTP status 500 INTERNAL SERVER ERROR
     *         - Generic response body: "Internal error"
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
    }
}
