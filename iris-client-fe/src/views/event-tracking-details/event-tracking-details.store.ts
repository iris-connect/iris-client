import { DataRequestClientUpdate, DataRequestDetails } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Dispatch, Module } from "vuex";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";

export type EventTrackingDetailsState = {
  eventTrackingDetails: DataRequestDetails | null;
  eventTrackingDetailsLoading: boolean;
  eventTrackingDetailsLoadingError: ErrorMessage;
  dataRequestPatchOngoing: boolean;
  dataRequestPatchError: ErrorMessage;
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
    setDataRequestPatchOngoing(
      state: EventTrackingDetailsState,
      payload: boolean
    ): void;
    setDataRequestPatchError(
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
    patchDataRequest(
      { commit, dispatch }: { commit: Commit; dispatch: Dispatch },
      payload: {
        id: string;
        data: DataRequestClientUpdate;
      }
    ): Promise<void>;
  };
}

const defaultState: EventTrackingDetailsState = {
  eventTrackingDetails: null,
  eventTrackingDetailsLoading: false,
  eventTrackingDetailsLoadingError: null,
  dataRequestPatchOngoing: false,
  dataRequestPatchError: null,
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
    setDataRequestPatchOngoing(state, payload) {
      state.dataRequestPatchOngoing = payload;
    },
    setDataRequestPatchError(state, payload) {
      state.dataRequestPatchError = payload;
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
        eventTrackingDetails = normalizeDataRequestDetails(
          (await authClient.getLocationDetails(eventId)).data,
          true
        );
      } catch (e) {
        commit("setEventTrackingDetailsLoadingError", getErrorMessage(e));
      } finally {
        commit("setEventTrackingDetails", eventTrackingDetails);
        commit("setEventTrackingDetailsLoading", false);
      }
    },
    async patchDataRequest({ commit, dispatch }, payload): Promise<void> {
      commit("setDataRequestPatchOngoing", true);
      commit("setDataRequestPatchError", null);
      try {
        await authClient.dataRequestsClientLocationsCodePatch(
          payload.id,
          payload.data
        );
      } catch (e) {
        commit("setDataRequestPatchError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setDataRequestPatchOngoing", false);
      }
      await dispatch("fetchEventTrackingDetails", payload.id);
    },
  },
};

export default eventTrackingDetails;
