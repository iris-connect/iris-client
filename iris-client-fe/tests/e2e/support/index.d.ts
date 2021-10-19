/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable<Subject> {
    logout(): Chainable<Subject>;
    login(): Chainable<Subject>;
    loginUsingUi(username: string, password: string): Chainable<Subject>;
    fetchUser(): Chainable<Subject>;
    assertInputValid(selector?: string): Chainable<Subject>;
    assertInputInvalid(message?: string): Chainable<Subject>;
    assertInputInvalid(selector: string, message?: string): Chainable<Subject>;
    getApp(): Chainable<Vue | undefined>;
    filterEventsByStatus(status: string): Chainable<Subject>;
    visitEventByStatus(status: string): Chainable<Subject>;
    checkEditableField(
      selector: string,
      config?: {
        field?: string;
        validation?: Array<"defined" | "sanitised">;
      }
    ): Chainable<Subject>;
    getBy(
      selector: string,
      options?: Partial<Loggable & Timeoutable & Withinable & Shadow>
    ): Chainable<JQuery<HTMLElement>>;
    getByLike(
      selector: string,
      options?: Partial<Loggable & Timeoutable & Withinable & Shadow>
    ): Chainable<JQuery<HTMLElement>>;
  }
}
