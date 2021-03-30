import {
  ExistingDataRequestClientWithLocation,
  DataRequestDetails,
  DataRequestDetailsStatusEnum,
  Sex,
} from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";

export const dummyDataRequest: ExistingDataRequestClientWithLocation = {
  externalRequestId: "external-id",
  start: new Date().toDateString(),
  end: new Date().toDateString(),
  locationInformation: dummyLocations[0],
};

// copied from another branch, remove to avoid merge conflict
function hoursAgo(hours = 0) {
  const d = new Date();
  d.setHours(d.getHours() - hours);
  return d.toISOString();
}

export const dummyDataDetails: DataRequestDetails = {
  status: DataRequestDetailsStatusEnum.DataRequested,
  code: "ABCDE",
  name: "TestLokalität",
  externalRequestId: "12345",
  start: hoursAgo(5),
  end: hoursAgo(1),
  requestDetails: "leer",
  guests: [
    {
      firstName: "Max",
      lastName: "Mustermann",
      email: "max@example.de",
      phone: "01234 000000",
      mobilePhone: "0123 0815",
      sex: Sex.Male,
      attendanceInformation: {
        attendFrom: hoursAgo(9),
        attendTo: hoursAgo(3),
      },
    },
    {
      firstName: "Martina",
      lastName: "Mustermann",
      email: "a@b.de",
      phone: "01234 567890",
      mobilePhone: "0123 456789",
      sex: Sex.Female,
      attendanceInformation: {
        attendFrom: hoursAgo(10),
        attendTo: hoursAgo(8),
      },
    },
  ],
  dataProvider: {
    name: "GanzTolleApp",
    address: {},
  },
  additionalInformation: "keine",
  startDate: hoursAgo(4),
  endDate: hoursAgo(3),
};
