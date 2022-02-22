import { VaccinationStatus } from "@/api";

const getStatusName = (status?: VaccinationStatus): string => {
  switch (status) {
    case VaccinationStatus.VACCINATED:
      return "Geimpft";
    case VaccinationStatus.NOT_VACCINATED:
      return "Ungeimpft";
    case VaccinationStatus.SUSPICIOUS_PROOF:
      return "Verdächtiger Nachweis";
    default:
      return "Unbekannt";
  }
};

const getStatusColor = (status?: VaccinationStatus | string): string => {
  switch (status) {
    case VaccinationStatus.VACCINATED:
    case getStatusName(VaccinationStatus.VACCINATED):
      return "green";
    case VaccinationStatus.NOT_VACCINATED:
    case getStatusName(VaccinationStatus.NOT_VACCINATED):
      return "red";
    case VaccinationStatus.SUSPICIOUS_PROOF:
    case getStatusName(VaccinationStatus.SUSPICIOUS_PROOF):
      return "warning";
    default:
      return "gray";
  }
};

const vaccinationConstants = {
  getStatusColor,
  getStatusName,
};

export default vaccinationConstants;
