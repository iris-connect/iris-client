import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { IrisMessageDetails } from "@/api";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { normalizeIrisMessageDetails } from "@/views/iris-message-details/iris-message-details.data";

export type IrisMessageDetailsState = {
  message: IrisMessageDetails | null;
  messageLoading: boolean;
  messageLoadingError: ErrorMessage;
  messageSaving: boolean;
  messageSavingError: ErrorMessage;
};

export interface IrisMessageDetailsModule
  extends Module<IrisMessageDetailsState, RootState> {
  mutations: {
    setMessage(
      state: IrisMessageDetailsState,
      payload: IrisMessageDetails
    ): void;
    setMessageLoading(state: IrisMessageDetailsState, payload: boolean): void;
    setMessageLoadingError(
      state: IrisMessageDetailsState,
      payload: ErrorMessage
    ): void;
    setMessageSaving(state: IrisMessageDetailsState, payload: boolean): void;
    setMessageSavingError(
      state: IrisMessageDetailsState,
      payload: ErrorMessage
    ): void;
    reset(state: IrisMessageDetailsState, payload: null): void;
  };
  actions: {
    fetchMessage(
      { commit }: { commit: Commit },
      messageId: string
    ): Promise<void>;
    markAsRead(
      { commit }: { commit: Commit },
      messageId: string
    ): Promise<void>;
  };
}

const defaultState: IrisMessageDetailsState = {
  message: null,
  messageLoading: false,
  messageLoadingError: null,
  messageSaving: false,
  messageSavingError: null,
};

const irisMessageDetails: IrisMessageDetailsModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setMessage(state, payload) {
      state.message = payload;
    },
    setMessageLoading(state, payload) {
      state.messageLoading = payload;
    },
    setMessageLoadingError(state, payload) {
      state.messageLoadingError = payload;
    },
    setMessageSaving(state, payload) {
      state.messageSaving = payload;
    },
    setMessageSavingError(state, payload) {
      state.messageSavingError = payload;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchMessage({ commit }, messageId) {
      let data: IrisMessageDetails | null = null;
      commit("setMessageLoading", true);
      commit("setMessageLoadingError", null);
      try {
        data = normalizeIrisMessageDetails(
          (await authClient.irisMessageDetailsGet(messageId)).data,
          true
        );
      } catch (e) {
        commit("setMessageLoadingError", getErrorMessage(e));
      } finally {
        commit("setMessage", data);
        commit("setMessageLoading", false);
      }
    },
    async markAsRead({ commit }, messageId) {
      commit("setMessageSaving", true);
      commit("setMessageSavingError", null);
      try {
        const data: IrisMessageDetails = normalizeIrisMessageDetails(
          (await authClient.irisMessagesSetIsRead(messageId)).data,
          true
        );
        commit("setMessage", data);
      } catch (e) {
        commit("setMessageLoadingError", getErrorMessage(e));
      } finally {
        commit("setMessageSaving", false);
      }
    },
  },
};

export default irisMessageDetails;
