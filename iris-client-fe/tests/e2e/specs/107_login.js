// https://docs.cypress.io/api/introduction/api.html

import {getTestAdminCredentials} from "../support/commands";

describe("Login", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
  });
  it("should redirect unauthenticated user to login page", () => {
    cy.visit("/");
    cy.location("pathname").should("equal", "/user/login");
  });
  it("should authenticate user and redirect to dashboard or the intercepted auth route", () => {
    const credentials = getTestAdminCredentials();
    cy.loginUsingUi(credentials.userName, credentials.password);
    cy.location("pathname").should("not.equal", "/user/login");
    cy.getBy("user-menu.activator").should("exist");
    cy.logout();
  });
  it("should display login form validation errors", () => {
    cy.get("form").within(() => {
      cy.getBy(".v-btn{submit}").click();
      cy.assertInputInvalidByRule("input{userName}");
      cy.assertInputInvalidByRule("input{password}");
    });
  });
  it("should display authentication error message if user tries to sign-in with invalid credentials", () => {
    cy.loginUsingUi("e2e_test_invalid_userName", "e2e_test_invalid_password");
    cy.getBy("user-login-error").should("contain.text", "401");
  });
});
