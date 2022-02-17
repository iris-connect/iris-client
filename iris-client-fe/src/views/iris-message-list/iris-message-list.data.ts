import {
  IrisMessage,
  IrisMessageContext,
  IrisMessageFolder,
  IrisMessageHdContact,
  PageIrisMessages,
} from "@/api";
import { Complete, normalizeData, normalizeValue } from "@/utils/data";

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
        isDefault: normalizer("isDefault", undefined, "boolean"),
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
        isOwn: normalizer("isOwn", undefined, "boolean"),
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
        hasFileAttachments: normalizer(
          "hasFileAttachments",
          undefined,
          "boolean"
        ),
      };
      return normalized;
    },
    parse,
    "IrisMessage"
  );
};

export const normalizePageIrisMessages = (
  source?: PageIrisMessages,
  parse?: boolean
): PageIrisMessages => {
  return normalizeData(
    source,
    (normalizer) => {
      const content = normalizer("content", [], "array");
      const normalized: Complete<PageIrisMessages> = {
        totalElements: normalizer("totalElements", undefined, "number"),
        totalPages: normalizer("totalPages", undefined, "number"),
        size: normalizer("size", undefined, "number"),
        content: content.map((item) => normalizeIrisMessage(item)),
        number: normalizer("number", undefined, "number"),
        sort: normalizer("sort", undefined, "any"),
        first: normalizer("first", undefined, "boolean"),
        last: normalizer("last", undefined, "boolean"),
        numberOfElements: normalizer("numberOfElements", undefined, "number"),
        pageable: normalizer("pageable", undefined, "any"),
        empty: normalizer("empty", undefined, "boolean"),
      };
      return normalized;
    },
    parse,
    "PageIndexCase"
  );
};
