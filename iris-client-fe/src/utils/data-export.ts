import { sanitiseField } from "./sanitisation";

// eslint-disable-next-line @typescript-eslint/no-var-requires
const { Parser } = require("json2csv");

export interface Header {
  text: string;
  value: string;
}

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

const headerStandardForIndexTrackingContacts = [
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
  {
    text: "Adresse",
    value: "address",
  },
  {
    text: "Arbeitsplatz",
    value: "workPlace",
  },
  {
    text: "Kontaktsituation",
    value: "basicConditions",
  },
];

const headerStandardForIndexTrackingEvents = [
  {
    text: "Event",
    value: "name",
  },
  {
    text: "Telefonnummer",
    value: "phone",
  },
  {
    text: "Adresse",
    value: "address",
  },
  {
    text: "zus. Informationen",
    value: "additionalInformation",
  },
];

const headerStandardForEventTracking = [
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
  {
    text: "Adresse",
    value: "address",
  },
];

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
  rows: Array<Array<string>>,
  fileName: string
): void {
  const header = headerStandardForEventTracking;
  exportCsvWithoutQuote(header, rows, fileName, ",");
};

const exportStandardCsvForIndexTrackingContacts = function (
  rows: Array<Array<string>>,
  fileName: string
): void {
  const header = headerStandardForIndexTrackingContacts;
  exportCsvWithoutQuote(header, rows, fileName, ",");
};

const exportStandardCsvForIndexTrackingEvents = function (
  rows: Array<Array<string>>,
  fileName: string
): void {
  const header = headerStandardForIndexTrackingEvents;
  exportCsvWithoutQuote(header, rows, fileName, ",");
};

const exportAlternativeStandardCsvForEventTracking = function (
  rows: Array<Array<string>>,
  fileName: string
): void {
  const header = headerStandardForEventTracking;
  exportCsvWithQuote(header, rows, fileName, ";");
};

const exportSormasEventParticipantsCsv = function (
  rows: Array<EventParticipantData>,
  fileName: string
): void {
  const headers = headerSormasEventParticipants;
  exportCsvWithQuote(
    headers,
    (rows as unknown) as Array<Array<string>>,
    fileName,
    ";"
  );
};

const exportSormasContactPersonCsv = function (
  rows: Array<ContactCaseData>,
  fileName: string
): void {
  const headers = headerSormasContactPerson;
  exportCsvWithQuote(
    headers,
    (rows as unknown) as Array<Array<string>>,
    fileName,
    ";"
  );
};

const exportCsvWithQuote = function (
  headers: Array<Header>,
  rows: Array<Array<string>>,
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
      const test = sanitiseRows(rows, separator);
      const csv = parser.parse(test);
      downloadCsvFile(fileName, csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportCsvWithoutQuote = function (
  headers: Array<Header>,
  rows: Array<Array<string>>,
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
      const test = sanitiseRows(rows, separator);
      const csv = parser.parse(test);
      downloadCsvFile(fileName, csv);
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

const sanitiseRows = function (
  rows: Array<Array<string>>,
  separator: string
): unknown {
  let rowsDict: Array<Record<string, string>>;
  rowsDict = JSON.parse(JSON.stringify(rows));
  rowsDict = rowsDict.map((row) => {
    for (const prop in row) {
      row[prop] = sanitizeField(row[prop].toString(), separator);
    }
    return row;
  });
  return rowsDict;
};

export const sanitizeField = function (
  field: string | undefined,
  separator: string
): string {
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
