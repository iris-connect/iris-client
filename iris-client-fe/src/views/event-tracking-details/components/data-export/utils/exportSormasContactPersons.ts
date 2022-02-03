import dataExport from "@/utils/data-export/data-export";
import { GuestListTableRow } from "@/views/event-tracking-details/utils/mappedData";
import { DataRequestDetails } from "@/api";

export type ContactCaseData = {
  description: string;
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
    text: "description",
    value: "description",
  },
  {
    text: "person.firstName",
    value: "firstName",
  },
  {
    text: "person.lastName",
    value: "lastName",
  },
  {
    text: "person.sex",
    value: "sex",
  },
  {
    text: "person.phone",
    value: "phone",
  },
  {
    text: "person.emailAddress",
    value: "email",
  },
  {
    text: "person.address.postalCode",
    value: "postalCode",
  },
  {
    text: "person.address.city",
    value: "city",
  },
  {
    text: "person.address.street",
    value: "street",
  },
  {
    text: "person.address.houseNumber",
    value: "houseNumber",
  },
];

const mapData = (
  event: DataRequestDetails | null,
  tableRows: GuestListTableRow[]
): ContactCaseData[] => {
  const data: ContactCaseData[] = [];
  tableRows.forEach((element) => {
    const description =
      "Aus Ereignis " +
      (event?.externalRequestId || "-") +
      ": " +
      element.comment +
      " // " +
      element.checkInTime +
      " Uhr bis " +
      element.checkOutTime +
      " Uhr (Maximale Kontaktdauer " +
      element.maxDuration +
      ")";
    const dataInstance: ContactCaseData = {
      description: description,
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
  rows: ContactCaseData[],
  fileName: string
): Promise<unknown> => {
  return dataExport.exportCsv(getHeaders(), rows, { fileName, quoted: false });
};

const exportSormasContactPersons = {
  mapData,
  exportCsv,
};

export default exportSormasContactPersons;
