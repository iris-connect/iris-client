import dataExport, { Row } from "@/utils/data-export/data-export";
import { getAddressHeaders } from "@/utils/data-export/common";

const getHeaders = () => {
  return [
    {
      text: "Nachname",
      value: "lastName",
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
  ];
};

const exportCsv = (
  rows: Row[],
  fileName: string,
  quoted?: boolean
): Promise<unknown> => {
  const headers = getHeaders();
  return dataExport.exportCsv(headers, rows, {
    fileName,
    quoted: quoted !== false,
  });
};

const exportXlsx = (rows: Row[], fileName: string) => {
  const headers = getHeaders();
  return dataExport.exportXlsx(headers, rows, { fileName });
};

const exportCsvStandard = {
  exportCsv,
  exportXlsx,
};

export default exportCsvStandard;
