import {
  Page,
  VaccinationExtendedStatus,
  VaccinationReport,
  VaccinationReportDetails,
  VaccinationStatusCount,
  VREmployee,
  VRFacility,
  VRFacilityContactPerson,
} from "@/api";
import { Complete, getEnumKeys, normalizeData } from "@/utils/data";
import { normalizeAddress } from "@/views/event-tracking-details/event-tracking-details.data";
import { normalizePage } from "@/common/normalizer";

export const normalizeVREmployee = (source?: VREmployee, parse?: boolean) => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        firstName: normalizer("firstName", undefined),
        lastName: normalizer("lastName", undefined),
        address: normalizeAddress(source?.address),
        vaccination: normalizer("vaccination", undefined),
        vaccinationStatus: normalizer("vaccinationStatus", undefined),
        eMail: normalizer("eMail", undefined),
        phone: normalizer("phone", undefined),
        dateOfBirth: normalizer("dateOfBirth", undefined, "dateString"),
        sex: normalizer("sex", undefined),
      };
    },
    parse,
    "VRFacility"
  );
};

export const normalizeVaccinationReportDetails = (
  source?: VaccinationReportDetails,
  parse?: boolean
) => {
  return normalizeData(
    source,
    (normalizer) => {
      const employees = normalizer("employees", undefined, "array") || [];
      return {
        ...normalizeVaccinationReport(source),
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
) => {
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

export const normalizeVRFacility = (source?: VRFacility, parse?: boolean) => {
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
) => {
  return normalizeData(
    source,
    (normalizer) => {
      const count: VaccinationStatusCount = {};
      getEnumKeys(VaccinationExtendedStatus).forEach((key) => {
        const status = VaccinationExtendedStatus[key];
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
) => {
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
) => {
  return normalizePage(normalizeVaccinationReport, source, parse);
};
