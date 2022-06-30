import { defineConfig } from "cypress";

export default defineConfig({
  fixturesFolder: "tests/e2e/fixtures",
  screenshotsFolder: "tests/e2e/screenshots",
  downloadsFolder: "tests/e2e/downloads",
  videosFolder: "tests/e2e/videos",
  env: {
    FAIL_FAST_ENABLED: false,
  },
  e2e: {
    setupNodeEvents(on, config) {
      // eslint-disable-next-line @typescript-eslint/no-var-requires
      require("cypress-fail-fast/plugin")(on, config);
      config.env.MOCK_SERVER =
        process.env.VUE_APP_ENABLE_MOCK_SERVER === "true";
      return config;
    },
    specPattern: "tests/e2e/specs/**/*.{js,jsx,ts,tsx}",
    supportFile: "tests/e2e/support/index.js",
    excludeSpecPattern: process.env.CI ? ["tests/e2e/specs/all.cy.ts"] : [],
  },
});
