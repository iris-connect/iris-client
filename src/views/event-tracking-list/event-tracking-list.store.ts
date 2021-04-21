import { ExistingDataRequestClientWithLocationList } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import authClient from "@/api-client";

export type EventTrackingListState = {
  eventTrackingList: ExistingDataRequestClientWithLocationList | null;
  eventTrackingListLoading: boolean;
};

export interface EventTrackingListModule
  extends Module<EventTrackingListState, RootState> {
  mutations: {
    setEventTrackingList(
      state: EventTrackingListState,
      eventTrackingList: ExistingDataRequestClientWithLocationList | null
    ): void;
    setEventTrackingListLoading(
      state: EventTrackingListState,
      payload: boolean
    ): void;
    reset(state: EventTrackingListState, payload: null): void;
  };
  actions: {
    fetchEventTrackingList({ commit }: { commit: Commit }): Promise<void>;
  };
}

const defaultState: EventTrackingListState = {
  eventTrackingList: null,
  eventTrackingListLoading: false,
};

const eventTrackingList: EventTrackingListModule = {
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
    reset(state) {
      // we can keep the data, no need to reset it
      // Object.assign(state, { ...defaultState });
      state.eventTrackingListLoading = false;
    },
  },
  actions: {
    async fetchEventTrackingList({ commit }) {
      let eventTrackingList: ExistingDataRequestClientWithLocationList | null = null;
      commit("setEventTrackingListLoading", true);
      try {
        eventTrackingList = (await authClient.dataRequestsClientLocationsGet())
          .data;
      } finally {
        commit("setEventTrackingList", eventTrackingList);
        commit("setEventTrackingListLoading", false);
      }
    },
  },
};

export default eventTrackingList;
