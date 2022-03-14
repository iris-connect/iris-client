import { VaccinationExtendedStatus, VaccinationType } from "@/api";

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

const getVaccinationType = (type?: string): string => {
  switch (type) {
    case VaccinationType.COVID_19:
      return "Covid-19";
    default:
      return VaccinationType.COVID_19;
  }
};

const vaccinationReportConstants = {
  getStatusColor,
  getStatusName,
  getVaccinationType,
};

export default vaccinationReportConstants;
