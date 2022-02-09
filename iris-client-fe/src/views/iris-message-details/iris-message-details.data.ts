import {
  IrisMessageContext,
  IrisMessageDataAttachment,
  IrisMessageDetails,
  IrisMessageFileAttachment,
} from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import { normalizeIrisMessage } from "@/views/iris-message-list/iris-message-list.data";

const normalizeIrisMessageDataAttachment = (
  source?: IrisMessageDataAttachment,
  parse?: boolean
): IrisMessageDataAttachment => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<IrisMessageDataAttachment> = {
        id: normalizer("id", ""),
        discriminator: normalizer("discriminator", undefined),
        description: normalizer("description", ""),
        isImported: normalizer("isImported", undefined, "boolean"),
      };
      return normalized;
    },
    parse,
    "IrisMessageDataAttachment"
  );
};

const normalizeIrisMessageFileAttachment = (
  source?: IrisMessageFileAttachment,
  parse?: boolean
): IrisMessageFileAttachment => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<IrisMessageFileAttachment> = {
        id: normalizer("id", ""),
        name: normalizer("name", ""),
        type: normalizer("type", undefined),
      };
      return normalized;
    },
    parse,
    "IrisMessageFileAttachment"
  );
};

export const normalizeIrisMessageDetails = (
  source?: IrisMessageDetails,
  parse?: boolean
): IrisMessageDetails => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: IrisMessageDetails = {
        ...normalizeIrisMessage(source),
        context: normalizer("context", IrisMessageContext.Unknown),
        dataAttachments: source?.dataAttachments
          ? source.dataAttachments.map((item) =>
              normalizeIrisMessageDataAttachment(item)
            )
          : undefined,
        fileAttachments: source?.fileAttachments
          ? source.fileAttachments.map((item) =>
              normalizeIrisMessageFileAttachment(item)
            )
          : undefined,
      };
      return normalized;
    },
    parse,
    "IrisMessageDetails"
  );
};
