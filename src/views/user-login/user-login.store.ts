import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { RootState } from "@/store/types";
import {
  Credentials,
  IrisClientFrontendApiFactory,
  User,
  UserRole,
} from "@/api";
import store from "@/store";
import authClient, { clientConfig, sessionFromResponse } from "@/api-client";
import { RawLocation } from "vue-router";
import { omit } from "lodash";

export type UserLoginState = {
  authenticating: boolean;
  authenticationError: ErrorMessage;
  session: UserSession | null;
  interceptedRoute: RawLocation;
  user: User | null;
  userLoading: boolean;
  userLoadingError: ErrorMessage;
};

export type UserSession = {
  token: string;
};

export interface UserLoginModule extends Module<UserLoginState, RootState> {
  mutations: {
    setInterceptedRoute(state: UserLoginState, payload: RawLocation): void;
    setAuthenticating(state: UserLoginState, payload: boolean): void;
    setAuthenticationError(state: UserLoginState, payload: ErrorMessage): void;
    setSession(state: UserLoginState, payload: UserSession | null): void;
    setUser(state: UserLoginState, payload: User | null): void;
    setUserLoading(state: UserLoginState, payload: boolean): void;
    setUserLoadingError(state: UserLoginState, payload: ErrorMessage): void;
    reset(state: UserLoginState, payload: null): void;
  };
  actions: {
    authenticate(
      { commit }: { commit: Commit },
      formData: Credentials
    ): Promise<void>;
    fetchAuthenticatedUser({ commit }: { commit: Commit }): Promise<void>;
  };
  getters: {
    isAuthenticated(): boolean;
    isAdmin(): boolean;
    userDisplayName(): string;
  };
}

const defaultState: UserLoginState = {
  authenticating: false,
  authenticationError: null,
  session: null,
  interceptedRoute: "/",
  user: null,
  userLoading: false,
  userLoadingError: null,
};

const userLogin: UserLoginModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setInterceptedRoute(state, payload) {
      state.interceptedRoute = payload;
    },
    setAuthenticating(state, submitting: boolean) {
      state.authenticating = submitting;
    },
    setAuthenticationError(state, error: ErrorMessage) {
      state.authenticationError = error;
    },
    setSession(state, session) {
      state.session = session;
    },
    setUser(state: UserLoginState, payload: User | null) {
      state.user = payload;
    },
    setUserLoading(state: UserLoginState, payload: boolean) {
      state.userLoading = payload;
    },
    setUserLoadingError(state: UserLoginState, payload: ErrorMessage) {
      state.userLoadingError = payload;
    },
    reset(state) {
      Object.assign(state, { ...omit(defaultState, "session") });
    },
  },
  actions: {
    async authenticate({ commit }, credentials): Promise<void> {
      commit("setAuthenticationError", null);
      commit("setAuthenticating", true);
      let session: UserSession | null | unknown = null;
      try {
        const client = IrisClientFrontendApiFactory(clientConfig);
        const response = await client.login(credentials);
        session = sessionFromResponse(response);
      } catch (e) {
        commit("setAuthenticationError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setSession", session);
        commit("setAuthenticating", false);
      }
    },
    async fetchAuthenticatedUser({
      commit,
    }: {
      commit: Commit;
    }): Promise<void> {
      commit("setUserLoadingError", null);
      commit("setUserLoading", true);
      let user = null;
      try {
        user = (await authClient.userProfileGet()).data;
      } catch (e) {
        commit("setUserLoadingError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setUser", user);
        commit("setUserLoading", false);
      }
    },
  },
  getters: {
    isAuthenticated(): boolean {
      return !!store.state.userLogin.session?.token;
    },
    isAdmin(): boolean {
      return store.state.userLogin.user?.role === UserRole.Admin;
    },
    userDisplayName(): string {
      const user = store.state.userLogin.user;
      return (
        [user?.firstName, user?.lastName].join(" ").trim() ??
        user?.userName ??
        ""
      );
    },
  },
};

export default userLogin;
