import { DataRequestDetails, IrisClientFrontendApiFactory } from "@/api";
import { clientConfig } from "@/main";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";

export type EventTrackingDetailsState = {
  eventTrackingDetails: DataRequestDetails | null;
  eventTrackingDetailsLoading: boolean;
};

export interface EventTrackingDetailsModule
  extends Module<EventTrackingDetailsState, RootState> {
  mutations: {
    setEventTrackingDetails(
      state: EventTrackingDetailsState,
      eventTrackingDetails: DataRequestDetails | null
    ): void;
    setEventTrackingDetailsLoading(
      state: EventTrackingDetailsState,
      payload: boolean
    ): void;
    reset(state: EventTrackingDetailsState, payload: null): void;
  };
  actions: {
    fetchEventTrackingDetails(
      { commit }: { commit: Commit },
      eventId: string
    ): Promise<void>;
  };
}

const defaultState: EventTrackingDetailsState = {
  eventTrackingDetails: null,
  eventTrackingDetailsLoading: false,
};

const eventTrackingDetails: EventTrackingDetailsModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setEventTrackingDetails(state, eventTrackingDetails) {
      state.eventTrackingDetails = eventTrackingDetails;
    },
    setEventTrackingDetailsLoading(state, loading) {
      state.eventTrackingDetailsLoading = loading;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchEventTrackingDetails({ commit }, eventId) {
      const client = IrisClientFrontendApiFactory(clientConfig);
      let eventTrackingDetails: DataRequestDetails | null = null;
      commit("setEventTrackingDetails", eventTrackingDetails);
      commit("setEventTrackingDetailsLoading", true);
      try {
        eventTrackingDetails = (await client.getLocationDetails(eventId)).data;
      } finally {
        commit("setEventTrackingDetails", eventTrackingDetails);
        commit("setEventTrackingDetailsLoading", false);
      }
    },
  },
};

export default eventTrackingDetails;
