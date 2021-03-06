import {
  IrisMessageContext,
  IrisMessageDataAttachment,
  IrisMessageDetails,
} from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import { normalizeIrisMessage } from "@/views/iris-message-list/iris-message-list.data";

const normalizeIrisMessageDataAttachment = (
  source?: IrisMessageDataAttachment,
  parse?: boolean
) => {
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

export const normalizeIrisMessageDetails = (
  source?: IrisMessageDetails,
  parse?: boolean
) => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        ...normalizeIrisMessage(source),
        context: normalizer("context", IrisMessageContext.Unknown),
        dataAttachments: source?.dataAttachments
          ? source.dataAttachments.map((item) =>
              normalizeIrisMessageDataAttachment(item)
            )
          : undefined,
      };
    },
    parse,
    "IrisMessageDetails"
  );
};
