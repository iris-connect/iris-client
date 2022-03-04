import { VaccinationExtendedStatus } from "@/api";

const getStatusName = (status?: string): string => {
  switch (status) {
    case VaccinationExtendedStatus.VACCINATED:
      return "Geimpft";
    case VaccinationExtendedStatus.NOT_VACCINATED:
      return "Ungeimpft";
    case VaccinationExtendedStatus.SUSPICIOUS_PROOF:
      return "Verdächtiger Nachweis";
    default:
      return "Unbekannt";
  }
};

const getStatusColor = (status?: string): string => {
  switch (status) {
    case VaccinationExtendedStatus.VACCINATED:
    case getStatusName(VaccinationExtendedStatus.VACCINATED):
      return "green";
    case VaccinationExtendedStatus.NOT_VACCINATED:
    case getStatusName(VaccinationExtendedStatus.NOT_VACCINATED):
      return "red";
    case VaccinationExtendedStatus.SUSPICIOUS_PROOF:
    case getStatusName(VaccinationExtendedStatus.SUSPICIOUS_PROOF):
      return "warning";
    default:
      return "gray";
  }
};

const vaccinationReportConstants = {
  getStatusColor,
  getStatusName,
};

export default vaccinationReportConstants;
