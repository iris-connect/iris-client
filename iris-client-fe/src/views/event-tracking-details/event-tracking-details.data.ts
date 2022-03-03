import {
  Address,
  DataRequestDetails,
  Guest,
  GuestAllOfAttendanceInformation,
  GuestList,
  GuestListDataProvider,
  LocationAddress,
  LocationContact,
  LocationContext,
  LocationInformation,
} from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import _isNil from "lodash/isNil";
import { normalizeMetaData } from "@/common/normalizer";

const normalizeLocationAddress = (
  source?: LocationAddress,
  parse?: boolean
): LocationAddress => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<LocationAddress> = {
        street: normalizer("street", ""),
        city: normalizer("city", ""),
        zip: normalizer("zip", ""),
      };
      return normalized;
    },
    parse,
    "LocationAddress"
  );
};

const normalizeLocationContact = (
  source?: LocationContact,
  parse?: boolean
): LocationContact => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<LocationContact> = {
        officialName: normalizer("officialName", undefined),
        representative: normalizer("representative", undefined),
        address: normalizeLocationAddress(source?.address),
        ownerEmail: normalizer("ownerEmail", undefined),
        email: normalizer("email", undefined),
        phone: normalizer("phone", undefined),
      };
      return normalized;
    },
    parse,
    "LocationContact"
  );
};

const normalizeLocationContext = (
  source?: LocationContext,
  parse?: boolean
): LocationContext => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<LocationContext> = {
        id: normalizer("id", ""),
        name: normalizer("name", ""),
      };
      return normalized;
    },
    parse,
    "LocationContext"
  );
};

export const normalizeLocationInformation = (
  source?: LocationInformation,
  parse?: boolean
): LocationInformation => {
  return normalizeData(
    source,
    (normalizer) => {
      const contexts = normalizer("contexts", undefined, "array");
      const normalized: Complete<LocationInformation> = {
        id: normalizer("id", ""),
        providerId: normalizer("providerId", ""),
        name: normalizer("name", ""),
        publicKey: normalizer("publicKey", undefined),
        contact: normalizeLocationContact(source?.contact),
        contexts: contexts?.map((context) => normalizeLocationContext(context)),
      };
      return normalized;
    },
    parse,
    "LocationInformation"
  );
};

export const normalizeAddress = (
  source?: Address,
  parse?: boolean
): Address => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<Address> = {
        street: normalizer("street", undefined),
        houseNumber: normalizer("houseNumber", undefined),
        zipCode: normalizer("zipCode", undefined),
        city: normalizer("city", undefined),
      };
      return normalized;
    },
    parse,
    "Address"
  );
};

const normalizeGuestAllOfAttendanceInformation = (
  source?: GuestAllOfAttendanceInformation,
  parse?: boolean
): GuestAllOfAttendanceInformation => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<GuestAllOfAttendanceInformation> = {
        descriptionOfParticipation: normalizer(
          "descriptionOfParticipation",
          undefined
        ),
        attendFrom: normalizer("attendFrom", ""),
        attendTo: normalizer("attendTo", ""),
        additionalInformation: normalizer("additionalInformation", undefined),
      };
      return normalized;
    },
    parse,
    "GuestAllOfAttendanceInformation"
  );
};

const normalizeGuest = (source?: Guest, parse?: boolean): Guest => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<Guest> = {
        firstName: normalizer("firstName", ""),
        lastName: normalizer("lastName", ""),
        dateOfBirth: normalizer("dateOfBirth", undefined),
        sex: normalizer("sex", undefined),
        email: normalizer("email", undefined),
        phone: normalizer("phone", undefined),
        mobilePhone: normalizer("mobilePhone", undefined),
        address: !_isNil(source?.address)
          ? normalizeAddress(source?.address)
          : source?.address,
        attendanceInformation: normalizeGuestAllOfAttendanceInformation(
          source?.attendanceInformation
        ),
        identityChecked: normalizer("identityChecked", undefined, "boolean"),
      };
      return normalized;
    },
    parse,
    "Guest"
  );
};

const normalizeGuestListDataProvider = (
  source?: GuestListDataProvider,
  parse?: boolean
): GuestListDataProvider => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<GuestListDataProvider> = {
        name: normalizer("name", ""),
        address: normalizeAddress(source?.address),
      };
      return normalized;
    },
    parse,
    "GuestListDataProvider"
  );
};

const normalizeGuestList = (source?: GuestList, parse?: boolean): GuestList => {
  return normalizeData(
    source,
    (normalizer) => {
      const guests = normalizer("guests", [], "array");
      const normalized: Complete<GuestList> = {
        guests: guests.map((guest) => normalizeGuest(guest, true)),
        dataProvider: normalizeGuestListDataProvider(source?.dataProvider),
        additionalInformation: normalizer("additionalInformation", undefined),
        startDate: normalizer("startDate", undefined, "dateString"),
        endDate: normalizer("endDate", undefined, "dateString"),
      };
      return normalized;
    },
    parse,
    "GuestList"
  );
};

export const normalizeDataRequestDetails = (
  source?: DataRequestDetails,
  parse?: boolean
): DataRequestDetails => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        comment: normalizer("comment", undefined),
        status: normalizer("status", undefined),
        code: normalizer("code", undefined),
        name: normalizer("name", undefined),
        externalRequestId: normalizer("externalRequestId", undefined),
        start: normalizer("start", undefined, "dateString"),
        end: normalizer("end", undefined, "dateString"),
        requestedAt: normalizer("requestedAt", undefined, "dateString"),
        requestDetails: normalizer("requestDetails", undefined),
        locationInformation: source?.locationInformation
          ? normalizeLocationInformation(source?.locationInformation)
          : source?.locationInformation,
        submissionData: source?.submissionData
          ? normalizeGuestList(source?.submissionData)
          : undefined,
        ...normalizeMetaData(source),
      };
    },
    parse,
    "DataRequestDetails"
  );
};
