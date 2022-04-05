import {
  Address,
  DataRequestDetails,
  DataRequestStatus,
  Guest,
  LocationInformation,
} from "@/api";
import { getFormattedDate } from "@/utils/date";
import Genders from "@/constants/Genders";
import { getValidPhoneNumber } from "@/utils/misc";
import { DataTableHeader } from "vuetify";

export type FormData = {
  name?: string;
  externalRequestId?: string;
  comment?: string;
};

export type EventData = {
  code: string;
  startTime: string;
  endTime: string;
  generatedTime: string;
  status?: DataRequestStatus;
  lastChange: string;
  location?: LocationInformation;
  additionalInformation: string;
};

export type GuestListTableRow = {
  lastName: string;
  firstName: string;
  checkInTime: string;
  checkOutTime: string;
  maxDuration: string;
  comment: string;
  sex: string;
  email: string;
  phone: string;
  mobilePhone: string;
  address: string;
  raw: Guest;
};

function getFormattedAddress(address?: Address | null): string {
  if (address) {
    return `${sanitiseFieldForDisplay(
      address.street
    )} ${sanitiseFieldForDisplay(
      address.houseNumber
    )} \n${sanitiseFieldForDisplay(address.zipCode)} ${sanitiseFieldForDisplay(
      address.city
    )}`;
  }
  return "-";
}
function sanitiseFieldForDisplay(text = ""): string {
  const RE = RegExp(/\s+/, "g");
  return text.replace(RE, " ");
}

export const getFormData = (event: DataRequestDetails | null): FormData => {
  return {
    externalRequestId: event?.externalRequestId || "",
    name: event?.name || "",
    comment: event?.comment || "",
  };
};

export const getEventData = (event: DataRequestDetails | null): EventData => {
  return {
    code: event?.code || "",
    startTime: getFormattedDate(event?.start),
    endTime: getFormattedDate(event?.end),
    generatedTime: getFormattedDate(event?.requestedAt),
    status: event?.status,
    lastChange: getFormattedDate(event?.lastModifiedAt),
    location: event?.locationInformation,
    additionalInformation: event?.requestDetails || "-",
  };
};

type DataTableHeaders = {
  [K in "headers" | "expandedHeaders"]: DataTableHeader[];
};

export const getGuestListTableHeaders = (
  selectable: boolean
): DataTableHeaders => {
  const headers: DataTableHeader[] = [
    {
      text: "Nachname",
      value: "lastName",
      align: "start",
    },
    {
      text: "Vorname",
      value: "firstName",
    },
    {
      text: "Check-In",
      value: "checkInTime",
    },
    {
      text: "Check-Out",
      value: "checkOutTime",
    },
    {
      text: "max. Kontaktdauer",
      value: "maxDuration",
    },
    {
      text: "Kommentar",
      value: "comment",
    },
    { text: "", value: "data-table-expand" },
  ];
  return {
    headers: selectable
      ? [{ text: "", value: "data-table-select" }, ...headers]
      : headers,
    expandedHeaders: [
      {
        text: "Geschlecht",
        value: "sex",
      },
      {
        text: "E-Mail",
        value: "email",
      },
      {
        text: "Telefon",
        value: "phone",
      },
      {
        text: "Mobil",
        value: "mobilePhone",
      },
      {
        text: "Adresse",
        value: "address",
      },
    ],
  };
};

export const getGuestListTableRows = (
  guests?: Guest[] | null,
  eventStart?: string,
  eventEnd?: string
): GuestListTableRow[] => {
  return (guests || []).map((guest, index) => {
    const attendTo = guest.attendanceInformation?.attendTo;
    const checkOut = attendTo ? new Date(attendTo) : null;

    const attendFrom = guest.attendanceInformation?.attendFrom;
    const checkIn = attendFrom ? new Date(attendFrom) : null;

    const startTime = eventStart ? new Date(eventStart) : checkIn;
    const endTime = eventEnd ? new Date(eventEnd) : checkOut;

    let checkInTime = "-";
    if (checkIn && startTime) {
      checkInTime = getFormattedDate(checkIn);
    }

    let checkOutTime = "-";
    if (checkOut) {
      checkOutTime = getFormattedDate(checkOut);
    }

    // min(iE, cE)-max(iS, cS)
    // |--------------| TIME INFECTED PERSON _i Started and Ended_ AT LOCATION
    //       |----------------| TIME CONTACT PERSON _c Started and Ended_ AT LOCATION
    //       |--------| RELEVANT

    let maxDuration = "keine";
    if (checkOut && checkIn && startTime && endTime) {
      const durationSeconds =
        (Math.min(checkOut.valueOf(), endTime.valueOf()) -
          Math.max(checkIn.valueOf(), startTime.valueOf())) /
        1000;
      const hours = Math.floor(durationSeconds / 3600);
      const minutes = Math.round((durationSeconds - hours * 3600) / 60);
      if (durationSeconds > 0) {
        if (hours > 0)
          maxDuration = hours.toString() + "h " + minutes.toString() + "min";
        else if (minutes > 0) maxDuration = minutes.toString() + "min";
      }
    }
    return {
      id: index,
      lastName: sanitiseFieldForDisplay(guest.lastName) || "-",
      firstName: sanitiseFieldForDisplay(guest.firstName) || "-",
      checkInTime,
      checkOutTime,
      maxDuration: maxDuration,
      comment:
        sanitiseFieldForDisplay(
          guest.attendanceInformation.additionalInformation
        ) || "-",
      sex: guest.sex ? Genders.getName(guest.sex) : "-",
      email: sanitiseFieldForDisplay(guest.email) || "-",
      phone:
        sanitiseFieldForDisplay(
          getValidPhoneNumber(guest.phone, guest.mobilePhone)
        ) || "-",
      mobilePhone: sanitiseFieldForDisplay(guest.mobilePhone) || "-",
      address: getFormattedAddress(guest.address),
      raw: guest,
    };
  });
};
