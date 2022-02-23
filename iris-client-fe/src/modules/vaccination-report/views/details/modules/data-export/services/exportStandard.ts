import dataExport, { Row } from "@/utils/data-export/data-export";
import { composeAddressHeader } from "@/utils/data-export/common";

const getHeaders = () => {
  return [
    {
      text: "Vorname",
      value: "firstName",
    },
    {
      text: "Nachname",
      value: "lastName",
    },
    composeAddressHeader(),
    {
      text: "Erreger",
      value: "vaccination",
    },
    {
      text: "Impfstatus",
      value: "vaccinationStatus",
    },
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

const exportStandard = {
  exportCsv,
  exportXlsx,
};

export default exportStandard;
