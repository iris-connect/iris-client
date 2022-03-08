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

import _escapeRegExp from "lodash/escapeRegExp";
import dayjs from "../../../src/utils/date";

const validationRules = {
  defined: "Pflichtfeld",
  minLength: /Mindestens \d+ Zeichen/,
  maxLength: /Höchstens \d+ Zeichen/,
  password: "Bitte geben Sie ein gültiges Passwort an",
  sanitised: "Aus Sicherheitsgründen ist",
  nameConventions:
    "Aus Sicherheitsgründen ist die Verwendung von Spezialcharakter in Namen eingeschränkt.",
  date: "Bitte geben Sie ein Datum an",
  dateFormat: "Bitte geben Sie ein Datum im Format DD.MM.YYYY ein",
  time: "Bitte geben Sie eine Uhrzeit an",
  timeFormat: "Bitte geben Sie eine Uhrzeit im Format HH:mm an",
  dateStart: "Bitte geben Sie einen Zeitpunkt in der Vergangenheit an",
  dateEnd: "Bitte geben Sie einen Zeitpunkt an, der nach dem Beginn liegt",
  location: "Bitte wählen Sie einen Ereignisort aus",
};

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

Cypress.Commands.add("login", (credentials) => {
  cy.getApp().then((app) => {
    cy.log("authenticate user");
    return app.$store.dispatch("userLogin/authenticate", {
      userName: credentials?.userName ?? Cypress.env("auth_username"),
      password: credentials?.password ?? Cypress.env("auth_password"),
    });
  });
  cy.visit("/");
});

Cypress.Commands.add(
  "filterDataTableByStatus",
  { prevSubject: "optional" },
  (subject, arg1, arg2) => {
    const status = subject ? arg1 : arg2;
    if (subject) {
      cy.wrap(subject).as("dataTable");
    } else {
      cy.getBy(arg1).as("dataTable");
    }
    // wait for initial loading
    cy.get("@dataTable").should("not.have.class", "is-loading");
    // apply filter
    cy.getBy(`status.select.${status}`).click();
    // wait for filtered loading
    cy.get("@dataTable").should("not.have.class", "is-loading");
    cy.get("@dataTable").then(($table) => {
      if ($table.hasClass("is-empty")) {
        cy.log(`data-table has no items with status '${status}'`);
      } else {
        cy.get("@dataTable").within(() => {
          cy.get("tbody tr")
            .not(".v-row-group__header")
            .each(($item) => {
              cy.wrap($item).within(() => {
                if (status === "all") {
                  cy.getByLike("status.").should("exist");
                } else {
                  cy.getByLike("status.").should(
                    "have.attr",
                    "data-test",
                    `status.${status}`
                  );
                }
              });
            });
        });
      }
    });
  }
);

Cypress.Commands.add(
  "visitByStatus",
  { prevSubject: "optional" },
  (subject, arg1, arg2) => {
    const status = subject ? arg1 : arg2;
    if (subject) {
      cy.wrap(subject).as("dataTable");
    } else {
      cy.getBy(arg1).as("dataTable");
    }
    cy.get("@dataTable").within(() => {
      cy.getBy("status." + status)
        .should("exist")
        .first()
        .closest("tr")
        .within(() => {
          cy.getBy(".v-btn{select}").click();
        });
    });
  }
);

Cypress.Commands.add(
  "checkTooltip",
  { prevSubject: "optional" },
  (subject, arg1, arg2) => {
    const tooltip = subject ? arg1 : arg2;
    if (subject) {
      cy.wrap(subject).as("activator");
    } else {
      cy.getBy(arg1).as("activator");
    }
    cy.get("@activator").trigger("mouseenter");
    cy.root().closest("#app").find(tooltip).should("be.visible");
    cy.get("@activator").trigger("mouseleave");
    cy.root().closest("#app").find(tooltip).should("not.be.visible");
    return cy.get("@activator");
  }
);

Cypress.Commands.add(
  "getDataTableRow",
  { prevSubject: "optional" },
  (subject, arg1, arg2, arg3) => {
    const accessor = subject ? arg1 : arg2;
    const search = subject ? arg2 : arg3;
    if (subject) {
      cy.wrap(subject).as("dataTable");
    } else {
      cy.getBy(arg1).as("dataTable");
    }
    cy.get("@dataTable").should("not.have.class", "is-loading");
    if (search !== false) {
      cy.getBy("input{search}")
        .should("exist")
        .clear()
        .type(accessor, { log: false });
    }
    cy.get("@dataTable")
      .should("not.have.class", "is-loading")
      .contains(accessor, { log: false })
      .closest("tr")
      .first();
  }
);

Cypress.Commands.add(
  "findDataTableRow",
  { prevSubject: "optional" },
  (subject, arg1, arg2, arg3) => {
    const column = subject ? arg1 : arg2;
    const content = subject ? arg2 : arg3;
    if (subject) {
      cy.wrap(subject).as("dataTable");
    } else {
      cy.getBy(arg1).as("dataTable");
    }
    cy.wrap(null).as("tableRow");
    cy.get("@dataTable").should("not.have.class", "is-loading");
    cy.get("@dataTable").then(($table) => {
      if ($table.hasClass("is-empty")) {
        cy.log("data-table has no items");
      } else {
        cy.get("@dataTable").within(() => {
          cy.get(`tbody tr td:nth-child(${column})`).each(($cell) => {
            let isMatch = false;
            if (typeof content === "string" && content.startsWith(".")) {
              if ($cell.find(content).length > 0) {
                isMatch = true;
              }
            } else {
              if ($cell.text().match(content) !== null) {
                isMatch = true;
              }
            }
            if (isMatch) {
              cy.wrap($cell).closest("tr").should("exist").as("tableRow");
              return false;
            }
          });
        });
      }
    });
    return cy.get("@tableRow");
  }
);

Cypress.Commands.add(
  "sortDataTable",
  { prevSubject: "optional" },
  (subject, arg1, arg2, arg3) => {
    const column = subject ? arg1 : arg2;
    const sortDir = subject ? arg2 : arg3;
    if (subject) {
      cy.wrap(subject).as("dataTable");
    } else {
      cy.getBy(arg1).as("dataTable");
    }
    cy.get("@dataTable").should("not.have.class", "is-loading");
    cy.get("@dataTable").within(() => {
      cy.get(`thead tr th:nth-child(${column})`)
        .should("have.class", "sortable")
        .as("tableSort")
        .then(() => {
          for (let i = 0; i < 3; i++) {
            cy.get("@tableSort").then(($tableSort) => {
              if (!$tableSort.hasClass(sortDir)) {
                cy.get("@tableSort").click();
              }
            });
          }
          cy.get("@tableSort").should("have.class", sortDir);
        });
    });
  }
);

Cypress.Commands.add(
  "visitByDataTableCellValue",
  { prevSubject: "optional" },
  (subject, arg1, arg2, arg3) => {
    const column = subject ? arg1 : arg2;
    const content = subject ? arg2 : arg3;
    cy.wrap(false).as("rowExists");
    if (subject) {
      cy.wrap(subject).as("dataTable");
    } else {
      cy.getBy(arg1).as("dataTable");
    }
    cy.get("@dataTable")
      .findDataTableRow(column, content)
      .then(($row) => {
        if ($row) {
          cy.wrap(true).as("rowExists");
          cy.wrap($row).click();
        } else {
          cy.log("data-table has no matching items");
        }
      });
    return cy.get("@rowExists");
  }
);

Cypress.Commands.add("visitUserByAccessor", (accessor) => {
  cy.location("pathname").should("equal", "/admin/user/list");
  cy.getDataTableRow("view.data-table", accessor).within(() => {
    cy.getBy(".v-btn{edit}")
      .within(() => {
        cy.root()
          .should("have.attr", "href")
          .and("match", /\/admin\/user\/edit\/\w+/);
      })
      .click();
  });
  cy.location("pathname").should("contain", "/admin/user/edit");
});

Cypress.Commands.add("getRootMessageFolder", (context) => {
  cy.location("pathname").should("equal", "/iris-messages/list");
  cy.getBy("message-folders-data-tree").should("exist");
  return cy
    .getBy(`{message-folders-data-tree} {select.${context}}`)
    .should("exist")
    .first();
});

Cypress.Commands.add("getMessageDataTableRow", (accessor, context) => {
  cy.location("pathname").should("equal", "/iris-messages/list");
  cy.getRootMessageFolder(context).click();
  cy.getDataTableRow("view.data-table", accessor).should("exist");
});

Cypress.Commands.add("visitMessageDetailsWithAttachments", () => {
  const gteOne = /^[1-9][0-9]*$/;
  cy.visit("/iris-messages/list");
  cy.findDataTableRow("view.data-table", 1, gteOne).should("exist").click();
  cy.location("pathname").should("contain", "/iris-messages/details");
});

Cypress.Commands.add("getMessageDataAttachmentItem", (imported) => {
  if (imported === true) {
    cy.getBy("{message-data.list-item}.is-imported").should("exist").first();
  } else if (imported === false) {
    cy.getBy("message-data.list-item")
      .not(".is-imported")
      .should("exist")
      .first();
  } else {
    cy.getBy("message-data.list-item").should("exist").first();
  }
});

Cypress.Commands.add(
  "selectOwnIrisMessageContact",
  { prevSubject: "optional" },
  (subject, arg1, arg2) => {
    // arg1 = selector, arg2 = menu
    const menu = subject ? arg1 : arg2;
    if (subject) {
      cy.wrap(subject).as("field");
    } else {
      cy.getBy(arg1).as("field");
    }
    cy.get("@field").closest(".v-input").should("not.have.class", "is-empty");
    cy.getApp().then((app) => {
      const contacts = app.$store.state.e2eTests.irisMessageHdContacts;
      const ownContact = contacts.find((c) => c.own === true);
      cy.wrap(ownContact).should("exist").should("not.be.empty");
      cy.get("@field").selectAutocompleteValue(menu, ownContact.name);
    });
    return cy.get("@field");
  }
);

Cypress.Commands.add(
  "selectAutocompleteValue",
  { prevSubject: "optional" },
  (subject, arg1, arg2, arg3) => {
    // arg1 = selector, arg2 = menu, arg3 = value
    const menu = subject ? arg1 : arg2;
    const value = subject ? arg2 : arg3;
    if (subject) {
      cy.wrap(subject).as("field");
    } else {
      cy.getBy(arg1).as("field");
    }
    cy.get("@field")
      .type(value)
      .closest(".v-input")
      .click()
      .closest("#app")
      .find(menu)
      .should("exist")
      .within(() => {
        cy.contains(value).as("value").click();
      });
    cy.get("@field").assertInputValid().should("have.value", value);
    return cy.get("@field");
  }
);

Cypress.Commands.add(
  "selectFieldValue",
  { prevSubject: "optional" },
  (subject, arg1, arg2, arg3) => {
    // arg1 = selector, arg2 = menu, arg3 = value
    const menu = subject ? arg1 : arg2;
    const value = subject ? arg2 : arg3;
    if (subject) {
      cy.wrap(subject).as("field");
    } else {
      cy.getBy(arg1).as("field");
    }
    cy.get("@field")
      .closest(".v-input")
      .click()
      .closest("#app")
      .find(menu)
      .should("exist")
      .contains(value)
      .click();
    cy.get("@field")
      .assertInputValid()
      .closest(".v-select__selections")
      .should("contain", value);
    return cy.get("@field");
  }
);

Cypress.Commands.add("editInputField", (selector, config) => {
  const validation = config?.validation ?? ["sanitised"];
  const action = config?.action ?? "add";
  const text = config?.text ?? "e2e.input";
  cy.getBy(selector)
    .as("field")
    .should("exist")
    .invoke("val")
    .then((value) => {
      cy.get("@field").clear().should("be.empty");
      if (validation.indexOf("defined") > -1) {
        cy.get("@field").assertInputInvalidByRule("defined");
      } else {
        cy.get("@field").assertInputValid();
      }
      if (validation.indexOf("sanitised") > -1) {
        cy.get("@field")
          .type("-")
          .assertInputInvalidByRule("sanitised")
          .clear();
      }
      if (action === "add") {
        cy.get("@field").type((value || "") + text, { log: false });
      } else if (action === "remove") {
        const textMatch = new RegExp(`${_escapeRegExp(text)}$`);
        cy.get("@field").type((value || "").replace(textMatch, ""), {
          log: false,
        });
      }
      cy.get("@field").assertInputValid();
    });
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
            cy.get(field).assertInputInvalidByRule("defined");
          } else {
            cy.get(field).assertInputValid();
          }
          // type an invalid character, check if validations works properly
          if (validation.indexOf("sanitised") > -1) {
            cy.get(field)
              .type("-")
              .assertInputInvalidByRule("sanitised")
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
  return cy.getBy(selector);
});

Cypress.Commands.add(
  "setDateTimeFieldValue",
  { prevSubject: "optional" },
  (subject, arg1, arg2) => {
    const date = subject ? arg1 : arg2;
    if (subject) {
      cy.wrap(subject).as("field");
    } else {
      cy.getBy(arg1).as("field");
    }
    cy.get("@field").within(() => {
      cy.getBy("date-input-field")
        .clear()
        .type(dayjs(date).format("DD.MM.YYYY"))
        .type("{esc}");
      cy.getBy("time-input-field")
        .clear()
        .type(dayjs(date).format("HH:mm"))
        .type("{esc}");
    });
  }
);

Cypress.Commands.add(
  "validateDateTimeField",
  { prevSubject: "optional" },
  (subject, arg1, arg2) => {
    const required = (subject ? arg1 : arg2) !== false;
    if (subject) {
      cy.wrap(subject).as("field");
    } else {
      cy.getBy(arg1).as("field");
    }
    cy.get("@field")
      .should("exist")
      .within(() => {
        cy.getBy("date-input-field")
          .as("date")
          .invoke("val")
          .then((value) => {
            cy.get("@date").clear();
            if (required) {
              cy.get("@date").assertInputInvalidByRule("date");
            } else {
              cy.get("@date").assertInputValid();
            }
            cy.get("@date")
              .type("1234")
              .type("{esc}")
              .assertInputInvalidByRule("dateFormat")
              .clear();
            if (value && typeof value === "string") {
              cy.get("@date").type(value);
            }
            cy.get("@date").type("{esc}");
          });
        cy.getBy("time-input-field")
          .as("time")
          .invoke("val")
          .then((value) => {
            cy.get("@time").clear();
            if (required) {
              cy.get("@time").assertInputInvalidByRule("time");
            } else {
              cy.get("@time").assertInputValid();
            }
            cy.get("@time")
              .type("1234")
              .type("{esc}")
              .assertInputInvalidByRule("timeFormat")
              .clear();
            if (value && typeof value === "string") {
              cy.get("@time").type(value);
            }
            cy.get("@time").type("{esc}");
          });
      });
  }
);

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
      cy.wrap(subject).as("input");
    } else {
      cy.getBy(selector).as("input");
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
  (subject, arg1, arg2) => {
    const message = subject ? arg1 : arg2;
    if (subject) {
      cy.wrap(subject).as("input");
    } else {
      cy.getBy(arg1).as("input");
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

Cypress.Commands.add(
  "assertInputInvalidByRule",
  { prevSubject: "optional" },
  (subject, arg1, arg2) => {
    const rule = subject ? arg1 : arg2;
    const message = validationRules[rule] || validationRules.defined;
    if (subject) {
      cy.wrap(subject).assertInputInvalid(message);
    } else {
      cy.assertInputInvalid(arg1, message);
    }
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

Cypress.Commands.add("changeOwnPassword", (credentials, password) => {
  cy.login(credentials);
  cy.fetchUser();
  cy.getApp().then((app) => {
    cy.visit("/admin/user/edit/" + app.$store.state.userLogin.user?.id);
    cy.get("form")
      .should("not.have.class", "is-loading")
      .within(() => {
        cy.getBy("input{oldPassword}").should("not.exist");
        cy.getBy("input{password}")
          .type(password, { log: false })
          .assertInputValid();
        cy.getBy("input{oldPassword}").should("exist");
        cy.getBy(".v-btn{submit}").click();
        cy.getBy("input{oldPassword}")
          .should("exist")
          .assertInputInvalidByRule("defined")
          .type("p", { log: false })
          .assertInputValid();
        cy.getBy(".v-btn{submit}").click();
        cy.getBy("error.edit").should("exist");
        cy.getBy("input{oldPassword}")
          .clear()
          .type(credentials.password, { log: false })
          .assertInputValid();
        cy.getBy(".v-btn{submit}").click();
      });
    cy.location("pathname").should("not.contain", "/admin/user/edit/");
  });
});
