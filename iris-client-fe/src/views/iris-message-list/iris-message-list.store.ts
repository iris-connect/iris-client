import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { IrisMessageFolder, IrisMessageQuery, PageIrisMessages } from "@/api";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";

export type IrisMessageListState = {
  messageList: PageIrisMessages | null;
  messageListLoading: boolean;
  messageListLoadingError: ErrorMessage;
  messageFolders: IrisMessageFolder[] | null;
  messageFoldersLoading: boolean;
  messageFoldersLoadingError: ErrorMessage;
  unreadMessageCount: number;
};

export interface IrisMessageListModule
  extends Module<IrisMessageListState, RootState> {
  mutations: {
    setMessageList(
      state: IrisMessageListState,
      payload: PageIrisMessages
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
    reset(state: IrisMessageListState, payload: null): void;
  };
  actions: {
    fetchMessages(
      { commit }: { commit: Commit },
      payload: IrisMessageQuery
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
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchMessages({ commit }, query: IrisMessageQuery) {
      let list: PageIrisMessages | null = null;
      commit("setMessageListLoading", true);
      commit("setMessageListLoadingError", null);
      try {
        list = (await authClient.irisMessagesGet({ params: query })).data;
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
        list = (await authClient.irisMessageFoldersGet()).data;
      } catch (e) {
        commit("setMessageFoldersLoadingError", getErrorMessage(e));
      } finally {
        commit("setMessageFolders", list);
        commit("setMessageFoldersLoading", false);
      }
    },
    async fetchUnreadMessageCount({ commit }) {
      let count = 0;
      try {
        count = (await authClient.irisUnreadMessageCountGet()).data;
      } finally {
        commit("setUnreadMessageCount", count);
      }
    },
  },
};

export default irisMessageList;
