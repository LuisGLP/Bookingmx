/**
 * Jest configuration for the frontend JS modules.
 *
 * Key options:
 *  - testEnvironment: Uses a Node environment for pure JS logic (Graph, API mocks, utilities).
 *  - collectCoverage: Enables coverage tracking for all executed tests.
 *  - collectCoverageFrom: Ensures Jest instruments all JS files inside /js even if not tested directly.
 *  - coverageReporters: Generates both text summary in console and LCOV reports for tools like SonarQube.
 */
export default {
    testEnvironment: "node",
    collectCoverage: true,
    collectCoverageFrom: ["js/**/*.js"],
    coverageReporters: ["text", "lcov"]
};
