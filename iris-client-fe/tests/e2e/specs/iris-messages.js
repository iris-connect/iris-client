import _shuffle from "lodash/shuffle";
import _sampleSize from "lodash/sampleSize";
import _random from "lodash/random";

const generateRandomText = (key = "", size) => {
  const chars =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".split("");
  return "E2E" + key + _shuffle(_sampleSize(chars, size)).join("");
};

describe("IrisMessages", () => {
  const timestamp = new Date().getTime();
  const message = {
    subject: generateRandomText(`Subject_${timestamp}_`, _random(10, 15)),
    body: generateRandomText(`Body_${timestamp}_`, _random(10, 150)),
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
    cy.getRootMessageFolder("INBOX").should("have.class", "v-btn--active");
    cy.getRootMessageFolder("OUTBOX").should("not.have.class", "v-btn--active");
    cy.getRootMessageFolder("INBOX").click();
    cy.getBy("view.data-table")
      .should("exist")
      .get(".v-data-table-header")
      .should("contain", "Von")
      .should("not.contain", "An");
    cy.getRootMessageFolder("OUTBOX").click();
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
        cy.selectOwnIrisMessageContact(
          "input{hdRecipient}",
          ".select-menu-recipient"
        );
      });
  });
  it("should create a new message and display the message details", () => {
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
    cy.getMessageDataTableRow(message.subject, "OUTBOX")
      .should("not.have.class", "font-weight-bold")
      .click();
    cy.location("pathname").should("contain", "/iris-messages/details");
    cy.getBy("view.iris-message-details").should(
      "not.have.class",
      "v-card--loading"
    );
    cy.getBy("message.createdAt").should("not.be.empty");
    cy.getBy("message.author").should("not.be.empty");
    cy.getBy("message.recipient").should("not.be.empty");
    cy.getBy("message.subject").should("contain", message.subject);
    cy.getBy("message.body").should("contain", message.body);
  });
  it("should display the unread message count in a badge in the app-bar and and decrease it by opening an unread message", () => {
    cy.visit("/iris-messages/list");
    cy.getBy("app-bar.nav.link.iris-message-list")
      .as("navLink")
      .should("not.have.class", "is-loading");
    cy.getBy("{iris-messages.unread.count} .v-badge__badge")
      .as("badge")
      .should("be.visible")
      .invoke("text")
      .then((textContent) => {
        const count = parseInt(textContent);
        expect(count).to.be.gte(1);
        cy.getRootMessageFolder("INBOX").click();
        cy.get(".v-data-table tr.font-weight-bold").should("exist").click();
        cy.location("pathname").should("contain", "/iris-messages/details");
        cy.getBy("view.iris-message-details").should(
          "not.have.class",
          "v-card--loading"
        );
        cy.get("@navLink").should("not.have.class", "is-loading");
        if (count <= 1) {
          cy.get("@badge").should("not.be.visible");
        } else {
          cy.get("@badge").invoke("text").then(parseInt).should("be.lt", count);
        }
      });
  });
});
