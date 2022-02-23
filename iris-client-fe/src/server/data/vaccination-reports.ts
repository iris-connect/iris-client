import {
  Address,
  VaccinationReport,
  VaccinationReportDetails,
  VaccinationStatus,
  VREmployee,
  VRFacilityContactPerson,
} from "@/api";
import { daysAgo } from "@/server/utils/date";
import { getEnumKeys } from "@/utils/data";
import _sample from "lodash/sample";
import _random from "lodash/random";
import _times from "lodash/times";

const createAddress = (): Address => {
  return {
    street: _sample(["Dunkle Gasse", "Hauptstraße", "Testweg", "Tastallee"]),
    houseNumber: `${_random(1, 20)}`,
    zipCode: `${_random(10000, 99999)}`,
    city: _sample(["Muffhausen", "Testort", "Testing", "Einsiedlerhof"]),
  };
};

const createEmployee = (): VREmployee => {
  const enumKeys = getEnumKeys(VaccinationStatus);
  return {
    firstName: _sample(["Max", "Bärbel", "Gabi", "Lena", "Klaus"]),
    lastName: _sample(["Muster", "Kulli", "Glasklar", "Taff"]),
    address: createAddress(),
    vaccination: "Covid-19",
    vaccinationStatus:
      VaccinationStatus[enumKeys[_random(0, enumKeys.length - 1)]],
  };
};

const getVaccinationStatusCount = (
  employees: VREmployee[]
): VaccinationReport["vaccinationStatusCount"] => {
  const count: VaccinationReport["vaccinationStatusCount"] = {};
  for (const value of getEnumKeys(VaccinationStatus)) {
    count[VaccinationStatus[value]] = employees.filter(
      (e) => e.vaccinationStatus === VaccinationStatus[value]
    ).length;
  }
  return count;
};

const createContactPerson = (): VRFacilityContactPerson => {
  return {
    firstName: _sample(["Max", "Gustav", "Gabi", "Lena", "Klaus"]),
    lastName: _sample(["Muster", "Testor", "Glasklar", "Taff"]),
    eMail: _sample(["max@muster.de", "test@beispiel.com"]),
    phone: _sample(["06973928393", "1235612446"]),
  };
};

const createReport = (id: string) => {
  const employees = _times(_random(1, 20), createEmployee);
  return {
    id,
    facility: {
      name: _sample([
        "Müsterfirmá",
        "Beispielbetrieb",
        "Testfabrik",
        "Pflegeheim",
        "Beispiel:Ünternehmen",
        "Test.Corp",
      ]),
      address: createAddress(),
      contactPerson: createContactPerson(),
    },
    employees: employees,
    reportedAt: daysAgo(_random(1, 5)),
    vaccinationStatusCount: getVaccinationStatusCount(employees),
  };
};

export const vaccinationReportList: VaccinationReportDetails[] = [
  createReport("1"),
  createReport("2"),
  createReport("3"),
  createReport("4"),
  createReport("5"),
];
