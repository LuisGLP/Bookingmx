package com.bookingmx.reservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BookingMxApplication
 *
 * This is the main entry point for the Spring Boot application.
 * The @SpringBootApplication annotation enables:
 * - Component scanning
 * - Auto-configuration
 * - Spring Boot application setup
 *
 * Running the main method starts the embedded server (e.g., Tomcat)
 * and initializes the entire application context.
 */
@SpringBootApplication
public class BookingMxApplication {

    /**
     * main
     *
     * Application entry point. Boots the Spring framework and starts
     * the BookingMX Reservations API.
     *
     * @param args Optional command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(BookingMxApplication.class, args);
    }
}
