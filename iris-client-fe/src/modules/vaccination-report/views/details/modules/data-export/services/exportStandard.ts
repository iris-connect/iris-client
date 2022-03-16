import dataExport, { Row } from "@/utils/data-export/data-export";
import { composeAddressHeader } from "@/utils/data-export/common";
import vaccinationReportConstants from "@/modules/vaccination-report/services/constants";
import _get from "lodash/get";

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
    {
      text: "Geschlecht",
      value: "sex",
    },
    {
      text: "Geburtsdatum",
      value: "dateOfBirth",
    },
    composeAddressHeader(),
    {
      text: "E-Mail",
      value: "eMail",
    },
    {
      text: "Telefon",
      value: "phone",
    },
    {
      text: "Erreger",
      value: "vaccination",
    },
    {
      text: "Impfstatus",
      value: "vaccinationStatus",
      compose: (row: Row) => {
        const status = _get(row, "vaccinationStatus");
        if (typeof status === "string") {
          return vaccinationReportConstants.getStatusName(status);
        }
        return "";
      },
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
