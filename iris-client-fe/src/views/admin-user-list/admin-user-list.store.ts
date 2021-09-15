import { UserList } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Dispatch, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";
import { normalizeUserList } from "@/views/admin-user-list/admin-user-list.data";

export type AdminUserListState = {
  userList: UserList | null;
  userListLoading: boolean;
  userListLoadingError: ErrorMessage;
  userDeleteOngoing: boolean;
  userDeleteError: ErrorMessage;
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
    setUserDeleteOngoing(state: AdminUserListState, payload: boolean): void;
    setUserDeleteError(state: AdminUserListState, payload: ErrorMessage): void;
    reset(state: AdminUserListState, payload: null): void;
  };
  actions: {
    fetchUserList({ commit }: { commit: Commit }): Promise<void>;
    deleteUser(
      { commit, dispatch }: { commit: Commit; dispatch: Dispatch },
      id: string
    ): Promise<void>;
  };
}

const defaultState: AdminUserListState = {
  userList: null,
  userListLoading: false,
  userListLoadingError: null,
  userDeleteOngoing: false,
  userDeleteError: null,
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
    setUserDeleteOngoing(state, loading) {
      state.userDeleteOngoing = loading;
    },
    setUserDeleteError(state, error) {
      state.userDeleteError = error;
    },
    reset(state) {
      state.userListLoading = false;
      state.userListLoadingError = null;
      state.userDeleteOngoing = false;
      state.userDeleteError = null;
    },
  },
  actions: {
    async fetchUserList({ commit }) {
      let userList: UserList | null = null;
      commit("setUserListLoadingError", null);
      commit("setUserListLoading", true);
      try {
        userList = normalizeUserList((await authClient.usersGet()).data, true);
      } catch (e) {
        commit("setUserListLoadingError", getErrorMessage(e));
      } finally {
        commit("setUserList", userList);
        commit("setUserListLoading", false);
      }
    },
    async deleteUser({ commit, dispatch }, id) {
      commit("setUserDeleteError", null);
      commit("setUserDeleteOngoing", true);
      try {
        await authClient.usersIdDelete(id);
      } catch (e) {
        commit("setUserDeleteError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setUserDeleteOngoing", false);
      }
      await dispatch("fetchUserList");
    },
  },
};

export default adminUserList;
