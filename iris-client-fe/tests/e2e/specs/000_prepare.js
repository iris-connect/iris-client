// https://docs.cypress.io/api/introduction/api.html

import {
  getTestAdminCredentials,
  useDedicatedTestUser,
} from "../support/commands";

describe("AdminCreate", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
  });
  it("should create the test admin if dedicated test user is used", () => {
    if (useDedicatedTestUser()) {
      cy.login({
        userName: Cypress.env("auth_username"),
        password: Cypress.env("auth_password"),
      });
      cy.getApp().then((app) => {
        app.$store.dispatch("adminUserCreate/createUser", {
          ...getTestAdminCredentials(),
          role: "ADMIN",
        });
      });
    }
  });
});
