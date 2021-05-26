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
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
      });
      const csv = parser.parse(rows);
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
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
        delimiter: ";",
        quote: "",
      });
      const csv = parser.parse(rows);
      downloadCsvFile(fileName, csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportSormasEventParticipantsCsv = function (
  headers: Array<Header>,
  rows: Array<EventParticipantData>,
  fileName: string
): Promise<string> {
  headers = [
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
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
        delimiter: ";",
        quote: "",
      });
      const csv = parser.parse(rows);
      downloadCsvFile(fileName, csv);
      resolve(csv);
    } catch (error) {
      reject(error);
    }
  });
};

const exportSormasContactPersonCsv = function (
  headers: Array<Header>,
  rows: Array<ContactCaseData>,
  fileName: string
): Promise<string> {
  headers = [
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
      const parser = new Parser({
        fields,
        withBOM: true,
        defaultValue: "-",
        delimiter: ";",
        quote: "",
      });
      const csv = parser.parse(rows);
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

const dataExport = {
  exportStandardCsv,
  exportAlternativeStandardCsv,
  exportSormasEventParticipantsCsv,
  exportSormasContactPersonCsv,
};

export default dataExport;
