import { IrisMessageHdContact } from "@/api";
import { normalizeData } from "@/utils/data";
import { normalizeIrisMessageHdContact } from "@/views/iris-message-list/iris-message-list.data";

export const normalizeIrisMessageHdContacts = (
  source?: IrisMessageHdContact[],
  parse?: boolean
): IrisMessageHdContact[] => {
  return normalizeData(
    source,
    () => {
      return (source || []).map((item) => normalizeIrisMessageHdContact(item));
    },
    parse,
    "IrisMessageHdContacts"
  );
};
