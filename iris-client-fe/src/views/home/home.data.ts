import { Statistics } from "@/api";
import { Complete, normalizeData } from "@/utils/data";

export const normalizeStatistics = (
  source?: Statistics,
  parse?: boolean
): Statistics => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<Statistics> = {
        eventsCount: normalizer("eventsCount", undefined, "number"),
        indexCasesCount: normalizer("indexCasesCount", undefined, "number"),
        sumStatus: normalizer("sumStatus", undefined, "number"),
      };
      return normalized;
    },
    parse,
    "Statistics"
  );
};
