import { RootState } from "@/store/types";

import { Module } from "vuex";
import { UserRole } from "@/api";
import { parseData } from "@/utils/data";

export type MockApiStoreState = {
  authenticatedUserRole: UserRole | null;
};

export interface MockApiStoreModule
  extends Module<MockApiStoreState, RootState> {
  mutations: {
    setAuthenticatedUserRole(
      state: MockApiStoreState,
      payload: UserRole | null
    ): void;
    reset(state: MockApiStoreState, payload: null): void;
  };
}

const defaultState: MockApiStoreState = {
  authenticatedUserRole: null,
};

const mockApi: MockApiStoreModule = {
  namespaced: true,
  state() {
    return parseData(defaultState);
  },
  mutations: {
    setAuthenticatedUserRole(state, payload) {
      state.authenticatedUserRole = payload;
    },
    reset(state) {
      Object.assign(state, parseData(defaultState));
    },
  },
};

export default mockApi;
