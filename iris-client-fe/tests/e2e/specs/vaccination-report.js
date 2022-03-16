describe("VaccinationReport", () => {
  const reportTableCol = {
    all: 3,
    NOT_VACCINATED: 4,
    SUSPICIOUS_PROOF: 5,
  };
  const gteOne = /^[1-9][0-9]*$/;
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.visit("/");
    cy.login();
  });
  afterEach(() => {
    cy.logout();
  });
  it("should navigate to a vaccination-report with at least one employee - if exists", () => {
    cy.visit("/vaccination-report/list");
    cy.visitByDataTableCellValue(
      "view.data-table",
      reportTableCol.all,
      gteOne
    ).then((success) => {
      if (success) {
        cy.location("pathname").should(
          "contain",
          "/vaccination-report/details"
        );
      }
    });
  });
  it("should navigate to a vaccination-report with at least one not vaccinated employee - if exists", () => {
    cy.visit("/vaccination-report/list");
    cy.sortDataTable("view.data-table", 4, "desc");
    cy.visitByDataTableCellValue(
      "view.data-table",
      reportTableCol.NOT_VACCINATED,
      gteOne
    ).then((success) => {
      if (success) {
        cy.location("pathname").should(
          "contain",
          "/vaccination-report/details"
        );
      }
    });
  });
  it("should navigate to a vaccination-report with at least one employee with a suspicious proof - if exists", () => {
    cy.visit("/vaccination-report/list");
    cy.sortDataTable("view.data-table", 5, "desc");
    cy.visitByDataTableCellValue(
      "view.data-table",
      reportTableCol.SUSPICIOUS_PROOF,
      gteOne
    ).then((success) => {
      if (success) {
        cy.location("pathname").should(
          "contain",
          "/vaccination-report/details"
        );
      }
    });
  });
  it("should filter the employee list based on vaccination status", () => {
    cy.visit("/vaccination-report/list");
    cy.visitByDataTableCellValue(
      "view.data-table",
      reportTableCol.all,
      gteOne
    ).then((success) => {
      if (success) {
        cy.location("pathname").should(
          "contain",
          "/vaccination-report/details"
        );
        cy.getBy("vaccination-report.employee.data-table")
          .filterDataTableByStatus("NOT_VACCINATED")
          .filterDataTableByStatus("SUSPICIOUS_PROOF")
          .filterDataTableByStatus("all");
      }
    });
  });
  it("should export vaccination report data as csv or xlsx file", () => {
    cy.visit("/vaccination-report/list");
    cy.visitByDataTableCellValue(
      "view.data-table",
      reportTableCol.all,
      gteOne
    ).then((success) => {
      if (success) {
        cy.getBy(".v-btn{export.default}").should("be.disabled");
        cy.getBy(".v-btn{export-dialog.activator}").should("be.disabled");
        cy.getBy("vaccination-report.employee.data-table")
          .should("exist")
          .should("not.have.class", "is-loading")
          .within(() => {
            cy.get("tbody tr")
              .not(".v-row-group__header")
              .should("have.length.at.least", 1);
            cy.get("thead .v-simple-checkbox").click();
          });
        cy.getBy(".v-btn{export.default}").should("not.be.disabled").click();
        cy.getBy(".v-btn{export-dialog.activator}")
          .should("not.be.disabled")
          .click();
        cy.getBy("export-dialog")
          .should("be.visible")
          .within(() => {
            cy.getBy("export.csv.standard").should("exist").click();
            cy.getBy("export.xlsx.standard").should("exist").click();
            cy.getBy(".v-btn{close}").should("exist").click();
          });
        cy.getBy("export-dialog").should("not.be.visible");
      }
    });
  });
});
