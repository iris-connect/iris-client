import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { RootState } from "@/store/types";
import { Credentials, IrisClientFrontendApiFactory } from "@/api";
import { clientConfig } from "@/main";

export type UserLoginState = {
  authenticating: boolean;
  authenticationError: ErrorMessage;
  session: UserSession | null;
};

export type UserSession = {
  token: string;
  expires: number;
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
      // @todo: remove unknown type as soon as response type is defined
      let session: UserSession | null | unknown = null;
      try {
        const client = IrisClientFrontendApiFactory(clientConfig);
        // @todo: remove unknown type as soon as response type is defined
        session = (await client.login(credentials)).data as unknown;
      } catch (e) {
        commit("setAuthenticationError", getErrorMessage(e));
        return Promise.reject(e);
      } finally {
        commit("setSession", session);
        commit("setAuthenticating", false);
      }
    },
  },
};

export default userLogin;
