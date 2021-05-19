import { ExistingDataRequestClientWithLocation, PageEvent } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import authClient from "@/api-client";
import { generateQuery } from "@/api/common";

export type EventTrackingListState = {
  eventTrackingList: Array<ExistingDataRequestClientWithLocation> | null;
  eventTrackingListLoading: boolean;
  tableData: any;
};

export interface EventTrackingListModule
  extends Module<EventTrackingListState, RootState> {
  mutations: {
    setEventTrackingList(
      state: EventTrackingListState,
      eventTrackingList: Array<ExistingDataRequestClientWithLocation> | null
    ): void;
    setEventTrackingListLoading(
      state: EventTrackingListState,
      payload: boolean
    ): void;
    setTableData(state: EventTrackingListState, tableData: any): void;
    reset(state: EventTrackingListState, payload: null): void;
  };
  actions: {
    fetchEventTrackingList(
      { commit }: { commit: Commit },
      payload: null
    ): Promise<void>;
  };
}

const defaultState: EventTrackingListState = {
  eventTrackingList: null,
  eventTrackingListLoading: false,
  tableData: {
    search: "",
    page: 1,
    itemsPerPage: 5,
    numberOfPages: 1,
    totalElements: 0,
    headers: [
      {
        text: "Ext.ID",
        align: "start",
        sortable: true,
        value: "extID",
      },
      { text: "Event", value: "name" },
      { text: "Ort", value: "address" },
      { text: "Zeit (Start)", value: "startTime" },
      { text: "Zeit (Ende)", value: "endTime" },
      { text: "Generiert", value: "generatedTime" },
      { text: "Status", value: "status" },
      { text: "Letzte Änderung", value: "lastChange" },
      { text: "", value: "actions" },
    ],
  },
};

const eventTrackingList: EventTrackingListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setEventTrackingList(state, eventTrackingList) {
      state.eventTrackingList = eventTrackingList;
    },
    setEventTrackingListLoading(state, loading) {
      state.eventTrackingListLoading = loading;
    },
    setTableData(state, tableData) {
      state.tableData = tableData;
    },
    reset(state) {
      // we can keep the data, no need to reset it
      // Object.assign(state, { ...defaultState });
      state.eventTrackingListLoading = false;
    },
  },
  actions: {
    async fetchEventTrackingList({ commit }, page: any) {
      let eventTrackingList: PageEvent | null = null;
      const query = page ? generateQuery(page) : null;
      commit("setEventTrackingListLoading", true);
      try {
        eventTrackingList = (
          await authClient.dataRequestsClientLocationsGet(query)
        ).data;
        page.numberOfPages = eventTrackingList.totalPages;
        page.totalElements = eventTrackingList.totalElements;
      } finally {
        commit("setTableData", page);
        commit("setEventTrackingList", eventTrackingList?.content);
        commit("setEventTrackingListLoading", false);
      }
    },
  },
};

export default eventTrackingList;
