import {
  Configuration,
  DataRequestClient,
  IrisClientFrontendApiFactory,
  LocationInformation,
} from "@/api";
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
  };
  actions: {
    fetchEventLocations(
      { commit }: { commit: Commit },
      keyword: string
    ): Promise<void>;
    createEventTracking(
      { commit }: { commit: Commit },
      formData: DataRequestClient
    ): Promise<void>;
  };
}

const productDetail: EventTrackingFormModule = {
  namespaced: true,
  state() {
    return {
      locations: null,
      locationsLoading: false,
      eventCreationOngoing: false,
    };
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
  },
  actions: {
    async fetchEventLocations({ commit }, keyword) {
      const client = IrisClientFrontendApiFactory(
        new Configuration({
          // TODO use basepath for iris-api-client (sidecar)
          basePath: "https://iris-location.service/api",
        })
      );
      let locations: LocationInformation[] | null = null;
      commit("setProductLoading", true);
      try {
        locations = (await client.searchSearchKeywordGet(keyword)).data
          .locations;
      } finally {
        commit("setEventLocations", locations);
        commit("setEventLocationsLoading", false);
      }
    },
    async createEventTracking({ commit }, dataRequestClient) {
      commit("setEventCreationOngoing", true);
      try {
        const client = IrisClientFrontendApiFactory(
          new Configuration({
            // TODO use basepath for iris-api-client (sidecar)
            basePath: "https://iris-public-server.service/api",
          })
        );
        await client.dataRequestsPost(dataRequestClient);
      } finally {
        commit("setEventCreationOngoing", false);
      }
    },
  },
};

export default productDetail;
