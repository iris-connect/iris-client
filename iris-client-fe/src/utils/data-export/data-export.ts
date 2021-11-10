import XLSX from "xlsx";
import sanitization from "@/utils/data-export/sanitization";

export type Row = Record<string, unknown>;
export type Header =
  | string
  | {
      text: string;
      value: string;
      compose?: (row: Row, header?: Header) => string;
    };

const getHeaderRow = (headers: Header[]): string[] => {
  return headers.map((header) => {
    if (typeof header === "string") return header;
    return header.text;
  });
};

const exportXlsx = (headers: Header[], rows: Row[], fileName: string) => {
  const sanitizedRows = sanitization.sanitizeRows(rows, headers, false);
  const wb = XLSX.utils.book_new();
  const ws = XLSX.utils.aoa_to_sheet([getHeaderRow(headers), ...sanitizedRows]);
  XLSX.utils.book_append_sheet(wb, ws);
  XLSX.writeFile(wb, `${fileName}.xlsx`);
};

const exportCsv = (
  headers: Header[],
  rows: Row[],
  fileName: string,
  quoted?: boolean
): Promise<string> => {
  return new Promise((resolve, reject) => {
    try {
      const forceQuotes = quoted !== false;
      const delimiter = forceQuotes ? "," : ";";
      const replaceDelimiters =
        window.irisAppContext?.csvExportStandardAtomicAddress !== "true";
      let sanitizedRows = sanitization.sanitizeRows(
        rows,
        headers,
        replaceDelimiters
      );
      // sheetjs doesn't apply quotes for empty cells. => ("a","b",,"c",,)
      // We have to do it ourself to ensure backwards compatibility => ("a","b","","c","","")
      // To achieve this we set a quote as cell value and undo it after the csv is generated.
      // sheetjs will automatically enclose it in quotes and escape it by preceding it with another quote resulting in """".
      // """" as field value should be safe to use as a placeholder because it cannot be confused with a sanitized field value as quotes are removed by the sanitizer.
      if (forceQuotes) {
        sanitizedRows = sanitizedRows.map((row) => {
          return row.map((field) => (field ? field : '"'));
        });
      }
      const ws = XLSX.utils.aoa_to_sheet([
        getHeaderRow(headers),
        ...sanitizedRows,
      ]);
      let csv = XLSX.utils.sheet_to_csv(ws, {
        FS: delimiter,
        forceQuotes,
      });
      // Replace the enclosed and escaped placeholder quote ("""") with a blank value that is enclosed in quotes ("").
      if (forceQuotes) {
        csv = csv.replace(/"{4}/g, '""');
      }
      downloadCsvFile(fileName, csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

type LegacyNavigator = Navigator & {
  msSaveBlob?: (blob: Blob, defaultName?: string) => boolean;
};

const downloadCsvFile = function (fileName: string, csv: string): void {
  const legacyNavigator = navigator as LegacyNavigator;
  if (legacyNavigator.msSaveBlob) {
    // IE10
    legacyNavigator.msSaveBlob(
      new Blob([csv], {
        type: "application/octet-stream",
      }),
      `${fileName}.csv`
    );
  } else {
    // HTML5
    const link = document.createElement("a");
    link.setAttribute(
      "href",
      encodeURI("data:text/csv;charset=utf-8," + "\uFEFF" + csv)
    );
    link.setAttribute("download", `${fileName}.csv`);
    document.body.appendChild(link); // required for FF
    link.click();
    document.body.removeChild(link);
  }
};

const dataExport = {
  exportCsv,
  exportXlsx,
};

export default dataExport;
