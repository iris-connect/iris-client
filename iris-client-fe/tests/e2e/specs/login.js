// https://docs.cypress.io/api/introduction/api.html

describe("Login", () => {
  it("should show error message if user tries to sign-in with invalid credentials", () => {
    cy.login("e2e_test_invalid_userName", "e2e_test_invalid_password");
    cy.get(".error--text").should("exist").contains("401");
  });
  it("should authenticate user and redirect to dashboard or the intercepted auth route", () => {
    cy.login(Cypress.env("auth_username"), Cypress.env("auth_password"));
    cy.url().should("not.include", "/user/login");
    cy.get(".v-toolbar button .mdi-account-circle").should("exist");
  });
});
