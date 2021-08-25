import {
  DataRequestStatus,
  ExistingDataRequestClientWithLocation,
  PageEvent,
  Statistics,
} from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { DataQuery, getSortAttribute } from "@/api/common";
import { normalizePageEvent } from "@/views/event-tracking-list/event-tracking-list.data";

export type HomeState = {
  eventTrackingList: Array<ExistingDataRequestClientWithLocation> | null;
  eventTrackingListLoading: boolean;
  eventTrackingListError: ErrorMessage;
  statistics: Statistics;
};

export interface HomeModule extends Module<HomeState, RootState> {
  mutations: {
    setEventTrackingList(
      state: HomeState,
      eventTrackingList: Array<ExistingDataRequestClientWithLocation> | null
    ): void;
    setEventTrackingListLoading(state: HomeState, payload: boolean): void;
    setEventTrackingListError(state: HomeState, payload: ErrorMessage): void;
    setStatistics(state: HomeState, stats: Statistics): void;
    reset(state: HomeState, payload: null): void;
  };
  actions: {
    fetchEventTrackingList({ commit }: { commit: Commit }): Promise<void>;
    fetchStatistics({ commit }: { commit: Commit }): Promise<void>;
  };
}

const defaultState: HomeState = {
  eventTrackingList: null,
  eventTrackingListLoading: false,
  eventTrackingListError: null,
  statistics: { eventsCount: 0, indexCasesCount: 0, sumStatus: 0 },
};

const home: HomeModule = {
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
    setEventTrackingListError(state, error: ErrorMessage) {
      state.eventTrackingListError = error;
    },
    setStatistics(state, stats: Statistics) {
      state.statistics = stats;
    },
    reset(state) {
      // we can keep the data, no need to reset it
      state.eventTrackingListLoading = false;
    },
  },
  actions: {
    async fetchEventTrackingList({ commit }) {
      let eventTrackingList: PageEvent | null = null;
      commit("setEventTrackingListError", null);
      commit("setEventTrackingListLoading", true);
      const query: DataQuery = {
        page: 0,
        size: 10,
        sort: getSortAttribute("generatedTime") + ",desc",
        status: DataRequestStatus.DataReceived,
      };
      try {
        eventTrackingList = normalizePageEvent(
          (await authClient.dataRequestsClientLocationsGet({ query: query }))
            .data,
          true
        );
      } catch (e) {
        commit("setEventTrackingListError", getErrorMessage(e));
      } finally {
        commit("setEventTrackingList", eventTrackingList?.content);
        commit("setEventTrackingListLoading", false);
      }
    },
    async fetchStatistics({ commit }) {
      let statistics: Statistics | null = {
        eventsCount: 0,
        indexCasesCount: 0,
        sumStatus: 0,
      };
      try {
        statistics = (await authClient.getWeeklyData()).data;
      } catch (e) {
        // TODO
      } finally {
        commit("setStatistics", statistics);
      }
    },
  },
};

export default home;
