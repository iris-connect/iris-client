import _get from "lodash/get";
import _set from "lodash/set";
import _toString from "lodash/toString";

// eslint-disable-next-line @typescript-eslint/no-var-requires
const { Parser } = require("json2csv");

export interface Header {
  text: string;
  value: string;
}

export type Row = Record<string, unknown>;
type SanitizedRow = {
  [index: string]: SanitizedRow | string;
};

export type TableRow = {
  lastName: string;
  firstName: string;
  checkInTime: string;
  checkOutTime: string;
  maxDuration: string;
  comment: string;
  sex: string;
  email: string;
  phone: string;
  mobilePhone: string;
  address: string;
};

export type ContactCaseData = {
  description: string;
  firstName: string;
  lastName: string;
  sex: string;
  phone: string;
  email: string;
  postalCode: string;
  city: string;
  street: string;
  houseNumber: string;
};

export type EventParticipantData = {
  involvementDescription: string;
  firstName: string;
  lastName: string;
  sex: string;
  phone: string;
  email: string;
  postalCode: string;
  city: string;
  street: string;
  houseNumber: string;
};

const getAddressHeaders = () => {
  if (window.irisAppContext?.csvExportStandardAtomicAddress === "true") {
    return [
      {
        text: "Straße",
        value: "raw.address.street",
      },
      {
        text: "Hausnummer",
        value: "raw.address.houseNumber",
      },
      {
        text: "Postleitzahl",
        value: "raw.address.zipCode",
      },
      {
        text: "Ort",
        value: "raw.address.city",
      },
    ];
  }
  return [
    {
      text: "Adresse",
      value: "address",
    },
  ];
};

const getHeadersStandardForIndexTrackingContacts = () => {
  return [
    {
      text: "Nachname",
      value: "lastName",
      align: "start",
    },
    {
      text: "Vorname",
      value: "firstName",
    },
    {
      text: "Geburtsdatum",
      value: "dateOfBirth",
    },
    {
      text: "Erster Kontakt am",
      value: "firstContactDate",
    },
    {
      text: "Letzter Kontakt am",
      value: "lastContactDate",
    },
    {
      text: "Kontaktkathegorie",
      value: "contactCategory",
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
    {
      text: "Arbeitsplatz",
      value: "workPlace",
    },
    {
      text: "Kontaktsituation",
      value: "basicConditions",
    },
  ];
};

const getHeadersStandardForIndexTrackingEvents = () => {
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

const getHeaderStandardForEventTracking = () => {
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

const headerSormasContactPerson = [
  {
    text: "description",
    value: "description",
  },
  {
    text: "person.firstName",
    value: "firstName",
  },
  {
    text: "person.lastName",
    value: "lastName",
  },
  {
    text: "person.sex",
    value: "sex",
  },
  {
    text: "person.phone",
    value: "phone",
  },
  {
    text: "person.emailAddress",
    value: "email",
  },
  {
    text: "person.address.postalCode",
    value: "postalCode",
  },
  {
    text: "person.address.city",
    value: "city",
  },
  {
    text: "person.address.street",
    value: "street",
  },
  {
    text: "person.address.houseNumber",
    value: "houseNumber",
  },
];

const headerSormasEventParticipants = [
  {
    text: "EventParticipant",
    value: "involvementDescription",
  },
  {
    text: "Person",
    value: "firstName",
  },
  {
    text: "Person",
    value: "lastName",
  },
  {
    text: "Person",
    value: "sex",
  },
  {
    text: "Person",
    value: "phone",
  },
  {
    text: "Person",
    value: "email",
  },
  {
    text: "Person",
    value: "postalCode",
  },
  {
    text: "Person",
    value: "city",
  },
  {
    text: "Person",
    value: "street",
  },
  {
    text: "Person",
    value: "houseNumber",
  },
];

const exportStandardCsvForEventTracking = function (
  rows: Row[],
  fileName: string
): void {
  const headers = getHeaderStandardForEventTracking();
  exportCsvWithoutQuote(headers, rows, fileName, ",");
};

const exportStandardCsvForIndexTrackingContacts = function (
  rows: Row[],
  fileName: string
): void {
  const headers = getHeadersStandardForIndexTrackingContacts();
  exportCsvWithoutQuote(headers, rows, fileName, ",");
};

const exportStandardCsvForIndexTrackingEvents = function (
  rows: Row[],
  fileName: string
): void {
  const headers = getHeadersStandardForIndexTrackingEvents();
  exportCsvWithoutQuote(headers, rows, fileName, ",");
};

const exportAlternativeStandardCsvForEventTracking = function (
  rows: Row[],
  fileName: string
): void {
  const headers = getHeaderStandardForEventTracking();
  exportCsvWithQuote(headers, rows, fileName, ";");
};

const exportSormasEventParticipantsCsv = function (
  rows: Array<EventParticipantData>,
  fileName: string
): void {
  exportCsvWithQuote(headerSormasEventParticipants, rows, fileName, ";");
};

const exportSormasContactPersonCsv = function (
  rows: Array<ContactCaseData>,
  fileName: string
): void {
  exportCsvWithQuote(headerSormasContactPerson, rows, fileName, ";");
};

const exportCsvWithQuote = function (
  headers: Header[],
  rows: Row[],
  fileName: string,
  separator: string
): void {
  new Promise((resolve, reject) => {
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
        delimiter: separator,
        quote: "",
      });
      const sanitizedRows = sanitizeRows(rows, headers);
      const csv = parser.parse(sanitizedRows);
      downloadCsvFile(fileName, csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportCsvWithoutQuote = function (
  headers: Array<Header>,
  rows: Row[],
  fileName: string,
  separator: string
): void {
  new Promise((resolve, reject) => {
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
        delimiter: separator,
      });
      const sanitizedRows = sanitizeRows(rows, headers);
      const csv = parser.parse(sanitizedRows);
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
    link.setAttribute("href", encodeURI("data:text/csv;charset=utf-8," + csv));
    link.setAttribute("download", `${fileName}.csv`);
    document.body.appendChild(link); // required for FF
    link.click();
    document.body.removeChild(link);
  }
};

const sanitizeRows = function (rows: Row[], headers: Header[]): SanitizedRow[] {
  const rowsDict: Row[] = JSON.parse(JSON.stringify(rows));
  return rowsDict.map((row) => {
    // we start with an empty object, copying over only those properties that are listed in the headers config.
    // we do not use "const" as we change sanitizedRow with lodash
    // eslint-disable-next-line
    let sanitizedRow: SanitizedRow = {}
    headers.forEach((header) => {
      // header.value can be a property name like "firstName" or a path like "raw.address.street". We use lodash _get to get the field (=value).
      const field = _get(row, header.value);
      // we apply the sanitized field to the row
      _set(sanitizedRow, header.value, sanitizeField(_toString(field)));
    });
    return sanitizedRow;
  });
};

export const sanitizeField = function (field: string | undefined): string {
  // Some of the steps are unnecessary or may seem overly restrictive.
  // This is intended to provide redundancy in case some sanitization gets broken with future changes. If this leads to issues, some of the restrictions may be relaxed with care.

  if (field == null) {
    // Catches both null and undefined
    return "";
  }

  // Replace newlines and other line breaks with a space
  const regex_linebreaks = /\r?\n|\r/g; // Recognizes /r (CR), /n (LF) and /r/n (CRLF)
  field = field.replace(regex_linebreaks, " ");

  // Strip whitespace at the beginning and end
  field = field.trim();

  // Remove everything not whitelisted (this restriction may be relaxed at some point)
  const regex_whitelist = /[^a-zA-Z0-9äüöÄÜÖß(): \-@+.;,]+/g; // Matches everything *not* in the group (the whitelist)
  field = field.replace(regex_whitelist, "");

  // Ensure beginning of string has no trigger characters (+,- and @ are allowed, but they should not start the string)
  const regex_beginning = /^[=+\-@\t\r \n]+/g;
  field = field.replace(regex_beginning, "");

  /**
   * json2csv uses a seperator to split table columns. We currently are using ; and , as those.
   * If such a sign would appear in the content than it would result in a split.
   * To prevent this we change the offending symbol to "/". We can't quote the whole string in every desired output format.
   */

  const regex_separator = /[,;]+/g;
  field = field.replace(regex_separator, "/");

  return field;
};

const dataExport = {
  exportStandardCsvForIndexTrackingContacts,
  exportStandardCsvForIndexTrackingEvents,
  exportStandardCsvForEventTracking,
  exportAlternativeStandardCsvForEventTracking,
  exportSormasEventParticipantsCsv,
  exportSormasContactPersonCsv,
};

export default dataExport;
