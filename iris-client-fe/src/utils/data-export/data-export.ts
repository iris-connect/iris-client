import { utils, writeFile, CellObject, FullProperties, WorkSheet } from "xlsx";
import sanitization from "@/utils/data-export/sanitization";
import appConfig from "@/config";
import dayjs from "@/utils/date";

export type Row = Record<string, unknown>;
export type Header =
  | string
  | {
      text: string;
      value: string;
      compose?: (row: Row, header?: Header) => string;
    };

export interface ExportConfig {
  fileName: string;
}

export interface ExportConfigCSV extends ExportConfig {
  quoted?: boolean;
}

type ExportConfigXLSXFormat = {
  colFormats?: Record<string, string>;
  autoFormat?: boolean;
};

export type ExportConfigXLSX = ExportConfig &
  FullProperties &
  ExportConfigXLSXFormat;

type WorksheetRange = {
  sRow?: number;
  sCol?: number;
  eRow: number;
  eCol: number;
};

const getHeaderRow = (headers: Header[]): string[] => {
  return headers.map((header) => {
    if (typeof header === "string") return header;
    return header.text;
  });
};

const formatXlsxCells = (
  ws: WorkSheet,
  range: WorksheetRange,
  config: ExportConfigXLSXFormat
) => {
  const defaultFmt = "@";
  for (let c = range.sCol || 0; c <= range.eCol; c++) {
    const colHeader = ws[utils.encode_cell({ r: 0, c })];
    for (let r = range.sRow || 0; r <= range.eRow; r++) {
      const cell = ws[utils.encode_cell({ r, c })];
      cell.z = defaultFmt;
      if (config.autoFormat) {
        autoFormatXlsxCell(cell);
      }
      // in addition to autoFormat we can define formats for columns
      const colFormat = config.colFormats?.[colHeader.v];
      if (colFormat) {
        cell.z = colFormat;
      }
    }
  }
};

// DD.MM.YYYY
const LOCALIZED_DATE_FORMAT = dayjs.localeData().longDateFormat("L");

export const EXPORT_DATE_FORMAT = {
  APP: LOCALIZED_DATE_FORMAT,
  XLSX: LOCALIZED_DATE_FORMAT,
};

// 24hr time format: dayjs: HH:mm, xlsx: hh:mm
export const EXPORT_DATE_TIME_FORMAT = {
  APP: LOCALIZED_DATE_FORMAT + " HH:mm",
  XLSX: LOCALIZED_DATE_FORMAT + " hh:mm",
};

const EXPORT_DATE_FORMATS = [EXPORT_DATE_TIME_FORMAT, EXPORT_DATE_FORMAT];

const autoFormatXlsxCell = (cell: CellObject): void => {
  const cellValue = cell.v;
  if (typeof cellValue === "boolean") return;
  EXPORT_DATE_FORMATS.forEach((format) => {
    if (dayjs(cellValue, format.APP, true).isValid()) {
      cell.z = format.XLSX;
      return;
    }
  });
};

const exportXlsx = (
  headers: Header[],
  rows: Row[],
  config: ExportConfigXLSX
) => {
  const sanitizedRows = sanitization.sanitizeRows(rows, headers, false);
  const wb = utils.book_new();
  if (!wb.Props) {
    wb.Props = {
      Version: appConfig.appVersionId,
    };
  }
  wb.Props = new Proxy(wb.Props, {
    get: (o, p: keyof FullProperties) =>
      p === "Application" ? "IRIS connect" : o[p],
  });
  const headerRow = getHeaderRow(headers);
  const ws = utils.aoa_to_sheet([headerRow, ...sanitizedRows]);
  formatXlsxCells(
    ws,
    { eRow: sanitizedRows.length, eCol: headerRow.length - 1 },
    config
  );
  utils.book_append_sheet(wb, ws, "Tabelle1");
  writeFile(wb, `${config.fileName}.xlsx`);
};

const exportCsv = (
  headers: Header[],
  rows: Row[],
  config: ExportConfigCSV
): Promise<string> => {
  return new Promise((resolve, reject) => {
    try {
      const forceQuotes = config.quoted !== false;
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
      const ws = utils.aoa_to_sheet([getHeaderRow(headers), ...sanitizedRows]);
      let csv = utils.sheet_to_csv(ws, {
        FS: delimiter,
        forceQuotes,
      });
      // Replace the enclosed and escaped placeholder quote ("""") with a blank value that is enclosed in quotes ("").
      if (forceQuotes) {
        csv = csv.replace(/"{4}/g, '""');
      }
      downloadCsvFile(config.fileName, csv);
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
