import { RootState } from "@/store/types";

import { Module } from "vuex";

export type NormalizeSettingsState = {
  logEnabled: boolean;
};

export interface NormalizeSettingsModule
  extends Module<NormalizeSettingsState, RootState> {
  mutations: {
    setLogEnabled(state: NormalizeSettingsState, enabled: boolean): void;
    reset(state: NormalizeSettingsState, payload: null): void;
  };
}

const defaultState: NormalizeSettingsState = {
  logEnabled: false,
};

const normalizeSettings: NormalizeSettingsModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setLogEnabled(state, enabled) {
      state.logEnabled = enabled;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
};

export default normalizeSettings;
