import {
  VaccinationRecordDetails,
  VaccinationStatus,
  VrCompanyEmployee,
} from "@/api";
import { daysAgo } from "@/server/utils/date";

const employees: VrCompanyEmployee[] = [
  {
    firstName: "Mit",
    lastName: "Arbeiter",
    street: "Mitarbeiterstraße",
    houseNumber: "5",
    zipCode: "12345",
    city: "Mitarbeiterstadt",
    vaccination: "Covid-19",
    vaccinationStatus: VaccinationStatus.VACCINATED,
  },
  {
    firstName: "Ohne",
    lastName: "Arbeiterin",
    street: "Ohnearbeiterstraße",
    houseNumber: "15",
    zipCode: "54321",
    city: "Ohne-Stadt",
    vaccination: "Covid-19",
    vaccinationStatus: VaccinationStatus.NOT_VACCINATED,
  },
  {
    firstName: "Frag",
    lastName: "Würdig",
    street: "Unsersiösplatz",
    houseNumber: "53",
    zipCode: "11111",
    city: "Mitarbeiterstadt",
    vaccination: "Covid-19",
    vaccinationStatus: VaccinationStatus.SUSPICIOUS_PROOF,
  },
];

export const vaccinationRecordList: VaccinationRecordDetails[] = [
  {
    id: "1",
    company: {
      name: "Musterfirma",
      street: "Musterstraße",
      houseNumber: "24",
      zipCode: "12345",
      city: "Musterstadt",
      contactPerson: {
        firstName: "Max",
        lastName: "Mustermann",
        eMail: "max@muster.de",
        phone: "06973928393",
      },
    },
    employees: employees,
    reportedAt: daysAgo(3),
  },
  {
    id: "2",
    company: {
      name: "Beispielbetrieb",
      street: "Betriebstraße",
      houseNumber: "3",
      zipCode: "11111",
      city: "Behausen",
      contactPerson: {
        firstName: "Bärbel",
        lastName: "Musterfrau",
        eMail: "baerbel@muster.de",
        phone: "12343928393",
      },
    },
    employees: employees,
    reportedAt: daysAgo(1),
  },
];
