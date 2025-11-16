package com.bookingmx.reservations.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void testBadRequestHandler() {
        BadRequestException ex = new BadRequestException("Bad request error");
        ResponseEntity<?> response = handler.badRequest(ex);

        assertEquals(400, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Bad request error", body.get("message"));
    }

    @Test
    void testNotFoundHandler() {
        NotFoundException ex = new NotFoundException("Not found");
        ResponseEntity<?> response = handler.notFound(ex);

        assertEquals(404, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Not found", body.get("message"));
    }

    @Test
    void testGenericHandler() {
        Exception ex = new Exception("Some error");
        ResponseEntity<?> response = handler.generic(ex);

        assertEquals(500, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Unexpected error", body.get("message"));
    }

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
