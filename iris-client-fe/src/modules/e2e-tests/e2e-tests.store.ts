import { RootState } from "@/store/types";

import { Module } from "vuex";
import { IrisMessageHdContact } from "@/api";
import { parseData } from "@/utils/data";

export type E2ETestsStoreState = {
  irisMessageHdContacts: IrisMessageHdContact[] | null;
};

export interface E2ETestsStoreModule
  extends Module<E2ETestsStoreState, RootState> {
  mutations: {
    setIrisMessageHdContacts(
      state: E2ETestsStoreState,
      payload: IrisMessageHdContact[]
    ): void;
    reset(state: E2ETestsStoreState, payload: null): void;
  };
}

const defaultState: E2ETestsStoreState = {
  irisMessageHdContacts: null,
};

const e2eTests: E2ETestsStoreModule = {
  namespaced: true,
  state() {
    return parseData(defaultState);
  },
  mutations: {
    setIrisMessageHdContacts(state, payload) {
      state.irisMessageHdContacts = payload;
    },
    reset(state) {
      Object.assign(state, parseData(defaultState));
    },
  },
};

export default e2eTests;
