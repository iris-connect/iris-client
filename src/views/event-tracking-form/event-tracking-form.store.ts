import {
  DataRequestClient,
  DataRequestDetails,
  LocationInformation,
} from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";

export type EventTrackingFormState = {
  locations: LocationInformation[] | null;
  locationsLoading: boolean;
  locationsError: ErrorMessage;
  eventCreationOngoing: boolean;
  eventCreationError: ErrorMessage;
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
    setEventLocationsError(
      state: EventTrackingFormState,
      payload: ErrorMessage
    ): void;
    setEventCreationOngoing(
      state: EventTrackingFormState,
      payload: boolean
    ): void;
    setEventCreationError(
      state: EventTrackingFormState,
      payload: ErrorMessage
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
  locationsError: null,
  eventCreationOngoing: false,
  eventCreationError: null,
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
    setEventLocationsError(state, error: ErrorMessage) {
      state.locationsError = error;
    },
    setEventCreationOngoing(state, loading: boolean) {
      state.eventCreationOngoing = loading;
    },
    setEventCreationError(state, error: ErrorMessage) {
      state.eventCreationError = error;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchEventLocations({ commit }, keyword) {
      let locations: LocationInformation[] | null = null;
      commit("setEventLocationsError", null);
      commit("setEventLocationsLoading", true);
      try {
        locations = (await authClient.searchSearchKeywordGet(keyword)).data
          .locations;
      } catch (e) {
        commit("setEventLocationsError", getErrorMessage(e));
      } finally {
        commit("setEventLocations", locations);
        commit("setEventLocationsLoading", false);
      }
    },

    async createEventTracking(
      { commit },
      dataRequestClient
    ): Promise<DataRequestDetails> {
      commit("setEventCreationError", null);
      commit("setEventCreationOngoing", true);
      try {
        return await (
          await authClient.dataRequestsClientLocationsPost(dataRequestClient)
        ).data;
      } catch (e) {
        commit("setEventCreationError", getErrorMessage(e));
        return Promise.reject(e);
      } finally {
        commit("setEventCreationOngoing", false);
      }
    },
  },
};

export default eventTrackingForm;
