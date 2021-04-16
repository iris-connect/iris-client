import { ExistingDataRequestClientWithLocationList } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import authClient from "@/api-client";

export type HomeState = {
  eventTrackingList: ExistingDataRequestClientWithLocationList | null;
  eventTrackingListLoading: boolean;
};

export interface HomeModule extends Module<HomeState, RootState> {
  mutations: {
    setEventTrackingList(
      state: HomeState,
      eventTrackingList: ExistingDataRequestClientWithLocationList | null
    ): void;
    setEventTrackingListLoading(state: HomeState, payload: boolean): void;
  };
  actions: {
    fetchEventTrackingList({ commit }: { commit: Commit }): Promise<void>;
  };
}

const home: HomeModule = {
  namespaced: true,
  state() {
    return {
      eventTrackingList: null,
      eventTrackingListLoading: false,
    };
  },
  mutations: {
    setEventTrackingList(state, eventTrackingList) {
      state.eventTrackingList = eventTrackingList;
    },
    setEventTrackingListLoading(state, loading) {
      state.eventTrackingListLoading = loading;
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

export default home;
