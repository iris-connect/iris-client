import dataExport from "@/utils/data-export/data-export";
import { TableRow } from "@/views/event-tracking-details/utils/mappedData";

export type EventParticipantData = {
  involvementDescription: string;
  firstName: string;
  lastName: string;
  sex: string;
  phone: string;
  email: string;
  postalCode: string;
  city: string;
  street: string;
  houseNumber: string;
};

const getHeaders = () => [
  {
    text: "EventParticipant",
    value: "involvementDescription",
  },
  {
    text: "Person",
    value: "firstName",
  },
  {
    text: "Person",
    value: "lastName",
  },
  {
    text: "Person",
    value: "sex",
  },
  {
    text: "Person",
    value: "phone",
  },
  {
    text: "Person",
    value: "email",
  },
  {
    text: "Person",
    value: "postalCode",
  },
  {
    text: "Person",
    value: "city",
  },
  {
    text: "Person",
    value: "street",
  },
  {
    text: "Person",
    value: "houseNumber",
  },
];

const mapData = (tableRows: TableRow[]): EventParticipantData[] => {
  const data: EventParticipantData[] = [];

  const headerInstance: EventParticipantData = {
    involvementDescription: "involvementDescription",
    firstName: "person.firstName",
    lastName: "person.lastName",
    sex: "person.sex",
    phone: "person.phone",
    email: "person.emailAddress",
    postalCode: "person.address.postalCode",
    city: "person.address.city",
    street: "person.address.street",
    houseNumber: "person.address.houseNumber",
  };
  data.push(headerInstance);

  tableRows.forEach((element) => {
    const involvementDescription =
      element.comment +
      " // " +
      element.checkInTime +
      " Uhr bis " +
      element.checkOutTime +
      " Uhr (Maximale Kontaktdauer " +
      element.maxDuration +
      ")";

    const dataInstance: EventParticipantData = {
      involvementDescription: involvementDescription,
      firstName: element.firstName,
      lastName: element.lastName,
      sex: element.raw.sex || "",
      phone: element.phone,
      email: element.email,
      postalCode: element.raw.address?.zipCode || "",
      city: element.raw.address?.city || "",
      street: element.raw.address?.street || "",
      houseNumber: element.raw.address?.houseNumber || "",
    };
    data.push(dataInstance);
  });
  return data;
};

const exportCsv = (
  rows: EventParticipantData[],
  fileName: string
): Promise<unknown> => {
  return dataExport.exportCsv(getHeaders(), rows, { fileName, quoted: false });
};

const exportSormasEventParticipants = {
  mapData,
  exportCsv,
};

export default exportSormasEventParticipants;
