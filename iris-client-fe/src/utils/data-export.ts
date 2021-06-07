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
    const fields = headers.filter((v) => v);
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
    const fields = headers.filter((v) => v);
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
      row[prop] = sanitiseField(row[prop].toString(), separator);
    }
    return row;
  });
  return rowsDict;
};

export const sanitiseField = function (
  field: string | undefined,
  separator = ""
): string {
  const possibleSeperatorRE = /[,;]/g;
  const whitespaceRE = RegExp(/\s+/, "g");
  const whitelistRE = /([\p{L}\p{N}]@[\p{L}\p{N}])|[\p{L}\p{N}()[\]:./ -]/gu;
  const headWhitelistRE = /^([()[\]]*[\p{L}\p{N}])+/u;

  if (!field) {
    return field || "-";
  }

  field = field.replace(possibleSeperatorRE, "/");
  field = field.replace(whitespaceRE, " ");
  const matches = field.match(whitelistRE);
  field = matches?.join("") || "";

  /**
   * json2csv uses a seperator to split table columns. We currently are using ; and , as those.
   * If such a sign would appear in the content than it would result in a split.
   * To prevent this we change the seperators symbol with another symbol not used as such.
   */
  if (separator != "") {
    let separator_replacement = "/";
    if (separator === "/") separator_replacement = ".";
    while (field.includes(separator))
      field = field.replace(separator, separator_replacement);
  }
  while (!headWhitelistRE.test(field) && field.length > 0) {
    field = field.substring(1);
  }

  if (field.length == 0) {
    return "-";
  }
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
