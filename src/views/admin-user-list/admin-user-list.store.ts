import { UserList } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";

export type AdminUserListState = {
  userList: UserList | null;
  userListLoading: boolean;
  userListLoadingError: ErrorMessage;
};

export interface AdminUserListModule
  extends Module<AdminUserListState, RootState> {
  mutations: {
    setUserList(
      state: AdminUserListState,
      eventTrackingList: UserList | null
    ): void;
    setUserListLoading(state: AdminUserListState, payload: boolean): void;
    setUserListLoadingError(
      state: AdminUserListState,
      payload: ErrorMessage
    ): void;
    reset(state: AdminUserListState, payload: null): void;
  };
  actions: {
    fetchUserList({ commit }: { commit: Commit }): Promise<void>;
  };
}

const defaultState: AdminUserListState = {
  userList: null,
  userListLoading: false,
  userListLoadingError: null,
};

const adminUserList: AdminUserListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setUserList(state, userList) {
      state.userList = userList;
    },
    setUserListLoading(state, loading) {
      state.userListLoading = loading;
    },
    setUserListLoadingError(state, error) {
      state.userListLoadingError = error;
    },
    reset(state) {
      state.userListLoading = false;
      state.userListLoadingError = null;
    },
  },
  actions: {
    async fetchUserList({ commit }) {
      let userList: UserList | null = null;
      commit("reset");
      try {
        userList = (await authClient.usersGet()).data;
      } catch (e) {
        commit("setUserListLoadingError", getErrorMessage(e));
      } finally {
        commit("setUserList", userList);
        commit("setUserListLoading", false);
      }
    },
  },
};

export default adminUserList;
