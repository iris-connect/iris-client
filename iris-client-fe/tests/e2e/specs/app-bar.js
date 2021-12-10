// https://docs.cypress.io/api/introduction/api.html

describe("AppBar", () => {
  const navLinks = {
    dashboard: "/",
    "event-list": "/events/list",
    "index-list": "/cases/list",
    "iris-message-list": "/iris-messages/list",
    about: "/about",
  };
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
  });
  it("should display an empty app bar without navigation links, app-menu and user-menu - if user is not logged in", () => {
    cy.location("pathname").should("equal", "/user/login");
    cy.getBy("app-bar")
      .should("exist")
      .within(() => {
        Object.keys(navLinks).forEach((key) => {
          cy.getBy(`app-bar.nav.link.${key}`).should("not.exist");
        });
        cy.getBy("app-menu.activator").should("not.exist");
        cy.getBy("user-menu.activator").should("not.exist");
      });
    cy.getBy("app-menu").should("not.exist");
    cy.getBy("user-menu").should("not.exist");
  });
  it("should display an app bar with an app-menu, a user-menu and navigation links, enable indexTracking and navigate to the links - if user is logged in", () => {
    cy.login();
    cy.visit("/?indexTracking=enabled");
    cy.location("pathname").should("not.equal", "/user/login");
    cy.getBy("app-bar")
      .should("exist")
      .within(() => {
        Object.keys(navLinks).forEach((key) => {
          cy.getBy(`app-bar.nav.link.${key}`)
            .should("exist")
            .should("have.attr", "href", navLinks[key])
            .click()
            .should("have.class", "v-btn--active");
          cy.location("pathname").should("equal", navLinks[key]);
        });
        cy.getBy("app-menu.activator").should("exist").click();
        cy.getBy("user-menu.activator").should("exist").click();
      });
    cy.getBy("app-menu").should("exist");
    cy.getBy("user-menu").should("exist");
    cy.logout();
  });
});
