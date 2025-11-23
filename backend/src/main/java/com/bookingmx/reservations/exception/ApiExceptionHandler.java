package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

/**
 * ApiExceptionHandler
 *
 * This class provides centralized exception handling for the application.
 * It intercepts exceptions thrown by controllers and returns consistent,
 * structured error responses in JSON format.
 *
 * The structure of the error body includes:
 * - timestamp: exact moment the error occurred
 * - status: HTTP status code
 * - message: human-readable error description
 */

@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Handles BadRequestException.
     *
     * @param ex The thrown BadRequestException containing the specific error message.
     * @return ResponseEntity<?> with:
     *         - HTTP status 400 BAD REQUEST
     *         - JSON error body created by errorBody()
     *
     * This method is triggered when the client sends invalid or incomplete input.
     */

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody(ex.getMessage(), 400));
    }

    /**
     * Handles NotFoundException.
     *
     * @param ex The thrown NotFoundException containing the error message.
     * @return ResponseEntity<?> with:
     *         - HTTP status 404 NOT FOUND
     *         - JSON error body
     *
     * This method is used when a requested resource cannot be found.
     */

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(ex.getMessage(), 404));
    }

    /**
     * Handles any unexpected or uncaught exception.
     *
     * @param ex The unexpected thrown exception.
     * @return ResponseEntity<?> with:
     *         - HTTP status 500 INTERNAL SERVER ERROR
     *         - JSON error body containing a generic message
     *
     * This prevents raw stack traces or sensitive information from being exposed.
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody("Unexpected error", 500));
    }

    /**
     * Builds a standardized JSON error body.
     *
     * @param message The error message to include.
     * @param status  The HTTP status code.
     * @return Map<String, Object> containing:
     *         - "timestamp": the moment the error occurred (ISO-8601 string)
     *         - "status": the HTTP status code
     *         - "message": the error message
     *
     * This method ensures all error responses share the same structure.
     */

    private Map<String, Object> errorBody(String message, int status) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status,
                "message", message
        );
    }
}
