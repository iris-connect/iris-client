import { DataRequestCaseExtendedDetails } from "@/api";
import { entryNormalizer, finalizeData } from "@/utils/data";

export const normalizeDataRequestCaseExtendedDetails = (
  source?: DataRequestCaseExtendedDetails,
  parse?: boolean
): DataRequestCaseExtendedDetails => {
  const normalizer = entryNormalizer(source);
  const normalized: DataRequestCaseExtendedDetails = {
    externalCaseId: normalizer("externalCaseId", ""),
    name: normalizer("name", undefined),
    comment: normalizer("comment", undefined),
    start: normalizer("start", "", "dateString"),
    end: normalizer("end", undefined, "dateString"),
    caseId: normalizer("caseId", undefined),
    status: normalizer("status", undefined),
    nonce: normalizer("nonce", undefined),
  };
  return finalizeData(
    normalized,
    source,
    parse,
    "DataRequestCaseExtendedDetails"
  );
};
