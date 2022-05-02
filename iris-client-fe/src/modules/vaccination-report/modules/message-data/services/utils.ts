import { VaccinationReport } from "@/api";
import { getFormattedDate } from "@/utils/date";
import { join } from "@/utils/misc";

export const getDefaultDescription = (
  report?: VaccinationReport | null
): string => {
  const name = report?.facility?.name;
  const reportedAt = getFormattedDate(report?.reportedAt, "L", "");
  return name ? join([name, reportedAt], " ") : "";
};
