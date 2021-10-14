// https://docs.cypress.io/api/introduction/api.html

describe("Dashboard", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
    cy.login();
  });
  afterEach(() => {
    cy.logout();
  });
  it("should display the events counter widget and navigate to the events list", () => {
    cy.getBy("counter-widget.events").within(() => {
      cy.contains("Ereignisse/Woche").should("exist");
      cy.get("a").should("have.attr", "href", "/events/list").click();
      cy.location("pathname").should("equal", "/events/list");
    });
  });
  it("should display the index-cases counter widget, enable indexTracking and navigate to the index case list", () => {
    cy.getBy("counter-widget.index-cases").within(() => {
      cy.contains("Indexfälle/Woche").should("exist");
      cy.get("a")
        .should("have.attr", "href", "/cases/list")
        .should("have.class", "v-btn--disabled");
    });
    cy.visit("/?indexTracking=enabled");
    cy.getBy('{counter-widget.index-cases} a[href="/cases/list"]')
      .should("not.have.class", "v-btn--disabled")
      .click();
    cy.location("pathname").should("equal", "/cases/list");
  });
  it("should display the status counter widget and navigate to the events list", () => {
    cy.getBy("counter-widget.status").within(() => {
      cy.contains("Statusänderungen").should("exist");
      cy.get("a").should("have.attr", "href", "/events/list").click();
      cy.location("pathname").should("equal", "/events/list");
    });
  });
  it("should display 'new event' link and navigate to the event creation page", () => {
    cy.getBy("link.new-event")
      .should("have.attr", "href", "/events/new")
      .click();
    cy.location("pathname").should("equal", "/events/new");
  });
  it("should display 'new index case' link, enable indexTracking and navigate to the index case creation page", () => {
    cy.getBy("link.new-index-case")
      .should("have.attr", "href", "/cases/new")
      .should("have.class", "v-btn--disabled");
    cy.visit("/?indexTracking=enabled");
    cy.getBy("link.new-index-case")
      .should("not.have.class", "v-btn--disabled")
      .click();
    cy.location("pathname").should("equal", "/cases/new");
  });
  it("should display a data table with new events", () => {
    cy.getBy("data-table.open-events").within(() => {
      cy.contains("Offene Ereignisse").should("exist");
      cy.get(".v-data-table").should("exist");
    });
  });
});
