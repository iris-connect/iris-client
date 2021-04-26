import {
  DataRequestCaseDetails,
  DataRequestCaseData,
  IrisClientFrontendApiFactory,
} from "@/api";
import { clientConfig } from "@/main";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";

export type IndexTrackingDetailsState = {
  indexTrackingDetails: DataRequestCaseData | null;
  indexTrackingDetailsLoading: boolean;
};

export interface IndexTrackingDetailsModule
  extends Module<IndexTrackingDetailsState, RootState> {
  mutations: {
    setIndexTrackingDetails(
      state: IndexTrackingDetailsState,
      indexTrackingDetails: DataRequestCaseDetails | null
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
      const client = IrisClientFrontendApiFactory(clientConfig);
      let indexTrackingDetails: DataRequestCaseData | null = null;
      commit("setIndexTrackingDetails", indexTrackingDetails);
      commit("setIndexTrackingDetailsLoading", true);
      try {
        indexTrackingDetails = (
          await client.dataRequestClientCasesCaseIdGet(indexId)
        ).data;
        //console.log("fetchId "+indexId);
        //console.log(indexTrackingDetails);
      } finally {
        commit("setIndexTrackingDetails", indexTrackingDetails);
        commit("setIndexTrackingDetailsLoading", false);
      }
    },
  },
};

export default indexTrackingDetails;
