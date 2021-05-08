import { DataRequestCaseClient, DataRequestCaseExtendedDetails } from "@/api";
import client from "@/api-client";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";

export type IndexTrackingFormState = {
  indexCreationOngoing: boolean;
  indexCreationError: ErrorMessage;
};

export interface IndexTrackingFormModule
  extends Module<IndexTrackingFormState, RootState> {
  mutations: {
    setIndexCreationOngoing(
      state: IndexTrackingFormState,
      payload: boolean
    ): void;
    setIndexCreationError(
      state: IndexTrackingFormState,
      payload: ErrorMessage
    ): void;
    reset(state: IndexTrackingFormState, payload: null): void;
  };
  actions: {
    createIndexTracking(
      { commit }: { commit: Commit },
      formData: DataRequestCaseClient
    ): Promise<DataRequestCaseExtendedDetails>;
  };
}

const defaultState: IndexTrackingFormState = {
  indexCreationOngoing: false,
  indexCreationError: null,
};

const indexTrackingForm: IndexTrackingFormModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setIndexCreationOngoing(state, loading: boolean) {
      state.indexCreationOngoing = loading;
    },
    setIndexCreationError(state, error: ErrorMessage) {
      state.indexCreationError = error;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async createIndexTracking(
      { commit },
      dataRequestCaseClient
    ): Promise<DataRequestCaseExtendedDetails> {
      commit("setIndexCreationError", null);
      commit("setIndexCreationOngoing", true);
      try {
        return await (
          await client.dataRequestClientCasesPost(dataRequestCaseClient)
        ).data;
      } catch (e) {
        commit("setIndexCreationError", getErrorMessage(e));
        return Promise.reject(e);
      } finally {
        commit("setIndexCreationOngoing", false);
      }
    },
  },
};

export default indexTrackingForm;
