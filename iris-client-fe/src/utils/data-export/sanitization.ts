import _toString from "lodash/toString";
import { Header, Row } from "@/utils/data-export/data-export";
import _isFunction from "lodash/isFunction";
import _get from "lodash/get";

const sanitizeLineBreaks = (field: string): string => {
  // Replace newlines and other line breaks with a space
  // Strip whitespace at the beginning and end
  const regex_linebreaks = / *(\r?\n|\r) */g; // Recognizes /r (CR), /n (LF) and /r/n (CRLF) including surrounding spaces
  return field.replace(regex_linebreaks, " ").trim();
};

const sanitizeWhitelist = (field: string): string => {
  // Remove everything not whitelisted (this restriction may be relaxed at some point)
  const regex_whitelist = /[^a-zA-Z0-9äüöÄÜÖß(): \-@+.;,]+/g; // Matches everything *not* in the group (the whitelist)
  return field.replace(regex_whitelist, "");
};

const sanitizeTrigger = (field: string): string => {
  // Ensure the string has no trigger characters at the beginning or preceded by a delimiter character to mitigate Formula injection
  if (isPhoneNumberLike(field)) {
    return ` ${field}`;
  } else {
    const regex_trigger_beginning = /^[=+\-@\t\r\n ]+/g;
    const regex_trigger_delimiter = /(?<=[,;])[=+\-@\t\r\n]+/g;
    return field
      .replace(regex_trigger_beginning, "")
      .replace(regex_trigger_delimiter, "");
  }
};

const isPhoneNumberLike = (field: string): boolean => {
  const regex_phone =
    /^\+[ ]?[(]?[ ]?[0123456789]{1,10}[ ]?[)]?[0123456789 \-/]+$/g;
  return regex_phone.test(field);
};

const sanitizeField = (
  field: string | undefined,
  replaceDelimiters?: boolean
): string => {
  // Some of the steps are unnecessary or may seem overly restrictive.
  // This is intended to provide redundancy in case some sanitization gets broken with future changes. If this leads to issues, some of the restrictions may be relaxed with care.

  if (field == null) {
    // Catches both null and undefined
    return "";
  }

  field = sanitizeLineBreaks(field);
  field = sanitizeWhitelist(field);
  field = sanitizeTrigger(field);

  /**
   * csv files use delimiters (, or ;) to split table columns.
   * If a delimiter is part of a field value it would result in a split.
   * To prevent this...
   * ... we have to quote the field value if it contains any delimiters - this is handled automatically be sheetjs
   * ... OR ...
   * ... we change the offending symbol to "/".
   * please note: to ensure backwards compatibility, delimiters are replaced by default.
   */

  if (replaceDelimiters !== false) {
    const regex_separator = /[,;]+/g;
    field = field.replace(regex_separator, "/");
  }

  return field;
};

// header can be either of type Header or string:
// string:
// - it is used as key to pick the object value (lodash _get isn't used because the string could contain path separators like ".")
// Header:
// - if it has a compose function: use it to compute the value
// - else if it has a value: It can be used as a property name like "firstName" or a path like "raw.address.street". We use lodash _get to get the field (=value).
// - else: Use the text as key to pick the object value (lodash _get isn't used because the string could contain path separators like ".")
const getFieldValue = (row: Row, header: Header): unknown => {
  if (typeof header === "string") {
    return row[header];
  }
  if (header.compose && _isFunction(header.compose)) {
    return header.compose(row);
  }
  return header.value ? _get(row, header.value) : row[header.text];
};

const sanitizeRows = (
  rows: Row[],
  headers: Header[] | string[],
  replaceDelimiters?: boolean
): string[][] => {
  const rowsDict: Row[] = JSON.parse(JSON.stringify(rows));
  return rowsDict.map((row) => {
    const sRow: string[] = [];
    headers.forEach((header) => {
      const field = getFieldValue(row, header);
      sRow.push(sanitizeField(_toString(field), replaceDelimiters));
    });
    return sRow;
  });
};

const sanitization = {
  sanitizeField,
  sanitizeRows,
};

export default sanitization;
