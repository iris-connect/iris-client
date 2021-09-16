import { RootState } from "@/store/types";

import { Module } from "vuex";

export type NormalizeSettingsState = {
  enabled: boolean;
  logEnabled: boolean;
};

export interface NormalizeSettingsModule
  extends Module<NormalizeSettingsState, RootState> {
  mutations: {
    setEnabled(state: NormalizeSettingsState, enabled: boolean): void;
    setLogEnabled(state: NormalizeSettingsState, enabled: boolean): void;
    reset(state: NormalizeSettingsState, payload: null): void;
  };
}

const defaultState: NormalizeSettingsState = {
  enabled: true,
  logEnabled: false,
};

const normalizeSettings: NormalizeSettingsModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setEnabled(state: NormalizeSettingsState, enabled: boolean) {
      state.enabled = enabled;
    },
    setLogEnabled(state, enabled) {
      state.logEnabled = enabled;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
};

export default normalizeSettings;
