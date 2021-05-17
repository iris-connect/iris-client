import {
  ExistingDataRequestClientWithLocationList,
  DataRequestDetails,
  Sex,
  Guest,
  GuestList,
  DataRequestStatus,
} from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";
import { daysAgo, hoursAgo } from "@/server/utils/date";

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
      status: DataRequestStatus.DataRequested,
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
      status: DataRequestStatus.DataReceived,
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
      status: DataRequestStatus.Closed,
      lastUpdatedAt: hoursAgo(7),
      requestedAt: hoursAgo(9),
    },
    {
      externalRequestId: "bowling-457",
      start: hoursAgo(3),
      end: hoursAgo(1),
      code: "BOWL457",
      locationInformation: dummyLocations[2],
      name: "Fall 91247",
      requestDetails: "Tisch 9",
      status: DataRequestStatus.Aborted,
      lastUpdatedAt: hoursAgo(1),
      requestedAt: hoursAgo(4),
    },
  ],
};

export const dummyDataDetails: DataRequestDetails = {
  status: DataRequestStatus.Aborted,
  code: "ABCDE",
  name: "TestLokalität",
  externalRequestId: "12345",
  start: hoursAgo(5),
  end: hoursAgo(1),
  requestDetails: "leer",
  lastModifiedAt: hoursAgo(4),
  requestedAt: hoursAgo(6),
  submissionData: {
    guests: [
      {
        firstName: "Max",
        lastName: "Mustermann",
        email: "max@example.de",
        phone: "01234 000000",
        mobilePhone: "0123 0815",
        sex: Sex.Male,
        address: {
          street: "Universitätsplatz",
          houseNumber: "1",
          zipCode: "39104",
          city: "Magdeburg",
        },
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
        address: {
          street: "Universitätsplatz",
          houseNumber: "1",
          zipCode: "39104",
          city: "Magdeburg",
        },
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
  },
  locationInformation: dummyLocations[1],
  comment: "",
};

export const getDummyDetailsWithStatus = (id: string): DataRequestDetails => {
  const dataRequest = dummyDataRequests.dataRequests?.find(
    (request) => request.code === id
  );
  if (dataRequest) {
    const status = dataRequest.status;
    const guests: Guest[] =
      status !== DataRequestStatus.DataRequested &&
      status !== DataRequestStatus.Aborted
        ? dummyDataDetails?.submissionData?.guests ?? []
        : [];
    return {
      ...dummyDataDetails,
      ...dataRequest,
      status,
      submissionData: <GuestList | undefined>{
        ...dummyDataDetails.submissionData,
        guests,
      },
    };
  }
  return dummyDataDetails;
};
