import { Guest } from "@/api";
import { getNormalizedValue, normalizeData } from "@/utils/data";
import { normalizeGuests } from "@/views/event-tracking-details/event-tracking-details.data";

export type EventTrackingMessageDataImportSelection = {
  selectables?: {
    guests?: Guest[];
  };
  duplicates?: {
    guests?: string[];
  };
};

export const normalizeImportSelectionSelectables = (
  source?: EventTrackingMessageDataImportSelection["selectables"],
  parse?: boolean
): EventTrackingMessageDataImportSelection["selectables"] => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        guests: normalizer("guests", normalizeGuests(source?.guests)),
      };
    },
    parse,
    "EventTrackingMessageDataImportSelection.Selectables"
  );
};

export const normalizeImportSelectionDuplicates = (
  source?: EventTrackingMessageDataImportSelection["duplicates"],
  parse?: boolean
): EventTrackingMessageDataImportSelection["duplicates"] => {
  return normalizeData(
    source,
    (normalizer) => {
      const guests = normalizer("guests", [], "array");
      return {
        guests: guests?.map((guest) => getNormalizedValue(guest, "")),
      };
    },
    parse,
    "EventTrackingMessageDataImportSelection.Duplicates"
  );
};

export const normalizeEventTrackingMessageDataImportSelection = (
  source?: EventTrackingMessageDataImportSelection,
  parse?: boolean
): EventTrackingMessageDataImportSelection => {
  return normalizeData(
    source,
    () => {
      return {
        selectables: normalizeImportSelectionSelectables(source?.selectables),
        duplicates: normalizeImportSelectionDuplicates(source?.duplicates),
      };
    },
    parse,
    "EventTrackingMessageDataImportSelection"
  );
};
