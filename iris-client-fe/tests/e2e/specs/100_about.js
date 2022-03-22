// https://docs.cypress.io/api/introduction/api.html

describe("About", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
  });
  it("should check auth-nav guard, navigate to the about page and display content", () => {
    cy.visit("/about");
    cy.location("pathname")
      .should("not.equal", "/about")
      .and("equal", "/user/login");
    cy.login();
    cy.visit("/about");
    cy.location("pathname").should("equal", "/about");
    cy.getBy("install.version").should("contain", "version_e2e_test");
    cy.getBy("install.build").should("contain", "build_e2e_test");
    cy.getBy("contact")
      .should("exist")
      .and("contain", "E2ETestName")
      .and("contain", "E2ETestMail")
      .and("contain", "E2ETestPhone");
    cy.getBy("logo.inoeg").should("have.css", "background-image");
    cy.getBy("logo.bss").should("have.css", "background-image");
    cy.logout();
  });
});
