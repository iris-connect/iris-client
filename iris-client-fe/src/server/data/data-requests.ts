import {
  DataRequestDetails,
  Sex,
  Guest,
  GuestList,
  DataRequestStatus,
  ExistingDataRequestClientWithLocation,
} from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";
import { daysAgo, hoursAgo } from "@/server/utils/date";

export const dummyDataRequests: Array<ExistingDataRequestClientWithLocation> = [
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
    requestDetails:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer vel velit dictum, congue felis eget, ullamcorper felis. Donec semper arcu a nibh placerat, ut hendrerit arcu ultrices. Curabitur eget libero vitae mauris condimentum ullamcorper at ut dui. Praesent volutpat lectus ut augue fringilla bibendum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam maximus nunc sem, ac accumsan massa tristique et. Nam non elit ut felis volutpat dignissim. In egestas scelerisque lectus, et maximus massa vehicula in.Nunc ac dui mattis, ultrices nulla sit amet, rhoncus magna. Curabitur ultricies varius lacus quis aliquam. Aenean vitae finibus risus. Nunc id sapien porttitor nisi pharetra bibendum quis sed nunc. Etiam viverra ex nec venenatis lacinia. Ut et accumsan enim. Maecenas eget aliquam nisl. Phasellus porttitor accumsan lorem et tristique. Suspendisse ac risus non lacus placerat pretium.Sed non ante eu urna varius varius quis at enim. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Morbi sed ante at tellus bibendum interdum eu quis ante. Etiam et nisl lectus. Cras ultricies mauris quis sapien sagittis facilisis. Vestibulum sollicitudin lacus in arcu accumsan consequat. Mauris sed interdum tortor. Mauris at imperdiet massa. In malesuada elementum laoreet. Praesent ultrices sit amet lectus ac imperdiet. Sed nunc nisi, porta sit amet augue sed, posuere tempor lacus. Proin consectetur, magna in sodales molestie, augue est bibendum nibh, non mattis est justo nec ex. Aliquam ultrices purus vitae maximus lobortis.Etiam ullamcorper euismod enim, vel interdum lacus sagittis eu. Donec faucibus eget risus eget gravida. Vivamus semper aliquet commodo. Integer mollis euismod erat, sit amet aliquam leo sodales vitae. Morbi cursus et justo ut ultricies. Nunc feugiat eget velit at tincidunt. Quisque et vulputate mauris, et bibendum nibh. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque feugiat ultricies dui ut imperdiet.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse eget neque et lorem rutrum rhoncus. Sed et ex quis arcu aliquam luctus vitae molestie nisl. Donec cursus velit at lacinia lobortis. Praesent ac mauris in elit volutpat feugiat sit amet ac ipsum. Sed sed justo fringilla, ornare ipsum ac, pharetra nisi. Duis sed ornare elit. Etiam dolor magna, porttitor nec mollis elementum, fringilla sit amet sem. Aenean ac augue nunc. Nunc fringilla dolor eget malesuada dignissim. Sed a libero quis orci egestas euismod eu vel felis. Sed ut nulla dui. Morbi tincidunt leo massa, sit amet congue nunc condimentum vitae.Generated 5 paragraphs, 403 words, 2666 bytes of Lorem Ipsum",
    status: DataRequestStatus.Aborted,
    lastUpdatedAt: hoursAgo(1),
    requestedAt: hoursAgo(4),
  },
];

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
      {
        firstName: " Must\"er'm'an´;=,n",
        lastName: "=?+-@!*/\\%€%=@+µMaxßüäö;",
        email: "=max@e\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nxample.@.de",
        phone: "=01234 000000",
        mobilePhone: "=0123 0815",
        sex: Sex.Male,
        address: {
          street: "=Universitätsplatz",
          houseNumber: "=1",
          zipCode: "=39104",
          city: "=Magdeburg",
        },
        attendanceInformation: {
          attendFrom: hoursAgo(9),
          attendTo: hoursAgo(5),
          additionalInformation: "=(Security-Test)",
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
  const dataRequest = dummyDataRequests.find((request) => request.code === id);
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
