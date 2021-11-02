import { CheckinApp, CheckinAppStatus } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";
import _unionBy from "lodash/unionBy";
import _find from "lodash/find";
import {
  normalizeCheckinAppList,
  normalizeCheckinAppStatusInfo,
} from "@/views/checkin-app-status-list/checkin-app-status-list.data";

export interface AppStatusInfo {
  name?: string;
  loading?: boolean;
  message?: string | null;
  status?: CheckinAppStatus;
}

export type AppStatusInfoList = AppStatusInfo[];

export type CheckinAppStatusListState = {
  list: CheckinApp[] | null;
  listLoading: boolean;
  listLoadingError: ErrorMessage;
  appStatusInfoList: AppStatusInfo[] | null;
};

export interface CheckinAppStatusListModule
  extends Module<CheckinAppStatusListState, RootState> {
  mutations: {
    setList(
      state: CheckinAppStatusListState,
      payload: CheckinApp[] | null
    ): void;
    setListLoading(state: CheckinAppStatusListState, payload: boolean): void;
    setListLoadingError(
      state: CheckinAppStatusListState,
      payload: ErrorMessage
    ): void;
    reset(state: CheckinAppStatusListState): void;
    setAppStatusInfoList(
      state: CheckinAppStatusListState,
      payload: AppStatusInfo[] | null
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
  getters: {
    appStatusInfo(
      state: CheckinAppStatusListState
    ): (name: string) => AppStatusInfo;
  };
}

const defaultState: CheckinAppStatusListState = {
  list: null,
  listLoading: false,
  listLoadingError: null,
  appStatusInfoList: null,
};

const checkinAppStatusList: CheckinAppStatusListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setList(state: CheckinAppStatusListState, payload: CheckinApp[] | null) {
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
      payload: AppStatusInfo[] | null
    ) {
      state.appStatusInfoList = payload;
    },
    setAppStatusInfo(
      state: CheckinAppStatusListState,
      payload: { name: string } & AppStatusInfo
    ) {
      state.appStatusInfoList = _unionBy(
        [payload],
        state.appStatusInfoList || [],
        "name"
      );
    },
    reset(state: CheckinAppStatusListState) {
      state.list = null;
      state.listLoading = false;
      state.listLoadingError = null;
      state.appStatusInfoList = null;
    },
  },
  actions: {
    async fetchList({ commit }) {
      let list: CheckinApp[] | null = null;
      commit("setListLoadingError", null);
      commit("setListLoading", true);
      try {
        list = normalizeCheckinAppList(
          (await authClient.checkinAppsGet()).data,
          true
        );
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
        const statusInfo = normalizeCheckinAppStatusInfo(
          (await authClient.checkinAppStatusGet(name)).data,
          true
        );
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
  getters: {
    appStatusInfo: (state) => (name: string) => {
      const list = state.appStatusInfoList;
      return _find(list, ["name", name]) || {};
    },
  },
};

export default checkinAppStatusList;
