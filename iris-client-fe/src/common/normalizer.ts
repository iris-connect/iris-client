import { Page, MetaData } from "@/api";
import { Complete, normalizeData } from "@/utils/data";

export const normalizePage = <T>(
  contentNormalizer: (source?: T, parse?: boolean) => T,
  source?: Page<T>,
  parse?: boolean
): Page<T> => {
  return normalizeData(
    source,
    (normalizer) => {
      const content = normalizer("content", [], "array");
      const normalized: Complete<Page<T>> = {
        totalElements: normalizer("totalElements", undefined, "number"),
        totalPages: normalizer("totalPages", undefined, "number"),
        size: normalizer("size", undefined, "number"),
        content: content.map((item) => contentNormalizer(item, parse)),
        number: normalizer("number", undefined, "number"),
        sort: normalizer("sort", undefined, "any"),
        first: normalizer("first", undefined, "boolean"),
        last: normalizer("last", undefined, "boolean"),
        numberOfElements: normalizer("numberOfElements", undefined, "number"),
        pageable: normalizer("pageable", undefined, "any"),
        empty: normalizer("empty", undefined, "boolean"),
      };
      return normalized;
    },
    parse,
    "Page"
  );
};

export const normalizeMetaData = (source?: MetaData, parse?: boolean) => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        createdBy: normalizer("createdBy", undefined),
        createdAt: normalizer("createdAt", undefined, "dateString"),
        lastModifiedBy: normalizer("lastModifiedBy", undefined),
        lastModifiedAt: normalizer("lastModifiedAt", undefined, "dateString"),
      };
    },
    parse,
    "MetaData"
  );
};
