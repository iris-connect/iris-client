import { IrisMessageAttachment, IrisMessageDetails } from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import { normalizeIrisMessage } from "@/views/iris-message-list/iris-message-list.data";

const normalizeIrisMessageAttachment = (
  source?: IrisMessageAttachment,
  parse?: boolean
): IrisMessageAttachment => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<IrisMessageAttachment> = {
        id: normalizer("id", ""),
        name: normalizer("name", ""),
        type: normalizer("type", undefined),
      };
      return normalized;
    },
    parse,
    "IrisMessageAttachment"
  );
};

export const normalizeIrisMessageDetails = (
  source?: IrisMessageDetails,
  parse?: boolean
): IrisMessageDetails => {
  return normalizeData(
    source,
    () => {
      const normalized: IrisMessageDetails = {
        ...normalizeIrisMessage(source),
        attachments: source?.attachments
          ? source.attachments.map((item) =>
              normalizeIrisMessageAttachment(item)
            )
          : undefined,
      };
      return normalized;
    },
    parse,
    "IrisMessageDetails"
  );
};
