import dayjs from "./../../../src/utils/date";

describe("Events", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
    cy.login();
  });
  afterEach(() => {
    cy.logout();
  });
  it("should filter the event list based on event status", () => {
    cy.visit("/events/list");
    cy.filterEventsByStatus("requested");
    cy.filterEventsByStatus("received");
    cy.filterEventsByStatus("closed");
    cy.filterEventsByStatus("aborted");
    cy.filterEventsByStatus("all");
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
          .first()
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
    const event = {
      externalId: "e2e_test " + dayjs().valueOf(),
      name: "E2E Test",
      requestDetails: "Bitte ignorieren Sie diese Anfrage.",
      start: dayjs().subtract(1, "day"),
      end: dayjs().subtract(1, "day").endOf("day"),
      location: "IRIS connect Demo",
    };
    cy.visit("/events/new");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.getBy("input{externalId}").type(event.externalId);
        cy.getBy("input{name}").type(event.name);
        cy.getBy("textarea{requestDetails}").type(event.requestDetails);
        cy.getBy("start").within(() => {
          cy.getBy("date-input-field").type(event.start.format("DD.MM.YYYY"));
          cy.getBy("time-input-field").type(event.start.format("HH:mm"));
        });
        cy.getBy("location-select.dialog.activator").click();
      });
    cy.getBy("location-select.dialog")
      .should("be.visible")
      .within(() => {
        cy.getBy("search.input").type("iris");
        cy.getBy("search.button").click();
        cy.get(".v-data-table")
          .contains(event.location)
          .first()
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
    cy.getBy("editable-field.externalRequestId").should(
      "contain",
      event.externalId
    );
    cy.getBy("editable-field.name").should("contain", event.name);
    cy.getBy("event.duration")
      .should("contain", event.start.format("LLL"))
      .and("contain", event.end.format("LLL"));
    cy.getBy("event.location").should("contain", event.location);
    cy.getBy("event.requestDetails").should("contain", event.requestDetails);
  });
  it("event status: requested: should trigger the cancel dialog", () => {
    cy.visitEventByStatus("requested");
    cy.getBy("event.status")
      .should("contain", "Angefragt")
      .within(() => {
        cy.getBy("button{event.cancel}").should("exist").click();
      });
    cy.getBy("confirm.dialog")
      .should("exist")
      .and("contain", "Anfrage abbrechen")
      .within(() => {
        cy.getBy("button{cancel}").should("exist").click();
      });
    cy.getBy("confirm.dialog").should("not.be.visible");
  });
  it("event status: received: should mark and unmark as edited / closed", () => {
    cy.visitEventByStatus("received");
    cy.getBy("event.status")
      .should("contain", "Geliefert")
      .within(() => {
        cy.getBy("button{event.close}").should("exist").click();
      });
    cy.getBy("event.status")
      .should("contain", "Bearbeitet")
      .within(() => {
        cy.getBy("button{event.resume}").should("exist").click();
      });
    cy.getBy("event.status")
      .should("contain", "Geliefert")
      .within(() => {
        cy.getBy("button{event.close}").should("exist");
      });
  });
  it("event status: closed: should mark and unmark as edited / closed", () => {
    cy.visitEventByStatus("closed");
    cy.getBy("event.status")
      .should("contain", "Bearbeitet")
      .within(() => {
        cy.getBy("button{event.resume}").should("exist").click();
      });
    cy.getBy("event.status")
      .should("contain", "Geliefert")
      .within(() => {
        cy.getBy("button{event.close}").should("exist").click();
      });
    cy.getBy("event.status")
      .should("contain", "Bearbeitet")
      .within(() => {
        cy.getBy("button{event.resume}").should("exist");
      });
  });
  it("should edit an existing event", () => {
    cy.visit("/events/list");
    cy.getBy("event-list.data-table")
      .contains("e2e_test")
      .first()
      .closest("tr")
      .within(() => {
        cy.getBy("select.button").click();
      });
    cy.location("pathname").should("contain", "/events/details");
    cy.checkEditableField("editable-field.externalRequestId");
    cy.checkEditableField("editable-field.name");
    cy.checkEditableField("editable-field.comment", {
      field: "textarea",
      validation: ["sanitised"],
    });
  });
  it("should export event data as csv file", () => {
    cy.visitEventByStatus("received");
    cy.getBy("button{export.standard}").should("be.disabled");
    cy.getBy("button{export-dialog.activator}").should("be.disabled");
    cy.getBy("event-details.contacts.data-table")
      .should("exist")
      .within(() => {
        cy.get("tbody tr").should("have.length.at.least", 2);
        cy.get("thead .v-simple-checkbox").click();
      });
    cy.getBy("export.standard").should("not.be.disabled").click();
    cy.getBy("export-dialog").should("not.exist");
    cy.getBy("button{export-dialog.activator}")
      .should("not.be.disabled")
      .click();
    cy.getBy("export-dialog")
      .should("be.visible")
      .within(() => {
        cy.getBy("export.standard").should("exist").click();
        cy.getBy("export.standard-alternative").should("exist").click();
        cy.getBy("export.sormas-participants").should("exist").click();
        cy.getBy("export.sormas-contact-person").should("exist").click();
        cy.getBy("button{close}").should("exist").click();
      });
    cy.getBy("export-dialog").should("not.exist");
  });
});
