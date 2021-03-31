import {
  ExistingDataRequestClientWithLocationList,
  ExistingDataRequestClientWithLocationStatusEnum,
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

export const dummyDataRequests: ExistingDataRequestClientWithLocationList = {
  dataRequests: [
    {
      externalRequestId: "pizza-mio-123",
      start: daysAgo(4, hoursAgo(3)),
      end: daysAgo(4, hoursAgo(1)),
      code: "PZMIO123",
      locationInformation: dummyLocations[0],
      name: "Fall 12638",
      requestDetails: "Bitte ignorieren Sie die...",
      status: ExistingDataRequestClientWithLocationStatusEnum.DataRequested,
      lastUpdatedAt: hoursAgo(1),
      requestedAt: hoursAgo(2),
    },
    {
      externalRequestId: "augustiner-456",
      start: daysAgo(6, hoursAgo(6)),
      end: daysAgo(6, hoursAgo(3)),
      code: "AUGUS345",
      locationInformation: dummyLocations[1],
      name: "Fall 63736",
      requestDetails: "Bitte beachten Sie, dass...",
      status: ExistingDataRequestClientWithLocationStatusEnum.DataReceived,
      lastUpdatedAt: hoursAgo(4),
      requestedAt: hoursAgo(6),
    },
    {
      externalRequestId: "bowling-456",
      start: hoursAgo(8),
      end: hoursAgo(7),
      code: "BOWL345",
      locationInformation: dummyLocations[2],
      name: "Fall 85938",
      requestDetails: "Tisch 7",
      status: ExistingDataRequestClientWithLocationStatusEnum.Closed,
      lastUpdatedAt: hoursAgo(7),
      requestedAt: hoursAgo(9),
    },
  ],
};
