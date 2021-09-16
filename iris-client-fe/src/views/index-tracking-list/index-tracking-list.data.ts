import { DataRequestCaseDetails, PageIndexCase } from "@/api";
import { Complete, normalizeData } from "@/utils/data";

const normalizeDataRequestCaseDetails = (
  source?: DataRequestCaseDetails,
  parse?: boolean
): DataRequestCaseDetails => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<DataRequestCaseDetails> = {
        externalCaseId: normalizer("externalCaseId", ""),
        name: normalizer("name", undefined),
        comment: normalizer("comment", undefined),
        start: normalizer("start", "", "dateString"),
        end: normalizer("end", undefined, "dateString"),
        caseId: normalizer("caseId", undefined),
        status: normalizer("status", undefined),
      };
      return normalized;
    },
    parse,
    "DataRequestCaseDetails"
  );
};

export const normalizePageIndexCase = (
  source?: PageIndexCase,
  parse?: boolean
): PageIndexCase => {
  return normalizeData(
    source,
    (normalizer) => {
      const content = normalizer("content", [], "array");
      const normalized: Complete<PageIndexCase> = {
        totalElements: normalizer("totalElements", undefined, "number"),
        totalPages: normalizer("totalPages", undefined, "number"),
        size: normalizer("size", undefined, "number"),
        content: content.map((item) => normalizeDataRequestCaseDetails(item)),
        number: normalizer("number", undefined, "number"),
        sort: normalizer("sort", undefined, "any"),
        first: normalizer("first", undefined, "boolean"),
        last: normalizer("last", true, "boolean"),
        numberOfElements: normalizer("numberOfElements", undefined, "number"),
        pageable: normalizer("pageable", undefined, "any"),
        empty: normalizer("empty", undefined, "boolean"),
      };
      return normalized;
    },
    parse,
    "PageIndexCase"
  );
};
