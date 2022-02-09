import { DataQuery } from "@/api/common";
import { normalizePageEvent } from "@/views/event-tracking-list/event-tracking-list.data";
import authClient from "@/api-client";
import asyncAction from "@/utils/asyncAction";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";
import { DataRequestClientUpdate } from "@/api";
import { apiBundleProvider } from "@/utils/api";

const fetchPageEvent = () => {
  const action = async (query: DataQuery) => {
    return normalizePageEvent(
      (await authClient.dataRequestsClientLocationsGet({ query })).data,
      true
    );
  };
  return asyncAction(action);
};

const fetchEventDetails = () => {
  const action = async (eventId: string) => {
    return normalizeDataRequestDetails(
      (await authClient.getLocationDetails(eventId)).data,
      true
    );
  };
  return asyncAction(action);
};

const patchDataRequest = () => {
  const action = async (payload: {
    id: string;
    data: DataRequestClientUpdate;
  }) => {
    return authClient.dataRequestsClientLocationsCodePatch(
      payload.id,
      payload.data
    );
  };
  return asyncAction(action);
};

export const eventTrackingApi = {
  fetchPageEvent,
  fetchEventDetails,
  patchDataRequest,
};

export const bundleEventTrackingApi = apiBundleProvider(eventTrackingApi);
