import {PageIndexCase} from "@/api";
import client from "@/api-client";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";

export type IndexTrackingListState = {
  indexTrackingList: PageIndexCase | null;
  indexTrackingListLoading: boolean;
};

export interface IndexTrackingListModule
  extends Module<IndexTrackingListState, RootState> {
  mutations: {
    setIndexTrackingList(
      state: IndexTrackingListState,
      indexTrackingList: PageIndexCase | null
    ): void;
    setIndexTrackingListLoading(
      state: IndexTrackingListState,
      payload: boolean
    ): void;
    reset(state: IndexTrackingListState, payload: null): void;
  };
  actions: {
    fetchIndexTrackingList({ commit }: { commit: Commit }, payload: null): Promise<void>;
  };
}

const defaultState: IndexTrackingListState = {
  indexTrackingList: null,
  indexTrackingListLoading: false,
};

const indexTrackingList: IndexTrackingListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setIndexTrackingList(state, indexTrackingList) {
      state.indexTrackingList = indexTrackingList;
    },
    setIndexTrackingListLoading(state, loading) {
      state.indexTrackingListLoading = loading;
    },
    reset(state) {
      // we can keep the data, no need to reset it
      // Object.assign(state, { ...defaultState });
      state.indexTrackingListLoading = false;
    }
  },
  actions: {
    async fetchIndexTrackingList({ commit }, page) {
      let indexTrackingList: PageIndexCase | null = null;
      const query = page ? { query: page } : null;
      commit("setIndexTrackingListLoading", true);
      try {
        indexTrackingList = (await client.dataRequestClientCasesGet(query)).data;
      } finally {
        commit("setIndexTrackingList", indexTrackingList);
        commit("setIndexTrackingListLoading", false);
      }
    },
  },
};

export default indexTrackingList;
