import _shuffle from "lodash/shuffle";
import _sampleSize from "lodash/sampleSize";
import _random from "lodash/random";

// @todo: add tests for message details & message mark read & unread message count

const generateRandomText = (key = "", size) => {
  const chars =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".split("");
  return "E2E" + key + _shuffle(_sampleSize(chars, size)).join("");
};

describe("IrisMessages", () => {
  const message = {
    subject: generateRandomText("Subject", _random(10, 15)),
    body: generateRandomText("Body", _random(100, 150)),
  };
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
    cy.login();
  });
  afterEach(() => {
    cy.logout();
  });
  it("should display the message folders and change the context", () => {
    cy.visit("/iris-messages/list");
    cy.getBy("message-folders").within(() => {
      cy.getBy("select.INBOX")
        .should("exist")
        .should("have.class", "v-btn--active");
      cy.getBy("select.OUTBOX")
        .should("exist")
        .should("not.have.class", "v-btn--active");
    });
    cy.getBy("{message-folders} {select.INBOX}").click();
    cy.getBy("view.data-table")
      .should("exist")
      .get(".v-data-table-header")
      .should("contain", "Von")
      .should("not.contain", "An");
    cy.getBy("{message-folders} {select.OUTBOX}").click();
    cy.getBy("view.data-table")
      .should("exist")
      .get(".v-data-table-header")
      .should("contain", "An")
      .should("not.contain", "Von");
  });
  it("should display the create message link, navigate to the message creation page and cancel the message creation", () => {
    cy.visit("/iris-messages/list");
    cy.getBy("view.link.create")
      .should("have.attr", "href", "/iris-messages/create")
      .click();
    cy.location("pathname").should("equal", "/iris-messages/create");
    cy.get("form").within(() => {
      cy.getBy(".v-btn{cancel}").should("exist").click();
    });
    cy.location("pathname").should("equal", "/iris-messages/list");
  });
  it("should validate the message creation form", () => {
    cy.visit("/iris-messages/create");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.getBy(".v-btn{submit}").should("exist").click();
        cy.assertInputInvalidByRule("input{hdRecipient}");
        cy.assertInputInvalidByRule("input{subject}")
          .type("-")
          .assertInputInvalidByRule("sanitised");
        cy.assertInputInvalidByRule("textarea{body}")
          .type("-")
          .assertInputInvalidByRule("sanitised");
        cy.assertInputValid("input{attachments}");
        cy.selectOwnIrisMessageContact(
          "input{hdRecipient}",
          ".select-menu-recipient"
        );
      });
  });
  it("should create a new message", () => {
    cy.visit("/iris-messages/create");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.selectOwnIrisMessageContact(
          "input{hdRecipient}",
          ".select-menu-recipient"
        );
        cy.getBy("input{subject}").should("exist").type(message.subject);
        cy.getBy("textarea{body}").should("exist").type(message.body);
        cy.getBy(".v-btn{submit}").should("exist").click();
      });
    cy.location("pathname").should("equal", "/iris-messages/list");
    cy.getBy("{message-folders} {select.INBOX}").click();
    cy.get(".v-data-table").should("not.have.class", "is-loading");
    cy.getBy("input{search}").should("exist").type(message.subject);
    cy.get(".v-data-table")
      .should("not.have.class", "is-loading")
      .contains(message.subject)
      .should("exist")
      .closest("tr")
      .should("have.class", "font-weight-bold");
    cy.getBy("{message-folders} {select.OUTBOX}").click();
    cy.getBy("input{search}").should("exist").type(message.subject);
    cy.get(".v-data-table")
      .contains(message.subject)
      .should("exist")
      .closest("tr")
      .should("not.have.class", "font-weight-bold");
  });
});
