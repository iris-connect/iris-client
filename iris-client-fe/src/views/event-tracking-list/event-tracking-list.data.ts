import { ExistingDataRequestClientWithLocation, PageEvent } from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import { normalizeLocationInformation } from "@/views/event-tracking-details/event-tracking-details.data";

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
  source?: PageEvent,
  parse?: boolean
): PageEvent => {
  return normalizeData(
    source,
    (normalizer) => {
      const content = normalizer("content", [], "array");
      const normalized: Complete<PageEvent> = {
        totalElements: normalizer("totalElements", undefined, "number"),
        totalPages: normalizer("totalPages", undefined, "number"),
        size: normalizer("size", undefined, "number"),
        content: content.map((item) =>
          normalizeExistingDataRequestClientWithLocation(item)
        ),
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
    "PageEvent"
  );
};