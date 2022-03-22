// https://docs.cypress.io/api/introduction/api.html

import { useDedicatedTestUser } from "../support/commands";

describe("AdminDelete", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
  });
  it("should delete the test admin if dedicated test user is used", () => {
    if (useDedicatedTestUser()) {
      let testAdminId;
      cy.login();
      cy.fetchUser();
      cy.getApp().then((app) => {
        testAdminId = app.$store.state.userLogin.user.id;
      });
      cy.logout();
      cy.login({
        userName: Cypress.env("auth_username"),
        password: Cypress.env("auth_password"),
      });
      cy.getApp().then((app) => {
        app.$store.dispatch("adminUserList/deleteUser", testAdminId);
      });
    }
  });
});
