import { UserUpsert } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";

export type AdminUserCreateState = {
  userCreationOngoing: boolean;
  userCreationError: ErrorMessage;
};

export interface AdminUserCreateModule
  extends Module<AdminUserCreateState, RootState> {
  mutations: {
    setUserCreationOngoing(state: AdminUserCreateState, payload: boolean): void;
    setUserCreationError(
      state: AdminUserCreateState,
      payload: ErrorMessage
    ): void;
    reset(state: AdminUserCreateState, payload: null): void;
  };
  actions: {
    createUser(
      { commit }: { commit: Commit },
      formData: UserUpsert
    ): Promise<void>;
  };
}

const defaultState: AdminUserCreateState = {
  userCreationOngoing: false,
  userCreationError: null,
};

const adminUserCreate: AdminUserCreateModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setUserCreationOngoing(state, loading) {
      state.userCreationOngoing = loading;
    },
    setUserCreationError(state, error) {
      state.userCreationError = error;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async createUser({ commit }, formData) {
      commit("setUserCreationError", null);
      commit("setUserCreationOngoing", true);
      try {
        await authClient.usersPost(formData);
      } catch (e) {
        commit("setUserCreationError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setUserCreationOngoing", false);
      }
    },
  },
};

export default adminUserCreate;
