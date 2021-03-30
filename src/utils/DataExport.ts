// eslint-disable-next-line @typescript-eslint/no-var-requires
const { Parser } = require("json2csv");

export interface Header {
  text: string;
  value: string;
}

const exportCsv = function (
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
      const parser = new Parser({ fields });
      const csv = parser.parse(rows);
      downloadCsvFile(fileName || "Export", csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const downloadCsvFile = function (fileName: string, csv: string): void {
  const link = document.createElement("a");
  link.setAttribute("href", encodeURI("data:text/csv;charset=utf-8," + csv));
  link.setAttribute("download", `${fileName}.csv`);
  document.body.appendChild(link);
  link.click();
};

const DataExport = {
  exportCsv,
};

export default DataExport;
