import { IrisMessageFileAttachment, IrisMessageDetails } from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import { normalizeIrisMessage } from "@/views/iris-message-list/iris-message-list.data";

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
    () => {
      const normalized: IrisMessageDetails = {
        ...normalizeIrisMessage(source),
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
