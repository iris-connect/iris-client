import { User, UserList, UserUpsert } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";
import messages from "@/common/messages";

export type AdminUserEditState = {
  user: User | null;
  userLoading: boolean;
  userLoadingError: ErrorMessage;
  userSavingOngoing: boolean;
  userSavingError: ErrorMessage;
};

export interface AdminUserEditModule
  extends Module<AdminUserEditState, RootState> {
  mutations: {
    setUser(state: AdminUserEditState, payload: User | null): void;
    setUserLoading(state: AdminUserEditState, payload: boolean): void;
    setUserLoadingError(state: AdminUserEditState, payload: ErrorMessage): void;
    setUserSavingOngoing(state: AdminUserEditState, payload: boolean): void;
    setUserSavingError(state: AdminUserEditState, payload: ErrorMessage): void;
    reset(state: AdminUserEditState, payload: null): void;
  };
  actions: {
    fetchUser({ commit }: { commit: Commit }, id: string): Promise<void>;
    editUser(
      { commit }: { commit: Commit },
      payload: {
        id: string;
        data: UserUpsert;
      }
    ): Promise<void>;
  };
}

const defaultState: AdminUserEditState = {
  user: null,
  userLoading: false,
  userLoadingError: null,
  userSavingOngoing: false,
  userSavingError: null,
};

const adminUserEdit: AdminUserEditModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setUser(state, payload) {
      state.user = payload;
    },
    setUserLoading(state, payload) {
      state.userLoading = payload;
    },
    setUserLoadingError(state, payload) {
      state.userLoadingError = payload;
    },
    setUserSavingOngoing(state, loading) {
      state.userSavingOngoing = loading;
    },
    setUserSavingError(state, error) {
      state.userSavingError = error;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchUser({ commit }, id) {
      let user: User | null = null;
      commit("setUserLoadingError", null);
      commit("setUserLoading", true);
      commit("setUser", null);
      try {
        user = await fetchUserById(id);
      } catch (e) {
        commit("setUserLoadingError", getErrorMessage(e));
      } finally {
        commit("setUser", user);
        commit("setUserLoading", false);
      }
    },
    async editUser({ commit }, payload) {
      commit("setUserSavingError", null);
      commit("setUserSavingOngoing", true);
      try {
        await authClient.usersIdPut(payload.id, payload.data);
      } catch (e) {
        commit("setUserSavingError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setUserSavingOngoing", false);
      }
    },
  },
};

// @todo: clarify: do we need a dedicated api endpoint for fetching user details?
const fetchUserById = async (id: string): Promise<User> => {
  const userList: UserList | undefined = (await authClient.usersGet()).data;
  const user = userList?.users?.find((user) => user.id === id);
  if (!user) {
    throw new Error(messages.error.userNotFound(id));
  }
  return user;
};

export default adminUserEdit;
