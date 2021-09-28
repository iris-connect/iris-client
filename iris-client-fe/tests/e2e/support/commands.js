// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This is will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

Cypress.Commands.add("login", (username, password) => {
  cy.clearLocalStorage();
  cy.visit("/");

  cy.url().should("include", "/user/login");
  cy.contains(".v-card__title", "Anmelden");

  cy.get("form").should("exist");
  cy.get("form").within(() => {
    cy.get("input[type=text]")
      .should("exist")
      .type(username, { log: false })
      .should((el$) => {
        if (el$.val() !== username) {
          throw new Error("Different value of typed username");
        }
      });
    cy.get("input[type=password]")
      .should("exist")
      .type(password, { log: false })
      .should((el$) => {
        if (el$.val() !== password) {
          throw new Error("Different value of typed password");
        }
      });
    cy.get("button").contains("Anmelden").should("exist").click();
  });
});
