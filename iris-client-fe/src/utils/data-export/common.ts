import { Row } from "@/utils/data-export/data-export";
import _replace from "lodash/replace";
import _get from "lodash/get";
import _has from "lodash/has";

export const getAddressHeaders = () => {
  if (window.irisAppContext?.csvExportStandardAtomicAddress === "true") {
    return [
      {
        text: "Adresse",
        value: "address",
        compose: (row: Row) => {
          if (!_has(row, "raw.address")) return "";
          return _replace(
            "raw.address.street raw.address.houseNumber, raw.address.zipCode raw.address.city",
            /([\w.\-_]+)/gi,
            (path) => {
              const value = _get(row, path);
              return typeof value === "string" ? value : "-";
            }
          );
        },
      },
    ];
  }
  return [
    {
      text: "Adresse",
      value: "address",
    },
  ];
};

export const getExportLabel = (
  selectionCount: number,
  totalCount: number,
  dataLabel?: [string, string],
  actionLabel?: string
) => {
  const action = actionLabel ? actionLabel : "exportieren";
  const label = [dataLabel?.[0] || "Datensatz", dataLabel?.[1] || "Datensätze"];
  if (totalCount <= 1) {
    return `${label[0]} ${action}`;
  }
  const exportCountLabel =
    selectionCount > 0 && selectionCount >= totalCount
      ? "Alle"
      : [selectionCount, "der", totalCount].join(" ");
  return [exportCountLabel, label[1], action].join(" ");
};
