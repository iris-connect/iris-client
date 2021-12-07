import dayjs from "./../../../src/utils/date";

describe("Index Cases", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/?indexTracking=enabled");
    cy.login();
  });
  afterEach(() => {
    cy.logout();
  });
  it("should filter the index cases list based on status", () => {
    cy.visit("/cases/list");
    cy.getBy("view.data-table")
      .filterDataTableByStatus("requested")
      .filterDataTableByStatus("received")
      .filterDataTableByStatus("closed")
      .filterDataTableByStatus("all");
  });
  it("should display the new index-case link, navigate to the index-case creation page and cancel the index-case creation", () => {
    cy.visit("/cases/list");
    cy.getBy("view.link.create")
      .should("have.attr", "href", "/cases/new")
      .click();
    cy.location("pathname").should("equal", "/cases/new");
    cy.get("form").within(() => {
      cy.getBy(".v-btn{cancel}").should("exist").click();
    });
    cy.location("pathname").should("equal", "/cases/list");
  });
  it("should validate the index-case creation form", () => {
    cy.visit("/cases/new");
    cy.location("pathname").should("equal", "/cases/new");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.getBy(".v-btn{submit}").click();
        cy.getBy("input{externalId}")
          .assertInputInvalidByRule()
          .type("-")
          .assertInputInvalidByRule("sanitised");
        cy.getBy("input{name}")
          .assertInputValid()
          .type("-")
          .assertInputInvalidByRule("sanitised");
        cy.getBy("textarea{comment}")
          .assertInputValid()
          .type("-")
          .assertInputInvalidByRule("sanitised");
        cy.getBy("start")
          .assertInputInvalidByRule("defined")
          .validateDateTimeField()
          .setDateTimeFieldValue(dayjs())
          .assertInputValid();
        cy.getBy("end")
          .assertInputInvalidByRule("defined")
          .validateDateTimeField()
          .setDateTimeFieldValue(dayjs().subtract(1, "day"))
          .assertInputInvalidByRule("dateEnd")
          .setDateTimeFieldValue(dayjs().add(1, "day"))
          .assertInputValid();
      });
  });
  it("should create a new index-case", () => {
    const indexCase = {
      externalId: "e2e_test " + dayjs().valueOf(),
      name: "E2E Test",
      comment: "Bitte ignorieren Sie diese Anfrage.",
      start: dayjs().subtract(1, "day"),
      end: dayjs().subtract(1, "day").endOf("day"),
    };
    cy.visit("/cases/new");
    cy.get("form")
      .should("exist")
      .within(() => {
        cy.getBy("input{externalId}").type(indexCase.externalId);
        cy.getBy("input{name}").type(indexCase.name);
        cy.getBy("textarea{comment}").type(indexCase.comment);
        cy.setDateTimeFieldValue("start", indexCase.start);
        cy.setDateTimeFieldValue("end", indexCase.end);
        cy.getBy(".v-btn{submit}").click();
      });
    cy.location("pathname").should("contain", "/cases/details");
    cy.getBy("case.externalId").should("contain", indexCase.externalId);
    cy.getBy("case.name").should("contain", indexCase.name);
    cy.getBy("case.comment").should("contain", indexCase.comment);
    cy.getBy("case.tan").should("exist");
    cy.getBy("case.duration")
      .should("contain", indexCase.start.format("LLL"))
      .and("contain", indexCase.end.format("LLL"));
    cy.getBy("case.status").should("contain", "Angefragt");
  });
  it("should export index case data as csv file", () => {
    cy.visit("/cases/list");
    cy.getBy("view.data-table")
      .filterDataTableByStatus("received")
      .visitByStatus("received");
    cy.getBy("case.contacts.tab").click();
    cy.getBy("case.events.export").should("not.exist");
    cy.getBy("case.contacts.export").should("exist").and("be.disabled");
    cy.getBy("case.contacts.data-table")
      .should("exist")
      .should("not.have.class", "is-loading")
      .within(() => {
        cy.get("tbody tr").should("have.length.at.least", 1);
        cy.get("thead .v-simple-checkbox").click();
      });
    cy.getBy("case.contacts.export").should("not.be.disabled").click();
    cy.getBy("case.events.tab").click();
    cy.getBy("case.contacts.export").should("not.exist");
    cy.getBy("case.events.export").should("exist").and("be.disabled");
    cy.getBy("case.events.data-table")
      .should("exist")
      .should("not.have.class", "is-loading")
      .within(() => {
        cy.get("tbody tr").should("have.length.at.least", 1);
        cy.get("thead .v-simple-checkbox").click();
      });
    cy.getBy("case.events.export").should("not.be.disabled").click();
  });
});
