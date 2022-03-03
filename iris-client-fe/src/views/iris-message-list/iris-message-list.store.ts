import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { IrisMessage, IrisMessageFolder, Page } from "@/api";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import {
  normalizeIrisMessageFolders,
  normalizePageIrisMessage,
  normalizeUnreadIrisMessageCount,
} from "@/views/iris-message-list/iris-message-list.data";
import { DataQuery } from "@/api/common";

export type IrisMessageListState = {
  messageList: Page<IrisMessage> | null;
  messageListLoading: boolean;
  messageListLoadingError: ErrorMessage;
  messageFolders: IrisMessageFolder[] | null;
  messageFoldersLoading: boolean;
  messageFoldersLoadingError: ErrorMessage;
  unreadMessageCount: number;
  unreadMessageCountLoading: boolean;
};

export interface IrisMessageListModule
  extends Module<IrisMessageListState, RootState> {
  mutations: {
    setMessageList(
      state: IrisMessageListState,
      payload: Page<IrisMessage>
    ): void;
    setMessageListLoading(state: IrisMessageListState, payload: boolean): void;
    setMessageListLoadingError(
      state: IrisMessageListState,
      payload: ErrorMessage
    ): void;
    setMessageFolders(
      state: IrisMessageListState,
      payload: IrisMessageFolder[]
    ): void;
    setMessageFoldersLoading(
      state: IrisMessageListState,
      payload: boolean
    ): void;
    setMessageFoldersLoadingError(
      state: IrisMessageListState,
      payload: ErrorMessage
    ): void;
    setUnreadMessageCount(state: IrisMessageListState, payload: number): void;
    setUnreadMessageCountLoading(
      state: IrisMessageListState,
      payload: boolean
    ): void;
    reset(state: IrisMessageListState, payload: null): void;
  };
  actions: {
    fetchMessages(
      { commit }: { commit: Commit },
      payload: DataQuery
    ): Promise<void>;
    fetchMessageFolders({ commit }: { commit: Commit }): Promise<void>;
    fetchUnreadMessageCount({ commit }: { commit: Commit }): Promise<void>;
  };
}

const defaultState: IrisMessageListState = {
  messageList: {
    content: [],
    totalElements: 0,
  },
  messageListLoading: false,
  messageListLoadingError: null,
  messageFolders: null,
  messageFoldersLoading: false,
  messageFoldersLoadingError: null,
  unreadMessageCount: 0,
  unreadMessageCountLoading: false,
};

const irisMessageList: IrisMessageListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setMessageList(state, payload) {
      state.messageList = payload;
    },
    setMessageListLoading(state, payload) {
      state.messageListLoading = payload;
    },
    setMessageListLoadingError(state, payload) {
      state.messageListLoadingError = payload;
    },
    setMessageFolders(state, payload) {
      state.messageFolders = payload;
    },
    setMessageFoldersLoading(state, payload) {
      state.messageFoldersLoading = payload;
    },
    setMessageFoldersLoadingError(state, payload) {
      state.messageFoldersLoadingError = payload;
    },
    setUnreadMessageCount(state, payload) {
      state.unreadMessageCount = payload;
    },
    setUnreadMessageCountLoading(state, payload) {
      state.unreadMessageCountLoading = payload;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchMessages({ commit }, query: DataQuery) {
      let list: Page<IrisMessage> | null = null;
      commit("setMessageListLoading", true);
      commit("setMessageListLoadingError", null);
      try {
        list = normalizePageIrisMessage(
          (await authClient.irisMessagesGet({ params: query })).data,
          true
        );
      } catch (e) {
        commit("setMessageListLoadingError", getErrorMessage(e));
      } finally {
        commit("setMessageList", list);
        commit("setMessageListLoading", false);
      }
    },
    async fetchMessageFolders({ commit }) {
      let list: IrisMessageFolder[] | null = null;
      commit("setMessageFoldersLoading", true);
      commit("setMessageFoldersLoadingError", null);
      try {
        list = normalizeIrisMessageFolders(
          (await authClient.irisMessageFoldersGet()).data,
          true
        );
      } catch (e) {
        commit("setMessageFoldersLoadingError", getErrorMessage(e));
      } finally {
        commit("setMessageFolders", list);
        commit("setMessageFoldersLoading", false);
      }
    },
    async fetchUnreadMessageCount({ commit }) {
      let count = 0;
      commit("setUnreadMessageCountLoading", true);
      try {
        count = normalizeUnreadIrisMessageCount(
          (await authClient.irisUnreadMessageCountGet()).data,
          true
        );
      } finally {
        commit("setUnreadMessageCount", count);
        commit("setUnreadMessageCountLoading", false);
      }
    },
  },
};

export default irisMessageList;
