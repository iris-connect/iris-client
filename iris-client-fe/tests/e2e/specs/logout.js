// https://docs.cypress.io/api/introduction/api.html

describe("Logout", () => {
  beforeEach(() => {
    cy.login(Cypress.env("auth_username"), Cypress.env("auth_password"));
  });
  it("should logout user and redirect to login page", () => {
    cy.get(".v-toolbar button .mdi-account-circle").should("exist").click();
    cy.get("div[role='menu']").contains("Abmelden").click();
    cy.get(".v-dialog .v-card__actions").contains("Abmelden").click();
    cy.url().should("include", "/user/login");
  });
});
