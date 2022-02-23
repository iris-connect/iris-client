import {
  Page,
  VaccinationReport,
  VaccinationReportDetails,
  VaccinationStatus,
  VaccinationStatusCount,
  VREmployee,
  VRFacility,
  VRFacilityContactPerson,
} from "@/api";
import { Complete, getEnumKeys, normalizeData } from "@/utils/data";
import { normalizeAddress } from "@/views/event-tracking-details/event-tracking-details.data";
import { normalizePage } from "@/common/normalizer";

export const normalizeVREmployee = (
  source?: VREmployee,
  parse?: boolean
): VREmployee => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        firstName: normalizer("firstName", undefined),
        lastName: normalizer("lastName", undefined),
        address: normalizeAddress(source?.address),
        vaccination: normalizer("vaccination", undefined),
        vaccinationStatus: normalizer("vaccinationStatus", undefined),
      };
    },
    parse,
    "VRFacility"
  );
};

export const normalizeVaccinationReportDetails = (
  source?: VaccinationReportDetails,
  parse?: boolean
): VaccinationReportDetails => {
  return normalizeData(
    source,
    (normalizer) => {
      const employees = normalizer("employees", undefined, "array") || [];
      const report = normalizeVaccinationReport(source);
      return {
        ...(report as Complete<typeof report>),
        employees: employees.map((employee) => normalizeVREmployee(employee)),
      };
    },
    parse,
    "VaccinationReportDetails"
  );
};

export const normalizeVRFacilityContactPerson = (
  source?: VRFacilityContactPerson,
  parse?: boolean
): VRFacilityContactPerson => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        firstName: normalizer("firstName", undefined),
        lastName: normalizer("lastName", undefined),
        eMail: normalizer("eMail", undefined),
        phone: normalizer("phone", undefined),
      };
    },
    parse,
    "VRFacility"
  );
};

export const normalizeVRFacility = (
  source?: VRFacility,
  parse?: boolean
): VRFacility => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        name: normalizer("name", ""),
        address: normalizeAddress(source?.address),
        contactPerson: normalizeVRFacilityContactPerson(source?.contactPerson),
      };
    },
    parse,
    "VRFacility"
  );
};

export const normalizeVaccinationStatusCount = (
  source?: VaccinationStatusCount,
  parse?: boolean
): VaccinationStatusCount => {
  return normalizeData(
    source,
    (normalizer) => {
      const count: VaccinationStatusCount = {};
      getEnumKeys(VaccinationStatus).forEach((key) => {
        const status = VaccinationStatus[key];
        count[status] = normalizer(status, undefined, "number");
      });
      return count as Complete<typeof count>;
    },
    parse,
    "VaccinationStatusCount"
  );
};

export const normalizeVaccinationReport = (
  source?: VaccinationReport,
  parse?: boolean
): VaccinationReport => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        id: normalizer("id", ""),
        facility: normalizeVRFacility(source?.facility),
        reportedAt: normalizer("reportedAt", undefined, "dateString"),
        vaccinationStatusCount: normalizeVaccinationStatusCount(
          source?.vaccinationStatusCount
        ),
      };
    },
    parse,
    "VaccinationReport"
  );
};

export const normalizePageVaccinationReport = (
  source?: Page<VaccinationReport>,
  parse?: boolean
): Page<VaccinationReport> => {
  return normalizePage(normalizeVaccinationReport, source, parse);
};
