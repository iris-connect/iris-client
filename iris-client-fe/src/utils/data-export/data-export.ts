import _get from "lodash/get";
import _set from "lodash/set";
import _toString from "lodash/toString";
import XLSX from "xlsx";

// eslint-disable-next-line @typescript-eslint/no-var-requires
const { Parser } = require("json2csv");

export interface Header {
  text: string;
  value: string;
  compose?: (row: Row, header?: Header) => string;
}

export type Row = Record<string, unknown>;
type SanitizedRow = {
  [index: string]: SanitizedRow | string;
};

const exportCsv = (
  headers: Header[],
  rows: Row[],
  fileName: string,
  quoted?: boolean
) => {
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
      const config = {
        fields,
        withBOM: true,
        defaultValue: "-",
        ...(quoted !== false
          ? { delimiter: "," }
          : { delimiter: ";", quote: "" }),
      };
      const parser = new Parser(config);
      const sanitizedRows = sanitizeRows(
        rows,
        headers,
        quoted !== false,
        config.delimiter
      );
      const csv = parser.parse(sanitizedRows);
      downloadCsvFile(fileName, csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportXlsx = (header: string[], rows: Row[], fileName: string) => {
  const data = rows.map((row) => {
    const item: Record<string, string> = {};
    header.forEach((col) => {
      const cellValue = row[col];
      const field = typeof cellValue === "string" ? cellValue : "";
      item[col] = sanitizeXlsxField(field);
    });
    return item;
  });
  const wb = XLSX.utils.book_new();
  const ws = XLSX.utils.json_to_sheet(data, { header });
  XLSX.utils.book_append_sheet(wb, ws);
  XLSX.writeFile(wb, `${fileName}.xlsx`);
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
    link.setAttribute("href", encodeURI("data:text/csv;charset=utf-8," + csv));
    link.setAttribute("download", `${fileName}.csv`);
    document.body.appendChild(link); // required for FF
    link.click();
    document.body.removeChild(link);
  }
};

const sanitizeRows = function (
  rows: Row[],
  headers: Header[],
  quoted: boolean,
  delimiter: string
): SanitizedRow[] {
  const rowsDict: Row[] = JSON.parse(JSON.stringify(rows));
  return rowsDict.map((row) => {
    // we start with an empty object, copying over only those properties that are listed in the headers config.
    // we do not use "const" as we change sanitizedRow with lodash
    // eslint-disable-next-line
    let sanitizedRow: SanitizedRow = {}
    headers.forEach((header) => {
      // header.value can be a property name like "firstName" or a path like "raw.address.street". We use lodash _get to get the field (=value).
      // Use the compose function to compute the value if required
      const field = header.compose
        ? header.compose(row)
        : _get(row, header.value);
      // we apply the sanitized field to the row
      _set(
        sanitizedRow,
        header.value,
        sanitizeField(_toString(field), quoted, delimiter)
      );
    });
    return sanitizedRow;
  });
};

const sanitizeLineBreaks = (field: string): string => {
  // Replace newlines and other line breaks with a space
  // Strip whitespace at the beginning and end
  const regex_linebreaks = /\r?\n|\r/g; // Recognizes /r (CR), /n (LF) and /r/n (CRLF)
  return field.replace(regex_linebreaks, " ").trim();
};

const sanitizeWhitelist = (field: string): string => {
  // Remove everything not whitelisted (this restriction may be relaxed at some point)
  const regex_whitelist = /[^a-zA-Z0-9äüöÄÜÖß(): \-@+.;,]+/g; // Matches everything *not* in the group (the whitelist)
  return field.replace(regex_whitelist, "");
};

const sanitizeTrigger = (field: string): string => {
  // Ensure the string has no trigger characters at the beginning or preceded by a delimiter character to mitigate Formula injection
  const regex_trigger = /(^|(?<=[,;]))[=+\-@\t\r\n]+/g;
  return field.replace(regex_trigger, "");
};

const isPhoneNumberLike = (field: string): boolean => {
  const regex_phone =
    /^\+[ ]?[(]?[ ]?[0123456789]{1,3}[ ]?[)]?[0123456789 \-/]+$/g;
  return regex_phone.test(field);
};

const sanitizeXlsxField = (field: string | undefined): string => {
  if (field == null) {
    // Catches both null and undefined
    return "";
  }

  field = sanitizeLineBreaks(field);

  field = sanitizeWhitelist(field);

  if (isPhoneNumberLike(field)) {
    field = ` ${field}`;
  }

  field = sanitizeTrigger(field);

  return field;
};

const sanitizeField = function (
  field: string | undefined,
  quoted: boolean,
  delimiter: string
): string {
  // Some of the steps are unnecessary or may seem overly restrictive.
  // This is intended to provide redundancy in case some sanitization gets broken with future changes. If this leads to issues, some of the restrictions may be relaxed with care.

  if (field == null) {
    // Catches both null and undefined
    return "";
  }

  let quote = false;

  field = sanitizeLineBreaks(field);

  field = sanitizeWhitelist(field);

  if (isPhoneNumberLike(field)) {
    field = ` ${field}`;
    quote = true;
  }

  field = sanitizeTrigger(field);

  /**
   * json2csv uses a delimiter to split table columns. We currently are using ; and , as those.
   * If such a sign would appear in the content than it would result in a split.
   * To prevent this...
   * ... we have to quote the field value if it contains any delimiters - but only if the field isn't quoted by the json2csv Parser.
   * ... OR ...
   * ... we change the offending symbol to "/".
   * please note: we use quotes only if atomic address export is enabled to ensure backwards compatibility
   */

  if (window.irisAppContext?.csvExportStandardAtomicAddress === "true") {
    const regex_delimiter = new RegExp(`${delimiter}+`, "g");
    if (regex_delimiter.test(field)) {
      quote = true;
    }
  } else {
    const regex_separator = /[,;]+/g;
    field = field.replace(regex_separator, "/");
  }

  // quote the field value if required ( quote = true ) - but only if the field isn't quoted by the json2csv Parser.
  if (quote && !quoted) {
    field = `"${field}"`;
  }

  return field;
};

const dataExport = {
  exportCsv,
  exportXlsx,
};

export default dataExport;
