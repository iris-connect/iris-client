import { DataRequestCaseData } from "@/api";
import client from "@/api-client";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { normalizeDataRequestCaseData } from "@/views/index-tracking-details/index-tracking-details.data";

export type IndexTrackingDetailsState = {
  indexTrackingDetails: DataRequestCaseData | null;
  indexTrackingDetailsLoading: boolean;
};

export interface IndexTrackingDetailsModule
  extends Module<IndexTrackingDetailsState, RootState> {
  mutations: {
    setIndexTrackingDetails(
      state: IndexTrackingDetailsState,
      indexTrackingDetails: DataRequestCaseData | null
    ): void;
    setIndexTrackingDetailsLoading(
      state: IndexTrackingDetailsState,
      payload: boolean
    ): void;
    reset(state: IndexTrackingDetailsState, payload: null): void;
  };
  actions: {
    fetchIndexTrackingDetails(
      { commit }: { commit: Commit },
      indexId: string
    ): Promise<void>;
  };
}

const defaultState: IndexTrackingDetailsState = {
  indexTrackingDetails: null,
  indexTrackingDetailsLoading: false,
};

const indexTrackingDetails: IndexTrackingDetailsModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setIndexTrackingDetails(state, indexTrackingDetails) {
      state.indexTrackingDetails = indexTrackingDetails;
    },
    setIndexTrackingDetailsLoading(state, loading) {
      state.indexTrackingDetailsLoading = loading;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchIndexTrackingDetails({ commit }, indexId) {
      let indexTrackingDetails: DataRequestCaseData | null = null;
      commit("setIndexTrackingDetails", indexTrackingDetails);
      commit("setIndexTrackingDetailsLoading", true);
      try {
        indexTrackingDetails = normalizeDataRequestCaseData(
          (await client.dataRequestClientCasesCaseIdGet(indexId)).data,
          true
        );
      } finally {
        commit("setIndexTrackingDetails", indexTrackingDetails);
        commit("setIndexTrackingDetailsLoading", false);
      }
    },
  },
};

export default indexTrackingDetails;
