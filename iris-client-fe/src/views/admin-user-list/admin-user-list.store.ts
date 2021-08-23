import { User, UserList, UserRole } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Dispatch, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";
import { normalize } from "@/utils/data";

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
        userList = normalizeUserList((await authClient.usersGet()).data);
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

export const normalizeUser = (user: User, fallback?: Partial<User>): User => {
  return {
    id: normalize<User, "id">(user, "id", "string", fallback?.id),
    userName: normalize<User, "userName">(
      user,
      "userName",
      "string",
      fallback?.userName || ""
    ),
    role: normalize<User, "role">(
      user,
      "role",
      "string",
      fallback?.role || UserRole.User
    ),
    firstName: normalize<User, "firstName">(
      user,
      "firstName",
      "string",
      fallback?.firstName
    ),
    lastName: normalize<User, "lastName">(
      user,
      "lastName",
      "string",
      fallback?.lastName
    ),
  };
};

export const normalizeUserList = (data: UserList): UserList => {
  const users = normalize<UserList, "users">(data, "users", "array", []);
  return {
    users: users?.map((user) => normalizeUser(user)),
  };
};

export default adminUserList;
