# BookingMx

Minimal vanilla JS + Spring Boot project to practice unit tests.

## Project Overview

**BookingMx** is a full-stack application demonstrating:

- RESTful API design with Spring Boot
- Graph algorithms (Dijkstra, BFS) in JavaScript
- Comprehensive unit testing (Jest, JUnit)
- Modern exception handling

---

### Technology Stack

**Backend:**

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Maven

**Frontend:**

- JavaScript (ES6+)
- HTML5/CSS3
- Jest (testing)

## Setup and Execution

### Backend

#### Prerequisites

- Java 17+
- Maven 3.6+

#### Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Backend available at:** `http://localhost:8081`

#### Run Backend Tests

```bash
cd backend
mvn test
```

### Frontend

#### Prerequisites

- Node.js 16+
- npm 8+

#### Run Frontend (Development)

```bash
cd frontend
npm install
npm run serve
```

---

### Issues Fixed

Backend
**Issue 1: Missing @Service Annontation**
**Problem:** The `ReservationService` class was missing the `@Service` annotation causing Spring to not reconize it as a bean.

**Solution:**

```bash
// filepath: [ReservationService.java](http://_vscodecontentref_/0)
package com.bookingmx.reservations.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service  // ← ADDED
public class ReservationService {

    private final ReservationRepository repository;

    @Autowired
    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    // ...existing code...
}
```
