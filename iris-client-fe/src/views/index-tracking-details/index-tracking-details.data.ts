import {
  ContactPerson,
  ContactPersonAllOfContactInformation,
  ContactPersonAllOfWorkPlace,
  ContactPersonList,
  ContactsAndEvents,
  ContactsAndEventsDataProvider,
  DataRequestCaseData,
  Event,
  EventList,
} from "@/api";
import { entryNormalizer, finalizeData } from "@/utils/data";
import { normalizeAddress } from "@/views/event-tracking-details/event-tracking-details.data";
import _isNil from "lodash/isNil";

export const normalizeContactsAndEventsDataProvider = (
  source?: ContactsAndEventsDataProvider,
  parse?: boolean
): ContactsAndEventsDataProvider => {
  const normalizer = entryNormalizer(source);
  const normalized: ContactsAndEventsDataProvider = {
    firstName: normalizer("firstName", ""),
    lastName: normalizer("lastName", ""),
    dateOfBirth: normalizer("dateOfBirth", "", "dateString"),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeEvent = (source?: Event, parse?: boolean): Event => {
  const normalizer = entryNormalizer(source);
  const normalized: Event = {
    name: normalizer("name", ""),
    phone: normalizer("phone", undefined),
    address: !_isNil(source?.address)
      ? normalizeAddress(source?.address)
      : source?.address,
    additionalInformation: normalizer("additionalInformation", undefined),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeEventList = (
  source?: EventList,
  parse?: boolean
): EventList => {
  const normalizer = entryNormalizer(source);
  const events = normalizer("events", [], "array");
  const normalized: EventList = {
    events: events.map((event) => normalizeEvent(event)),
    startDate: normalizer("startDate", undefined, "dateString"),
    endDate: normalizer("endDate", undefined, "dateString"),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeContactPersonAllOfContactInformation = (
  source?: ContactPersonAllOfContactInformation,
  parse?: boolean
): ContactPersonAllOfContactInformation => {
  const normalizer = entryNormalizer(source);
  const normalized: ContactPersonAllOfContactInformation = {
    firstContactDate: normalizer("firstContactDate", undefined, "dateString"),
    lastContactDate: normalizer("lastContactDate", undefined, "dateString"),
    contactCategory: !_isNil(source?.contactCategory)
      ? normalizer("contactCategory", undefined)
      : source?.contactCategory,
    basicConditions: normalizer("basicConditions", undefined),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeContactPersonAllOfWorkPlace = (
  source: ContactPersonAllOfWorkPlace,
  parse?: boolean
): ContactPersonAllOfWorkPlace => {
  const normalizer = entryNormalizer(source);
  const normalized: ContactPersonAllOfWorkPlace = {
    name: normalizer("name", undefined),
    pointOfContact: normalizer("pointOfContact", undefined),
    phone: normalizer("phone", undefined),
    address: !_isNil(source?.address)
      ? normalizeAddress(source?.address)
      : source?.address,
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeContactPerson = (
  source?: ContactPerson,
  parse?: boolean
): ContactPerson => {
  const normalizer = entryNormalizer(source);
  const normalized: ContactPerson = {
    firstName: normalizer("firstName", ""),
    lastName: normalizer("lastName", ""),
    dateOfBirth: normalizer("dateOfBirth", undefined, "dateString"),
    sex: normalizer("sex", undefined),
    email: normalizer("email", undefined),
    phone: normalizer("phone", undefined),
    mobilePhone: normalizer("mobilePhone", undefined),
    address: !_isNil(source?.address)
      ? normalizeAddress(source?.address)
      : source?.address,
    workPlace: source?.workPlace
      ? normalizeContactPersonAllOfWorkPlace(source?.workPlace)
      : source?.workPlace,
    contactInformation: source?.contactInformation
      ? normalizeContactPersonAllOfContactInformation(
          source?.contactInformation
        )
      : source?.contactInformation,
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeContactPersonList = (
  source?: ContactPersonList,
  parse?: boolean
): ContactPersonList => {
  const normalizer = entryNormalizer(source);
  const contactPersons = normalizer("contactPersons", [], "array");
  const normalized: ContactPersonList = {
    contactPersons: contactPersons.map((contactPerson) =>
      normalizeContactPerson(contactPerson)
    ),
    startDate: normalizer("startDate", undefined, "dateString"),
    endDate: normalizer("endDate", undefined, "dateString"),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeContactsAndEvents = (
  source?: ContactsAndEvents,
  parse?: boolean
): ContactsAndEvents => {
  const normalized: ContactsAndEvents = {
    contacts: normalizeContactPersonList(source?.contacts),
    events: normalizeEventList(source?.events),
    dataProvider: normalizeContactsAndEventsDataProvider(source?.dataProvider),
  };
  return finalizeData(normalized, source, parse);
};

export const normalizeDataRequestCaseData = (
  source?: DataRequestCaseData,
  parse?: boolean
): DataRequestCaseData => {
  const normalizer = entryNormalizer(source);
  const normalized: DataRequestCaseData = {
    externalCaseId: normalizer("externalCaseId", ""),
    name: normalizer("name", undefined),
    comment: normalizer("comment", undefined),
    start: normalizer("start", "", "dateString"),
    end: normalizer("end", undefined, "dateString"),
    caseId: normalizer("caseId", undefined),
    status: normalizer("status", undefined),
    nonce: normalizer("nonce", undefined),
    submissionUri: normalizer("submissionUri", ""),
    submissionData: source?.submissionData
      ? normalizeContactsAndEvents(source?.submissionData)
      : source?.submissionData,
  };
  return finalizeData(normalized, source, parse);
};
