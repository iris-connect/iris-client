import { DataRequestCaseDetails, PageIndexCase } from "@/api";
import { entryNormalizer, finalizeData } from "@/utils/data";

export const normalizeDataRequestCaseDetails = (
  source?: DataRequestCaseDetails,
  parse?: boolean
): DataRequestCaseDetails => {
  const normalizer = entryNormalizer(source);
  const normalized: DataRequestCaseDetails = {
    externalCaseId: normalizer("externalCaseId", ""),
    name: normalizer("name", undefined),
    comment: normalizer("comment", undefined),
    start: normalizer("start", "", "dateString"),
    end: normalizer("end", undefined, "dateString"),
    caseId: normalizer("caseId", undefined),
    status: normalizer("status", undefined),
  };
  return finalizeData(normalized, source, parse, "DataRequestCaseDetails");
};

export const normalizePageIndexCase = (
  source?: PageIndexCase,
  parse?: boolean
): PageIndexCase => {
  const normalizer = entryNormalizer(source);
  const content = normalizer("content", [], "array");
  const normalized: PageIndexCase = {
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
  return finalizeData(normalized, source, parse, "PageIndexCase");
};
