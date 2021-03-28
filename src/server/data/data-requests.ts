import { ExistingDataRequestClientWithLocation } from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";

export const dummyDataRequest: ExistingDataRequestClientWithLocation = {
  externalRequestId: "external-id",
  start: new Date().toDateString(),
  end: new Date().toDateString(),
  locationInformation: dummyLocations[0],
};
