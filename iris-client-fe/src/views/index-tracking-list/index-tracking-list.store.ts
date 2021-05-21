import { DataRequestCaseDetails, PageIndexCase } from "@/api";
import client from "@/api-client";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { DataPage, DataQuery, generateQuery } from "@/api/common";

export type IndexTrackingListState = {
  indexTrackingList: DataPage<DataRequestCaseDetails>;
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
    updatePageInfo(state: IndexTrackingListState, payload: DataQuery): void;
    reset(state: IndexTrackingListState, payload: null): void;
  };
  actions: {
    fetchIndexTrackingList(
      { commit, state }: { commit: Commit; state: IndexTrackingListState },
      payload: DataQuery
    ): Promise<void>;
  };
}

const defaultState: IndexTrackingListState = {
  indexTrackingList: {
    content: [],
    page: 1,
    itemsPerPage: 5,
    numberOfPages: 1,
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
      state.indexTrackingList.content = payload?.content;
      state.indexTrackingList.numberOfPages = payload?.totalPages;
      state.indexTrackingList.totalElements = payload?.totalElements;
    },
    setIndexTrackingListLoading(state, loading) {
      state.indexTrackingListLoading = loading;
    },
    updatePageInfo(state: IndexTrackingListState, payload: DataQuery) {
      if (payload.page) state.indexTrackingList.page = payload.page;
      if (payload.size) state.indexTrackingList.itemsPerPage = payload.size;
      if (payload.search !== undefined)
        state.indexTrackingList.search = payload.search;
      if (payload.status !== undefined)
        state.indexTrackingList.statusFilter = payload.status;
      if (payload.sort !== undefined)
        state.indexTrackingList.sortBy = payload.sort;
      state.indexTrackingList.sortOrderDesc = payload.sortOrderDesc;
    },
    reset(state) {
      // we can keep the data, no need to reset it
      // Object.assign(state, { ...defaultState });
      state.indexTrackingListLoading = false;
    },
  },
  actions: {
    async fetchIndexTrackingList({ commit, state }, queryDelta: DataQuery) {
      let indexTrackingList: PageIndexCase | null = null;
      commit("updatePageInfo", queryDelta);
      const query = queryDelta ? generateQuery(state.indexTrackingList) : null;
      commit("setIndexTrackingListLoading", true);
      try {
        indexTrackingList = (await client.dataRequestClientCasesGet(query))
          .data;
      } finally {
        commit("setIndexTrackingList", indexTrackingList);
        commit("setIndexTrackingListLoading", false);
      }
    },
  },
};

export default indexTrackingList;
