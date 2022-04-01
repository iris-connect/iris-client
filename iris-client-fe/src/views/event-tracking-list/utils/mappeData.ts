import {
  DataRequestStatus,
  ExistingDataRequestClientWithLocation,
} from "@/api";
import { join } from "@/utils/misc";
import dayjs from "@/utils/date";

function getFormattedAddress(
  data?: ExistingDataRequestClientWithLocation
): string {
  const contact = data?.locationInformation?.contact;
  const officialName = contact?.officialName;
  return join(
    [
      data?.locationInformation?.name ?? "-",
      officialName ? `(${officialName})` : "",
      contact?.address?.street,
      join([contact?.address?.zip, contact?.address?.city], " "),
    ],
    "\n"
  );
}

function getFormattedDate(date?: string): string {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format("LLL");
  }
  return "-";
}

export type EventTrackingListTableRow = {
  id: string;
  generatedTime: string;
  address: string;
  code: string;
  name: string;
  lastChange: string;
  startTime: string;
  endTime: string;
  extID: string;
  status: string;
};

export const getEventTrackingListTableRows = (
  eventTrackingList: ExistingDataRequestClientWithLocation[] | undefined,
  selectableStatus?: DataRequestStatus[] | null
): EventTrackingListTableRow[] => {
  return (eventTrackingList || []).map((dataRequest) => {
    return {
      id: dataRequest?.code || "",
      address: getFormattedAddress(dataRequest),
      endTime: getFormattedDate(dataRequest?.end),
      startTime: getFormattedDate(dataRequest?.start),
      generatedTime: getFormattedDate(dataRequest?.requestedAt),
      lastChange: getFormattedDate(dataRequest?.lastUpdatedAt),
      extID: dataRequest?.externalRequestId || "-",
      code: dataRequest?.code || "",
      name: dataRequest?.name || "-",
      status: dataRequest?.status?.toString() || "-",
      isSelectable:
        selectableStatus && dataRequest?.status
          ? selectableStatus.indexOf(dataRequest?.status) >= 0
          : true,
    };
  });
};
