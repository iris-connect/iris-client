import { MetaData } from "@/api";
import { Complete, normalizeData } from "@/utils/data";

export const normalizeMetaData = (
  source?: MetaData,
  parse?: boolean
): MetaData => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<MetaData> = {
        createdBy: normalizer("createdBy", undefined),
        createdAt: normalizer("createdAt", undefined, "dateString"),
        lastModifiedBy: normalizer("lastModifiedBy", undefined),
        lastModifiedAt: normalizer("lastModifiedAt", undefined, "dateString"),
      };
      return normalized;
    },
    parse,
    "MetaData"
  );
};
