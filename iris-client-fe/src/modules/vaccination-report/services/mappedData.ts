import {
  VaccinationReport,
  VaccinationStatus,
  VaccinationStatusCount,
  VREmployee,
} from "@/api";
import { getFormattedAddress } from "@/utils/address";
import _sum from "lodash/sum";
import _values from "lodash/values";
import { getFormattedDate } from "@/utils/date";
import { getEnumKeys } from "@/utils/data";
import vaccinationReportConstants from "@/modules/vaccination-report/services/constants";
import Genders from "@/constants/Genders";
import { DataTableHeaders } from "@/components/iris-data-table.vue";

export type VaccinationReportTableRow = {
  id?: string;
  facility: {
    name: string;
  };
  address: string;
  employeeCount: number;
  vaccinationStatusCount?: VaccinationStatusCount;
  reportedAt: string;
};

export type VREmployeeTableRow = {
  id: number | string;
  lastName: string;
  firstName: string;
  address: string;
  vaccination: string;
  vaccinationStatus: string;
  eMail: string;
  phone: string;
  dateOfBirth: string;
  sex: string;
  raw: VREmployee;
};

const getStatusTableHeader = (status: VaccinationStatus) => {
  return {
    text: `#\xa0${vaccinationReportConstants.getStatusName(status)}`,
    value: "vaccinationStatusCount." + status,
    sortable: true,
    width: 0,
  };
};

export const getVaccinationReportTableHeaders = () => [
  { text: "Einrichtung", value: "facility.name", sortable: true },
  { text: "Adresse", value: "address", sortable: false },
  { text: "#\xa0Mitarbeiter", value: "employeeCount", sortable: false },
  ...getEnumKeys(VaccinationStatus).map((s) =>
    getStatusTableHeader(VaccinationStatus[s])
  ),
  { text: "Meldung vom", value: "reportedAt", sortable: true },
];

export const getVREmployeeTableHeaders = (
  selectable: boolean
): DataTableHeaders => {
  const headers = [
    { text: "Nachname", value: "lastName", sortable: true },
    { text: "Vorname", value: "firstName", sortable: true },
    { text: "E-Mail", value: "eMail", sortable: true },
    { text: "Telefon", value: "phone", sortable: true },
    { text: "Impfstatus", value: "vaccinationStatus", sortable: true },
    { text: "", value: "data-table-expand" },
  ];
  return {
    headers: selectable
      ? [{ text: "", value: "data-table-select" }, ...headers]
      : headers,
    expandedHeaders: [
      { text: "Adresse", value: "address" },
      { text: "Geburtsdatum", value: "dateOfBirth" },
      { text: "Geschlecht", value: "sex" },
    ],
  };
};

export const getVaccinationReportTableRows = (
  vaccinationReports: VaccinationReport[] | undefined
): VaccinationReportTableRow[] => {
  return (vaccinationReports || []).map((report) => {
    const { facility } = report;
    return {
      id: report.id,
      facility: {
        name: facility?.name || "-",
      },
      address: getFormattedAddress(facility?.address),
      employeeCount: _sum(_values(report.vaccinationStatusCount)),
      vaccinationStatusCount: report.vaccinationStatusCount,
      reportedAt: getFormattedDate(report.reportedAt),
    };
  });
};

export const getVREmployeeTableRows = (
  employees: VREmployee[] | null | undefined
): VREmployeeTableRow[] => {
  return (employees || []).map((employee, index) => {
    return {
      id: index,
      lastName: employee.lastName || "-",
      firstName: employee.firstName || "-",
      address: getFormattedAddress(employee.address),
      vaccination:
        vaccinationReportConstants.getVaccinationType(employee.vaccination) ||
        "-",
      vaccinationStatus: employee.vaccinationStatus || "-",
      sex: Genders.getName(employee.sex) || "-",
      dateOfBirth: getFormattedDate(employee.dateOfBirth, "L"),
      eMail: employee.eMail || "-",
      phone: employee.phone || "-",
      raw: employee,
    };
  });
};
