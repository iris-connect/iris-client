import { Header, Row } from "@/utils/data-export/data-export";
import _replace from "lodash/replace";
import _get from "lodash/get";
import _has from "lodash/has";

export const composeAddressHeader = (format?: string): Header => {
  return {
    text: "Adresse",
    value: "address",
    compose: (row: Row) => {
      if (!_has(row, "raw.address")) return "";
      return _replace(
        format ||
          "raw.address.street raw.address.houseNumber, raw.address.zipCode raw.address.city",
        /([\w.\-_]+)/gi,
        (path) => {
          const value = _get(row, path);
          return typeof value === "string" ? value : "-";
        }
      );
    },
    replaceDelimiters: false,
  };
};

export const resolveAtomicAddressHeader = () => {
  if (window.irisAppContext?.csvExportStandardAtomicAddress === "true") {
    return composeAddressHeader();
  }
  return {
    text: "Adresse",
    value: "address",
  };
};

export const getExportLabel = (
  selectionCount: number,
  totalCount: number,
  dataLabel?: string[] | null,
  actionLabel?: string | null | false
) => {
  const singular = dataLabel?.[0] || "Datensatz";
  const plural = dataLabel?.[1] || dataLabel?.[0] || "Datensätze";
  const ofLabel = (dataLabel || []).length === 1 ? "von" : "der";
  const action = actionLabel === false ? "" : actionLabel || "exportieren";
  if (totalCount <= 0) {
    return joinLabels([plural, action]);
  }
  if (totalCount === 1) {
    return joinLabels([singular, action]);
  }
  const exportCountLabel =
    selectionCount > 0 && selectionCount >= totalCount
      ? "Alle"
      : joinLabels([selectionCount, ofLabel, totalCount]);
  return [exportCountLabel, plural, action].join(" ");
};

const joinLabels = (arr: (number | string)[]): string => {
  return arr.join(" ").replace(/\s+/g, " ");
};
