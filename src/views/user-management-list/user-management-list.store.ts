import { UserList } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";

export type UserManagementListState = {
  userList: UserList | null;
  userListLoading: boolean;
  userListLoadingError: ErrorMessage;
};

export interface UserManagementListModule
  extends Module<UserManagementListState, RootState> {
  mutations: {
    setUserList(
      state: UserManagementListState,
      eventTrackingList: UserList | null
    ): void;
    setUserListLoading(state: UserManagementListState, payload: boolean): void;
    setUserListLoadingError(
      state: UserManagementListState,
      payload: ErrorMessage
    ): void;
    reset(state: UserManagementListState, payload: null): void;
  };
  actions: {
    fetchUserList({ commit }: { commit: Commit }): Promise<void>;
  };
}

const defaultState: UserManagementListState = {
  userList: null,
  userListLoading: false,
  userListLoadingError: null,
};

const userManagementList: UserManagementListModule = {
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
      // we can keep the data, no need to reset it
      // Object.assign(state, { ...defaultState });
      state.userListLoading = false;
      state.userListLoadingError = null;
    },
  },
  actions: {
    async fetchUserList({ commit }) {
      let userList: UserList | null = null;
      commit("setUserListLoadingError", null);
      commit("setUserListLoading", true);
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

export default userManagementList;
