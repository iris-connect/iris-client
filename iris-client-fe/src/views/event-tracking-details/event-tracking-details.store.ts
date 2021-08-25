import {
  Address,
  DataRequestClientUpdate,
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
import { RootState } from "@/store/types";

import { Commit, Dispatch, Module } from "vuex";
import authClient from "@/api-client";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";
import { entryNormalizer, notifyDifference, parseData } from "@/utils/data";
import _isNil from "lodash/isNil";

export type EventTrackingDetailsState = {
  eventTrackingDetails: DataRequestDetails | null;
  eventTrackingDetailsLoading: boolean;
  eventTrackingDetailsLoadingError: ErrorMessage;
  dataRequestPatchOngoing: boolean;
  dataRequestPatchError: ErrorMessage;
};

export interface EventTrackingDetailsModule
  extends Module<EventTrackingDetailsState, RootState> {
  mutations: {
    setEventTrackingDetails(
      state: EventTrackingDetailsState,
      eventTrackingDetails: DataRequestDetails | null
    ): void;
    setEventTrackingDetailsLoading(
      state: EventTrackingDetailsState,
      payload: boolean
    ): void;
    setEventTrackingDetailsLoadingError(
      state: EventTrackingDetailsState,
      payload: ErrorMessage
    ): void;
    setDataRequestPatchOngoing(
      state: EventTrackingDetailsState,
      payload: boolean
    ): void;
    setDataRequestPatchError(
      state: EventTrackingDetailsState,
      payload: ErrorMessage
    ): void;
    reset(state: EventTrackingDetailsState, payload: null): void;
  };
  actions: {
    fetchEventTrackingDetails(
      { commit }: { commit: Commit },
      eventId: string
    ): Promise<void>;
    patchDataRequest(
      { commit, dispatch }: { commit: Commit; dispatch: Dispatch },
      payload: {
        id: string;
        data: DataRequestClientUpdate;
      }
    ): Promise<void>;
  };
}

const defaultState: EventTrackingDetailsState = {
  eventTrackingDetails: null,
  eventTrackingDetailsLoading: false,
  eventTrackingDetailsLoadingError: null,
  dataRequestPatchOngoing: false,
  dataRequestPatchError: null,
};

const eventTrackingDetails: EventTrackingDetailsModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setEventTrackingDetails(state, eventTrackingDetails) {
      state.eventTrackingDetails = eventTrackingDetails;
    },
    setEventTrackingDetailsLoading(state, loading) {
      state.eventTrackingDetailsLoading = loading;
    },
    setEventTrackingDetailsLoadingError(state, payload) {
      state.eventTrackingDetailsLoadingError = payload;
    },
    setDataRequestPatchOngoing(state, payload) {
      state.dataRequestPatchOngoing = payload;
    },
    setDataRequestPatchError(state, payload) {
      state.dataRequestPatchError = payload;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async fetchEventTrackingDetails({ commit }, eventId) {
      let eventTrackingDetails: DataRequestDetails | null = null;
      commit("setEventTrackingDetailsLoading", true);
      commit("setEventTrackingDetailsLoadingError", null);
      try {
        eventTrackingDetails = normalizeDataRequestDetails(
          (await authClient.getLocationDetails(eventId)).data
        );
      } catch (e) {
        commit("setEventTrackingDetailsLoadingError", getErrorMessage(e));
      } finally {
        commit("setEventTrackingDetails", eventTrackingDetails);
        commit("setEventTrackingDetailsLoading", false);
      }
    },
    async patchDataRequest({ commit, dispatch }, payload): Promise<void> {
      commit("setDataRequestPatchOngoing", true);
      commit("setDataRequestPatchError", null);
      try {
        await authClient.dataRequestsClientLocationsCodePatch(
          payload.id,
          payload.data
        );
      } catch (e) {
        commit("setDataRequestPatchError", getErrorMessage(e));
        throw e;
      } finally {
        commit("setDataRequestPatchOngoing", false);
      }
      await dispatch("fetchEventTrackingDetails", payload.id);
    },
  },
};

const normalizeLocationAddress = (
  locationAddress?: LocationAddress
): LocationAddress => {
  const normalizer = entryNormalizer(locationAddress);
  return {
    street: normalizer("street", ""),
    city: normalizer("city", ""),
    zip: normalizer("zip", ""),
  };
};

const normalizeLocationContact = (
  locationContact?: LocationContact
): LocationContact => {
  const normalizer = entryNormalizer(locationContact);
  return {
    officialName: normalizer("officialName", undefined),
    representative: normalizer("representative", undefined),
    address: normalizeLocationAddress(locationContact?.address),
    ownerEmail: normalizer("ownerEmail", undefined),
    email: normalizer("email", undefined),
    phone: normalizer("phone", undefined),
  };
};

const normalizeLocationContext = (
  locationContext?: LocationContext
): LocationContext => {
  const normalizer = entryNormalizer(locationContext);
  return {
    id: normalizer("id", ""),
    name: normalizer("name", ""),
  };
};

const normalizeLocationInformation = (
  locationInformation?: LocationInformation
): LocationInformation => {
  const normalizer = entryNormalizer(locationInformation);
  const contexts = normalizer("contexts", undefined, "array");
  return {
    id: normalizer("id", ""),
    providerId: normalizer("providerId", ""),
    name: normalizer("name", ""),
    publicKey: normalizer("publicKey", undefined),
    contact: normalizeLocationContact(locationInformation?.contact),
    contexts: contexts?.map(normalizeLocationContext),
  };
};

const normalizeAddress = (address?: Address): Address => {
  const normalizer = entryNormalizer(address);
  return {
    street: normalizer("street", undefined),
    houseNumber: normalizer("houseNumber", undefined),
    zipCode: normalizer("zipCode", undefined),
    city: normalizer("city", undefined),
  };
};

const normalizeGuestAllOfAttendanceInformation = (
  attendanceInformation?: GuestAllOfAttendanceInformation
): GuestAllOfAttendanceInformation => {
  const normalizer = entryNormalizer(attendanceInformation);
  return {
    descriptionOfParticipation: normalizer(
      "descriptionOfParticipation",
      undefined
    ),
    attendFrom: normalizer("attendFrom", ""),
    attendTo: normalizer("attendTo", ""),
    additionalInformation: normalizer("additionalInformation", undefined),
  };
};

const normalizeGuest = (guest?: Guest): Guest => {
  const normalizer = entryNormalizer(guest);
  return {
    firstName: normalizer("firstName", ""),
    lastName: normalizer("lastName", ""),
    dateOfBirth: normalizer("dateOfBirth", undefined),
    sex: normalizer("sex", undefined),
    email: normalizer("email", undefined),
    phone: normalizer("phone", undefined),
    mobilePhone: normalizer("mobilePhone", undefined),
    address: !_isNil(guest?.address)
      ? normalizeAddress(guest?.address)
      : guest?.address,
    attendanceInformation: normalizeGuestAllOfAttendanceInformation(
      guest?.attendanceInformation
    ),
    identityChecked: normalizer("identityChecked", undefined, "boolean"),
  };
};

const normalizeGuestListDataProvider = (
  dataProvider?: GuestListDataProvider
): GuestListDataProvider => {
  const normalizer = entryNormalizer(dataProvider);
  return {
    name: normalizer("name", ""),
    address: normalizeAddress(dataProvider?.address),
  };
};

const normalizeGuestList = (guestList?: GuestList): GuestList => {
  const normalizer = entryNormalizer(guestList);
  const guests = normalizer("guests", [], "array");
  return {
    guests: guests.map(normalizeGuest),
    dataProvider: normalizeGuestListDataProvider(guestList?.dataProvider),
    additionalInformation: normalizer("additionalInformation", undefined),
    startDate: normalizer("startDate", undefined, "dateString"),
    endDate: normalizer("endDate", undefined, "dateString"),
  };
};

export const normalizeDataRequestDetails = (
  details?: DataRequestDetails
): DataRequestDetails => {
  const normalizer = entryNormalizer(details);
  const normalizedDetails = {
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
    locationInformation: details?.locationInformation
      ? normalizeLocationInformation(details?.locationInformation)
      : details?.locationInformation,
    submissionData: details?.submissionData
      ? normalizeGuestList(details?.submissionData)
      : undefined,
  };
  const parsedData = parseData(normalizedDetails);
  notifyDifference(details, parsedData, "DataRequestDetails");
  return parsedData;
};

export default eventTrackingDetails;
