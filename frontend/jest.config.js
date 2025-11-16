export default {
  testEnvironment: "node",
  collectCoverage: true,
  // Focus coverage on the graph visualization module
  collectCoverageFrom: ["js/graph.js"],
  coverageReporters: ["text", "lcov"],
  // No transforms needed; we're using native ESM
  transform: {},
};
