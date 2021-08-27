import { Statistics } from "@/api";
import { entryNormalizer, finalizeData } from "@/utils/data";

export const normalizeStatistics = (
  source?: Statistics,
  parse?: boolean
): Statistics => {
  const normalizer = entryNormalizer(source);
  const normalized: Statistics = {
    eventsCount: normalizer("eventsCount", undefined, "number"),
    indexCasesCount: normalizer("indexCasesCount", undefined, "number"),
    sumStatus: normalizer("sumStatus", undefined, "number"),
  };
  return finalizeData(normalized, source, parse, "Statistics");
};
