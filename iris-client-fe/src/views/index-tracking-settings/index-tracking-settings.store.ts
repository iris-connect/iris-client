// @todo - indexTracking: optional remove this file once index cases are permanently activated again

import { RootState } from "@/store/types";

import { Module } from "vuex";

export type IndexTrackingSettingsState = {
  indexTrackingEnabled: boolean;
};

export interface IndexTrackingSettingsModule
  extends Module<IndexTrackingSettingsState, RootState> {
  mutations: {
    setIndexTrackingEnabled(
      state: IndexTrackingSettingsState,
      enabled: boolean
    ): void;
    reset(state: IndexTrackingSettingsState, payload: null): void;
  };
}

const defaultState: IndexTrackingSettingsState = {
  indexTrackingEnabled: false,
};

const indexTrackingSettings: IndexTrackingSettingsModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setIndexTrackingEnabled(state, enabled) {
      state.indexTrackingEnabled = enabled;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
};

export default indexTrackingSettings;
