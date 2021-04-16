import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { RootState } from "@/store/types";
import { Credentials, IrisClientFrontendApiFactory } from "@/api";
import store from "@/store";
import { clientConfig, sessionFromResponse } from "@/api-client";

export type UserLoginState = {
  authenticating: boolean;
  authenticationError: ErrorMessage;
  session: UserSession | null;
};

export type UserSession = {
  token: string;
};

export interface UserLoginModule extends Module<UserLoginState, RootState> {
  mutations: {
    setAuthenticating(state: UserLoginState, payload: boolean): void;
    setAuthenticationError(state: UserLoginState, payload: ErrorMessage): void;
    setSession(state: UserLoginState, payload: UserSession | null): void;
    reset(state: UserLoginState, payload: null): void;
  };
  actions: {
    authenticate(
      { commit }: { commit: Commit },
      formData: Credentials
    ): Promise<void>;
  };
  getters: {
    isAuthenticated(): boolean;
  };
}

const defaultState: UserLoginState = {
  authenticating: false,
  authenticationError: null,
  session: null,
};

const userLogin: UserLoginModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setAuthenticating(state, submitting: boolean) {
      state.authenticating = submitting;
    },
    setAuthenticationError(state, error: ErrorMessage) {
      state.authenticationError = error;
    },
    setSession(state, session) {
      state.session = session;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
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
        return Promise.reject(e);
      } finally {
        commit("setSession", session);
        commit("setAuthenticating", false);
      }
    },
  },
  getters: {
    isAuthenticated(): boolean {
      return !!store.state.userLogin.session?.token;
    },
  },
};

export default userLogin;
