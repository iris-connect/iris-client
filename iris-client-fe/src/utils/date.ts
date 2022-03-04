import dayjs from "dayjs";
import "dayjs/locale/de";
import localizedFormat from "dayjs/plugin/localizedFormat";
import customParseFormat from "dayjs/plugin/customParseFormat";
import isSameOrBefore from "dayjs/plugin/isSameOrBefore";
import isSameOrAfter from "dayjs/plugin/isSameOrAfter";
import localeData from "dayjs/plugin/localeData";
import relativeTime from "dayjs/plugin/relativeTime";
import { join } from "@/utils/misc";

dayjs.locale("de");

dayjs.extend(localizedFormat);
dayjs.extend(customParseFormat);
dayjs.extend(isSameOrBefore);
dayjs.extend(isSameOrAfter);
dayjs.extend(localeData);
dayjs.extend(relativeTime);

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

export const getRelativeTime = (
  date?: string | Date,
  prefix?: string[],
  format?: string,
  defaultValue?: string
) => {
  const formattedDate = getFormattedDate(date, format, defaultValue);
  if (date && dayjs(date).isValid()) {
    const d = dayjs(date);
    if (dayjs().diff(d, "days") < 1) {
      return join([prefix?.[0], d.fromNow()], " ");
    }
    return join([prefix?.[1], formattedDate], " ");
  }
  return formattedDate;
};
