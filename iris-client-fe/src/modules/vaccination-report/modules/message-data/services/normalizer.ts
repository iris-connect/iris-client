import { VREmployee } from "@/api";
import { getNormalizedValue, normalizeData } from "@/utils/data";
import { normalizeVREmployee } from "@/modules/vaccination-report/services/normalizer";

export type VaccinationReportMessageDataImportSelection = {
  selectables?: {
    employees?: VREmployee[];
  };
  duplicates?: {
    employees?: string[];
  };
};

export const normalizeImportSelectionSelectables = (
  source?: VaccinationReportMessageDataImportSelection["selectables"],
  parse?: boolean
): VaccinationReportMessageDataImportSelection["selectables"] => {
  return normalizeData(
    source,
    (normalizer) => {
      const employees = normalizer("employees", undefined, "array") || [];
      return {
        employees: employees.map((employee) => normalizeVREmployee(employee)),
      };
    },
    parse,
    "VaccinationReportMessageDataImportSelection.Selectables"
  );
};

export const normalizeImportSelectionDuplicates = (
  source?: VaccinationReportMessageDataImportSelection["duplicates"],
  parse?: boolean
): VaccinationReportMessageDataImportSelection["duplicates"] => {
  return normalizeData(
    source,
    (normalizer) => {
      const employees = normalizer("employees", [], "array");
      return {
        employees: employees?.map((employee) =>
          getNormalizedValue(employee, "")
        ),
      };
    },
    parse,
    "VaccinationReportMessageDataImportSelection.Duplicates"
  );
};

export const normalizeVaccinationReportMessageDataImportSelection = (
  source?: VaccinationReportMessageDataImportSelection,
  parse?: boolean
): VaccinationReportMessageDataImportSelection => {
  return normalizeData(
    source,
    () => {
      return {
        selectables: normalizeImportSelectionSelectables(source?.selectables),
        duplicates: normalizeImportSelectionDuplicates(source?.duplicates),
      };
    },
    parse,
    "VaccinationReportMessageDataImportSelection"
  );
};
