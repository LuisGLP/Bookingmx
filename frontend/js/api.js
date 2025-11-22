// ---------------------------------------------------------------------------
// Minimal API client used to communicate with the backend Reservations API.
// This module centralizes fetch calls and exposes simple helper functions.
//
// Each function:
//   - Sends a request to the Spring Boot backend.
//   - Validates the response status.
//   - Throws a readable error message on failures.
//   - Returns JSON payloads for successful operations.
//
// This file is intentionally small and framework-agnostic so it can be easily
// mocked and tested in tools like Jest.
// ----------

const BASE_URL = "http://localhost:8080/api/reservations";

/**
 * Fetches the full list of reservations from the backend.
 * Throws an error if the request fails.
 */
export async function listReservations() {
    const res = await fetch(BASE_URL);
    if (!res.ok) throw new Error("Failed to fetch reservations");
    return res.json();
}

/**
 * Sends a POST request to create a new reservation.
 * @param {Object} payload - Reservation data (guestName, hotelName, dates, etc.)
 * Throws a backend-generated message or a default one.
 */
export async function createReservation(payload) {
    const res = await fetch(BASE_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error((await res.json()).message || "Create failed");
    return res.json();
}

/**
 * Updates an existing reservation using PUT.
 * @param {string|number} id - Reservation ID to update.
 * @param {Object} payload - Updated fields.
 */
export async function updateReservation(id, payload) {
    const res = await fetch(`${BASE_URL}/${encodeURIComponent(id)}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error((await res.json()).message || "Update failed");
    return res.json();
}

/**
 * Cancels a reservation by ID using DELETE.
 * Backend returns updated reservation or confirmation payload.
 */
export async function cancelReservation(id) {
    const res = await fetch(`${BASE_URL}/${encodeURIComponent(id)}`, { method: "DELETE" });
    if (!res.ok) throw new Error((await res.json()).message || "Cancel failed");
    return res.json();
}
