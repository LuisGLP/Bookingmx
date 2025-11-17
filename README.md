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

**Frontend available at:** `http://localhost:5173`

#### Run Frontend Tests

```bash
cd frontend
npm test
npm run test:coverage  # With coverage report
```

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

**Frontend available at:** `http://localhost:5173`

#### Run Frontend Tests

```bash
cd frontend
npm test
npm run test:coverage  # With coverage report
```

### Project Structure

```bash
BookingMx/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/bookingmx/reservations/
│   │   │   │   ├── controller/
│   │   │   │   │   ├── ReservationController.java
│   │   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │   ├── service/
│   │   │   │   │   └── ReservationService.java      ← FIXED (@Service)
│   │   │   │   ├── model/
│   │   │   │   ├── dto/
│   │   │   │   ├── repo/
│   │   │   │   └── exception/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/
│   │           └── ReservationServiceTest.java
│   ├── pom.xml
│   └── TESTING_NOTES.md
│
├── frontend/
│   ├── js/
│   │   ├── graph.js
│   │   ├── api.js
│   │   └── graph.test.js
│   ├── app.js                                       ← UPDATED
│   ├── index.html
│   ├── styles.css
│   ├── package.json
│   ├── jest.config.js
│   ├── .gitignore
│   └── JEST_TEST_SETUP.md
│
└── README.md
```

---

### Issues Fixed

Backend
**Issue 1: Missing @Service Annontation**
**Problem:** The `ReservationService` class was missing the `@Service` annotation causing Spring to not reconize it as a bean.

**Solution:**

```java
// filepath: [ReservationService.java]
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

Frontend
**Issue 2: Cancel Button Visible After Cancellation**
**Problem:** The "Cancel Reservation" button remained visible after cancelling a reservation.

**Solution:**

```javascript
// filepath: frontend/app.js
async function cancelReservation(id) {
  if (!confirm("Are you sure you want to cancel this reservation?")) return;

  try {
    const data = await updateReservationStatus(id, "CANCELLED");

    // Update UI
    const card = document.querySelector(`[data-reservation-id="${id}"]`);
    if (card) {
      const statusBadge = card.querySelector(".status-badge");
      statusBadge.textContent = "CANCELLED";
      statusBadge.className = "status-badge status-cancelled";

      // NEW: Hide cancel button
      const cancelBtn = card.querySelector(".btn-cancel");
      if (cancelBtn) {
        cancelBtn.style.display = "none"; // ← ADDED
      }
    }

    alert("Reservation cancelled successfully");
  } catch (error) {
    alert("Error cancelling: " + error.message);
  }
}
```
