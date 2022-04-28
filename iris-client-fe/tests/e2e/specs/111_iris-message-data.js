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
  it("should validate the message data creation form dialog (for event-tracking)", () => {
    cy.visit("/iris-messages/create");
    cy.getBy("message-data.export-dialog.activator").should("exist").click();
    cy.getBy("message-data.export-form")
      .should("exist")
      .within(() => {
        cy.getBy(".v-btn{submit}").should("exist").click();
        cy.getBy("input{discriminator}").selectFieldValue(
          ".select-menu-message-data-discriminator",
          "Ereignis"
        );
        cy.assertInputInvalidByRule("input{description}")
          .type("-")
          .assertInputInvalidByRule("sanitised")
          .clear();
        cy.assertInputInvalidByRule(".stepper-input{payload.event}");
        cy.assertInputInvalidByRule(".stepper-input{payload.guests}");
        cy.getBy("{payload.event} .v-data-table")
          .should("exist")
          .should("not.have.class", "is-loading")
          .within(() => {
            cy.get("tbody tr.is-selectable")
              .should("have.length.at.least", 1)
              .first()
              .find(".v-simple-checkbox")
              .should("exist")
              .click();
          });
        cy.assertInputValid("input{description}");
      });
  });
  it("should create a new message with data attachment (for event-tracking)", () => {
    cy.visit("/iris-messages/create");
    cy.getBy("message-data.export-dialog.activator").should("exist").click();
    cy.getBy("message-data.export-form")
      .should("exist")
      .within(() => {
        cy.getBy("input{discriminator}").selectFieldValue(
          ".select-menu-message-data-discriminator",
          "Ereignis"
        );
        cy.getBy("{payload.event} .v-data-table")
          .should("exist")
          .should("not.have.class", "is-loading")
          .within(() => {
            cy.get("tbody tr.is-selectable")
              .should("have.length.at.least", 1)
              .first()
              .find(".v-simple-checkbox")
              .should("exist")
              .click();
          });
        cy.getBy("{payload.guests} .v-data-table")
          .should("exist")
          .should("not.have.class", "is-loading")
          .within(() => {
            cy.get("tbody tr")
              .should("have.length.at.least", 1)
              .first()
              .find(".v-simple-checkbox")
              .should("exist")
              .click();
          });
        cy.getBy(".v-btn{submit}").should("exist").click();
      });
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
  });
  it("should display the message details and data preview dialog", () => {
    cy.visitMessageDetailsWithAttachments();
    cy.getMessageDataAttachmentItem(null).within(() => {
      cy.getBy("message-data.preview").should("exist").click();
    });
    cy.getBy("message-data.preview.dialog").should("be.visible");
  });
  it("should start the import of the message data attachment, creating a new entry", () => {
    cy.visitMessageDetailsWithAttachments();
    cy.getMessageDataAttachmentItem(false).within(() => {
      cy.getBy("message-data.import.activator").should("exist").click();
    });
    cy.get(".menu-message-data-import")
      .should("be.visible")
      .within(() => {
        cy.getBy("message-data.import.add").should("exist").click();
      });
    cy.getBy("confirm-dialog").should("exist");
  });
  it("should import the message data attachment, updating an existing entry", () => {
    cy.visitMessageDetailsWithAttachments();
    cy.getMessageDataAttachmentItem(false).within(() => {
      cy.getBy("message-data.import.activator").should("exist").click();
    });
    cy.get(".menu-message-data-import")
      .should("be.visible")
      .within(() => {
        cy.getBy("message-data.import.update").should("exist").click();
      });
    cy.getBy("message-data.import-form")
      .should("exist")
      .within(() => {
        cy.getBy("{payload.event} .v-data-table")
          .should("exist")
          .should("not.have.class", "is-loading")
          .within(() => {
            cy.get("tbody tr.is-selectable")
              .should("have.length.at.least", 1)
              .first()
              .find(".v-simple-checkbox")
              .should("exist")
              .click();
          });
        cy.getBy("{payload.guests} .v-data-table")
          .should("be.visible")
          .should("not.have.class", "is-empty")
          .within(() => {
            cy.get("tbody tr")
              .should("have.length.at.least", 1)
              .first()
              .find(".v-simple-checkbox")
              .should("exist")
              .click();
          });
        cy.getBy(".v-btn{submit}").should("exist").click();
      });
    cy.getBy("confirm-dialog")
      .should("exist")
      .within(() => {
        cy.getBy("button{confirm}").click();
      });
    cy.getBy("message-data.import.success").should("be.visible");
    cy.getMessageDataAttachmentItem(true).within(() => {
      cy.getBy("message-data.import.activator").should("be.disabled");
    });
  });
});
