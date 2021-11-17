import dataExport, { Row } from "@/utils/data-export/data-export";
import { getAddressHeaders } from "@/utils/data-export/common";

const getHeaders = () => {
  return [
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
      text: "Geburtsdatum",
      value: "dateOfBirth",
    },
    {
      text: "Erster Kontakt am",
      value: "firstContactDate",
    },
    {
      text: "Letzter Kontakt am",
      value: "lastContactDate",
    },
    {
      text: "Kontaktkathegorie",
      value: "contactCategory",
    },
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
    ...getAddressHeaders(),
    {
      text: "Arbeitsplatz",
      value: "workPlace",
    },
    {
      text: "Kontaktsituation",
      value: "basicConditions",
    },
  ];
};

const exportCsv = (rows: Row[], fileName: string): Promise<unknown> => {
  const headers = getHeaders();
  return dataExport.exportCsv(headers, rows, { fileName });
};

const exportStandardContacts = {
  exportCsv,
};

export default exportStandardContacts;
