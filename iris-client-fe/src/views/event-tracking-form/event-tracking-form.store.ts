import { DataRequestClient, DataRequestDetails, LocationList } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";
import { DataQuery } from "@/api/common";
import { normalizeLocationList } from "@/views/event-tracking-form/event-tracking-form.data";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";

export type EventTrackingFormState = {
  locationList: LocationList | null;
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
      locationList: LocationList | null
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
      query: DataQuery
    ): Promise<void>;
    createEventTracking(
      { commit }: { commit: Commit },
      formData: DataRequestClient
    ): Promise<DataRequestDetails>;
  };
}

const defaultState: EventTrackingFormState = {
  locationList: {
    page: 0,
    size: 20,
    locations: [],
    totalElements: 0,
  },
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
    setEventLocations(state, locationList) {
      state.locationList = locationList;
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
    async fetchEventLocations({ commit }, query) {
      let locationList: LocationList | null = null;
      commit("setEventLocationsError", null);
      commit("setEventLocationsLoading", true);
      try {
        locationList = normalizeLocationList(
          (await authClient.searchSearchKeywordGet({ params: query })).data,
          true
        );
      } catch (e) {
        commit("setEventLocationsError", getErrorMessage(e));
      } finally {
        commit("setEventLocations", locationList);
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
        return normalizeDataRequestDetails(
          (await authClient.dataRequestsClientLocationsPost(dataRequestClient))
            .data,
          true
        );
      } catch (e) {
        commit("setEventCreationError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setEventCreationOngoing", false);
      }
    },
  },
};

export default eventTrackingForm;
