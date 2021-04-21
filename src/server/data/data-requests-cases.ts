import {
  DataRequestCaseDetails,
  DataRequestCaseDetailsStatusEnum,
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

export const dummyDataRequestsCases: Array<DataRequestCaseDetails> = [
  {
    status: DataRequestCaseDetailsStatusEnum.DataRequested,
    caseId: "12345",
    name: "IndexFallCode123",
    externalCaseId: "12345",
    start: hoursAgo(10),
    comment: "leerer Kommentar",
  },
  {
    status: DataRequestCaseDetailsStatusEnum.DataReceived,
    caseId: "1111111",
    name: "IndexWasWeisich",
    externalCaseId: "1111",
    start: hoursAgo(3),
    end: hoursAgo(1),
    comment: "das könnte ihr Kommentar sein",
  },
  {
    status: DataRequestCaseDetailsStatusEnum.Closed,
    caseId: "2233",
    name: "IndexBezeichner1",
    externalCaseId: "22",
    start: hoursAgo(2),
    comment: "leer",
  },
];

export const dummyDataCaseDetails: DataRequestCaseDetails = {
  status: DataRequestCaseDetailsStatusEnum.DataRequested,
  caseId: "12345",
  name: "IndexFallCode123",
  externalCaseId: "12345",
  start: hoursAgo(5),
  end: hoursAgo(1),
  comment: "leerer Kommentar",
};

export const getDummyDetailsCasesWithStatus = (
  id: string
): DataRequestCaseDetails => {
  const dataRequest = dummyDataRequestsCases?.find(
    (request) => request.externalCaseId === id
  );
  if (dataRequest) {
    const status = (<unknown>(
      dataRequest.status
    )) as DataRequestCaseDetailsStatusEnum;
    return {
      ...dummyDataCaseDetails,
      ...dataRequest,
      status,
    };
  }
  console.log(dummyDataCaseDetails);
  return dummyDataCaseDetails;
};
