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

Cypress.Commands.add("filterEventsByStatus", (status) => {
  cy.location("pathname").should("equal", "/events/list");
  // wait for initial loading
  cy.getBy("event-list.data-table").should("not.have.class", "is-loading");
  // apply filter
  cy.getBy(`event.status.select.${status}`).click();
  // wait for filtered loading
  cy.getBy("event-list.data-table").should("not.have.class", "is-loading");
  cy.getBy("event-list.data-table").then(($table) => {
    if ($table.hasClass("is-empty")) {
      cy.log(`list has no events with status '${status}'`);
    } else {
      cy.getBy("{event-list.data-table} tbody tr").each(($item) => {
        cy.wrap($item).within(() => {
          if (status === "all") {
            cy.getByLike("event.status.").should("exist");
          } else {
            cy.getByLike("event.status.").should(
              "have.attr",
              "data-test",
              `event.status.${status}`
            );
          }
        });
      });
    }
  });
});

Cypress.Commands.add("visitEventByStatus", (status) => {
  cy.getBy("event-list.data-table").within(() => {
    cy.getBy("event.status." + status)
      .should("exist")
      .first()
      .closest("tr")
      .within(() => {
        cy.getBy("select.button").click();
      });
  });
  cy.location("pathname").should("contain", "/events/details");
});

Cypress.Commands.add("checkEditableField", (selector, config) => {
  const field = config?.field ?? "input";
  const validation = config?.validation ?? ["defined", "sanitised"];
  cy.getBy(selector)
    .should("exist")
    .within(() => {
      // check display state of editable field (text, button)
      cy.get(".editable-button_text").should("exist");
      // trigger edit functionality
      cy.get(".editable-button").should("exist").click();
      // check if save button exists (checkmark)
      cy.get("button.mdi-check").should("exist");
      // check input field and access its value
      cy.get(field)
        .should("exist")
        .invoke("val")
        .then((value) => {
          const hasValue = typeof value === "string" && value.length > 0;
          // clear value and check if it is empty
          cy.get(field).clear().should("be.empty");
          // check if empty value validations works properly
          if (validation.indexOf("defined") > -1) {
            cy.get(field).assertInputInvalid("Pflichtfeld");
          } else {
            cy.get(field).assertInputValid();
          }
          // type an invalid character, check if validations works properly
          if (validation.indexOf("sanitised") > -1) {
            cy.get(field)
              .type("-")
              .assertInputInvalid(
                "Aus Sicherheitsgründen ist ein Spezialcharakter nicht als erstes Symbol erlaubt."
              )
              .clear();
          }
          if (hasValue) {
            // trigger undo and check if field has value if field was not empty
            cy.get("button.mdi-undo-variant").should("exist").click();
          } else {
            // trigger save if field was empty - should exit edit mode without submitting the value
            cy.get("button.mdi-check").click();
          }
          cy.root()
            .get(".editable-button_text")
            .invoke("text")
            .should("match", new RegExp(`${value}\\s*$`));
          // edit the field by appending the string: "e2e:editable" and check if new string exists after submitting
          cy.get(".editable-button").click();
          cy.get(field).type("e2e:editable");
          cy.get("button.mdi-check").click();
          cy.root()
            .get(".editable-button_text")
            .invoke("text")
            .should("match", new RegExp(`${value}e2e:editable\\s*$`));
          // edit the field by removing the appended string and check if original value is restored after submitting
          cy.get(".editable-button").click();
          cy.get(field).clear();
          if (hasValue) {
            cy.get(field).type(value);
          }
          cy.get("button.mdi-check").click();
          cy.root()
            .get(".editable-button_text")
            .invoke("text")
            .should("match", new RegExp(`${value}\\s*$`));
        });
    });
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
