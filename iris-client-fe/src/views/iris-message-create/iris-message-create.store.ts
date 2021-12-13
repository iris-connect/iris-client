import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { IrisMessageInsert, IrisMessageHdContact } from "@/api";
import { normalizeIrisMessageHdContacts } from "@/views/iris-message-create/iris-message-create.data";

export type IrisMessageCreateState = {
  messageCreationOngoing: boolean;
  messageCreationError: ErrorMessage;
  contacts: IrisMessageHdContact[] | null;
  contactsLoading: boolean;
  contactsLoadingError: ErrorMessage;
};

export interface IrisMessageDetailsModule
  extends Module<IrisMessageCreateState, RootState> {
  mutations: {
    setMessageCreationOngoing(
      state: IrisMessageCreateState,
      payload: boolean
    ): void;
    setMessageCreationError(
      state: IrisMessageCreateState,
      payload: ErrorMessage
    ): void;
    setContacts(
      state: IrisMessageCreateState,
      payload: IrisMessageHdContact[]
    ): void;
    setContactsLoading(state: IrisMessageCreateState, payload: boolean): void;
    setContactsLoadingError(
      state: IrisMessageCreateState,
      payload: ErrorMessage
    ): void;
    reset(state: IrisMessageCreateState, payload: null): void;
  };
  actions: {
    createMessage(
      { commit }: { commit: Commit },
      data: IrisMessageInsert
    ): Promise<void>;
    fetchRecipients({ commit }: { commit: Commit }): Promise<void>;
  };
}

const defaultState: IrisMessageCreateState = {
  messageCreationOngoing: false,
  messageCreationError: null,
  contacts: null,
  contactsLoading: false,
  contactsLoadingError: null,
};

const irisMessageCreate: IrisMessageDetailsModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setMessageCreationOngoing(state, payload) {
      state.messageCreationOngoing = payload;
    },
    setMessageCreationError(state, payload) {
      state.messageCreationError = payload;
    },
    setContacts(state, payload) {
      state.contacts = payload;
    },
    setContactsLoading(state, payload) {
      state.contactsLoading = payload;
    },
    setContactsLoadingError(state, payload) {
      state.contactsLoadingError = payload;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async createMessage({ commit }, data) {
      commit("setMessageCreationError", null);
      commit("setMessageCreationOngoing", true);
      try {
        await authClient.irisMessagesPost(data);
      } catch (e) {
        commit("setMessageCreationError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setMessageCreationOngoing", false);
      }
    },
    async fetchRecipients({ commit }) {
      let list: IrisMessageHdContact[] | null = null;
      commit("setContactsLoading", true);
      commit("setContactsLoadingError", null);
      try {
        const requestOptions = {
          params: { includeOwn: !!window.Cypress },
        };
        list = normalizeIrisMessageHdContacts(
          (await authClient.irisMessageHdContactsGet(requestOptions)).data,
          true
        );
      } catch (e) {
        commit("setContactsLoadingError", getErrorMessage(e));
      } finally {
        commit("setContacts", list);
        commit("setContactsLoading", false);
      }
    },
  },
};

export default irisMessageCreate;
