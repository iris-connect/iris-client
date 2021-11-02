// https://docs.cypress.io/api/introduction/api.html

describe("Checkin Apps: Status", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
    cy.login();
  });
  afterEach(() => {
    cy.logout();
  });
  it("should navigate to the checkin-app-status-list page", () => {
    cy.getBy("app-menu.activator").click();
    cy.getBy("app-menu")
      .should("exist")
      .within(() => {
        cy.getBy("app-menu.item.checkin-app-status-list")
          .should("exist")
          .click();
      });
    cy.location("pathname").should("equal", "/checkin-app-status/list");
  });
  it("should filter the checkin-app-status-list based on app status", () => {
    cy.visit("/checkin-app-status/list");
    cy.getBy("view.data-table")
      .filterDataTableByStatus("ok")
      .filterDataTableByStatus("warning")
      .filterDataTableByStatus("error")
      .filterDataTableByStatus("unknown")
      .filterDataTableByStatus("all");
  });
  it("should display the provider status of the selected location when creating a new event", () => {
    cy.visit("/events/new");
    cy.location("pathname").should("equal", "/events/new");
    cy.getBy("location-select-dialog.activator").click();
    cy.getBy("location-select-dialog")
      .should("be.visible")
      .within(() => {
        cy.getBy("search.input").type("iris");
        cy.getBy("search.button").click();
        cy.get(".v-data-table")
          .contains("IRIS connect Demo")
          .first()
          .should("exist")
          .closest("tr")
          .within(() => {
            cy.getBy(".v-btn{select}").click();
          });
      });
    cy.getBy("location-select-dialog").should("not.be.visible");
    cy.getBy("status-info.loading").should("exist");
    cy.getBy("status-info.resolved").should("exist");
  });
});
