import { LocationList } from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import { normalizeLocationInformation } from "@/views/event-tracking-details/event-tracking-details.data";

export const normalizeLocationList = (
  source?: LocationList,
  parse?: boolean
): LocationList => {
  return normalizeData(
    source,
    (normalizer) => {
      const locations = normalizer("locations", [], "array");
      const normalized: Complete<LocationList> = {
        locations: locations.map((location) =>
          normalizeLocationInformation(location)
        ),
        totalElements: normalizer("totalElements", 0, "number"),
        page: normalizer("page", 0, "number"),
        size: normalizer("size", 0, "number"),
      };
      return normalized;
    },
    parse,
    "LocationList"
  );
};
