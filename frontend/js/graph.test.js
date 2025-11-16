import {
  Graph,
  validateGraphData,
  buildGraph,
  getNearbyCities,
  sampleData,
} from "./graph.js";

describe("Graph Class", () => {
  describe("constructor", () => {
    test("should create an empty graph", () => {
      const graph = new Graph();
      expect(graph.adj).toBeInstanceOf(Map);
      expect(graph.adj.size).toBe(0);
    });
  });

  describe("addCity", () => {
    test("should add a valid city", () => {
      const graph = new Graph();
      graph.addCity("TestCity");
      expect(graph.adj.has("TestCity")).toBe(true);
      expect(graph.adj.get("TestCity")).toEqual([]);
    });

    test("should not add duplicate cities", () => {
      const graph = new Graph();
      graph.addCity("TestCity");
      graph.addCity("TestCity");
      expect(graph.adj.size).toBe(1);
    });

    test("should throw error for invalid city name (empty string)", () => {
      const graph = new Graph();
      expect(() => graph.addCity("")).toThrow("Invalid city name");
    });

    test("should throw error for invalid city name (number)", () => {
      const graph = new Graph();
      expect(() => graph.addCity(123)).toThrow("Invalid city name");
    });
  });

  describe("neighbors", () => {
    test("should return all neighbors", () => {
      const graph = new Graph();
      graph.addCity("CityA");
      graph.addCity("CityB");
      graph.addCity("CityC");
      graph.addEdge("CityA", "CityB", 10);
      graph.addEdge("CityA", "CityC", 20);

      const neighbors = graph.neighbors("CityA");
      expect(neighbors).toHaveLength(2);
    });

    test("should throw error for unknown city", () => {
      const graph = new Graph();
      expect(() => graph.neighbors("NonExistent")).toThrow("Unknown city");
    });
  });
});

describe("validateGraphData", () => {
  test("should validate correct sample data", () => {
    const result = validateGraphData(sampleData);
    expect(result.ok).toBe(true);
  });

  test("should accept minimal valid data", () => {
    const data = { cities: ["CityA"], edges: [] };
    const result = validateGraphData(data);
    expect(result.ok).toBe(true);
  });

  test("should reject when cities is not an array", () => {
    const data = { cities: "not-array", edges: [] };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("cities/edges must be arrays");
  });

  test("should reject when edges is not an array", () => {
    const data = { cities: [], edges: "not-array" };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("cities/edges must be arrays");
  });

  test("should reject invalid city entry (empty string)", () => {
    const data = { cities: ["CityA", ""], edges: [] };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("invalid city entry");
  });

  test("should reject invalid city entry (whitespace only)", () => {
    const data = { cities: ["CityA", "   "], edges: [] };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("invalid city entry");
  });

  test("should reject invalid city entry (number)", () => {
    const data = { cities: ["CityA", 123], edges: [] };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("invalid city entry");
  });

  test("should reject edge referencing unknown city (from)", () => {
    const data = {
      cities: ["CityA", "CityB"],
      edges: [{ from: "UnknownCity", to: "CityA", distance: 50 }],
    };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("edge references unknown city");
  });

  test("should reject edge with non-numeric distance", () => {
    const data = {
      cities: ["CityA", "CityB"],
      edges: [{ from: "CityA", to: "CityB", distance: "fifty" }],
    };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("invalid distance");
  });

  test("should reject malformed edge (null)", () => {
    const data = {
      cities: ["CityA", "CityB"],
      edges: [null],
    };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("edge references unknown city");
  });

  test("should reject edge with missing fields", () => {
    const data = {
      cities: ["CityA", "CityB"],
      edges: [{ from: "CityA" }],
    };
    const result = validateGraphData(data);
    expect(result.ok).toBe(false);
    expect(result.reason).toBe("edge references unknown city");
  });

  test("should accept edge with zero distance", () => {
    const data = {
      cities: ["CityA", "CityB"],
      edges: [{ from: "CityA", to: "CityB", distance: 0 }],
    };
    const result = validateGraphData(data);
    expect(result.ok).toBe(true);
  });
});

describe("getNearbyCities", () => {
  let testGraph;

  beforeEach(() => {
    testGraph = new Graph();
    testGraph.addCity("CenterCity");
    testGraph.addCity("NearCity");
    testGraph.addCity("MediumCity");
    testGraph.addCity("FarCity");
    testGraph.addCity("IsolatedCity");

    testGraph.addEdge("CenterCity", "NearCity", 50);
    testGraph.addEdge("CenterCity", "MediumCity", 150);
    testGraph.addEdge("CenterCity", "FarCity", 300);
  });

  test("should return nearby cities within default distance (250km)", () => {
    const result = getNearbyCities(testGraph, "CenterCity");
    expect(result).toHaveLength(2);
    expect(result).toContainEqual({ city: "NearCity", distance: 50 });
    expect(result).toContainEqual({ city: "MediumCity", distance: 150 });
  });

  test("should return cities sorted by distance (ascending)", () => {
    const result = getNearbyCities(testGraph, "CenterCity", 500);
    expect(result).toHaveLength(3);
    expect(result[0]).toEqual({ city: "NearCity", distance: 50 });
    expect(result[1]).toEqual({ city: "MediumCity", distance: 150 });
    expect(result[2]).toEqual({ city: "FarCity", distance: 300 });
  });

  test("should filter by custom max distance", () => {
    const result = getNearbyCities(testGraph, "CenterCity", 100);
    expect(result).toHaveLength(1);
    expect(result).toContainEqual({ city: "NearCity", distance: 50 });
  });

  test("should return empty array for city with no neighbors", () => {
    const result = getNearbyCities(testGraph, "IsolatedCity", 1000);
    expect(result).toEqual([]);
  });

  test("should return empty array for unknown city", () => {
    const result = getNearbyCities(testGraph, "NonExistentCity", 250);
    expect(result).toEqual([]);
  });

  test("should throw error for invalid graph type", () => {
    expect(() => getNearbyCities({}, "CenterCity", 250)).toThrow(
      "graph must be Graph"
    );
  });

  test("should throw error for null graph", () => {
    expect(() => getNearbyCities(null, "CenterCity", 250)).toThrow(
      "graph must be Graph"
    );
  });

  test("should return empty array for non-string destination", () => {
    const result = getNearbyCities(testGraph, 123, 250);
    expect(result).toEqual([]);
  });

  test("should include cities at exactly max distance", () => {
    const result = getNearbyCities(testGraph, "CenterCity", 50);
    expect(result).toHaveLength(1);
    expect(result).toContainEqual({ city: "NearCity", distance: 50 });
  });

  test("should handle zero max distance", () => {
    const graph = new Graph();
    graph.addCity("A");
    graph.addCity("B");
    graph.addEdge("A", "B", 0);

    const result = getNearbyCities(graph, "A", 0);
    expect(result).toHaveLength(1);
    expect(result).toContainEqual({ city: "B", distance: 0 });
  });

  test("should work with sample data - Guadalajara", () => {
    const graph = buildGraph(sampleData.cities, sampleData.edges);
    const result = getNearbyCities(graph, "Guadalajara", 100);

    expect(result.length).toBeGreaterThan(0);
    expect(result.every((r) => r.distance <= 100)).toBe(true);
    expect(result[0].distance).toBeLessThanOrEqual(
      result[result.length - 1].distance
    );
  });

  test("should work with sample data - Zapopan", () => {
    const graph = buildGraph(sampleData.cities, sampleData.edges);
    const result = getNearbyCities(graph, "Zapopan", 50);

    expect(result.some((r) => r.city === "Guadalajara")).toBe(true);
  });

  test("should return empty array for empty string destination", () => {
    const result = getNearbyCities(testGraph, "", 250);
    expect(result).toEqual([]);
  });
});
