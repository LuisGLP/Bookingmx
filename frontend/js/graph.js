// graph.js
// ---------------------------------------------------------------------------
// Graph data structures and basic algorithms for handling nearby-city lookups.
// The implementation is intentionally simple, deterministic, and fully
// side-effect-free (except for Graph mutation), making it easy to mock or test
// with Jest.
//
// The module includes:
//   - A Graph class with adjacency lists
//   - Helper functions to validate raw graph data
//   - A builder that constructs the graph from dataset arrays
//   - A function to fetch nearby cities within a max distance
//   - A small sample dataset used for demos or tests
// ---------------------------------------------------------------------------

export class Graph {
    constructor() {
        // Adjacency list mapping each city to an array of { to, distance }
        this.adj = new Map();
    }

    /**
     * Adds a city node to the graph if it does not exist.
     * @param {string} name - Name of the city.
     * Throws if the name is invalid.
     */
    addCity(name) {
        if (!name || typeof name !== "string") throw new Error("Invalid city name");
        if (!this.adj.has(name)) this.adj.set(name, []);
    }

    /**
     * Adds an undirected edge between two cities.
     * @param {string} from - Origin city.
     * @param {string} to - Destination city.
     * @param {number} distanceKm - Distance in kilometers.
     * Validates that cities exist and distance is non-negative.
     */
    addEdge(from, to, distanceKm) {
        if (!this.adj.has(from) || !this.adj.has(to)) throw new Error("Unknown city");
        if (!Number.isFinite(distanceKm) || distanceKm < 0) throw new Error("Invalid distance");
        this.adj.get(from).push({ to, distance: distanceKm });
        this.adj.get(to).push({ to: from, distance: distanceKm }); // undirected edge
    }

    /**
     * Returns all neighbors for a given city.
     * @param {string} city - The city to query.
     * Throws if city does not exist.
     */
    neighbors(city) {
        if (!this.adj.has(city)) throw new Error("Unknown city");
        return [...this.adj.get(city)];
    }
}

/**
 * Validates raw input data for graph construction.
 * Ensures:
 *   - cities is an array of unique non-empty strings
 *   - edges reference valid cities
 *   - edges have valid non-negative distances
 *
 * @returns {Object} { ok: boolean, reason?: string }
 */
export function validateGraphData({ cities, edges }) {
    if (!Array.isArray(cities) || !Array.isArray(edges))
        return { ok: false, reason: "cities/edges must be arrays" };

    const citySet = new Set(cities);
    if (citySet.size !== cities.length)
        return { ok: false, reason: "duplicate cities" };

    for (const c of cities)
        if (typeof c !== "string" || !c.trim())
            return { ok: false, reason: "invalid city entry" };

    for (const e of edges) {
        const { from, to, distance } = e ?? {};
        if (!citySet.has(from) || !citySet.has(to))
            return { ok: false, reason: "edge references unknown city" };
        if (!Number.isFinite(distance) || distance < 0)
            return { ok: false, reason: "invalid distance" };
    }

    return { ok: true };
}

/**
 * Builds a Graph instance from arrays of city names and edge objects.
 * @param {string[]} cities
 * @param {Array<{from:string,to:string,distance:number}>} edges
 * @returns {Graph}
 */
export function buildGraph(cities, edges) {
    const g = new Graph();
    for (const c of cities) g.addCity(c);
    for (const { from, to, distance } of edges) g.addEdge(from, to, distance);
    return g;
}

/**
 * Retrieves nearby cities directly connected to a destination, filtered by
 * a maximum distance threshold.
 *
 * This does not perform multi-hop pathfinding — only direct neighbors.
 * Intended as an MVP helper for UI suggestions or quick lookups.
 *
 * @param {Graph} graph - The Graph instance.
 * @param {string} destination - City for which neighbors are requested.
 * @param {number} maxDistanceKm - Distance filter (default: 250 km).
 * @returns {Array<{city:string,distance:number}>}
 */
export function getNearbyCities(graph, destination, maxDistanceKm = 250) {
    if (!(graph instanceof Graph)) throw new Error("graph must be Graph");
    if (typeof destination !== "string" || !graph.adj.has(destination)) return [];

    const neighbors = graph.neighbors(destination);

    return neighbors
        .filter(n => n.distance <= maxDistanceKm)
        .sort((a, b) => a.distance - b.distance)
        .map(n => ({ city: n.to, distance: n.distance }));
}

// ---------------------------------------------------------------------------
// Sample dataset used for quick demos, tests, or the application’s initial state.
// Cities are from the Guadalajara metro area and nearby regional hubs.
// ---------------------------------------------------------------------------
export const sampleData = {
    cities: [
        "Guadalajara", "Tlaquepaque", "Zapopan", "Tepatitlán",
        "Lagos de Moreno", "Tala", "Tequila"
    ],
    edges: [
        { from: "Guadalajara", to: "Zapopan", distance: 12 },
        { from: "Guadalajara", to: "Tlaquepaque", distance: 10 },
        { from: "Guadalajara", to: "Tepatitlán", distance: 78 },
        { from: "Guadalajara", to: "Tequila", distance: 60 },
        { from: "Zapopan", to: "Tala", distance: 35 },
        { from: "Tepatitlán", to: "Lagos de Moreno", distance: 85 }
    ]
};
