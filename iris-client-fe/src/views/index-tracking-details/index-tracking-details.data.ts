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
import { Complete, normalizeData } from "@/utils/data";
import { normalizeAddress } from "@/views/event-tracking-details/event-tracking-details.data";
import _isNil from "lodash/isNil";

const normalizeContactsAndEventsDataProvider = (
  source?: ContactsAndEventsDataProvider,
  parse?: boolean
): ContactsAndEventsDataProvider => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<ContactsAndEventsDataProvider> = {
        firstName: normalizer("firstName", ""),
        lastName: normalizer("lastName", ""),
        dateOfBirth: normalizer("dateOfBirth", ""),
      };
      return normalized;
    },
    parse,
    "ContactsAndEventsDataProvider"
  );
};

const normalizeEvent = (source?: Event, parse?: boolean): Event => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<Event> = {
        name: normalizer("name", ""),
        phone: normalizer("phone", undefined),
        address: !_isNil(source?.address)
          ? normalizeAddress(source?.address)
          : source?.address,
        additionalInformation: normalizer("additionalInformation", undefined),
      };
      return normalized;
    },
    parse,
    "Event"
  );
};

const normalizeEventList = (source?: EventList, parse?: boolean): EventList => {
  return normalizeData(
    source,
    (normalizer) => {
      const events = normalizer("events", [], "array");
      const normalized: Complete<EventList> = {
        events: events.map((event) => normalizeEvent(event)),
        startDate: normalizer("startDate", undefined, "dateString"),
        endDate: normalizer("endDate", undefined, "dateString"),
      };
      return normalized;
    },
    parse,
    "EventList"
  );
};

const normalizeContactPersonAllOfContactInformation = (
  source?: ContactPersonAllOfContactInformation,
  parse?: boolean
): ContactPersonAllOfContactInformation => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<ContactPersonAllOfContactInformation> = {
        firstContactDate: normalizer(
          "firstContactDate",
          undefined,
          "dateString"
        ),
        lastContactDate: normalizer("lastContactDate", undefined, "dateString"),
        contactCategory: !_isNil(source?.contactCategory)
          ? normalizer("contactCategory", undefined)
          : source?.contactCategory,
        basicConditions: normalizer("basicConditions", undefined),
      };
      return normalized;
    },
    parse,
    "ContactPersonAllOfContactInformation"
  );
};

const normalizeContactPersonAllOfWorkPlace = (
  source: ContactPersonAllOfWorkPlace,
  parse?: boolean
): ContactPersonAllOfWorkPlace => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<ContactPersonAllOfWorkPlace> = {
        name: normalizer("name", undefined),
        pointOfContact: normalizer("pointOfContact", undefined),
        phone: normalizer("phone", undefined),
        address: !_isNil(source?.address)
          ? normalizeAddress(source?.address)
          : source?.address,
      };
      return normalized;
    },
    parse,
    "ContactPersonAllOfWorkPlace"
  );
};

const normalizeContactPerson = (
  source?: ContactPerson,
  parse?: boolean
): ContactPerson => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<ContactPerson> = {
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
        workPlace: source?.workPlace
          ? normalizeContactPersonAllOfWorkPlace(source?.workPlace)
          : source?.workPlace,
        contactInformation: source?.contactInformation
          ? normalizeContactPersonAllOfContactInformation(
              source?.contactInformation
            )
          : source?.contactInformation,
      };
      return normalized;
    },
    parse,
    "ContactPerson"
  );
};

const normalizeContactPersonList = (
  source?: ContactPersonList,
  parse?: boolean
): ContactPersonList => {
  return normalizeData(
    source,
    (normalizer) => {
      const contactPersons = normalizer("contactPersons", [], "array");
      const normalized: Complete<ContactPersonList> = {
        contactPersons: contactPersons.map((contactPerson) =>
          normalizeContactPerson(contactPerson)
        ),
        startDate: normalizer("startDate", undefined, "dateString"),
        endDate: normalizer("endDate", undefined, "dateString"),
      };
      return normalized;
    },
    parse,
    "ContactPersonList"
  );
};

const normalizeContactsAndEvents = (
  source?: ContactsAndEvents,
  parse?: boolean
): ContactsAndEvents => {
  return normalizeData(
    source,
    () => {
      const normalized: Complete<ContactsAndEvents> = {
        contacts: normalizeContactPersonList(source?.contacts),
        events: normalizeEventList(source?.events),
        dataProvider: normalizeContactsAndEventsDataProvider(
          source?.dataProvider
        ),
      };
      return normalized;
    },
    parse,
    "ContactsAndEvents"
  );
};

export const normalizeDataRequestCaseData = (
  source?: DataRequestCaseData,
  parse?: boolean
): DataRequestCaseData => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<DataRequestCaseData> = {
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
      return normalized;
    },
    parse,
    "DataRequestCaseData"
  );
};
