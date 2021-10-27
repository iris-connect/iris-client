import { PageEvent } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import authClient from "@/api-client";
import { DataQuery } from "@/api/common";
import { normalizePageEvent } from "@/views/event-tracking-list/event-tracking-list.data";

export type EventTrackingListState = {
  eventTrackingList: PageEvent | null;
  eventTrackingListLoading: boolean;
};

export interface EventTrackingListModule
  extends Module<EventTrackingListState, RootState> {
  mutations: {
    setEventTrackingList(
      state: EventTrackingListState,
      eventTrackingList: PageEvent
    ): void;
    setEventTrackingListLoading(
      state: EventTrackingListState,
      payload: boolean
    ): void;
    reset(state: EventTrackingListState, payload: null): void;
  };
  actions: {
    fetchEventTrackingList(
      { commit }: { commit: Commit },
      payload: DataQuery
    ): Promise<void>;
  };
}

const defaultState: EventTrackingListState = {
  eventTrackingList: {
    content: [],
    totalElements: 0,
  },
  eventTrackingListLoading: false,
};

const eventTrackingList: EventTrackingListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setEventTrackingList(state, payload) {
      state.eventTrackingList = payload;
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
    async fetchEventTrackingList({ commit }, query: DataQuery) {
      let eventTrackingList: PageEvent | null = null;
      commit("setEventTrackingListLoading", true);
      try {
        eventTrackingList = normalizePageEvent(
          (await authClient.dataRequestsClientLocationsGet({ query })).data,
          true
        );
      } finally {
        commit("setEventTrackingList", eventTrackingList);
        commit("setEventTrackingListLoading", false);
      }
    },
  },
};

export default eventTrackingList;
