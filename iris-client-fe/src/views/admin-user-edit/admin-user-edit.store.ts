import { User, UserList, UserRole, UserUpdate } from "@/api";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import authClient from "@/api-client";
import messages from "@/common/messages";
import store from "@/store";
import { normalizeUser } from "@/views/user-login/user-login.data";

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
        data: UserUpdate;
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
        user = normalizeUser(await fetchUserById(id), true);
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
        await authClient.usersIdPatch(payload.id, payload.data);
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
  const currentUser = store.state.userLogin.user;
  // fetch the user profile if the current user wants to edit her/himself
  // we could use the user profile stored in the vuex store as well but it might not be up to date
  if (currentUser?.id === id) {
    return (await authClient.userProfileGet()).data;
  }
  // throw an error if the current user wants to edit someone else and doesn't have the access rights to do so
  if (currentUser?.role !== UserRole.Admin) {
    throw new Error(messages.error.accessDenied);
  }
  const userList: UserList | undefined = (await authClient.usersGet()).data;
  const user = userList?.users?.find((user) => user.id === id);
  if (!user) {
    throw new Error(messages.error.userNotFound(id));
  }
  return user;
};

export default adminUserEdit;
