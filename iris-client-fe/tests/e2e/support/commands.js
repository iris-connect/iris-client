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

Cypress.Commands.add("getBy", (selector, options) => {
  let alias = selector.replace(/{(\S+)}/g, '[data-test="$1"]');
  if (!/data-test/.test(alias)) {
    alias = `[data-test="${alias}"]`;
  }
  return cy.get(alias, options);
});

Cypress.Commands.add("getByLike", (selector, options) => {
  let alias = selector.replace(/{(\S+)}/g, '[data-test*="$1"]');
  if (!/data-test/.test(alias)) {
    alias = `[data-test*="${alias}"]`;
  }
  return cy.get(alias, options);
});

Cypress.Commands.add("getApp", () => {
  cy.window({ log: false })
    .its("irisApp", { log: false })
    .then((app) => {
      return cy.wrap(app);
    });
});

Cypress.Commands.add("logout", () => {
  cy.getApp().then((app) => {
    cy.log("logout user");
    return app.$store.dispatch("userLogin/logout");
  });
  cy.clearLocalStorage();
});

Cypress.Commands.add("login", () => {
  cy.getApp().then((app) => {
    cy.log("authenticate user");
    return app.$store.dispatch("userLogin/authenticate", {
      userName: Cypress.env("auth_username"),
      password: Cypress.env("auth_password"),
    });
  });
  cy.visit("/");
});

Cypress.Commands.add("fetchUser", () => {
  cy.getApp().then((app) => {
    cy.log("fetch authenticated user");
    return app.$store.dispatch("userLogin/fetchAuthenticatedUser");
  });
});

Cypress.Commands.add("assertInputValid", (selector) => {
  const inputField = cy.getBy(selector).closest(".v-input");
  inputField
    .should("not.have.class", "error--text")
    .and("not.have.descendants", ".error--text");
});

Cypress.Commands.add("assertInputInvalid", (selector, message) => {
  const inputField = cy.getBy(selector).closest(".v-input");
  inputField
    .should("have.class", "error--text")
    .and("have.descendants", ".error--text");
  if (message) {
    inputField.should("contain", message);
  }
});

Cypress.Commands.add("loginUsingUi", (username, password) => {
  cy.location("pathname").should("equal", "/user/login");
  cy.get("form")
    .should("exist")
    .within(() => {
      cy.getBy("input{userName}")
        .type(username, { log: false })
        .should((el$) => {
          if (el$.val() !== username) {
            throw new Error("Different value of typed username");
          }
        });
      cy.getBy("input{password}")
        .type(password, { log: false })
        .should((el$) => {
          if (el$.val() !== password) {
            throw new Error("Different value of typed password");
          }
        });
      cy.getBy("button{submit}").should("exist").click();
    });
});
