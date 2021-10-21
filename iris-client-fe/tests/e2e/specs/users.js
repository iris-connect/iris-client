// https://docs.cypress.io/api/introduction/api.html

import _random from "lodash/random";
import _sampleSize from "lodash/sampleSize";
import _shuffle from "lodash/shuffle";
import _flatten from "lodash/flatten";
import _escapeRegExp from "lodash/escapeRegExp";

const generateRandomUserName = (key = "") => {
  const chars =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".split("");
  return "E2E" + key + _shuffle(_sampleSize(chars, _random(10, 15))).join("");
};

const generateRandomPassword = () => {
  const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split(
    ""
  );
  const numbers = "0123456789".split("");
  const specialChars = "_-#()@§!".split("");
  const source = [
    [chars, _random(8, 16)],
    [numbers, _random(2, 8)],
    [specialChars, _random(1, 4)],
  ];
  const samples = source.map((s) => _sampleSize(...s));
  // prepend a alphanumeric character as special chars are not allowed as first character
  return _sampleSize(chars) + _shuffle(_flatten(samples)).join("");
};

describe("Users", () => {
  const user = {
    firstName: "Test",
    lastName: "User",
    role: "Nutzung",
    userName: generateRandomUserName("User"),
    password: generateRandomPassword(),
  };
  const userAccessor = Cypress.env("MOCK_SERVER")
    ? "E2ETestUser"
    : user.userName;
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
  });
  afterEach(() => {
    cy.logout();
  });
  it("should display the user create link, navigate to the user creation page and cancel the user creation", () => {
    cy.login();
    cy.fetchUser();
    cy.visit("/admin/user/list");
    cy.getBy("admin-user-list.link.user-create")
      .should("have.attr", "href", "/admin/user/create")
      .click();
    cy.location("pathname").should("equal", "/admin/user/create");
    cy.get("form").within(() => {
      cy.getBy(".v-btn{cancel}").should("exist").click();
    });
    cy.location("pathname").should("equal", "/admin/user/list");
  });
  it("should validate the user creation form", () => {
    cy.login();
    cy.fetchUser();
    cy.visit("/admin/user/create");
    cy.get("form").within(() => {
      cy.getBy(".v-btn{submit}").should("exist").click();
      cy.assertInputValid("input{firstName}")
        .type("-")
        .assertInputInvalidByRule("sanitised");
      cy.assertInputValid("input{lastName}")
        .type("-")
        .assertInputInvalidByRule("sanitised");
      cy.assertInputValid("input{role}")
        .closest(".v-select__selections")
        .should("contain", "Nutzung");
      cy.assertInputInvalidByRule("input{userName}")
        .type("-")
        .assertInputInvalidByRule("sanitised")
        .clear()
        .type(generateRandomUserName(), { log: false })
        .assertInputValid();
      cy.assertInputInvalidByRule("input{password}")
        .type("p")
        .assertInputInvalidByRule("password")
        .clear()
        .type(generateRandomPassword(), { log: false })
        .assertInputValid();
    });
  });
  it("should create a new user", () => {
    cy.login();
    cy.fetchUser();
    cy.visit("/admin/user/create");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.getBy("input{firstName}")
          .should("exist")
          .type(user.firstName, { log: false });
        cy.getBy("input{lastName}")
          .should("exist")
          .type(user.lastName, { log: false });
        cy.getBy("input{userName}")
          .should("exist")
          .type(user.userName, { log: false });
        cy.selectInputValue("input{role}", ".select-menu-role", "Nutzung");
        cy.getBy("input{password}")
          .should("exist")
          .type(user.password, { log: false });
        cy.getBy(".v-btn{submit}").should("exist").click();
      });
    cy.location("pathname").should("equal", "/admin/user/list");
    cy.getBy("input{search}")
      .should("exist")
      .type(user.userName, { log: false });
    cy.get(".v-data-table").should("contain", user.userName, { log: false });
  });
  it("should edit a user, revert changes and set a new password", () => {
    const editText = "e2e.input.edit";
    const editMatch = new RegExp(`${_escapeRegExp(editText)}$`);
    cy.login();
    cy.fetchUser();
    cy.visit("/admin/user/list");
    cy.visitUserByAccessor(userAccessor);
    cy.get("form")
      .should("not.have.class", "is-loading")
      .within(() => {
        cy.editInputField("input{firstName}", { text: editText });
        cy.editInputField("input{lastName}", { text: editText });
        cy.editInputField("input{userName}", { text: editText });
        cy.selectInputValue(
          "input{role}",
          ".select-menu-role",
          "Administration"
        );
        cy.getBy(".v-btn{submit}").click();
      });
    cy.location("pathname").should("equal", "/admin/user/list");
    cy.visitUserByAccessor(userAccessor + editText);
    cy.get("form")
      .should("not.have.class", "is-loading")
      .within(() => {
        cy.getBy("input{firstName}").invoke("val").should("match", editMatch);
        cy.getBy("input{lastName}").invoke("val").should("match", editMatch);
        cy.getBy("input{userName}").invoke("val").should("match", editMatch);
        cy.getBy("input{role}")
          .closest(".v-select__selections")
          .should("contain", "Administration");
        cy.getBy("input{password}").should("not.have.value").assertInputValid();
        cy.editInputField("input{firstName}", {
          text: editText,
          action: "remove",
        });
        cy.editInputField("input{lastName}", {
          text: editText,
          action: "remove",
        });
        cy.editInputField("input{userName}", {
          text: editText,
          action: "remove",
        });
        cy.selectInputValue("input{role}", ".select-menu-role", "Nutzung");
        cy.getBy("input{password}")
          .type(generateRandomPassword(), { log: false })
          .assertInputValid();
        cy.getBy(".v-btn{submit}").click();
      });
    cy.location("pathname").should("equal", "/admin/user/list");
  });
  it("as admin: should navigate to admin-user-list page", () => {
    cy.login();
    cy.fetchUser();
    cy.getBy("user-menu.activator").click();
    cy.getBy("user-menu")
      .should("exist")
      .within(() => {
        cy.getBy("user-menu.item.user-profile").should("not.exist");
        cy.getBy("user-menu.item.admin-user-list").should("exist").click();
      });
    cy.location("pathname").should("equal", "/admin/user/list");
  });
  it("as user: should navigate to user profile page and ensure that protected input fields are disabled", () => {
    const credentials = {
      userName: Cypress.env("MOCK_SERVER") ? "user" : user.userName,
      password: user.password,
    };
    cy.login(credentials);
    cy.fetchUser();
    cy.getBy("user-menu.activator").should("exist").click();
    cy.getBy("user-menu")
      .should("exist")
      .within(() => {
        cy.getBy("user-menu.item.admin-user-list").should("not.exist");
        cy.getBy("user-menu.item.user-profile").should("exist").click();
      });
    cy.getApp().then((app) => {
      cy.location("pathname").should(
        "equal",
        "/admin/user/edit/" + app.$store.state.userLogin.user?.id
      );
      cy.get("form").within(() => {
        cy.getBy("input{userName}").should("be.disabled");
        cy.getBy("input{role}").should("be.disabled");
      });
      cy.visit("/admin/user/list");
      cy.location("pathname").should("not.equal", "/admin/user/list");
    });
  });
  it("should delete a user", () => {
    cy.login();
    cy.fetchUser();
    cy.visit("/admin/user/list");
    cy.getDataTableRow(userAccessor, "admin-user-list.data-table").within(
      () => {
        cy.getBy(".v-btn{delete}").click();
      }
    );
    cy.getBy("user-delete.confirm-dialog")
      .should("exist")
      .within(() => {
        cy.get(".v-card__title").should(
          "contain",
          `Konto ${userAccessor} löschen?`
        );
        cy.getBy(".v-btn{confirm}").click();
      });
    cy.getBy("user-delete.confirm-dialog").should("not.be.visible");
    cy.getBy("input{search}").clear().type(userAccessor);
    cy.getBy("admin-user-list.data-table").should("not.contain", userAccessor);
  });
});
