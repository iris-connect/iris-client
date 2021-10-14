// https://docs.cypress.io/api/introduction/api.html

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
    cy.loginUsingUi(Cypress.env("auth_username"), Cypress.env("auth_password"));
    cy.location("pathname").should("not.equal", "/user/login");
    cy.getBy("user-menu.activator").should("exist");
    cy.logout();
  });
  it("should display login form validation errors", () => {
    cy.get("form").within(() => {
      cy.getBy("button{submit}").click();
      cy.assertInputInvalid("input{userName}", "Pflichtfeld");
      cy.assertInputInvalid("input{password}", "Pflichtfeld");
    });
  });
  it("should display authentication error message if user tries to sign-in with invalid credentials", () => {
    cy.loginUsingUi("e2e_test_invalid_userName", "e2e_test_invalid_password");
    cy.getBy("user-login-error").should("contain.text", "401");
  });
});
