import {
  Page,
  VaccinationRecord,
  VaccinationRecordDetails,
  VaccinationStatus,
  VaccinationStatusCount,
  VREmployee,
  VRFacility,
  VRFacilityContactPerson,
} from "@/api";
import { Complete, normalizeData } from "@/utils/data";
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
        vaccinationStatus: normalizer(
          "vaccinationStatus",
          VaccinationStatus.UNKNOWN
        ),
      };
    },
    parse,
    "VRFacility"
  );
};

export const normalizeVaccinationRecordDetails = (
  source?: VaccinationRecordDetails,
  parse?: boolean
): VaccinationRecordDetails => {
  return normalizeData(
    source,
    (normalizer) => {
      const employees = normalizer("employees", undefined, "array") || [];
      const record = normalizeVaccinationRecord(source);
      return {
        ...(record as Complete<typeof record>),
        employees: employees.map((employee) => normalizeVREmployee(employee)),
      };
    },
    parse,
    "VaccinationRecordDetails"
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
      return {
        [VaccinationStatus.VACCINATED]: normalizer(
          VaccinationStatus.VACCINATED,
          undefined,
          "number"
        ),
        [VaccinationStatus.NOT_VACCINATED]: normalizer(
          VaccinationStatus.NOT_VACCINATED,
          undefined,
          "number"
        ),
        [VaccinationStatus.SUSPICIOUS_PROOF]: normalizer(
          VaccinationStatus.SUSPICIOUS_PROOF,
          undefined,
          "number"
        ),
        [VaccinationStatus.UNKNOWN]: normalizer(
          VaccinationStatus.UNKNOWN,
          undefined,
          "number"
        ),
      };
    },
    parse,
    "VaccinationStatusCount"
  );
};

export const normalizeVaccinationRecord = (
  source?: VaccinationRecord,
  parse?: boolean
): VaccinationRecord => {
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
    "VaccinationRecord"
  );
};

export const normalizePageVaccinationRecord = (
  source?: Page<VaccinationRecord>,
  parse?: boolean
): Page<VaccinationRecord> => {
  return normalizePage(normalizeVaccinationRecord, source, parse);
};
