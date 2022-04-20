import { ExistingDataRequestClientWithLocation, Page } from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import { normalizeLocationInformation } from "@/views/event-tracking-details/event-tracking-details.data";
import { normalizePage } from "@/common/normalizer";

const normalizeExistingDataRequestClientWithLocation = (
  source?: ExistingDataRequestClientWithLocation,
  parse?: boolean
): ExistingDataRequestClientWithLocation => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<ExistingDataRequestClientWithLocation> = {
        status: normalizer("status", undefined),
        code: normalizer("code", undefined),
        name: normalizer("name", undefined),
        externalRequestId: normalizer("externalRequestId", undefined),
        start: normalizer("start", undefined, "dateString"),
        end: normalizer("end", undefined, "dateString"),
        requestedAt: normalizer("requestedAt", undefined, "dateString"),
        lastUpdatedAt: normalizer("lastUpdatedAt", undefined, "dateString"),
        requestDetails: normalizer("requestDetails", undefined),
        locationInformation: source?.locationInformation
          ? normalizeLocationInformation(source?.locationInformation)
          : source?.locationInformation,
      };
      return normalized;
    },
    parse,
    "ExistingDataRequestClientWithLocation"
  );
};

export const normalizePageEvent = (
  source?: Page<ExistingDataRequestClientWithLocation>,
  parse?: boolean
) => {
  return normalizePage(
    normalizeExistingDataRequestClientWithLocation,
    source,
    parse
  );
};
