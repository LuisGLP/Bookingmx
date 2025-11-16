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

// Graph UI
const form = document.getElementById("graph-form");
const destinationEl = document.getElementById("destination");
const maxDistanceEl = document.getElementById("maxDistance");
const nearbyList = document.getElementById("nearby-list");

const validation = validateGraphData(sampleData);
const graph = validation.ok
  ? buildGraph(sampleData.cities, sampleData.edges)
  : null;

form.addEventListener("submit", (e) => {
  e.preventDefault();
  if (!graph) return;
  const dest = destinationEl.value.trim();
  const maxD = Number(maxDistanceEl.value);
  const results = getNearbyCities(graph, dest, maxD);
  nearbyList.innerHTML = "";
  if (results.length === 0) {
    nearbyList.innerHTML = `<li>No nearby cities found. Check destination or adjust distance.</li>`;
    return;
  }
  for (const r of results) {
    const li = document.createElement("li");
    li.textContent = `${r.city} — ${r.distance} km`;
    nearbyList.appendChild(li);
  }
});

// Reservations UI
const resForm = document.getElementById("reservation-form");
const refreshBtn = document.getElementById("refresh");
const listEl = document.getElementById("reservation-list");

async function refreshReservations() {
  listEl.innerHTML = "<li>Loading...</li>";
  try {
    const items = await listReservations();
    listEl.innerHTML = "";
    for (const r of items) {
      const li = document.createElement("li");
      // ⬇️ si el estatus es "cancelled", no se muestra el botón
      const cancelBtn =
        r.status.toLowerCase() === "canceled"
          ? ""
          : `<button data-id="${r.id}" class="cancel">Cancel</button>`;
      li.innerHTML = `
        <strong>#${r.id}</strong> ${r.guestName} @ ${r.hotelName}
        (${r.checkIn} → ${r.checkOut}) [${r.status}]
        ${cancelBtn}
      `;
      listEl.appendChild(li);
    }
  } catch (e) {
    listEl.innerHTML = `<li>Error: ${e.message}</li>`;
  }
}

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

refreshBtn.addEventListener("click", refreshReservations);
refreshReservations();
