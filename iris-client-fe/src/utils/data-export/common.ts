import { Row } from "@/utils/data-export/data-export";
import _replace from "lodash/replace";
import _get from "lodash/get";

export const getAddressHeaders = () => {
  if (window.irisAppContext?.csvExportStandardAtomicAddress === "true") {
    return [
      {
        text: "Adresse",
        value: "address",
        compose: (row: Row) => {
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
