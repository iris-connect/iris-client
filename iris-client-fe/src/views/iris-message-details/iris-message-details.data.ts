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

export const normalizeIrisMessageDetails = (
  source?: IrisMessageDetails,
  parse?: boolean
): IrisMessageDetails => {
  return normalizeData(
    source,
    () => {
      const normalized: IrisMessageDetails = normalizeIrisMessage(source);
      return normalized;
    },
    parse,
    "IrisMessageDetails"
  );
};
