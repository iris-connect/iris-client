/* eslint-disable @typescript-eslint/no-explicit-any */

import {
  ApiResponse,
  assertParamExists,
  DataQuery,
  RequestOptions,
} from "./common";
import { BaseAPI } from "./base";
import { UserSession } from "@/views/user-login/user-login.store";

/**
 *
 * @export
 * @interface Address
 */
export interface Address {
  /**
   *
   * @type {string}
   * @memberof Address
   */
  street?: string;
  /**
   *
   * @type {string}
   * @memberof Address
   */
  houseNumber?: string;
  /**
   *
   * @type {string}
   * @memberof Address
   */
  zipCode?: string;
  /**
   *
   * @type {string}
   * @memberof Address
   */
  city?: string;
}
/**
 * Category of contact that describes the intensity and thus the risk of infection of the contact.
 * @export
 * @enum {string}
 */
export enum ContactCategory {
  HighRisk = "HIGH_RISK",
  HighRiskMed = "HIGH_RISK_MED",
  MediumRiskMed = "MEDIUM_RISK_MED",
  LowRisk = "LOW_RISK",
  NoRisk = "NO_RISK",
}

/**
 * Extended person data type for contact persons who had contact with the queried person during the queried time.
 * @export
 * @interface ContactPerson
 */
export interface ContactPerson {
  /**
   *
   * @type {string}
   * @memberof ContactPerson
   */
  firstName: string;
  /**
   *
   * @type {string}
   * @memberof ContactPerson
   */
  lastName: string;
  /**
   *
   * @type {string}
   * @memberof ContactPerson
   */
  dateOfBirth?: string;
  /**
   *
   * @type {Sex}
   * @memberof ContactPerson
   */
  sex?: Sex;
  /**
   *
   * @type {string}
   * @memberof ContactPerson
   */
  email?: string;
  /**
   *
   * @type {string}
   * @memberof ContactPerson
   */
  phone?: string;
  /**
   *
   * @type {string}
   * @memberof ContactPerson
   */
  mobilePhone?: string;
  /**
   *
   * @type {Address}
   * @memberof ContactPerson
   */
  address?: Address | null;
  /**
   *
   * @type {ContactPersonAllOfWorkPlace}
   * @memberof ContactPerson
   */
  workPlace?: ContactPersonAllOfWorkPlace;
  /**
   *
   * @type {ContactPersonAllOfContactInformation}
   * @memberof ContactPerson
   */
  contactInformation?: ContactPersonAllOfContactInformation;
}
/**
 *
 * @export
 * @interface ContactPersonAllOf
 */
export interface ContactPersonAllOf {
  /**
   *
   * @type {ContactPersonAllOfWorkPlace}
   * @memberof ContactPersonAllOf
   */
  workPlace?: ContactPersonAllOfWorkPlace;
  /**
   *
   * @type {ContactPersonAllOfContactInformation}
   * @memberof ContactPersonAllOf
   */
  contactInformation?: ContactPersonAllOfContactInformation;
}
/**
 * Additional informations about the contact(s) with the queried person.
 * @export
 * @interface ContactPersonAllOfContactInformation
 */
export interface ContactPersonAllOfContactInformation {
  /**
   *
   * @type {string}
   * @memberof ContactPersonAllOfContactInformation
   */
  firstContactDate?: string;
  /**
   *
   * @type {string}
   * @memberof ContactPersonAllOfContactInformation
   */
  lastContactDate?: string;
  /**
   *
   * @type {ContactCategory}
   * @memberof ContactPersonAllOfContactInformation
   */
  contactCategory?: ContactCategory | null;
  /**
   * Informations about the basic conditions such as: from, to, place, inside|outside, mask yes|no, distance >=|< 1,5m, ventilated yes|no, remarks.
   * @type {string}
   * @memberof ContactPersonAllOfContactInformation
   */
  basicConditions?: string;
}
/**
 * Additional informations about the work place of the contact person.
 * @export
 * @interface ContactPersonAllOfWorkPlace
 */
export interface ContactPersonAllOfWorkPlace {
  /**
   * Name of work place
   * @type {string}
   * @memberof ContactPersonAllOfWorkPlace
   */
  name?: string;
  /**
   *
   * @type {string}
   * @memberof ContactPersonAllOfWorkPlace
   */
  pointOfContact?: string;
  /**
   *
   * @type {string}
   * @memberof ContactPersonAllOfWorkPlace
   */
  phone?: string;
  /**
   *
   * @type {Address}
   * @memberof ContactPersonAllOfWorkPlace
   */
  address?: Address | null;
}
/**
 * A collection of contact persons who had contact with the queried person during the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!
 * @export
 * @interface ContactPersonList
 */
export interface ContactPersonList {
  /**
   *
   * @type {Array<ContactPerson>}
   * @memberof ContactPersonList
   */
  contactPersons: Array<ContactPerson>;
  /**
   * Start date of contacts for this list.
   * @type {string}
   * @memberof ContactPersonList
   */
  startDate?: string;
  /**
   * End date of contacts for this list.
   * @type {string}
   * @memberof ContactPersonList
   */
  endDate?: string;
}
/**
 * This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!(`dataToTransport` in the general description of the API.)
 * @export
 * @interface ContactsAndEvents
 */
export interface ContactsAndEvents {
  /**
   *
   * @type {ContactPersonList}
   * @memberof ContactsAndEvents
   */
  contacts: ContactPersonList;
  /**
   *
   * @type {EventList}
   * @memberof ContactsAndEvents
   */
  events: EventList;
  /**
   *
   * @type {ContactsAndEventsDataProvider}
   * @memberof ContactsAndEvents
   */
  dataProvider: ContactsAndEventsDataProvider;
}
/**
 *
 * @export
 * @interface ContactsAndEventsDataProvider
 */
export interface ContactsAndEventsDataProvider {
  /**
   *
   * @type {string}
   * @memberof ContactsAndEventsDataProvider
   */
  firstName: string;
  /**
   *
   * @type {string}
   * @memberof ContactsAndEventsDataProvider
   */
  lastName: string;
  /**
   *
   * @type {string}
   * @memberof ContactsAndEventsDataProvider
   */
  dateOfBirth: string;
}
/**
 *
 * @export
 * @interface ContactsEventsSubmission
 */
export interface ContactsEventsSubmission {
  /**
   * The encrypted secret key for encryption. (`keyToTransport` in the general description of the API.)
   * @type {string}
   * @memberof ContactsEventsSubmission
   */
  secret: string;
  /**
   * Reference to the used encryption key. This must be the value from keyReference of the DataRequest as this matches the passed and thus used key.
   * @type {string}
   * @memberof ContactsEventsSubmission
   */
  keyReference: string;
  /**
   *
   * @type {ContactsAndEvents}
   * @memberof ContactsEventsSubmission
   */
  encryptedData: ContactsAndEvents;
}
/**
 *
 * @export
 * @interface ContactsEventsSubmissionAllOf
 */
export interface ContactsEventsSubmissionAllOf {
  /**
   *
   * @type {ContactsAndEvents}
   * @memberof ContactsEventsSubmissionAllOf
   */
  encryptedData: ContactsAndEvents;
}
/**
 *
 * @export
 * @interface Credentials
 */
export interface Credentials {
  /**
   *
   * @type {string}
   * @memberof Credentials
   */
  userName?: string;
  /**
   *
   * @type {string}
   * @memberof Credentials
   */
  password?: string;
}
/**
 * A data request with all parameters relevant for the data submission.
 * @export
 * @interface DataRequest
 */
export interface DataRequest {
  /**
   * Name of the requesting health department.
   * @type {string}
   * @memberof DataRequest
   */
  healthDepartment: string;
  /**
   * The key of the requesting health department that must be used for encryption. The key is encoded with Base64.
   * @type {string}
   * @memberof DataRequest
   */
  keyOfHealthDepartment: string;
  /**
   * Reference id of the given key. This reference must be included in the submission in order to identify the correct private key for decryption at the health department.
   * @type {string}
   * @memberof DataRequest
   */
  keyReference: string;
  /**
   * The start time for which data should be submitted with this request.
   * @type {string}
   * @memberof DataRequest
   */
  start: string;
  /**
   * The end time for which data should be submitted with this request.
   * @type {string}
   * @memberof DataRequest
   */
  end?: string;
  /**
   * Details of the data request, specifying it in more detail and narrowing down the data to be provided (e.g. table and environment, seat, rank, ...).
   * @type {string}
   * @memberof DataRequest
   */
  requestDetails?: string;
  /**
   * Comment from an IRIS user
   * @type {string}
   * @memberof DataRequest
   */
  comment?: string;
}
/**
 * Creates a new index case data request from FE - persistent data has to be refined. Starting with contact persons name.
 * @export
 * @interface DataRequestCaseClient
 */
export interface DataRequestCaseClient {
  /**
   * External case identifier. E.g. CaseID in Sormas.
   * @type {string}
   * @memberof DataRequestCaseClient
   */
  externalCaseId: string;
  /**
   * Friendly name for given case
   * @type {string}
   * @memberof DataRequestCaseClient
   */
  name?: string;
  /**
   * Comments on given case
   * @type {string}
   * @memberof DataRequestCaseClient
   */
  comment?: string;
  /**
   *
   * @type {string}
   * @memberof DataRequestCaseClient
   */
  start: string;
  /**
   *
   * @type {string}
   * @memberof DataRequestCaseClient
   */
  end?: string;
}
/**
 *
 * @export
 * @interface DataRequestCaseData
 */
export interface DataRequestCaseData {
  /**
   * External case identifier. E.g. CaseID in Sormas.
   * @type {string}
   * @memberof DataRequestCaseData
   */
  externalCaseId: string;
  /**
   * Friendly name for given case
   * @type {string}
   * @memberof DataRequestCaseData
   */
  name?: string;
  /**
   * Comments on given case
   * @type {string}
   * @memberof DataRequestCaseData
   */
  comment?: string;
  /**
   *
   * @type {string}
   * @memberof DataRequestCaseData
   */
  start: string;
  /**
   *
   * @type {string}
   * @memberof DataRequestCaseData
   */
  end?: string;
  /**
   * Internal case identifier. Used in listings etc.
   * @type {string}
   * @memberof DataRequestCaseData
   */
  caseId?: string;
  /**
   *
   * @type {DataRequestStatus}
   * @memberof DataRequestCaseData
   */
  status?: DataRequestStatus;
  /**
   * Nonce used in provider app to authorize data upload
   * @type {string}
   * @memberof DataRequestCaseData
   */
  nonce?: string;
  /**
   * The URI that can be used to submit contact data for this tracing code.
   * @type {string}
   * @memberof DataRequestCaseData
   */
  submissionUri: string;
  /**
   *
   * @type {ContactsAndEvents}
   * @memberof DataRequestCaseData
   */
  submissionData?: ContactsAndEvents;
  /**
   * The TAN
   * @type {string}
   * @memberof DataRequestCaseData
   */
  readableToken?: string;
}
/**
 *
 * @export
 * @interface DataRequestCaseDataAllOf
 */
export interface DataRequestCaseDataAllOf {
  /**
   *
   * @type {ContactsAndEvents}
   * @memberof DataRequestCaseDataAllOf
   */
  submissionData?: ContactsAndEvents;
}
/**
 * Details for index case
 * @export
 * @interface DataRequestCaseDetails
 */
export interface DataRequestCaseDetails {
  /**
   * External case identifier. E.g. CaseID in Sormas.
   * @type {string}
   * @memberof DataRequestCaseDetails
   */
  externalCaseId: string;
  /**
   * Friendly name for given case
   * @type {string}
   * @memberof DataRequestCaseDetails
   */
  name?: string;
  /**
   * Comments on given case
   * @type {string}
   * @memberof DataRequestCaseDetails
   */
  comment?: string;
  /**
   *
   * @type {string}
   * @memberof DataRequestCaseDetails
   */
  start: string;
  /**
   *
   * @type {string}
   * @memberof DataRequestCaseDetails
   */
  end?: string;
  /**
   * Internal case identifier. Used in listings etc.
   * @type {string}
   * @memberof DataRequestCaseDetails
   */
  caseId?: string;
  /**
   *
   * @type {DataRequestStatus}
   * @memberof DataRequestCaseDetails
   */
  status?: DataRequestStatus;
}
/**
 *
 * @export
 * @interface DataRequestCaseDetailsAllOf
 */
export interface DataRequestCaseDetailsAllOf {
  /**
   * Internal case identifier. Used in listings etc.
   * @type {string}
   * @memberof DataRequestCaseDetailsAllOf
   */
  caseId?: string;
  /**
   *
   * @type {DataRequestStatus}
   * @memberof DataRequestCaseDetailsAllOf
   */
  status?: DataRequestStatus;
}
/**
 * Details for index case
 * @export
 * @interface DataRequestCaseExtendedDetails
 */
export interface DataRequestCaseExtendedDetails {
  /**
   * External case identifier. E.g. CaseID in Sormas.
   * @type {string}
   * @memberof DataRequestCaseExtendedDetails
   */
  externalCaseId: string;
  /**
   * Friendly name for given case
   * @type {string}
   * @memberof DataRequestCaseExtendedDetails
   */
  name?: string;
  /**
   * Comments on given case
   * @type {string}
   * @memberof DataRequestCaseExtendedDetails
   */
  comment?: string;
  /**
   *
   * @type {string}
   * @memberof DataRequestCaseExtendedDetails
   */
  start: string;
  /**
   *
   * @type {string}
   * @memberof DataRequestCaseExtendedDetails
   */
  end?: string;
  /**
   * Internal case identifier. Used in listings etc.
   * @type {string}
   * @memberof DataRequestCaseExtendedDetails
   */
  caseId?: string;
  /**
   *
   * @type {DataRequestStatus}
   * @memberof DataRequestCaseExtendedDetails
   */
  status?: DataRequestStatus;
  /**
   * Nonce used in provider app to authorize data upload
   * @type {string}
   * @memberof DataRequestCaseExtendedDetails
   */
  nonce?: string;
}
/**
 *
 * @export
 * @interface DataRequestCaseExtendedDetailsAllOf
 */
export interface DataRequestCaseExtendedDetailsAllOf {
  /**
   * Nonce used in provider app to authorize data upload
   * @type {string}
   * @memberof DataRequestCaseExtendedDetailsAllOf
   */
  nonce?: string;
}
/**
 * The data request that will be sent by the FE.
 * @export
 * @interface DataRequestClient
 */
export interface DataRequestClient {
  /**
   * Id of the location to request the data from.
   * @type {string}
   * @memberof DataRequestClient
   */
  locationId: string;
  /**
   * ID of the App provider serving the location.
   * @type {string}
   * @memberof DataRequestClient
   */
  providerId: string;
  /**
   * Friendly name of the request to be identified easily by GA
   * @type {string}
   * @memberof DataRequestClient
   */
  name?: string;
  /**
   * External ID outside of IRIS
   * @type {string}
   * @memberof DataRequestClient
   */
  externalRequestId: string;
  /**
   * The start time for which data should be submitted with this request.
   * @type {string}
   * @memberof DataRequestClient
   */
  start: string;
  /**
   * The end time for which data should be submitted with this request.
   * @type {string}
   * @memberof DataRequestClient
   */
  end: string;
  /**
   * Details of the data request, specifying it in more detail and narrowing down the data to be provided (e.g. table and environment, seat, rank, ...).
   * @type {string}
   * @memberof DataRequestClient
   */
  requestDetails?: string;
  /**
   * Comment from an IRIS user.
   * @type {string}
   * @memberof DataRequestClient
   */
  comment?: string;
}
/**
 * The data request that will be updated by the FE.
 * @export
 * @interface DataRequestClientUpdate
 */
export interface DataRequestClientUpdate {
  /**
   * Friendly name of the request to be identified easily by GA
   * @type {string}
   * @memberof DataRequestClientUpdate
   */
  name?: string;
  /**
   * External ID outside of IRIS
   * @type {string}
   * @memberof DataRequestClientUpdate
   */
  externalRequestId?: string;
  /**
   * Comment from an IRIS user.
   * @type {string}
   * @memberof DataRequestClientUpdate
   */
  comment?: string;
  /**
   *
   * @type {DataRequestStatusUpdateByUser}
   * @memberof DataRequestClientUpdate
   */
  status?: DataRequestStatusUpdateByUser;
}
/**
 *
 * @export
 * @interface DataRequestDetails
 */
export interface DataRequestDetails {
  /**
   * Comments on given data request from GA employees
   * @type {string}
   * @memberof DataRequestDetails
   */
  comment?: string;
  /**
   *
   * @type {DataRequestStatus}
   * @memberof DataRequestDetails
   */
  status?: DataRequestStatus;
  /**
   * Code for DataRequest
   * @type {string}
   * @memberof DataRequestDetails
   */
  code?: string;
  /**
   * Friendly name of the request to be identified easily by GA
   * @type {string}
   * @memberof DataRequestDetails
   */
  name?: string;
  /**
   * External ID outside of IRIS
   * @type {string}
   * @memberof DataRequestDetails
   */
  externalRequestId?: string;
  /**
   * The start time for which data should be submitted with this request.
   * @type {string}
   * @memberof DataRequestDetails
   */
  start?: string;
  /**
   * The end time for which data should be submitted with this request.
   * @type {string}
   * @memberof DataRequestDetails
   */
  end?: string;
  /**
   * Timestamp when the data request was created.
   * @type {string}
   * @memberof DataRequestDetails
   */
  requestedAt?: string;
  /**
   * Timestamp when the data request was last updated.
   * @type {string}
   * @memberof DataRequestDetails
   */
  lastModifiedAt?: string;
  /**
   * Details of the data request, specifying it in more detail and narrowing down the data to be provided (e.g. table and environment, seat, rank, ...).
   * @type {string}
   * @memberof DataRequestDetails
   */
  requestDetails?: string;
  /**
   *
   * @type {LocationInformation}
   * @memberof DataRequestDetails
   */
  locationInformation?: LocationInformation;
  /**
   *
   * @type {GuestList}
   * @memberof DataRequestDetails
   */
  submissionData?: GuestList;
}
/**
 *
 * @export
 * @interface DataRequestDetailsAllOf
 */
export interface DataRequestDetailsAllOf {
  /**
   *
   * @type {GuestList}
   * @memberof DataRequestDetailsAllOf
   */
  submissionData?: GuestList;
}
/**
 * Status of data request.
 * @export
 * @enum {string}
 */
export enum DataRequestStatus {
  DataRequested = "DATA_REQUESTED",
  DataReceived = "DATA_RECEIVED",
  Closed = "CLOSED",
  Aborted = "ABORTED",
}

/**
 * Status of data request.
 * @export
 * @enum {string}
 */
export enum DataRequestStatusUpdateByUser {
  DataReceived = "DATA_RECEIVED",
  Closed = "CLOSED",
  Aborted = "ABORTED",
}

/**
 * An event, location or occasion visited by the queried person during the queried time.
 * @export
 * @interface Event
 */
export interface Event {
  /**
   * Name of the event
   * @type {string}
   * @memberof Event
   */
  name: string;
  /**
   *
   * @type {string}
   * @memberof Event
   */
  phone?: string;
  /**
   *
   * @type {Address}
   * @memberof Event
   */
  address?: Address | null;
  /**
   * Additional informations about the event.
   * @type {string}
   * @memberof Event
   */
  additionalInformation?: string;
}
/**
 * A collection of events visited by the queried person during the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!
 * @export
 * @interface EventList
 */
export interface EventList {
  /**
   *
   * @type {Array<Event>}
   * @memberof EventList
   */
  events: Array<Event>;
  /**
   * Start date of visits for this list.
   * @type {string}
   * @memberof EventList
   */
  startDate?: string;
  /**
   * End date of visits for this list.
   * @type {string}
   * @memberof EventList
   */
  endDate?: string;
}
/**
 *
 * @export
 * @interface ExistingDataRequestClientWithLocation
 */
export interface ExistingDataRequestClientWithLocation {
  /**
   *
   * @type {DataRequestStatus}
   * @memberof ExistingDataRequestClientWithLocation
   */
  status?: DataRequestStatus;
  /**
   * Code for DataRequest
   * @type {string}
   * @memberof ExistingDataRequestClientWithLocation
   */
  code?: string;
  /**
   * Friendly name of the request to be identified easily by GA
   * @type {string}
   * @memberof ExistingDataRequestClientWithLocation
   */
  name?: string;
  /**
   * External ID outside of IRIS
   * @type {string}
   * @memberof ExistingDataRequestClientWithLocation
   */
  externalRequestId?: string;
  /**
   * The start time for which data should be submitted with this request.
   * @type {string}
   * @memberof ExistingDataRequestClientWithLocation
   */
  start?: string;
  /**
   * The end time for which data should be submitted with this request.
   * @type {string}
   * @memberof ExistingDataRequestClientWithLocation
   */
  end?: string;
  /**
   * Timestamp when the data request was created.
   * @type {string}
   * @memberof ExistingDataRequestClientWithLocation
   */
  requestedAt?: string;
  /**
   * Timestamp when the data request was last updated.
   * @type {string}
   * @memberof ExistingDataRequestClientWithLocation
   */
  lastUpdatedAt?: string;
  /**
   * Details of the data request, specifying it in more detail and narrowing down the data to be provided (e.g. table and environment, seat, rank, ...).
   * @type {string}
   * @memberof ExistingDataRequestClientWithLocation
   */
  requestDetails?: string;
  /**
   *
   * @type {LocationInformation}
   * @memberof ExistingDataRequestClientWithLocation
   */
  locationInformation?: LocationInformation;
}
/**
 *
 * @export
 * @interface ExistingDataRequestClientWithLocationList
 */
export interface ExistingDataRequestClientWithLocationList {
  /**
   *
   * @type {Array<ExistingDataRequestClientWithLocation>}
   * @memberof ExistingDataRequestClientWithLocationList
   */
  dataRequests?: Array<ExistingDataRequestClientWithLocation>;
}
/**
 * Extended person data type for a guest who attended a queried event or location in the queried time.
 * @export
 * @interface Guest
 */
export interface Guest {
  /**
   *
   * @type {string}
   * @memberof Guest
   */
  firstName: string;
  /**
   *
   * @type {string}
   * @memberof Guest
   */
  lastName: string;
  /**
   *
   * @type {string}
   * @memberof Guest
   */
  dateOfBirth?: string;
  /**
   *
   * @type {Sex}
   * @memberof Guest
   */
  sex?: Sex;
  /**
   *
   * @type {string}
   * @memberof Guest
   */
  email?: string;
  /**
   *
   * @type {string}
   * @memberof Guest
   */
  phone?: string;
  /**
   *
   * @type {string}
   * @memberof Guest
   */
  mobilePhone?: string;
  /**
   *
   * @type {Address}
   * @memberof Guest
   */
  address?: Address | null;
  /**
   *
   * @type {GuestAllOfAttendanceInformation}
   * @memberof Guest
   */
  attendanceInformation: GuestAllOfAttendanceInformation;
  /**
   * The app indicates whether the data are verified with respect to identity (e.g. by phone number) = TRUE or whether they are unverified form inputs = FALSE.
   * @type {boolean}
   * @memberof Guest
   */
  identityChecked?: boolean;
}
/**
 *
 * @export
 * @interface GuestAllOf
 */
export interface GuestAllOf {
  /**
   *
   * @type {GuestAllOfAttendanceInformation}
   * @memberof GuestAllOf
   */
  attendanceInformation: GuestAllOfAttendanceInformation;
  /**
   * The app indicates whether the data are verified with respect to identity (e.g. by phone number) = TRUE or whether they are unverified form inputs = FALSE.
   * @type {boolean}
   * @memberof GuestAllOf
   */
  identityChecked?: boolean;
}
/**
 *
 * @export
 * @interface GuestAllOfAttendanceInformation
 */
export interface GuestAllOfAttendanceInformation {
  /**
   * Description of the type of participation.
   * @type {string}
   * @memberof GuestAllOfAttendanceInformation
   */
  descriptionOfParticipation?: string;
  /**
   * Start date/time of attendance of this guest.
   * @type {string}
   * @memberof GuestAllOfAttendanceInformation
   */
  attendFrom: string;
  /**
   * End date/time of attendance of this guest.
   * @type {string}
   * @memberof GuestAllOfAttendanceInformation
   */
  attendTo: string;
  /**
   * Additional informations about the attendance.
   * @type {string}
   * @memberof GuestAllOfAttendanceInformation
   */
  additionalInformation?: string;
}
/**
 * A collection of guests who attended a queried event or location in the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64! (`dataToTransport` in the general description of the API.)
 * @export
 * @interface GuestList
 */
export interface GuestList {
  /**
   *
   * @type {Array<Guest>}
   * @memberof GuestList
   */
  guests: Array<Guest>;
  /**
   *
   * @type {GuestListDataProvider}
   * @memberof GuestList
   */
  dataProvider: GuestListDataProvider;
  /**
   * Additional informations about the guest list and the event or location.
   * @type {string}
   * @memberof GuestList
   */
  additionalInformation?: string;
  /**
   * Start date/time of attendance for this guest list.
   * @type {string}
   * @memberof GuestList
   */
  startDate?: string;
  /**
   * End date/time of attendance for this guest list.
   * @type {string}
   * @memberof GuestList
   */
  endDate?: string;
}
/**
 *
 * @export
 * @interface GuestListDataProvider
 */
export interface GuestListDataProvider {
  /**
   * Name of Location, Institution or Organizer
   * @type {string}
   * @memberof GuestListDataProvider
   */
  name: string;
  /**
   *
   * @type {Address}
   * @memberof GuestListDataProvider
   */
  address: Address;
}
/**
 *
 * @export
 * @interface GuestsSubmission
 */
export interface GuestsSubmission {
  /**
   * The encrypted secret key for encryption. (`keyToTransport` in the general description of the API.)
   * @type {string}
   * @memberof GuestsSubmission
   */
  secret: string;
  /**
   * Reference to the used encryption key. This must be the value from keyReference of the DataRequest as this matches the passed and thus used key.
   * @type {string}
   * @memberof GuestsSubmission
   */
  keyReference: string;
  /**
   *
   * @type {GuestList}
   * @memberof GuestsSubmission
   */
  encryptedData: GuestList;
}
/**
 *
 * @export
 * @interface GuestsSubmissionAllOf
 */
export interface GuestsSubmissionAllOf {
  /**
   *
   * @type {GuestList}
   * @memberof GuestsSubmissionAllOf
   */
  encryptedData: GuestList;
}
/**
 * Anschrift des Standorts
 * @export
 * @interface LocationAddress
 */
export interface LocationAddress {
  /**
   * street + number
   * @type {string}
   * @memberof LocationAddress
   */
  street: string;
  /**
   * Stadt
   * @type {string}
   * @memberof LocationAddress
   */
  city: string;
  /**
   * Postleitzahl
   * @type {string}
   * @memberof LocationAddress
   */
  zip: string;
}
/**
 * Kontaktperson des Standorts
 * @export
 * @interface LocationContact
 */
export interface LocationContact {
  /**
   * Offizieller Unternehmensname
   * @type {string}
   * @memberof LocationContact
   */
  officialName?: string;
  /**
   * Ansprechpartner für dieses Unternehmen
   * @type {string}
   * @memberof LocationContact
   */
  representative?: string;
  /**
   *
   * @type {LocationAddress}
   * @memberof LocationContact
   */
  address: LocationAddress;
  /**
   * E-Mail des Inhabers
   * @type {string}
   * @memberof LocationContact
   */
  ownerEmail?: string;
  /**
   * ggf. E-Mail einer weiteren Kontaktperson
   * @type {string}
   * @memberof LocationContact
   */
  email?: string;
  /**
   * Telefonnummer eines Ansprechpartners
   * @type {string}
   * @memberof LocationContact
   */
  phone?: string;
}
/**
 * Ein Standort hat ggf. weitere Informationen wie Tische/Räume, etc.
 * @export
 * @interface LocationContext
 */
export interface LocationContext {
  /**
   * Interne ID des Kontext
   * @type {string}
   * @memberof LocationContext
   */
  id: string;
  /**
   * Bezeichnung
   * @type {string}
   * @memberof LocationContext
   */
  name: string;
}
/**
 * All information needed to create a new TracingTicket
 * @export
 * @interface LocationDataRequest
 */
export interface LocationDataRequest {
  /**
   * Name of the requesting health department.
   * @type {string}
   * @memberof LocationDataRequest
   */
  healthDepartment: string;
  /**
   * The key of the requesting health department that must be used for encryption. The key is encoded with Base64.
   * @type {string}
   * @memberof LocationDataRequest
   */
  keyOfHealthDepartment: string;
  /**
   * Reference id of the given key. This reference must be included in the submission in order to identify the correct private key for decryption at the health department.
   * @type {string}
   * @memberof LocationDataRequest
   */
  keyReference: string;
  /**
   * The start time for which data should be submitted with this request.
   * @type {string}
   * @memberof LocationDataRequest
   */
  start: string;
  /**
   * The end time for which data should be submitted with this request.
   * @type {string}
   * @memberof LocationDataRequest
   */
  end: string;
  /**
   * Details of the data request, specifying it in more detail and narrowing down the data to be provided (e.g. table and environment, seat, rank, ...).
   * @type {string}
   * @memberof LocationDataRequest
   */
  requestDetails?: string;
  /**
   * Comment from an IRIS user
   * @type {string}
   * @memberof LocationDataRequest
   */
  comment?: string;
  /**
   * The URI that can be used to submit contact data for this tracing code.
   * @type {string}
   * @memberof LocationDataRequest
   */
  submissionUri: string;
  /**
   * The id of the location.
   * @type {string}
   * @memberof LocationDataRequest
   */
  locationId: string;
}
/**
 *
 * @export
 * @interface LocationDataRequestAllOf
 */
export interface LocationDataRequestAllOf {
  /**
   * The URI that can be used to submit contact data for this tracing code.
   * @type {string}
   * @memberof LocationDataRequestAllOf
   */
  submissionUri: string;
  /**
   * The id of the location.
   * @type {string}
   * @memberof LocationDataRequestAllOf
   */
  locationId: string;
}
/**
 *
 * @export
 * @interface LocationInformation
 */
export interface LocationInformation {
  /**
   * Interne ID (beim Provider)
   * @type {string}
   * @memberof LocationInformation
   */
  id: string;
  /**
   * ID des App providers
   * @type {string}
   * @memberof LocationInformation
   */
  providerId: string;
  /**
   * Name des Standorts
   * @type {string}
   * @memberof LocationInformation
   */
  name: string;
  /**
   * Öffentlicher Schlüssel, ggf. für Nachrichtenaustausch
   * @type {string}
   * @memberof LocationInformation
   */
  publicKey?: string;
  /**
   *
   * @type {LocationContact}
   * @memberof LocationInformation
   */
  contact: LocationContact;
  /**
   *
   * @type {Array<LocationContext>}
   * @memberof LocationInformation
   */
  contexts?: Array<LocationContext>;
}
/**
 *
 * @export
 * @interface LocationList
 */
export interface LocationList {
  /**
   *
   * @type {Array<LocationInformation>}
   * @memberof LocationList
   */
  locations: Array<LocationInformation>;
  /**
   *
   * @type {number}
   * @memberof LocationList
   */
  totalElements: number;
  /**
   *
   * @type {number}
   * @memberof LocationList
   */
  page: number;
  /**
   *
   * @type {number}
   * @memberof LocationList
   */
  size: number;
}
/**
 * Basic data type of a person.
 * @export
 * @interface Person
 */
export interface Person {
  /**
   *
   * @type {string}
   * @memberof Person
   */
  firstName: string;
  /**
   *
   * @type {string}
   * @memberof Person
   */
  lastName: string;
  /**
   *
   * @type {string}
   * @memberof Person
   */
  dateOfBirth?: string;
  /**
   *
   * @type {Sex}
   * @memberof Person
   */
  sex?: Sex;
  /**
   *
   * @type {string}
   * @memberof Person
   */
  email?: string;
  /**
   *
   * @type {string}
   * @memberof Person
   */
  phone?: string;
  /**
   *
   * @type {string}
   * @memberof Person
   */
  mobilePhone?: string;
  /**
   *
   * @type {Address}
   * @memberof Person
   */
  address?: Address | null;
}
/**
 *
 * @export
 * @enum {string}
 */
export enum Sex {
  Male = "MALE",
  Female = "FEMALE",
  Other = "OTHER",
  Unknown = "UNKNOWN",
}

/**
 *
 * @export
 * @interface User
 */
export interface User {
  /**
   *
   * @type {string}
   * @memberof User
   */
  id?: string;
  /**
   *
   * @type {string}
   * @memberof User
   */
  firstName?: string;
  /**
   *
   * @type {string}
   * @memberof User
   */
  lastName?: string;
  /**
   *
   * @type {string}
   * @memberof User
   */
  userName: string;
  /**
   *
   * @type {UserRole}
   * @memberof User
   */
  role: UserRole;
}
/**
 *
 * @export
 * @interface UserInsert
 */
export interface UserInsert {
  /**
   *
   * @type {string}
   * @memberof UserInsert
   */
  firstName?: string;
  /**
   *
   * @type {string}
   * @memberof UserInsert
   */
  lastName?: string;
  /**
   *
   * @type {string}
   * @memberof UserInsert
   */
  userName: string;
  /**
   *
   * @type {string}
   * @memberof UserInsert
   */
  password: string;
  /**
   *
   * @type {UserRole}
   * @memberof UserInsert
   */
  role: UserRole;
}
/**
 *
 * @export
 * @interface UserList
 */
export interface UserList {
  /**
   *
   * @type {Array<User>}
   * @memberof UserList
   */
  users?: Array<User>;
}
/**
 *
 * @export
 * @enum {string}
 */
export enum UserRole {
  Admin = "ADMIN",
  User = "USER",
}

/**
 *
 * @export
 * @interface UserUpdate
 */
export interface UserUpdate {
  /**
   *
   * @type {string}
   * @memberof UserUpdate
   */
  firstName?: string;
  /**
   *
   * @type {string}
   * @memberof UserUpdate
   */
  lastName?: string;
  /**
   *
   * @type {string}
   * @memberof UserUpdate
   */
  userName?: string;
  /**
   *
   * @type {string}
   * @memberof UserUpdate
   */
  password?: string;
  /**
   *
   * @type {string}
   * @memberof UserUpdate
   */
  oldPassword?: string;
  /**
   *
   * @type {UserRole}
   * @memberof UserUpdate
   */
  role?: UserRole;
}
/**
 *
 * @export
 * @interface Statistics
 */
export interface Statistics {
  /**
   *
   * @type {number}
   * @memberof Statistics
   */
  eventsCount?: any;
  /**
   *
   * @type {number}
   * @memberof Statistics
   */
  indexCasesCount?: any;
  /**
   *
   * @type {number}
   * @memberof Statistics
   */
  sumStatus?: any;
}

/**
 *
 * @export
 * @interface Pageable
 */
export interface Pageable {
  /**
   *
   * @type {number}
   * @memberof Pageable
   */
  offset?: any;
  /**
   *
   * @type {Sort}
   * @memberof Pageable
   */
  sort?: any;
  /**
   *
   * @type {number}
   * @memberof Pageable
   */
  pageSize?: any;
  /**
   *
   * @type {number}
   * @memberof Pageable
   */
  pageNumber?: any;
  /**
   *
   * @type {boolean}
   * @memberof Pageable
   */
  paged?: any;
  /**
   *
   * @type {boolean}
   * @memberof Pageable
   */
  unpaged?: any;
}

/**
 *
 * @export
 * @interface PageIndexCase
 */
export interface PageIndexCase {
  /**
   *
   * @type {number}
   * @memberof PageIndexCase
   */
  totalElements?: any;
  /**
   *
   * @type {number}
   * @memberof PageIndexCase
   */
  totalPages?: any;
  /**
   *
   * @type {number}
   * @memberof PageIndexCase
   */
  size?: any;
  /**
   *
   * @type {Array&lt;DataRequestCaseDetails&gt;}
   * @memberof PageIndexCase
   */
  content: Array<DataRequestCaseDetails>;
  /**
   *
   * @type {number}
   * @memberof PageIndexCase
   */
  number?: any;
  /**
   *
   * @type {Sort}
   * @memberof PageIndexCase
   */
  sort?: any;
  /**
   *
   * @type {boolean}
   * @memberof PageIndexCase
   */
  first?: any;
  /**
   *
   * @type {boolean}
   * @memberof PageIndexCase
   */
  last?: any;
  /**
   *
   * @type {number}
   * @memberof PageIndexCase
   */
  numberOfElements?: any;
  /**
   *
   * @type {Pageable}
   * @memberof PageIndexCase
   */
  pageable?: any;
  /**
   *
   * @type {boolean}
   * @memberof PageIndexCase
   */
  empty?: any;
}

/**
 *
 * @export
 * @interface PageEvent
 */
export interface PageEvent {
  /**
   *
   * @type {number}
   * @memberof PageEvent
   */
  totalElements?: any;
  /**
   *
   * @type {number}
   * @memberof PageEvent
   */
  totalPages?: any;
  /**
   *
   * @type {number}
   * @memberof PageEvent
   */
  size?: any;
  /**
   *
   * @type {Array&lt;ExistingDataRequestClientWithLocation&gt;}
   * @memberof PageEvent
   */
  content: Array<ExistingDataRequestClientWithLocation>;
  /**
   *
   * @type {number}
   * @memberof PageEvent
   */
  number?: any;
  /**
   *
   * @type {Sort}
   * @memberof PageEvent
   */
  sort?: any;
  /**
   *
   * @type {boolean}
   * @memberof PageEvent
   */
  first?: any;
  /**
   *
   * @type {boolean}
   * @memberof PageEvent
   */
  last?: any;
  /**
   *
   * @type {number}
   * @memberof PageEvent
   */
  numberOfElements?: any;
  /**
   *
   * @type {Pageable}
   * @memberof PageEvent
   */
  pageable?: any;
  /**
   *
   * @type {boolean}
   * @memberof PageEvent
   */
  empty?: any;
}

/**
 *
 * @export
 * @interface CheckinApp
 */
export interface CheckinApp {
  name: string;
  groups?: string[];
}

export enum CheckinAppStatus {
  OK = "OK",
  WARNING = "WARNING",
  ERROR = "ERROR",
  UNKNOWN = "UNKNOWN",
}

export interface CheckinAppInfo {
  version?: string;
  name?: string;
}

/**
 *
 * @export
 * @interface CheckinAppStatusInfo
 */
export interface CheckinAppStatusInfo {
  info?: CheckinAppInfo;
  status: CheckinAppStatus;
  message: string;
}

export interface Sort {
  empty?: boolean;
  sorted?: boolean;
  unsorted?: boolean;
}

export interface Page<Content> {
  totalElements?: number;
  totalPages?: number;
  size?: number;
  content: Content[];
  number?: number;
  sort?: Sort;
  first?: boolean;
  last?: boolean;
  numberOfElements?: number;
  pageable?: Pageable;
  empty?: boolean;
}

export type IrisMessageQuery = DataQuery & {
  folder: string;
};

export type PageIrisMessages = Page<IrisMessage>;

export enum IrisMessageContext {
  Inbox = "inbox",
  Outbox = "outbox",
}

export interface IrisMessage {
  name?: string;
  folder: string;
  id: string;
  subject: string;
  body: string;
  author?: string;
  recipient?: string;
  createdAt?: string;
  isRead?: boolean;
}

export type IrisMessageFolder = {
  id: string;
  name: string;
  items?: IrisMessageFolder[];
  context?: IrisMessageContext;
};

/**
 * IrisClientFrontendApi - object-oriented interface
 * @export
 * @class IrisClientFrontendApi
 * @extends {BaseAPI}
 */
export class IrisClientFrontendApi extends BaseAPI {
  /**
   *
   * @summary Logout user
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public logout(options?: RequestOptions): ApiResponse {
    return this.apiRequest("GET", "/user/logout", null, options);
  }

  /**
   *
   * @summary Detail view for index data request with the data submissions already received
   * @param {string} caseId The internal unique CaseId of a index case in format.
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public dataRequestClientCasesCaseIdGet(
    caseId: string,
    options?: RequestOptions
  ): ApiResponse<DataRequestCaseData> {
    assertParamExists("dataRequestClientCasesCaseIdGet", "caseId", caseId);
    const path = `/data-requests-client/cases/${encodeURIComponent(caseId)}`;
    return this.apiRequest("GET", path, null, options);
  }

  /**
   *
   * @summary Fetches index cases
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public dataRequestClientCasesGet(
    options?: RequestOptions
  ): ApiResponse<PageIndexCase> {
    return this.apiRequest("GET", "/data-requests-client/cases", null, options);
  }

  /**
   *
   * @summary Creates a new tracing case for index case data
   * @param {DataRequestCaseClient} dataRequestCaseClient
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public dataRequestClientCasesPost(
    dataRequestCaseClient: DataRequestCaseClient,
    options?: RequestOptions
  ): ApiResponse<DataRequestCaseExtendedDetails> {
    assertParamExists(
      "dataRequestClientCasesPost",
      "dataRequestCaseClient",
      dataRequestCaseClient
    );
    return this.apiRequest(
      "POST",
      "/data-requests-client/cases",
      dataRequestCaseClient,
      options
    );
  }

  /**
   *
   * @summary Patches details of an existing data request
   * @param {string} code The unique code of a data request in format of a UUID sent by the health department.
   * @param {DataRequestClientUpdate} dataRequestClientUpdate
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public dataRequestsClientLocationsCodePatch(
    code: string,
    dataRequestClientUpdate: DataRequestClientUpdate,
    options?: RequestOptions
  ): ApiResponse {
    assertParamExists("dataRequestsClientLocationsCodePatch", "code", code);
    assertParamExists(
      "dataRequestsClientLocationsCodePatch",
      "dataRequestClientUpdate",
      dataRequestClientUpdate
    );
    const path = `/data-requests-client/events/${encodeURIComponent(code)}`;
    return this.apiRequest("PATCH", path, dataRequestClientUpdate, options);
  }

  /**
   *
   * @summary Fetches data requests
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public dataRequestsClientLocationsGet(
    options?: RequestOptions
  ): ApiResponse<PageEvent> {
    return this.apiRequest(
      "GET",
      "/data-requests-client/events",
      null,
      options
    );
  }

  /**
   *
   * @summary Submits a new data request
   * @param {DataRequestClient} dataRequestClient
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public dataRequestsClientLocationsPost(
    dataRequestClient: DataRequestClient,
    options?: RequestOptions
  ): ApiResponse<DataRequestDetails> {
    assertParamExists(
      "dataRequestsClientLocationsPost",
      "dataRequestClient",
      dataRequestClient
    );
    return this.apiRequest(
      "POST",
      "/data-requests-client/events",
      dataRequestClient,
      options
    );
  }

  /**
   *
   * @summary Detail view for Data Request with the data submissions already received
   * @param {string} code The unique code of a data request in format of a UUID sent by the health department.
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public getLocationDetails(
    code: string,
    options?: RequestOptions
  ): ApiResponse<DataRequestDetails> {
    assertParamExists("getLocationDetails", "code", code);
    const path = `/data-requests-client/events/${encodeURIComponent(code)}`;
    return this.apiRequest("GET", path, null, options);
  }

  /**
   *
   * @summary Authenticates a user against IRIS client
   * @param {Credentials} credentials
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public login(
    credentials: Credentials,
    options?: RequestOptions
  ): ApiResponse<UserSession> {
    assertParamExists("login", "credentials", credentials);
    return this.apiRequest("POST", "/login", credentials, options);
  }

  /**
   *
   * @param {*} [options] Override http request option.
   * @memberof IrisClientFrontendApi
   */
  public searchSearchKeywordGet(
    options?: RequestOptions
  ): ApiResponse<LocationList> {
    return this.apiRequest("GET", "/search", null, options);
  }

  /**
   *
   * @summary Get authenticated IRIS user
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public userProfileGet(options?: RequestOptions): ApiResponse<User> {
    return this.apiRequest("GET", "/user-profile", null, options);
  }

  /**
   *
   * @summary Get all IRIS users
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public usersGet(options?: RequestOptions): ApiResponse<UserList> {
    return this.apiRequest("GET", "/users", null, options);
  }

  /**
   *
   * @summary Delete IRIS user
   * @param {string} id The ID of an IRIS Client user.
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public usersIdDelete(id: string, options?: RequestOptions): ApiResponse {
    assertParamExists("usersIdDelete", "id", id);
    const path = `/users/${encodeURIComponent(id)}`;
    return this.apiRequest("DELETE", path, null, options);
  }

  /**
   *
   * @summary Update IRIS user
   * @param {string} id The ID of an IRIS Client user.
   * @param {UserUpdate} userUpdate
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public usersIdPatch(
    id: string,
    userUpdate: UserUpdate,
    options?: RequestOptions
  ): ApiResponse {
    assertParamExists("usersIdPatch", "id", id);
    assertParamExists("usersIdPatch", "userUpdate", userUpdate);
    const path = `/users/${encodeURIComponent(id)}`;
    return this.apiRequest("PATCH", path, userUpdate, options);
  }

  /**
   *
   * @summary Create IRIS user
   * @param {UserInsert} userInsert
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public usersPost(
    userInsert: UserInsert,
    options?: RequestOptions
  ): ApiResponse {
    assertParamExists("usersPost", "userInsert", userInsert);
    return this.apiRequest("POST", "/users", userInsert, options);
  }

  /**
   *
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof StatisticsControllerApi
   */
  public getWeeklyData(options?: RequestOptions): ApiResponse<Statistics> {
    return this.apiRequest(
      "GET",
      "/data-requests-client/statistics",
      null,
      options
    );
  }

  /**
   *
   * @summary Get connected Checkin Apps
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public checkinAppsGet(options?: RequestOptions): ApiResponse<CheckinApp[]> {
    return this.apiRequest("GET", "/status/checkin-apps", null, options);
  }

  /**
   *
   * @summary Get Checkin App Status
   * @param {string} name
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public checkinAppStatusGet(
    name: string,
    options?: RequestOptions
  ): ApiResponse<CheckinAppStatusInfo> {
    assertParamExists("checkinAppStatusGet", "name", name);
    const path = `/status/checkin-apps/${encodeURIComponent(name)}`;
    return this.apiRequest("GET", path, null, options);
  }

  /**
   * @summary Fetches iris messages
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public irisMessagesGet(
    options?: RequestOptions
  ): ApiResponse<PageIrisMessages> {
    return this.apiRequest("GET", "/iris-messages", null, options);
  }

  /**
   * @summary Fetches iris message folders
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public irisMessageFoldersGet(
    options?: RequestOptions
  ): ApiResponse<IrisMessageFolder[]> {
    return this.apiRequest("GET", "/iris-messages/folders", null, options);
  }

  /**
   * @summary Fetches number of unread messages
   * @param {*} [options] Override http request option.
   * @throws {RequiredError}
   * @memberof IrisClientFrontendApi
   */
  public irisUnreadMessageCountGet(
    options?: RequestOptions
  ): ApiResponse<number> {
    return this.apiRequest("GET", "/iris-messages/unread/count", null, options);
  }
}
