import { CheckinApp, CheckinAppStatus } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";

export interface AppStatusInfo {
  name?: string;
  loading?: boolean;
  message?: string | null;
  status?: CheckinAppStatus;
}

export type AppStatusInfoList = Record<string, AppStatusInfo>;

export type CheckinAppStatusListState = {
  list: CheckinApp[] | null;
  listLoading: boolean;
  listLoadingError: ErrorMessage;
  appStatusInfoList: AppStatusInfoList;
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
    setAppStatusInfoList(
      state: CheckinAppStatusListState,
      payload: AppStatusInfoList
    ): void;
    setAppStatusInfo(
      state: CheckinAppStatusListState,
      payload: AppStatusInfo
    ): void;
  };
  actions: {
    fetchList({ commit }: { commit: Commit }): Promise<void>;
    fetchStatusInfo(
      { commit }: { commit: Commit },
      payload: string
    ): Promise<void>;
  };
}

const defaultState: CheckinAppStatusListState = {
  list: null,
  listLoading: false,
  listLoadingError: null,
  appStatusInfoList: {},
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
    setAppStatusInfoList(
      state: CheckinAppStatusListState,
      payload: AppStatusInfoList
    ) {
      state.appStatusInfoList = payload;
    },
    setAppStatusInfo(
      state: CheckinAppStatusListState,
      payload: { name: string } & AppStatusInfo
    ) {
      state.appStatusInfoList = {
        ...state.appStatusInfoList,
        [payload.name]: payload,
      };
    },
    reset(state: CheckinAppStatusListState) {
      state.list = null;
      state.listLoading = false;
      state.listLoadingError = null;
      state.appStatusInfoList = {};
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
    async fetchStatusInfo({ commit }, name) {
      commit("setAppStatusInfo", {
        name,
        loading: true,
        message: "",
      });
      try {
        const statusInfo = (await authClient.checkinAppStatusGet(name)).data;
        commit("setAppStatusInfo", {
          name,
          loading: false,
          ...statusInfo,
        });
      } catch (e) {
        commit("setAppStatusInfo", {
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
