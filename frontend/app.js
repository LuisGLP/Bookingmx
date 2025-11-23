// -----------------------------------------------------------------------------
// Frontend controller that wires together the Graph module and the Reservations
// API client. This file is responsible only for UI interaction and DOM updates.
//
// Responsibilities:
//   - Build and validate the sample graph dataset
//   - Handle user form submission to compute nearby cities
//   - Load, create, and cancel reservations via REST API
//   - Update UI lists dynamically
// -----------------------------------------------------------------------------

import {
  sampleData,
  validateGraphData,
  buildGraph,
  getNearbyCities,
} from "./js/graph.js";
import {
  listReservations,
  createReservation,
  cancelReservation,
} from "./js/api.js";

// -----------------------------------------------------------------------------
// Graph UI Setup
// -----------------------------------------------------------------------------

const form = document.getElementById("graph-form");
const destinationEl = document.getElementById("destination");
const maxDistanceEl = document.getElementById("maxDistance");
const nearbyList = document.getElementById("nearby-list");

// Validate and build graph (using sample dataset)
const validation = validateGraphData(sampleData);
const graph = validation.ok
  ? buildGraph(sampleData.cities, sampleData.edges)
  : null;

// Handle "Find Nearby Cities" form submission
form.addEventListener("submit", (e) => {
  e.preventDefault();
  if (!graph) return;
  const dest = destinationEl.value.trim();
  const maxD = Number(maxDistanceEl.value);

  // Query graph for nearby cities
  const results = getNearbyCities(graph, dest, maxD);
  nearbyList.innerHTML = "";
  if (results.length === 0) {
    nearbyList.innerHTML = `<li>No nearby cities found. Check destination or adjust distance.</li>`;
    return;
  }

  // Render resulting city list
  for (const r of results) {
    const li = document.createElement("li");
    li.textContent = `${r.city} — ${r.distance} km`;
    nearbyList.appendChild(li);
  }
});

// -----------------------------------------------------------------------------
// Reservations UI Setup
// -----------------------------------------------------------------------------

const resForm = document.getElementById("reservation-form");
const refreshBtn = document.getElementById("refresh");
const listEl = document.getElementById("reservation-list");

/**
 * Fetches reservations from the backend and updates the UI list.
 * Displays loading state and handles fetch errors gracefully.
 */
async function refreshReservations() {
  listEl.innerHTML = "<li>Loading...</li>";
  try {
    const items = await listReservations();
    listEl.innerHTML = "";
    for (const r of items) {
      const li = document.createElement("li");
      li.innerHTML = `
        <strong>#${r.id}</strong> ${r.guestName} @ ${r.hotelName}
        (${r.checkIn} → ${r.checkOut}) [${r.status}]
        <button data-id=\"${r.id}\" class=\"cancel\">Cancel</button>
      `;
      listEl.appendChild(li);
    }
  } catch (e) {
    listEl.innerHTML = `<li>Error: ${e.message}</li>`;
  }
}

/**
 * Handles reservation creation form:
 *   - Collects form values
 *   - Sends POST to the API
 *   - Refreshes UI on success
 */
resForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  const payload = {
    guestName: document.getElementById("guestName").value.trim(),
    hotelName: document.getElementById("hotelName").value.trim(),
    checkIn: document.getElementById("checkIn").value,
    checkOut: document.getElementById("checkOut").value,
  };
  try {
    await createReservation(payload);
    await refreshReservations();
    resForm.reset();
  } catch (err) {
    alert(err.message);
  }
});

/**
 * Handles cancel button clicks using event delegation.
 * Ensures:
 *   - Only elements with .cancel trigger cancellation
 *   - Reservation list updates after successful API call
 */
listEl.addEventListener("click", async (e) => {
  const btn = e.target.closest(".cancel");
  if (!btn) return;
  const id = btn.getAttribute("data-id");
  try {
    await cancelReservation(id);
    await refreshReservations();
  } catch (err) {
    alert(err.message);
  }
});

// Refresh button to reload reservations list manually
refreshBtn.addEventListener("click", refreshReservations);

// Initial load
refreshReservations();
