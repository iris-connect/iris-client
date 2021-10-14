// https://docs.cypress.io/api/introduction/api.html

describe("AppBar", () => {
  const navLinks = {
    dashboard: "/",
    "event-list": "/events/list",
    "index-list": "/cases/list",
    about: "/about",
  };
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
  });
  it("should display an empty app bar without navigation links / user-menu - if user is not logged in", () => {
    cy.location("pathname").should("equal", "/user/login");
    cy.get("header.v-app-bar")
      .should("exist")
      .within(() => {
        Object.keys(navLinks).forEach((key) => {
          cy.getBy(`app-menu.nav.link.${key}`).should("not.exist");
        });
        cy.getBy("user-menu.activator").should("not.exist");
      });
    cy.getBy("user-menu").should("not.exist");
  });
  it("should display an app bar with a user-menu and navigation links, enable indexTracking and navigate to the links - if user is logged in", () => {
    cy.login();
    cy.visit("/?indexTracking=enabled");
    cy.location("pathname").should("not.equal", "/user/login");
    cy.get("header.v-app-bar")
      .should("exist")
      .within(() => {
        Object.keys(navLinks).forEach((key) => {
          cy.getBy(`app-menu.nav.link.${key}`)
            .should("exist")
            .should("have.attr", "href", navLinks[key])
            .click()
            .should("have.class", "v-btn--active");
          cy.location("pathname").should("equal", navLinks[key]);
        });
        cy.getBy("user-menu.activator").should("exist").click();
      });
    cy.getBy("user-menu").should("exist");
    cy.logout();
  });
  //@todo: add additional test for non admin users
  it("should open the user menu and navigate to the admin area or the user profile based on user role", () => {
    cy.login();
    cy.fetchUser();
    cy.getBy("user-menu.activator").should("exist").click();
    cy.getApp().then((app) => {
      if (app.$store.getters["userLogin/isAdmin"]) {
        cy.getBy("{user-menu.item.user-profile}").should("not.exist");
        cy.getBy("{user-menu.item.admin-user-list}").should("exist").click();
        cy.location("pathname").should("equal", "/admin/user/list");
      }
      if (app.$store.getters["userLogin/isUser"]) {
        cy.getBy("{user-menu.item.admin-user-list}").should("not.exist");
        cy.getBy("{user-menu.item.user-profile}").should("exist").click();
        cy.location("pathname").should(
          "equal",
          "/admin/user/edit/" + app.$store.state.userLogin.user?.id
        );
        cy.visit("/admin/user/list");
        cy.location("pathname").should("not.equal", "/admin/user/list");
      }
      cy.logout();
    });
  });
});
