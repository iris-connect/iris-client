import {
  IrisMessage,
  IrisMessageContext,
  IrisMessageFolder,
  IrisMessageHdContact,
  Page,
} from "@/api";
import { Complete, normalizeData, normalizeValue } from "@/utils/data";
import { normalizePage } from "@/common/normalizer";

export const normalizeUnreadIrisMessageCount = (
  source?: number,
  parse?: boolean
): number => {
  return normalizeValue(
    source,
    (normalizer) => {
      return normalizer(0, "number");
    },
    parse,
    "UnreadIrisMessageCount"
  );
};

const normalizeIrisMessageFolder = (
  source?: IrisMessageFolder,
  parse?: boolean
): IrisMessageFolder => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<IrisMessageFolder> = {
        id: normalizer("id", ""),
        name: normalizer("name", ""),
        items: source?.items
          ? normalizeIrisMessageFolders(source?.items)
          : undefined,
        context: normalizer("context", IrisMessageContext.Unknown),
      };
      return normalized;
    },
    parse,
    "IrisMessageFolder"
  );
};

export const normalizeIrisMessageFolders = (
  source?: IrisMessageFolder[],
  parse?: boolean
): IrisMessageFolder[] => {
  return normalizeData(
    source,
    () => {
      return (source || []).map((item) => normalizeIrisMessageFolder(item));
    },
    parse,
    "IrisMessageFolders"
  );
};

export const normalizeIrisMessageHdContact = (
  source?: IrisMessageHdContact,
  parse?: boolean
): IrisMessageHdContact => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<IrisMessageHdContact> = {
        id: normalizer("id", ""),
        name: normalizer("name", ""),
        own: normalizer("own", undefined, "boolean"),
      };
      return normalized;
    },
    parse,
    "IrisMessageHdContact"
  );
};

export const normalizeIrisMessage = (
  source?: IrisMessage,
  parse?: boolean
): IrisMessage => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<IrisMessage> = {
        id: normalizer("id", ""),
        folder: normalizer("folder", ""),
        context: normalizer("context", IrisMessageContext.Unknown),
        subject: normalizer("subject", ""),
        body: normalizer("body", ""),
        hdAuthor: normalizeIrisMessageHdContact(source?.hdAuthor),
        hdRecipient: normalizeIrisMessageHdContact(source?.hdRecipient),
        createdAt: normalizer("createdAt", "", "dateString"),
        isRead: normalizer("isRead", undefined, "boolean"),
      };
      return normalized;
    },
    parse,
    "IrisMessage"
  );
};

export const normalizePageIrisMessage = (
  source?: Page<IrisMessage>,
  parse?: boolean
): Page<IrisMessage> => {
  return normalizePage(normalizeIrisMessage, source, parse);
};
