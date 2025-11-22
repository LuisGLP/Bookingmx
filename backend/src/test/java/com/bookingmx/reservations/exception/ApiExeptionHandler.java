package com.bookingmx.reservations.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ApiExceptionHandlerTest
 *
 * This test class validates that the ApiExceptionHandler correctly processes
 * custom exceptions and generates the expected error responses.
 *
 * Each test verifies:
 * - Correct HTTP status codes
 * - Correct response body values (message, status, timestamp)
 */
class ApiExceptionHandlerTest {

    /**
     * handler
     *
     * Instance of ApiExceptionHandler used to call its exception
     * handling methods directly.
     */
    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    /**
     * Tests handling of BadRequestException.
     *
     * Verifies:
     * - HTTP 400 status code
     * - Correct error message in response body
     */
    @Test
    void testBadRequestHandler() {
        BadRequestException ex = new BadRequestException("Bad request error");
        ResponseEntity<?> response = handler.badRequest(ex);

        assertEquals(400, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Bad request error", body.get("message"));
    }

    /**
     * Tests handling of NotFoundException.
     *
     * Verifies:
     * - HTTP 404 status code
     * - Correct error message in response body
     */
    @Test
    void testNotFoundHandler() {
        NotFoundException ex = new NotFoundException("Not found");
        ResponseEntity<?> response = handler.notFound(ex);

        assertEquals(404, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Not found", body.get("message"));
    }

    /**
     * Tests handling of generic exceptions.
     *
     * Verifies:
     * - HTTP 500 status code
     * - Generic "Unexpected error" message
     */
    @Test
    void testGenericHandler() {
        Exception ex = new Exception("Some error");
        ResponseEntity<?> response = handler.generic(ex);

        assertEquals(500, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Unexpected error", body.get("message"));
    }

    /**
     * Tests the private errorBody() method using reflection.
     *
     * Ensures:
     * - Method exists and is accessible
     * - Returned map contains correct message and status
     * - Timestamp field is present
     *
     * @throws Exception if reflection access fails
     */
    @Test
    void testErrorBodyPrivateMethod() throws Exception {
        Method method = ApiExceptionHandler.class.getDeclaredMethod("errorBody", String.class, int.class);
        method.setAccessible(true);

        Map<String, Object> body = (Map<String, Object>) method.invoke(handler, "Test message", 123);

        assertEquals("Test message", body.get("message"));
        assertEquals(123, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }
}

