import { IrisMessageDetails } from "@/api";
import { normalizeData } from "@/utils/data";
import { normalizeIrisMessage } from "@/views/iris-message-list/iris-message-list.data";

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
