/// <reference types="cypress" />

import Vue from "vue";
import * as dayjs from "dayjs";
import { RootState } from "../../../src/store/types";

declare global {
  namespace Cypress {
    interface Chainable<Subject> {
      logout(): Chainable<Subject>;
      login(credentials?: {
        userName: string;
        password: string;
      }): Chainable<Subject>;
      loginUsingUi(username: string, password: string): Chainable<Subject>;
      fetchUser(): Chainable<Subject>;
      assertInputValid(selector?: string): Chainable<Subject>;
      assertInputInvalid(message?: string): Chainable<Subject>;
      assertInputInvalid(
        selector: string,
        message?: string
      ): Chainable<Subject>;
      assertInputInvalidByRule(rule?: string): Chainable<Subject>;
      assertInputInvalidByRule(
        selector: string,
        rule?: string
      ): Chainable<Subject>;
      getApp(): Chainable<(Vue & { $store: { state: RootState } }) | undefined>;
      filterDataTableByStatus(status: string): Chainable<Subject>;
      filterDataTableByStatus(
        selector: string,
        status: string
      ): Chainable<Subject>;
      visitByStatus(status: string): Chainable<Subject>;
      visitByStatus(selector: string, status: string): Chainable<Subject>;
      visitUserByAccessor(accessor: string): Chainable<Subject>;
      checkTooltip(tooltip: string): Chainable<Subject>;
      checkTooltip(selector: string, tooltip: string): Chainable<Subject>;
      validateDateTimeField(required?: boolean): Chainable<Subject>;
      validateDateTimeField(
        selector: string,
        required?: boolean
      ): Chainable<Subject>;
      setDateTimeFieldValue(date: dayjs.ConfigType): Chainable<Subject>;
      setDateTimeFieldValue(
        selector: string,
        date: dayjs.ConfigType
      ): Chainable<Subject>;
      getDataTableRow(accessor: string, table?: string): Chainable<Subject>;
      editInputField(
        selector: string,
        config?: {
          text?: string;
          action?: "add" | "remove";
          validation?: Array<"defined" | "sanitised">;
        }
      ): Chainable<Subject>;
      selectFieldValue(menu: string, value: string): Chainable<Subject>;
      selectFieldValue(
        selector: string,
        menu: string,
        value: string
      ): Chainable<Subject>;
      selectOwnIrisMessageContact(menu: string): Chainable<Subject>;
      selectOwnIrisMessageContact(
        selector: string,
        menu: string
      ): Chainable<Subject>;
      selectAutocompleteValue(menu: string, value: string): Chainable<Subject>;
      selectAutocompleteValue(
        selector: string,
        menu: string,
        value: string
      ): Chainable<Subject>;
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
      ): Chainable<JQuery>;
      getByLike(
        selector: string,
        options?: Partial<Loggable & Timeoutable & Withinable & Shadow>
      ): Chainable<JQuery>;
      changeOwnPassword(
        credentials: {
          userName: string;
          password: string;
        },
        newPassword: string
      ): Chainable<Subject>;
    }
  }
}
