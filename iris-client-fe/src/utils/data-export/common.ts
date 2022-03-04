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
  dataLabel?: string[] | null
) => {
  const singular = dataLabel?.[0] || "Datensatz";
  const plural = dataLabel?.[1] || dataLabel?.[0] || "Datensätze";
  const ofLabel = (dataLabel || []).length === 1 ? "von" : "der";
  if (totalCount <= 0) {
    return `${plural} exportieren`;
  }
  if (totalCount === 1) {
    return `${singular} exportieren`;
  }
  const exportCountLabel =
    selectionCount > 0 && selectionCount >= totalCount
      ? "Alle"
      : [selectionCount, ofLabel, totalCount].join(" ");
  return [exportCountLabel, plural, "exportieren"].join(" ");
};
