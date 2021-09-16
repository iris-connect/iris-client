import { PageIndexCase } from "@/api";
import client from "@/api-client";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { DataQuery } from "@/api/common";
import { normalizePageIndexCase } from "@/views/index-tracking-list/index-tracking-list.data";

export type IndexTrackingListState = {
  indexTrackingList: PageIndexCase | null;
  indexTrackingListLoading: boolean;
};

export interface IndexTrackingListModule
  extends Module<IndexTrackingListState, RootState> {
  mutations: {
    setIndexTrackingList(
      state: IndexTrackingListState,
      indexTrackingList: PageIndexCase
    ): void;
    setIndexTrackingListLoading(
      state: IndexTrackingListState,
      payload: boolean
    ): void;
    reset(state: IndexTrackingListState, payload: null): void;
  };
  actions: {
    fetchIndexTrackingList(
      { commit }: { commit: Commit },
      payload: DataQuery
    ): Promise<void>;
  };
}

const defaultState: IndexTrackingListState = {
  indexTrackingList: {
    content: [],
    totalElements: 0,
  },
  indexTrackingListLoading: false,
};

const indexTrackingList: IndexTrackingListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setIndexTrackingList(state, payload) {
      state.indexTrackingList = payload;
    },
    setIndexTrackingListLoading(state, loading) {
      state.indexTrackingListLoading = loading;
    },
    reset(state) {
      // we can keep the data, no need to reset it
      // Object.assign(state, { ...defaultState });
      state.indexTrackingListLoading = false;
    },
  },
  actions: {
    async fetchIndexTrackingList({ commit }, query: DataQuery) {
      let indexTrackingList: PageIndexCase | null = null;
      commit("setIndexTrackingListLoading", true);
      try {
        indexTrackingList = normalizePageIndexCase(
          (await client.dataRequestClientCasesGet({ params: query })).data,
          true
        );
      } finally {
        commit("setIndexTrackingList", indexTrackingList);
        commit("setIndexTrackingListLoading", false);
      }
    },
  },
};

export default indexTrackingList;
