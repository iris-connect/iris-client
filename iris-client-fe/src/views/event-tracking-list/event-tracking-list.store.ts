import { ExistingDataRequestClientWithLocation, PageEvent } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import authClient from "@/api-client";
import { DataPage, DataQuery, generateQuery } from "@/api/common";

export type EventTrackingListState = {
  eventTrackingList: DataPage<ExistingDataRequestClientWithLocation>;
  eventTrackingListLoading: boolean;
};

export interface EventTrackingListModule
  extends Module<EventTrackingListState, RootState> {
  mutations: {
    setEventTrackingList(
      state: EventTrackingListState,
      eventTrackingList: PageEvent
    ): void;
    setEventTrackingListLoading(
      state: EventTrackingListState,
      payload: boolean
    ): void;
    updatePageInfo(state: EventTrackingListState, payload: DataQuery): void;
    reset(state: EventTrackingListState, payload: null): void;
  };
  actions: {
    fetchEventTrackingList(
      { commit, state }: { commit: Commit; state: EventTrackingListState },
      payload: DataQuery
    ): Promise<void>;
  };
}

const defaultState: EventTrackingListState = {
  eventTrackingList: {
    content: [],
    page: 1,
    itemsPerPage: 5,
    numberOfPages: 1,
    totalElements: 0,
  },
  eventTrackingListLoading: false,
};

const eventTrackingList: EventTrackingListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setEventTrackingList(state, payload) {
      state.eventTrackingList.content = payload?.content;
      state.eventTrackingList.numberOfPages = payload?.totalPages;
      state.eventTrackingList.totalElements = payload?.totalElements;
    },
    setEventTrackingListLoading(state, loading) {
      state.eventTrackingListLoading = loading;
    },
    updatePageInfo(state: EventTrackingListState, payload: DataQuery) {
      if (payload.page) state.eventTrackingList.page = payload.page;
      if (payload.size) state.eventTrackingList.itemsPerPage = payload.size;
      if (payload.search !== undefined)
        state.eventTrackingList.search = payload.search;
      if (payload.status !== undefined)
        state.eventTrackingList.statusFilter = payload.status;
      if (payload.sort !== undefined)
        state.eventTrackingList.sortBy = payload.sort;
      state.eventTrackingList.sortOrderDesc = payload.sortOrderDesc;
    },
    reset(state) {
      // we can keep the data, no need to reset it
      // Object.assign(state, { ...defaultState });
      state.eventTrackingListLoading = false;
    },
  },
  actions: {
    async fetchEventTrackingList({ commit, state }, queryDelta: DataQuery) {
      let eventTrackingList: PageEvent | null = null;
      commit("updatePageInfo", queryDelta);
      const query = queryDelta ? generateQuery(state.eventTrackingList) : null;
      commit("setEventTrackingListLoading", true);
      try {
        eventTrackingList = (
          await authClient.dataRequestsClientLocationsGet(query)
        ).data;
      } finally {
        commit("setEventTrackingList", eventTrackingList);
        commit("setEventTrackingListLoading", false);
      }
    },
  },
};

export default eventTrackingList;
