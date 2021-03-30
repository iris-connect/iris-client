import {
  ExistingDataRequestClientWithLocationList,
  ExistingDataRequestClientWithLocationStatusEnum,
} from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";

function hoursAgo(hours = 0) {
  const d = new Date();
  d.setHours(d.getHours() - hours);
  return d.toISOString();
}

export const dummyDataRequests: ExistingDataRequestClientWithLocationList = {
  dataRequests: [
    {
      externalRequestId: "pizza-mio-123",
      start: hoursAgo(3),
      end: hoursAgo(0),
      code: "PZMIO123",
      locationInformation: dummyLocations[0],
      name: "Fall 12638",
      requestDetails: "Bitte ignorieren Sie die...",
      status: ExistingDataRequestClientWithLocationStatusEnum.DataRequested,
    },
    {
      externalRequestId: "augustiner-456",
      start: hoursAgo(6),
      end: hoursAgo(4),
      code: "AUGUS345",
      locationInformation: dummyLocations[1],
      name: "Fall 63736",
      requestDetails: "Bitte beachten Sie, dass...",
      status: ExistingDataRequestClientWithLocationStatusEnum.DataReceived,
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
    },
  ],
};
