import { DataRequestCaseExtendedDetails } from "@/api";
import { Complete, normalizeData } from "@/utils/data";

export const normalizeDataRequestCaseExtendedDetails = (
  source?: DataRequestCaseExtendedDetails,
  parse?: boolean
): DataRequestCaseExtendedDetails => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<DataRequestCaseExtendedDetails> = {
        externalCaseId: normalizer("externalCaseId", ""),
        name: normalizer("name", undefined),
        comment: normalizer("comment", undefined),
        start: normalizer("start", "", "dateString"),
        end: normalizer("end", undefined, "dateString"),
        caseId: normalizer("caseId", undefined),
        status: normalizer("status", undefined),
        nonce: normalizer("nonce", undefined),
      };
      return normalized;
    },
    parse,
    "DataRequestCaseExtendedDetails"
  );
};
