import {
  ExistingDataRequestClientWithLocationList,
  IrisClientFrontendApiFactory,
} from "@/api";
import { clientConfig } from "@/main";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";

export type HomeState = {
  eventTrackingList: ExistingDataRequestClientWithLocationList | null;
  eventTrackingListLoading: boolean;
  eventTrackingListError: ErrorMessage;
};

export interface HomeModule extends Module<HomeState, RootState> {
  mutations: {
    setEventTrackingList(
      state: HomeState,
      eventTrackingList: ExistingDataRequestClientWithLocationList | null
    ): void;
    setEventTrackingListLoading(state: HomeState, payload: boolean): void;
    setEventTrackingListError(state: HomeState, payload: ErrorMessage): void;
    reset(state: HomeState, payload: null): void;
  };
  actions: {
    fetchEventTrackingList({ commit }: { commit: Commit }): Promise<void>;
  };
}

const defaultState: HomeState = {
  eventTrackingList: null,
  eventTrackingListLoading: false,
  eventTrackingListError: null,
};

const home: HomeModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setEventTrackingList(state, eventTrackingList) {
      state.eventTrackingList = eventTrackingList;
    },
    setEventTrackingListLoading(state, loading) {
      state.eventTrackingListLoading = loading;
    },
    setEventTrackingListError(state, error: ErrorMessage) {
      state.eventTrackingListError = error;
    },
    reset(state) {
      // we can keep the data, no need to reset it
      state.eventTrackingListLoading = false;
    },
  },
  actions: {
    async fetchEventTrackingList({ commit }) {
      const client = IrisClientFrontendApiFactory(clientConfig);
      let eventTrackingList: ExistingDataRequestClientWithLocationList | null = null;
      commit("setEventTrackingListError", null);
      commit("setEventTrackingListLoading", true);
      try {
        eventTrackingList = (await client.dataRequestsClientLocationsGet())
          .data;
      } catch (e) {
        commit("setEventTrackingListError", getErrorMessage(e));
      } finally {
        commit("setEventTrackingList", eventTrackingList);
        commit("setEventTrackingListLoading", false);
      }
    },
  },
};

export default home;
