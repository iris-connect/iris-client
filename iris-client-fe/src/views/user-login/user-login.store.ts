import {
  AuthenticationStatus,
  Credentials,
  MfaAuthentication,
  User,
  UserRole,
} from "@/api";
import authClient, { baseClient } from "@/api-client";
import store from "@/store";
import { RootState } from "@/store/types";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { omit } from "lodash";
import { RawLocation } from "vue-router";
import { Commit, Module } from "vuex";
import { normalizeUser } from "@/views/user-login/user-login.data";

export type UserLoginState = {
  authenticating: boolean;
  authenticationError: ErrorMessage;
  mfa: MfaAuthentication | null;
  session: UserSession | null;
  interceptedRoute: RawLocation;
  user: User | null;
  userLoading: boolean;
  userLoadingError: ErrorMessage;
};

export type UserSession = {
  authenticated: boolean;
};

export interface UserLoginModule extends Module<UserLoginState, RootState> {
  mutations: {
    setInterceptedRoute(state: UserLoginState, payload: RawLocation): void;
    setAuthenticating(state: UserLoginState, payload: boolean): void;
    setAuthenticationError(state: UserLoginState, payload: ErrorMessage): void;
    setSession(state: UserLoginState, payload: UserSession | null): void;
    setMfa(state: UserLoginState, payload: MfaAuthentication | null): void;
    setUser(state: UserLoginState, payload: User | null): void;
    setUserLoading(state: UserLoginState, payload: boolean): void;
    setUserLoadingError(state: UserLoginState, payload: ErrorMessage): void;
    resetLogin(state: UserLoginState): void;
  };
  actions: {
    login({ commit }: { commit: Commit }, formData: Credentials): Promise<void>;
    refreshToken(): Promise<void>;
    fetchAuthenticatedUser(
      { commit }: { commit: Commit },
      silent?: boolean
    ): Promise<void>;
    verifyMfaOtp({ commit }: { commit: Commit }, otp: string): Promise<void>;
    logout({ commit }: { commit: Commit }): Promise<void>;
  };
  getters: {
    isAuthenticated(): boolean;
    isAdmin(): boolean;
    isUser(): boolean;
    userDisplayName(): string;
    isCurrentUser(): (id: string) => boolean;
  };
}

const defaultState: UserLoginState = {
  authenticating: false,
  authenticationError: null,
  mfa: null,
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
    setMfa(state, payload) {
      state.mfa = payload;
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
    resetLogin(state) {
      Object.assign(state, { ...omit(defaultState, ["session", "user"]) });
    },
  },
  actions: {
    async login({ commit }, credentials): Promise<void> {
      commit("setAuthenticationError", null);
      commit("setAuthenticating", true);
      let session: UserSession | null | unknown = null;
      let mfa: MfaAuthentication | null = null;
      try {
        mfa = (await baseClient.login(credentials)).data;
        session = {
          authenticated:
            mfa?.authenticationStatus === AuthenticationStatus.AUTHENTICATED,
        };
      } catch (e) {
        commit("setAuthenticationError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setSession", session);
        commit("setMfa", mfa);
        commit("setAuthenticating", false);
      }
    },
    async verifyMfaOtp(
      { commit }: { commit: Commit },
      otp: string
    ): Promise<void> {
      commit("setAuthenticationError", null);
      commit("setAuthenticating", true);
      let session: UserSession | null | unknown = null;
      try {
        const status = (await baseClient.mfaOtpPost(otp)).data
          .authenticationStatus;
        session = {
          authenticated: status === AuthenticationStatus.AUTHENTICATED,
        };
      } catch (e) {
        commit("setAuthenticationError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setSession", session);
        commit("setAuthenticating", false);
      }
    },
    async refreshToken(): Promise<void> {
      await baseClient.refreshToken();
    },
    async fetchAuthenticatedUser({ commit }, silent): Promise<void> {
      let user = null;
      if (silent) {
        try {
          user = normalizeUser((await authClient.userProfileGet()).data, true);
          if (user) commit("setUser", user);
        } catch (e) {
          // silent mode: do nothing
        }
        return;
      }
      commit("setUserLoadingError", null);
      commit("setUserLoading", true);
      try {
        user = normalizeUser((await authClient.userProfileGet()).data, true);
      } catch (e) {
        commit("setUserLoadingError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setUser", user);
        commit("setUserLoading", false);
      }
    },
    async logout({ commit }): Promise<void> {
      try {
        await authClient.logout();
      } finally {
        commit("setSession");
      }
    },
  },
  getters: {
    isAuthenticated(): boolean {
      return !!store.state.userLogin.session?.authenticated;
    },
    isAdmin(): boolean {
      return store.state.userLogin.user?.role === UserRole.Admin;
    },
    isUser(): boolean {
      return store.state.userLogin.user?.role === UserRole.User;
    },
    userDisplayName(): string {
      const user = store.state.userLogin.user;
      const fullName = [user?.firstName, user?.lastName].join(" ").trim();
      return fullName || user?.userName || "";
    },
    isCurrentUser: () => (id: string) => {
      const user = store.state.userLogin.user;
      return user?.id === id;
    },
  },
};

export default userLogin;
