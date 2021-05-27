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

const exportStandardCsv = function (
  headers: Array<Header>,
  rows: Array<Array<string>>,
  fileName: string
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
      const separator = ";";
      const parser = new Parser({
        fields,
        withBOM: true,
        delimiter: separator,
        defaultValue: "-",
      });
      const csv = parser.parse(sanitiseRows(rows, separator));
      downloadCsvFile(fileName, csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportAlternativeStandardCsv = function (
  headers: Array<Header>,
  rows: Array<Array<string>>,
  fileName: string
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
      const separator = ";";
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

const exportSormasEventParticipantsCsv = function (
  rows: Array<EventParticipantData>,
  fileName: string
): Promise<string> {
  const headers = [
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
      const separator = ";";
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
        delimiter: separator,
        quote: "",
      });
      const csv = parser.parse(sanitiseRows(rows, separator));
      downloadCsvFile(fileName, csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportSormasContactPersonCsv = function (
  rows: Array<ContactCaseData>,
  fileName: string
): Promise<string> {
  const headers = [
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
      const separator = ";";
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
        delimiter: separator,
        quote: "",
      });
      const csv = parser.parse(sanitiseRows(rows, separator));
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

const sanitiseRows = function (rows: unknown, separator: string): unknown {
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

  if (typeof field !== "undefined") {
    field = field.replace(possibleSeperatorRE, "/");
    field = field.replace(whitespaceRE, " ");
    const matches = field.match(whitelistRE);
    field = matches?.join("") || "";

    if (separator != "") {
      let separator_replacement = "/";
      if (separator === "/") separator_replacement = ".";
      while (field.includes(separator))
        field = field.replace(separator, separator_replacement);
    }
    while (!headWhitelistRE.test(field) && field.length > 0) {
      field = field.substring(1);
    }
    return field;
  } else return "";
};

const dataExport = {
  exportStandardCsv,
  exportAlternativeStandardCsv,
  exportSormasEventParticipantsCsv,
  exportSormasContactPersonCsv,
};

export default dataExport;
