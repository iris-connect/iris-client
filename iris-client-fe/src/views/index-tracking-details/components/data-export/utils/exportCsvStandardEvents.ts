import dataExport, { Row } from "@/utils/data-export/data-export";
import { getAddressHeaders } from "@/utils/data-export/common";

const getHeaders = () => {
  return [
    {
      text: "Event",
      value: "name",
    },
    {
      text: "Telefonnummer",
      value: "phone",
    },
    ...getAddressHeaders(),
    {
      text: "zus. Informationen",
      value: "additionalInformation",
    },
  ];
};

const exportData = (rows: Row[], fileName: string): Promise<unknown> => {
  const headers = getHeaders();
  return dataExport.exportCsv(headers, rows, fileName);
};

const exportCsvStandardEvents = {
  exportData,
};

export default exportCsvStandardEvents;
