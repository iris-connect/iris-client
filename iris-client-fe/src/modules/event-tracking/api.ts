import { DataQuery } from "@/api/common";
import { normalizePageEvent } from "@/views/event-tracking-list/event-tracking-list.data";
import authClient from "@/api-client";
import asyncAction from "@/utils/asyncAction";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";
import { DataRequestClientUpdate } from "@/api";

export const fetchPageEventAction = () => {
  const action = async (query: DataQuery) => {
    return normalizePageEvent(
      (await authClient.dataRequestsClientLocationsGet({ query })).data,
      true
    );
  };
  return asyncAction(action);
};

export const fetchEventDetailsAction = () => {
  const action = async (eventId: string) => {
    return normalizeDataRequestDetails(
      (await authClient.getLocationDetails(eventId)).data,
      true
    );
  };
  return asyncAction(action);
};

export const patchDataRequestAction = () => {
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
