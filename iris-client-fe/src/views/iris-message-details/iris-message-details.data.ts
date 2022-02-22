import { IrisMessageDetails } from "@/api";
import { normalizeIrisMessage } from "@/views/iris-message-list/iris-message-list.data";

export const normalizeIrisMessageDetails = (
  source?: IrisMessageDetails,
  parse?: boolean
): IrisMessageDetails => {
  return normalizeIrisMessage(source, parse);
};
