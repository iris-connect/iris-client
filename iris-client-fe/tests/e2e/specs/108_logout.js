// https://docs.cypress.io/api/introduction/api.html

describe("Logout", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
  });
  it("should open user-menu and logout user with confirmation dialog and redirect to login page", () => {
    cy.login();
    cy.getBy("user-menu.activator").should("exist").click();
    cy.getBy("{user-menu} {logout-confirm-dialog.activator}")
      .should("exist")
      .click();
    cy.getBy("logout-confirm-dialog.action.confirm").should("exist").click();
    cy.location("pathname").should("equal", "/user/login");
  });
});
