import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { IrisMessageDetails, IrisMessageViewData } from "@/api";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { normalizeIrisMessageDetails } from "@/views/iris-message-details/iris-message-details.data";
// disabled file attachments
// import fileDownload from "@/utils/fileDownload";
// import { AxiosResponse } from "axios";

export type IrisMessageDetailsState = {
  message: IrisMessageDetails | null;
  messageLoading: boolean;
  messageLoadingError: ErrorMessage;
  messageSaving: boolean;
  messageSavingError: ErrorMessage;
  dataAttachmentLoading: boolean;
  dataAttachmentLoadingError: ErrorMessage;
  fileAttachmentLoading: boolean;
  fileAttachmentLoadingError: ErrorMessage;
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
    setDataAttachmentLoading(
      state: IrisMessageDetailsState,
      payload: boolean
    ): void;
    setDataAttachmentLoadingError(
      state: IrisMessageDetailsState,
      payload: ErrorMessage
    ): void;
    setFileAttachmentLoading(
      state: IrisMessageDetailsState,
      payload: boolean
    ): void;
    setFileAttachmentLoadingError(
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
    importDataAttachment(
      { commit }: { commit: Commit },
      dataId: string
    ): Promise<void>;
    viewDataAttachment(
      { commit }: { commit: Commit },
      dataId: string
    ): Promise<IrisMessageViewData | undefined>;
    // disabled file attachments
    /*
    downloadFileAttachment(
      { commit }: { commit: Commit },
      fileId: string
    ): Promise<void>;
     */
  };
}

const defaultState: IrisMessageDetailsState = {
  message: null,
  messageLoading: false,
  messageLoadingError: null,
  messageSaving: false,
  messageSavingError: null,
  dataAttachmentLoading: false,
  dataAttachmentLoadingError: null,
  fileAttachmentLoading: false,
  fileAttachmentLoadingError: null,
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
    setDataAttachmentLoading(state, payload) {
      state.dataAttachmentLoading = payload;
    },
    setDataAttachmentLoadingError(state, payload) {
      state.dataAttachmentLoadingError = payload;
    },
    setFileAttachmentLoading(state, payload) {
      state.fileAttachmentLoading = payload;
    },
    setFileAttachmentLoadingError(state, payload) {
      state.fileAttachmentLoadingError = payload;
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
    async importDataAttachment({ commit }, dataId: string) {
      commit("setDataAttachmentLoading", true);
      commit("setDataAttachmentLoadingError", null);
      try {
        await authClient.irisMessageDataImport(dataId);
      } catch (e) {
        commit("setDataAttachmentLoadingError", getErrorMessage(e));
      } finally {
        commit("setDataAttachmentLoading", false);
      }
    },
    async viewDataAttachment({ commit }, dataId) {
      commit("setDataAttachmentLoading", true);
      commit("setDataAttachmentLoadingError", null);
      try {
        return (await authClient.irisMessageDataView(dataId)).data;
      } catch (e) {
        commit("setDataAttachmentLoadingError", getErrorMessage(e));
      } finally {
        commit("setDataAttachmentLoading", false);
      }
    },
    // disabled file attachments
    /*
    async downloadFileAttachment({ commit }, fileId: string) {
      commit("setFileAttachmentLoading", true);
      commit("setFileAttachmentLoadingError", null);
      try {
        const response = await authClient.irisMessageFileDownload(fileId);
        const fileName = extractFileName(response);
        fileDownload.download(response.data, fileName);
      } catch (e) {
        commit("setFileAttachmentLoadingError", getErrorMessage(e));
      } finally {
        commit("setFileAttachmentLoading", false);
      }
    },
     */
  },
};
// disabled file attachments
/*
const extractFileName = (response: AxiosResponse): string => {
  const fileName = (response.headers["content-disposition"] || "")
    .split("filename=")[1]
    .split(";")[0]
    .replace(/['"]/g, "");
  if (fileName.length <= 0) {
    throw new Error("invalid file name");
  }
  return fileName;
};
 */

export default irisMessageDetails;
