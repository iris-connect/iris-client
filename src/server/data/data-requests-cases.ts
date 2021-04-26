import {
  DataRequestCaseDetails,
  DataRequestCaseData,
  DataRequestCaseDetailsStatusEnum,
  Sex,
} from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";

function daysAgo(days = 0, date = new Date().toISOString()) {
  // could be that
  const d = new Date(date);
  d.setDate(d.getDate() - days);
  return d.toISOString();
}

function hoursAgo(hours = 0, date = new Date().toISOString()) {
  const d = new Date(date);
  d.setHours(d.getHours() - hours);
  return d.toISOString();
}

export const dummyDataRequestsCases: Array<DataRequestCaseData> = [
  {
    name: "IndexFallCode123",
    externalCaseId: "12345",
    start: hoursAgo(10),
    comment: "leerer Kommentar",
  },
  {
    name: "IndexWasWeisich",
    externalCaseId: "1111",
    start: hoursAgo(3),
    end: hoursAgo(1),
    comment: "das könnte ihr Kommentar sein",
  },
  {
    name: "IndexBezeichner1",
    externalCaseId: "2233",
    start: hoursAgo(2),
    comment: "leer",
  },
];

export const dummyDataCaseDetails: DataRequestCaseData = {
  externalCaseId: "12345",
  name: "IndexFallCode123",
  start: hoursAgo(5),
  end: hoursAgo(1),
  comment: "leerer Kommentar",
  submissionData: {
    contacts: {
      contactPersons: [
        {
          firstName: "Vorname 1",
          lastName: "Nachname 1",
        },
        {
          firstName: "Vorname 2",
          lastName: "Nachname 2",
        },
        {
          firstName: "Vorname 3",
          lastName: "Nachname 3",
        },
      ],
    },
    events: {
      events: [
        {
          name: "Event 1",
        },
        {
          name: "Event 2",
        },
      ],
    },
    dataProvider: {
      firstName: "firstName",
      lastName: "lastName",
      dateOfBirth: "test",
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
