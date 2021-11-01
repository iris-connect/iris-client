import { CheckinApp, CheckinAppStatus } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";

export interface AppWithStatus {
  loading?: boolean;
  message?: string | null;
  status?: CheckinAppStatus;
}

export type AppWithStatusList = Record<string, AppWithStatus>;

export type CheckinAppStatusListState = {
  list: CheckinApp[] | null;
  listLoading: boolean;
  listLoadingError: ErrorMessage;
  appWithStatusList: AppWithStatusList;
};

export interface CheckinAppStatusListModule
  extends Module<CheckinAppStatusListState, RootState> {
  mutations: {
    setList(state: CheckinAppStatusListState, payload: CheckinApp[]): void;
    setListLoading(state: CheckinAppStatusListState, payload: boolean): void;
    setListLoadingError(
      state: CheckinAppStatusListState,
      payload: ErrorMessage
    ): void;
    reset(state: CheckinAppStatusListState): void;
    setAppWithStatusList(
      state: CheckinAppStatusListState,
      payload: AppWithStatusList
    ): void;
    setAppStatus(
      state: CheckinAppStatusListState,
      payload: { name: string & AppWithStatus }
    ): void;
  };
  actions: {
    fetchList({ commit }: { commit: Commit }): Promise<void>;
    fetchStatus({ commit }: { commit: Commit }, payload: string): Promise<void>;
  };
}

const defaultState: CheckinAppStatusListState = {
  list: null,
  listLoading: false,
  listLoadingError: null,
  appWithStatusList: {},
};

const checkinAppStatusList: CheckinAppStatusListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setList(state: CheckinAppStatusListState, payload: CheckinApp[]) {
      state.list = payload;
    },
    setListLoading(state: CheckinAppStatusListState, payload: boolean) {
      state.listLoading = payload;
    },
    setListLoadingError(
      state: CheckinAppStatusListState,
      payload: ErrorMessage
    ) {
      state.listLoadingError = payload;
    },
    setAppWithStatusList(
      state: CheckinAppStatusListState,
      payload: AppWithStatusList
    ) {
      state.appWithStatusList = payload;
    },
    setAppStatus(
      state: CheckinAppStatusListState,
      payload: { name: string & AppWithStatus }
    ) {
      const { name, ...rest } = payload;
      state.appWithStatusList = {
        ...state.appWithStatusList,
        [name]: rest,
      };
    },
    reset(state: CheckinAppStatusListState) {
      state.list = null;
      state.listLoading = false;
      state.listLoadingError = null;
    },
  },
  actions: {
    async fetchList({ commit }) {
      let list: CheckinApp[] | null = null;
      commit("setListLoadingError", null);
      commit("setListLoading", true);
      try {
        list = (await authClient.checkinAppsGet()).data;
      } catch (e) {
        commit("setListLoadingError", getErrorMessage(e));
      } finally {
        commit("setList", list);
        commit("setListLoading", false);
      }
    },
    async fetchStatus({ commit }, name) {
      commit("setAppStatus", {
        name,
        loading: true,
        message: "",
      });
      try {
        const statusInfo = (await authClient.checkinAppStatusGet(name)).data;
        commit("setAppStatus", {
          name,
          loading: false,
          ...statusInfo,
        });
      } catch (e) {
        commit("setAppStatus", {
          name,
          loading: false,
          message: getErrorMessage(e) || "",
        });
        return Promise.reject(e);
      }
    },
  },
};

export default checkinAppStatusList;
