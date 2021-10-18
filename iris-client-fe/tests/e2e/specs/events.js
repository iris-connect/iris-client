import dayjs from "dayjs";

describe("Events", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
    cy.login();
  });
  afterEach(() => {
    cy.logout();
  });
  it("should display the new event link, navigate to the event creation page and cancel the event creation", () => {
    cy.visit("/events/list");
    cy.getBy("event-list.link.event-create")
      .should("have.attr", "href", "/events/new")
      .click();
    cy.location("pathname").should("equal", "/events/new");
    cy.get("form").within(() => {
      cy.getBy("button{cancel}").should("exist").click();
    });
    cy.location("pathname").should("equal", "/events/list");
  });
  it("should validate and open the location select dialog, select a location and cancel selection", () => {
    cy.visit("/events/new");
    cy.location("pathname").should("equal", "/events/new");
    cy.getBy("location-select.dialog").should("not.exist");
    cy.getBy("location-info").should("not.exist");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.getBy("button{submit}").click();
        cy.assertInputInvalid(
          "location-select.dialog.activator",
          "Bitte wählen Sie einen Ereignisort aus"
        )
          .should("contain", "Ereignisort auswählen")
          .click();
      });
    cy.getBy("location-select.dialog")
      .should("be.visible")
      .within(() => {
        cy.getBy("search.input").type("iri");
        cy.getBy("search.button").should("be.disabled");
        cy.getBy("search.input").type("s");
        cy.getBy("search.button").should("be.enabled").click();
        cy.get(".v-data-table")
          .contains("IRIS connect Demo")
          .should("exist")
          .closest("tr")
          .within(() => {
            cy.getBy("select.button").click();
          });
      });
    cy.getBy("location-select.dialog").should("not.be.visible");
    cy.getBy("location-info")
      .should("exist")
      .and("contain.text", "IRIS connect Demo");
    cy.getBy("location-select.dialog.activator")
      .assertInputValid()
      .should("contain", "Ereignisort ändern")
      .click();
    cy.getBy("location-select.dialog")
      .should("be.visible")
      .within(() => {
        cy.get(".v-data-table").contains("IRIS connect Demo").should("exist");
        cy.getBy("cancel").should("exist").click();
      });
    cy.getBy("location-select.dialog").should("not.be.visible");
  });
  it("should validate and auto-fill the event creation form", () => {
    cy.visit("/events/new");
    cy.location("pathname").should("equal", "/events/new");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.getBy("button{submit}").click();
        cy.assertInputInvalid("input{externalId}", "Pflichtfeld");
        cy.assertInputValid("input{name}");
        cy.assertInputValid("textarea{requestDetails}");
        cy.assertInputInvalid(
          "location-select.dialog.activator",
          "Bitte wählen Sie einen Ereignisort aus"
        );
        cy.getBy("start")
          .assertInputInvalid(
            "Bitte geben Sie einen Zeitpunkt in der Vergangenheit an"
          )
          .within(() => {
            cy.getBy("date-input-field")
              .assertInputInvalid("Bitte geben Sie ein Datum an")
              .type("1234")
              .assertInputInvalid(
                "Bitte geben Sie ein Datum im Format DD.MM.YYYY ein"
              )
              .clear()
              .type(dayjs().subtract(1, "day").format("DD.MM.YYYY"))
              .assertInputValid();
            cy.getBy("time-input-field")
              .assertInputInvalid("Bitte geben Sie eine Uhrzeit an")
              .type("1234")
              .assertInputInvalid(
                "Bitte geben Sie eine Uhrzeit im Format HH:mm an"
              )
              .clear()
              .type(dayjs().format("HH:mm"))
              .assertInputValid();
          });
        cy.getBy("start")
          .assertInputValid()
          .within(() => {
            cy.getBy("date-input-field")
              .clear()
              .type(dayjs().add(1, "day").format("DD.MM.YYYY"));
          });
        cy.getBy("start")
          .assertInputInvalid(
            "Bitte geben Sie einen Zeitpunkt in der Vergangenheit an"
          )
          .within(() => {
            cy.getBy("date-input-field")
              .clear()
              .type(dayjs().subtract(1, "day").format("DD.MM.YYYY"));
          });
        cy.getBy("end").within(() => {
          cy.getBy("date-input-field").should(
            "have.value",
            dayjs().subtract(1, "day").format("DD.MM.YYYY")
          );
          cy.getBy("time-input-field")
            .should("have.value", "23:59")
            .assertInputValid()
            .clear()
            .assertInputInvalid("Bitte geben Sie eine Uhrzeit an")
            .type("1234")
            .assertInputInvalid(
              "Bitte geben Sie eine Uhrzeit im Format HH:mm an"
            )
            .clear()
            .type("23:59");
          cy.getBy("date-input-field")
            .clear()
            .type(dayjs().subtract(2, "day").format("DD.MM.YYYY"));
        });
        cy.getBy("end").assertInputInvalid(
          "Bitte geben Sie einen Zeitpunkt an, der nach dem Beginn liegt"
        );
      });
  });
  it("should create a new event", () => {
    cy.visit("/events/list");
    cy.visit("/events/new");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.getBy("input{externalId}").type("e2e_test_" + dayjs().valueOf());
        cy.getBy("input{name}").type("E2E Test");
        cy.getBy("textarea{requestDetails}").type("E2E Test Details");
        cy.getBy("start").within(() => {
          cy.getBy("date-input-field").type(
            dayjs().subtract(1, "day").format("DD.MM.YYYY")
          );
          cy.getBy("time-input-field").type(dayjs().format("HH:mm"));
        });
        cy.getBy("location-select.dialog.activator").click();
      });
    cy.getBy("location-select.dialog")
      .should("be.visible")
      .within(() => {
        cy.getBy("search.input").type("iris");
        cy.getBy("search.button").click();
        cy.get(".v-data-table")
          .contains("IRIS connect Demo")
          .should("exist")
          .closest("tr")
          .within(() => {
            cy.getBy("select.button").click();
          });
      });
    cy.get("form").within(() => {
      cy.getBy("button{submit}").click();
    });
    cy.location("pathname").should("contain", "/events/details");
  });
});
