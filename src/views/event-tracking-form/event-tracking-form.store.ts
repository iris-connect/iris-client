import {
  DataRequestClient,
  DataRequestDetails,
  IrisClientFrontendApiFactory,
  LocationInformation,
} from "@/api";
import { clientConfig } from "@/main";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";

export type EventTrackingFormState = {
  locations: LocationInformation[] | null;
  locationsLoading: boolean;
  eventCreationOngoing: boolean;
};

export interface EventTrackingFormModule
  extends Module<EventTrackingFormState, RootState> {
  mutations: {
    setEventLocations(
      state: EventTrackingFormState,
      locations: LocationInformation[] | null
    ): void;
    setEventLocationsLoading(
      state: EventTrackingFormState,
      payload: boolean
    ): void;
    setEventCreationOngoing(
      state: EventTrackingFormState,
      payload: boolean
    ): void;
    reset(state: EventTrackingFormState, payload: null): void;
  };
  actions: {
    fetchEventLocations(
      { commit }: { commit: Commit },
      keyword: string
    ): Promise<void>;
    createEventTracking(
      { commit }: { commit: Commit },
      formData: DataRequestClient
    ): Promise<DataRequestDetails>;
  };
}

const defaultState: EventTrackingFormState = {
  locations: null,
  locationsLoading: false,
  eventCreationOngoing: false,
};

const eventTrackingForm: EventTrackingFormModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setEventLocations(state, locations) {
      state.locations = locations;
    },
    setEventLocationsLoading(state, loading: boolean) {
      state.locationsLoading = loading;
    },
    setEventCreationOngoing(state, loading: boolean) {
      state.eventCreationOngoing = loading;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchEventLocations({ commit }, keyword) {
      const client = IrisClientFrontendApiFactory(clientConfig);
      let locations: LocationInformation[] | null = null;
      commit("setEventLocationsLoading", true);
      try {
        locations = (await client.searchSearchKeywordGet(keyword)).data
          .locations;
      } finally {
        commit("setEventLocations", locations);
        commit("setEventLocationsLoading", false);
      }
    },

    async createEventTracking(
      { commit },
      dataRequestClient
    ): Promise<DataRequestDetails> {
      commit("setEventCreationOngoing", true);
      try {
        const client = IrisClientFrontendApiFactory(clientConfig);
        return await (
          await client.dataRequestsClientLocationsPost(dataRequestClient)
        ).data;
      } finally {
        commit("setEventCreationOngoing", false);
      }
    },
  },
};

export default eventTrackingForm;
