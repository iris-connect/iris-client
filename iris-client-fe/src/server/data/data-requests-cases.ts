import {
  ContactCategory,
  DataRequestCaseData,
  DataRequestStatus,
  Sex,
} from "@/api";
import { daysAgo, hoursAgo } from "@/server/utils/date";

export const dummyDataRequestsCases: Array<DataRequestCaseData> = [
  {
    caseId: "12345",
    name: "IndexNummer12345",
    externalCaseId: "12345",
    start: daysAgo(3),
    comment: "leerer Kommentar",
    submissionUri: "https://12345.proxy.exampleUrl.de",
    status: DataRequestStatus.DataReceived,
  },
  {
    caseId: "1111",
    name: "ReisenderImZug1234",
    externalCaseId: "1111",
    start: hoursAgo(3),
    end: hoursAgo(1),
    comment: "das könnte ihr Kommentar sein",
    submissionUri: "https://1111.proxy.exampleUrl.de",
    status: DataRequestStatus.DataRequested,
  },
  {
    caseId: "2233",
    name: "IndexBezeichner1",
    externalCaseId: "2233",
    start: hoursAgo(2),
    comment: "leer",
    submissionUri: "https://2233.proxy.exampleUrl.de",
    status: DataRequestStatus.Closed,
  },
];

export const dummyDataCaseDetails: DataRequestCaseData = {
  externalCaseId: "12345",
  name: "IndexFallCode123",
  comment: "leerer Kommentar",
  start: hoursAgo(15),
  end: hoursAgo(1),
  submissionUri: "https://12345.proxy.exampleUrl.de",
  submissionData: {
    contacts: {
      contactPersons: [
        {
          firstName: "Florian",
          lastName: "Bürger",
          dateOfBirth: "7.10.1970",
          sex: Sex.Male,
          email: "flori.an@buer.ger",
          phone: "012345 112233",
          mobilePhone: "0114477 774411",
          contactInformation: {
            firstContactDate: hoursAgo(15),
            lastContactDate: hoursAgo(4),
            contactCategory: ContactCategory.HighRisk,
            basicConditions: "Enger Kontakt, ohne Maske",
          },
          workPlace: {
            name: "Musterfirma",
            phone: "012345 112233",
            pointOfContact: "Arbeitsplatz",
            address: {
              city: "Musterstadt",
              street: "Teststraße",
              houseNumber: "23",
              zipCode: "12345",
            },
          },
        },
        {
          firstName: "Sara",
          lastName: "Mustermann",
          dateOfBirth: "1.1.2000",
          sex: Sex.Female,
          email: "sara.muster@ma.nn",
          mobilePhone: "01133 55669988",
          address: {
            street: "Hasenstrasse",
            houseNumber: "12",
            zipCode: "12345",
            city: "Hasenstadt",
          },
        },
        {
          firstName: "Niklas",
          lastName: "Mustermann",
          dateOfBirth: "20.10.1999",
          sex: Sex.Male,
          phone: "01122 2226555",
        },
      ],
      startDate: hoursAgo(10),
      endDate: hoursAgo(3),
    },
    events: {
      events: [
        {
          name: "Geburtstagsfeier",
          phone: "0123 321456",
          address: {
            street: "Hasenstrasse",
            houseNumber: "12",
            zipCode: "12345",
            city: "Hasenstadt",
          },
          additionalInformation: "Familienfeier im Garten",
        },
        {
          name: "Fussballspiel",
          additionalInformation: "Regionalliga Spiel",
        },
      ],
      startDate: hoursAgo(5),
      endDate: hoursAgo(1),
    },
    dataProvider: {
      firstName: "Max",
      lastName: "Mustermann",
      dateOfBirth: "01.01.1970",
    },
  },
};

export const getDummyDetailsCases = (id: string): DataRequestCaseData => {
  const dataRequest = dummyDataRequestsCases?.find(
    (request) => request.externalCaseId === id
  );
  if (dataRequest) {
    return {
      ...dummyDataCaseDetails,
      ...dataRequest,
    };
  }
  return dummyDataCaseDetails;
};
