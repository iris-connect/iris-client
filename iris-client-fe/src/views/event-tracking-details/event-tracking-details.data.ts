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
import { entryNormalizer, finalizeData } from "@/utils/data";
import _isNil from "lodash/isNil";

const normalizeLocationAddress = (
  source?: LocationAddress,
  parse?: boolean
): LocationAddress => {
  const normalizer = entryNormalizer(source);
  const normalized = {
    street: normalizer("street", ""),
    city: normalizer("city", ""),
    zip: normalizer("zip", ""),
  };
  return finalizeData(normalized, source, parse);
};

const normalizeLocationContact = (
  source?: LocationContact,
  parse?: boolean
): LocationContact => {
  const normalizer = entryNormalizer(source);
  const normalized = {
    officialName: normalizer("officialName", undefined),
    representative: normalizer("representative", undefined),
    address: normalizeLocationAddress(source?.address),
    ownerEmail: normalizer("ownerEmail", undefined),
    email: normalizer("email", undefined),
    phone: normalizer("phone", undefined),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeLocationContext = (
  source?: LocationContext,
  parse?: boolean
): LocationContext => {
  const normalizer = entryNormalizer(source);
  const normalized = {
    id: normalizer("id", ""),
    name: normalizer("name", ""),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeLocationInformation = (
  source?: LocationInformation,
  parse?: boolean
): LocationInformation => {
  const normalizer = entryNormalizer(source);
  const contexts = normalizer("contexts", undefined, "array");
  const normalized = {
    id: normalizer("id", ""),
    providerId: normalizer("providerId", ""),
    name: normalizer("name", ""),
    publicKey: normalizer("publicKey", undefined),
    contact: normalizeLocationContact(source?.contact),
    contexts: contexts?.map((context) => normalizeLocationContext(context)),
  };
  return finalizeData(normalized, source, parse);
};

const normalizeAddress = (source?: Address, parse?: boolean): Address => {
  const normalizer = entryNormalizer(source);
  const normalized = {
    street: normalizer("street", undefined),
    houseNumber: normalizer("houseNumber", undefined),
    zipCode: normalizer("zipCode", undefined),
    city: normalizer("city", undefined),
  };
  return finalizeData(normalized, source, parse);
};

const normalizeGuestAllOfAttendanceInformation = (
  source?: GuestAllOfAttendanceInformation,
  parse?: boolean
): GuestAllOfAttendanceInformation => {
  const normalizer = entryNormalizer(source);
  const normalized = {
    descriptionOfParticipation: normalizer(
      "descriptionOfParticipation",
      undefined
    ),
    attendFrom: normalizer("attendFrom", ""),
    attendTo: normalizer("attendTo", ""),
    additionalInformation: normalizer("additionalInformation", undefined),
  };
  return finalizeData(normalized, source, parse);
};

const normalizeGuest = (source?: Guest, parse?: boolean): Guest => {
  const normalizer = entryNormalizer(source);
  const normalized = {
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
  return finalizeData(normalized, source, parse);
};

const normalizeGuestListDataProvider = (
  source?: GuestListDataProvider,
  parse?: boolean
): GuestListDataProvider => {
  const normalizer = entryNormalizer(source);
  const normalized = {
    name: normalizer("name", ""),
    address: normalizeAddress(source?.address),
  };
  return finalizeData(normalized, source, parse);
};

const normalizeGuestList = (source?: GuestList, parse?: boolean): GuestList => {
  const normalizer = entryNormalizer(source);
  const guests = normalizer("guests", [], "array");
  const normalized = {
    guests: guests.map((guest) => normalizeGuest(guest, true)),
    dataProvider: normalizeGuestListDataProvider(source?.dataProvider),
    additionalInformation: normalizer("additionalInformation", undefined),
    startDate: normalizer("startDate", undefined, "dateString"),
    endDate: normalizer("endDate", undefined, "dateString"),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeDataRequestDetails = (
  source?: DataRequestDetails,
  parse?: boolean
): DataRequestDetails => {
  const normalizer = entryNormalizer(source);
  const normalized = {
    comment: normalizer("comment", undefined),
    status: normalizer("status", undefined),
    code: normalizer("code", undefined),
    name: normalizer("name", undefined),
    externalRequestId: normalizer("externalRequestId", undefined),
    start: normalizer("start", undefined, "dateString"),
    end: normalizer("end", undefined, "dateString"),
    requestedAt: normalizer("requestedAt", undefined, "dateString"),
    lastModifiedAt: normalizer("lastModifiedAt", undefined, "dateString"),
    requestDetails: normalizer("requestDetails", undefined),
    locationInformation: source?.locationInformation
      ? normalizeLocationInformation(source?.locationInformation)
      : source?.locationInformation,
    submissionData: source?.submissionData
      ? normalizeGuestList(source?.submissionData)
      : undefined,
  };
  return finalizeData(normalized, source, parse);
};
