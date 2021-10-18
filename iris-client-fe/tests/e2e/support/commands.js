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

Cypress.Commands.add(
  "checkEditableField",
  (selector, field = "input", isRequired) => {
    cy.getBy(selector)
      .should("exist")
      .within(() => {
        cy.get(".editable-button").should("exist").click();
        cy.get("button.mdi-check").should("exist");
        cy.get(field).should("exist").clear().should("be.empty");
        if (isRequired !== false) {
          cy.get(field).assertInputInvalid("Pflichtfeld");
        } else {
          cy.get(field).assertInputValid().type("test");
        }
        cy.get("button.mdi-undo-variant").should("exist").click();
        cy.get(field).should("not.be.empty");
        cy.get(".editable-button").should("exist").click();
        cy.get(field).type("e2e:editable");
        cy.get("button.mdi-check").click();
      });
    cy.getBy(selector).should("contain", "e2e:editable");
  });

Cypress.Commands.add("fetchUser", () => {
  cy.getApp().then((app) => {
    cy.log("fetch authenticated user");
    return app.$store.dispatch("userLogin/fetchAuthenticatedUser");
  });
});

Cypress.Commands.add(
  "assertInputValid",
  { prevSubject: "optional" },
  (subject, selector) => {
    if (subject) {
      cy.get(subject, { log: false }).as("input");
    } else {
      cy.getBy(selector, { log: false }).as("input");
    }
    cy.get("@input", { log: false })
      .closest(".v-input")
      .should("not.have.class", "error--text")
      .and("not.have.descendants", ".error--text");
    return cy.get("@input", { log: false });
  }
);

Cypress.Commands.add(
  "assertInputInvalid",
  { prevSubject: "optional" },
  (subject, selector, message) => {
    if (subject) {
      cy.get(subject, { log: false }).as("input");
    } else {
      cy.getBy(selector, { log: false }).as("input");
    }
    cy.get("@input", { log: false })
      .closest(".v-input")
      .within({ log: false }, () => {
        cy.root({ log: false })
          .should("have.class", "error--text")
          .and("have.descendants", ".error--text");
        if (message) {
          cy.root({ log: false }).should("contain", message);
        }
      });
    return cy.get("@input", { log: false });
  }
);

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
