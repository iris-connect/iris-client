import dayjs from "dayjs";
import "dayjs/locale/de";
import localizedFormat from "dayjs/plugin/localizedFormat";
import customParseFormat from "dayjs/plugin/customParseFormat";
import isSameOrBefore from "dayjs/plugin/isSameOrBefore";
import isSameOrAfter from "dayjs/plugin/isSameOrAfter";

dayjs.locale("de");

dayjs.extend(localizedFormat);
dayjs.extend(customParseFormat);
dayjs.extend(isSameOrBefore);
dayjs.extend(isSameOrAfter);

export default dayjs;

export const getFormattedDate = (
  date?: string | Date,
  format?: string,
  defaultValue?: string
): string => {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format(format || "LLL");
  }
  return defaultValue || "-";
};
