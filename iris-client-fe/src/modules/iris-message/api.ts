import authClient from "@/api-client";
import asyncAction from "@/utils/asyncAction";
import { normalizeIrisMessageDetails } from "@/views/iris-message-details/iris-message-details.data";
import {
  normalizeIrisMessageFolders,
  normalizePageIrisMessages,
  normalizeUnreadIrisMessageCount,
} from "@/views/iris-message-list/iris-message-list.data";
import { cancelTokenProvider, DataQuery } from "@/api/common";
import { IrisMessageDataSelectionPayload, IrisMessageInsert } from "@/api";
import { normalizeIrisMessageHdContacts } from "@/views/iris-message-create/iris-message-create.data";
import { apiBundleProvider } from "@/utils/api";
// disabled file attachments
// import fileDownload from "@/utils/fileDownload";
// import { AxiosResponse } from "axios";

const createMessage = () => {
  const action = async (data: IrisMessageInsert) => {
    return await authClient.irisMessagesPost(data);
  };
  return asyncAction(action);
};

const fetchRecipients = () => {
  const cancel_fetchRecipients = cancelTokenProvider();
  const action = async (search: string | null) => {
    const requestOptions = {
      cancelToken: cancel_fetchRecipients(),
      params: { includeOwn: !!window.Cypress, search },
    };
    return normalizeIrisMessageHdContacts(
      (await authClient.irisMessageHdContactsGet(requestOptions)).data,
      true
    );
  };
  return asyncAction(action);
};

const fetchAllowedFileTypes = () => {
  const action = async () => {
    return (await authClient.irisMessageAllowedFileTypesGet()).data;
  };
  return asyncAction(action);
};

const fetchMessages = () => {
  const action = async (query: DataQuery) => {
    return normalizePageIrisMessages(
      (await authClient.irisMessagesGet({ params: query })).data,
      true
    );
  };
  return asyncAction(action);
};

const fetchMessageFolders = () => {
  const action = async () => {
    return normalizeIrisMessageFolders(
      (await authClient.irisMessageFoldersGet()).data,
      true
    );
  };
  return asyncAction(action);
};

const fetchUnreadMessageCount = () => {
  const action = async () => {
    return normalizeUnreadIrisMessageCount(
      (await authClient.irisUnreadMessageCountGet()).data,
      true
    );
  };
  return asyncAction(action);
};

const fetchMessage = () => {
  const action = async (messageId: string) => {
    return normalizeIrisMessageDetails(
      (await authClient.irisMessageDetailsGet(messageId)).data,
      true
    );
  };
  return asyncAction(action);
};

const markAsRead = () => {
  const action = async (messageId: string) => {
    return normalizeIrisMessageDetails(
      (await authClient.irisMessagesSetIsRead(messageId)).data,
      true
    );
  };
  return asyncAction(action);
};

const importDataAttachmentAndAdd = () => {
  const action = async (dataId: string) => {
    return authClient.importIrisMessageDataAndAdd(dataId);
  };
  return asyncAction(action);
};

const importDataAttachmentAndUpdate = () => {
  const action = async (
    dataId: string,
    importTargetId: string,
    data: IrisMessageDataSelectionPayload
  ) => {
    return authClient.importIrisMessageDataAndUpdate(dataId, data, {
      params: { importTargetId },
    });
  };
  return asyncAction(action);
};

export const getMessageDataImportSelectionViewData = () => {
  const action = async (dataId: string, importTargetId?: string) => {
    return (
      await authClient.messageDataImportSelectionViewDataGet(dataId, {
        params: { importTargetId },
      })
    ).data;
  };
  return asyncAction(action);
};

export const getMessageDataViewData = () => {
  const action = async (dataId: string) => {
    return (await authClient.irisMessageDataViewDataGet(dataId)).data;
  };
  return asyncAction(action);
};

export const downloadFileAttachment = () => {
  // disabled file attachments
  // const action = async (fileId: string) => {
  //   const response = await authClient.irisMessageFileDownload(fileId);
  //   const fileName = extractFileName(response);
  //   return fileDownload.download(response.data, fileName);
  // };
  const action = () => null;
  return asyncAction(action);
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

export const irisMessageApi = {
  createMessage,
  fetchRecipients,
  fetchAllowedFileTypes,
  fetchMessages,
  fetchMessageFolders,
  fetchMessage,
  markAsRead,
  importDataAttachmentAndAdd,
  importDataAttachmentAndUpdate,
  getMessageDataImportSelectionViewData,
  getMessageDataViewData,
  downloadFileAttachment,
};

export const bundleIrisMessageApi = apiBundleProvider(irisMessageApi);

export const fetchUnreadMessageCountApi = fetchUnreadMessageCount();
