// eslint-disable-next-line @typescript-eslint/no-var-requires
const { Parser } = require("json2csv");

export interface Header {
  text: string;
  value: string;
}

const exportStandardCsv = function (
  headers: Array<Header>,
  rows: Array<Array<string>>,
  fileName?: string
): Promise<string> {
  return new Promise((resolve, reject) => {
    const fields = headers
      .map((header) => {
        if (!header.text) return;
        return {
          label: header.text,
          value: header.value,
        };
      })
      .filter((v) => v);
    try {
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
      });
      const csv = parser.parse(rows);
      downloadCsvFile(fileName || "Export", csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportAlternativeStandardCsv = function (
  headers: Array<Header>,
  rows: Array<Array<string>>,
  fileName?: string
): Promise<string> {
  console.log("exportAlternativeStandardCsv:");
  console.log("headers");
  console.log(headers);
  console.log("rows");
  console.log(rows);
  console.log("fileName");
  console.log(fileName);

  return new Promise((resolve, reject) => {
    const fields = headers
      .map((header) => {
        if (!header.text) return;
        return {
          label: header.text,
          value: header.value,
        };
      })
      .filter((v) => v);
    try {
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
        delimiter: ";",
        quote: "",
      });
      const csv = parser.parse(rows);
      downloadCsvFile(fileName || "Export", csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportSormasEventParticipantsCsv = function (
  headers: Array<Header>,
  rows: Array<Array<string>>,
  fileName?: string
): Promise<string> {
  return new Promise((resolve, reject) => {
    const fields = headers
      .map((header) => {
        if (!header.text) return;
        return {
          label: header.text,
          value: header.value,
        };
      })
      .filter((v) => v);
    try {
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
        delimiter: ";",
        quote: "",
      });
      const csv = parser.parse(rows);
      downloadCsvFile(fileName || "Export", csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportSormasContactPersonCsv = function (
  headers: Array<Header>,
  rows: Array<Array<string>>,
  fileName?: string
): Promise<string> {
  return new Promise((resolve, reject) => {
    const fields = headers
      .map((header) => {
        if (!header.text) return;
        return {
          label: header.text,
          value: header.value,
        };
      })
      .filter((v) => v);
    try {
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
        delimiter: ";",
        quote: "",
      });
      const csv = parser.parse(rows);
      downloadCsvFile(fileName || "Export", csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const downloadCsvFile = function (fileName: string, csv: string): void {
  if (navigator.msSaveBlob) {
    // IE10
    navigator.msSaveBlob(
      new Blob([csv], {
        type: "application/octet-stream",
      }),
      `${fileName}.csv`
    );
  } else {
    // HTML5
    const link = document.createElement("a");
    link.setAttribute("href", encodeURI("data:text/csv;charset=utf-8," + csv));
    link.setAttribute("download", `${fileName}.csv`);
    document.body.appendChild(link); // required for FF
    link.click();
    document.body.removeChild(link);
  }
};

const dataExport = {
  exportStandardCsv,
  exportAlternativeStandardCsv,
  exportSormasEventParticipantsCsv,
  exportSormasContactPersonCsv,
};

export default dataExport;
