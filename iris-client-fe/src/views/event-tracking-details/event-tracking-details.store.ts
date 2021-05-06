import { DataRequestDetails, DataRequestStatusUpdateByUser } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Dispatch, Module } from "vuex";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";

export type EventTrackingDetailsState = {
  eventTrackingDetails: DataRequestDetails | null;
  eventTrackingDetailsLoading: boolean;
  eventTrackingDetailsLoadingError: ErrorMessage;
  dataRequestAbortOngoing: boolean;
  dataRequestAbortError: ErrorMessage;
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
    setEventTrackingDetailsLoadingError(
      state: EventTrackingDetailsState,
      payload: ErrorMessage
    ): void;
    setDataRequestAbortOngoing(
      state: EventTrackingDetailsState,
      payload: boolean
    ): void;
    setDataRequestAbortError(
      state: EventTrackingDetailsState,
      payload: ErrorMessage
    ): void;
    reset(state: EventTrackingDetailsState, payload: null): void;
  };
  actions: {
    fetchEventTrackingDetails(
      { commit }: { commit: Commit },
      eventId: string
    ): Promise<void>;
    abortDataRequest(
      { commit, dispatch }: { commit: Commit; dispatch: Dispatch },
      eventId: string
    ): Promise<void>;
  };
}

const defaultState: EventTrackingDetailsState = {
  eventTrackingDetails: null,
  eventTrackingDetailsLoading: false,
  eventTrackingDetailsLoadingError: null,
  dataRequestAbortOngoing: false,
  dataRequestAbortError: null,
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
    setEventTrackingDetailsLoadingError(state, payload) {
      state.eventTrackingDetailsLoadingError = payload;
    },
    setDataRequestAbortOngoing(state, payload) {
      state.dataRequestAbortOngoing = payload;
    },
    setDataRequestAbortError(state, payload) {
      state.dataRequestAbortError = payload;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchEventTrackingDetails({ commit }, eventId) {
      let eventTrackingDetails: DataRequestDetails | null = null;
      commit("setEventTrackingDetailsLoading", true);
      commit("setEventTrackingDetailsLoadingError", null);
      try {
        eventTrackingDetails = (await authClient.getLocationDetails(eventId))
          .data;
      } catch (e) {
        commit("setEventTrackingDetailsLoadingError", getErrorMessage(e));
      } finally {
        commit("setEventTrackingDetails", eventTrackingDetails);
        commit("setEventTrackingDetailsLoading", false);
      }
    },
    async abortDataRequest({ commit, dispatch }, eventId): Promise<void> {
      commit("setDataRequestAbortOngoing", true);
      commit("setDataRequestAbortError", null);
      try {
        await authClient.dataRequestsClientLocationsCodePatch(eventId, {
          status: DataRequestStatusUpdateByUser.Aborted,
        });
      } catch (e) {
        commit("setDataRequestAbortError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setDataRequestAbortOngoing", false);
      }
      await dispatch("fetchEventTrackingDetails", eventId);
    },
  },
};

export default eventTrackingDetails;
