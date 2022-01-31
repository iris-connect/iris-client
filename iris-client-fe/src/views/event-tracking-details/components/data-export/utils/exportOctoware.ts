import Genders from "@/constants/Genders";
import { TableRow } from "@/views/event-tracking-details/utils/mappedData";
import { DataRequestDetails } from "@/api";
import { getFormattedDate } from "@/utils/date";
import { getValidPhoneNumber } from "@/utils/misc";
import dataExport, { Row } from "@/utils/data-export/data-export";

export type OctowareData = {
  ID: string;
  ART_ERFASSUNG: string;
  TN_R_UMGEBUNGART: string;
  UMGEBUNG: string;
  BEMERKUNG: string;
  DAT_MELDE: string;
  TN_R_KONTAKT_KAT: string;
  TN_R_KONT_INDXPERS: string;
  NAME: string;
  VORNAME: string;
  GESCHLECHT: string;
  DAT_GEBURT: string;
  GEB_ORT: string;
  GebLand_ISO_KENNZ: string;
  DAT_ERST_KONTAKT: string;
  DAT_LETZT_KONTAKT: string;
  Wohn_STRASSE: string;
  Wohn_HAUSNR_OD_PF_NR: string;
  Wohn_PLZ: string;
  Wohn_ORT: string;
  Wohn_Staat_ISO_KENNZ: string;
  Telefon: string;
  "E-Mail": string;
  REISERUECKKEHRER: string;
  TN_R_SYS_REL_BERUF: string;
};

const getHeaders = (): Array<keyof OctowareData> => {
  return [
    "ID",
    "ART_ERFASSUNG",
    "TN_R_UMGEBUNGART",
    "UMGEBUNG",
    "BEMERKUNG",
    "DAT_MELDE",
    "TN_R_KONTAKT_KAT",
    "TN_R_KONT_INDXPERS",
    "NAME",
    "VORNAME",
    "GESCHLECHT",
    "DAT_GEBURT",
    "GEB_ORT",
    "GebLand_ISO_KENNZ",
    "DAT_ERST_KONTAKT",
    "DAT_LETZT_KONTAKT",
    "Wohn_STRASSE",
    "Wohn_HAUSNR_OD_PF_NR",
    "Wohn_PLZ",
    "Wohn_ORT",
    "Wohn_Staat_ISO_KENNZ",
    "Telefon",
    "E-Mail",
    "REISERUECKKEHRER",
    "TN_R_SYS_REL_BERUF",
  ];
};

const mapData = (
  event: DataRequestDetails | null,
  tableRows: TableRow[]
): OctowareData[] => {
  const data: OctowareData[] = [];
  const location = event?.locationInformation;
  const locationName = location?.name || location?.contact?.officialName || "";
  const locationAddress = location?.contact?.address;
  const locationInfo = [
    locationAddress?.street ?? "",
    [locationAddress?.zip ?? "", locationAddress?.city ?? ""].join(" ").trim(),
  ]
    .filter((v) => v)
    .join(", ");
  tableRows.forEach((row) => {
    const guest = row.raw;
    const guestAddress = guest.address;
    data.push({
      ID: event?.externalRequestId || "",
      ART_ERFASSUNG: "IRIS connect",
      TN_R_UMGEBUNGART: "",
      UMGEBUNG: locationName.substr(0, 100),
      BEMERKUNG: locationInfo.substr(0, 248),
      DAT_MELDE: getFormattedDate(event?.requestedAt, "DD.MM.YYYY", ""),
      TN_R_KONTAKT_KAT: "",
      TN_R_KONT_INDXPERS: "",
      NAME: guest.lastName || "",
      VORNAME: guest.firstName || "",
      // the Genders mapper can be used here, because Octoware is using identical expressions.
      GESCHLECHT: guest.sex ? Genders.getName(guest.sex) : "",
      DAT_GEBURT: getFormattedDate(guest.dateOfBirth, "DD.MM.YYYY", ""),
      GEB_ORT: "",
      GebLand_ISO_KENNZ: "",
      DAT_ERST_KONTAKT: getFormattedDate(
        row.checkInTime,
        "DD.MM.YYYY HH:mm",
        ""
      ),
      DAT_LETZT_KONTAKT: getFormattedDate(
        row.checkOutTime,
        "DD.MM.YYYY HH:mm",
        ""
      ),
      Wohn_STRASSE: guestAddress?.street || "",
      Wohn_HAUSNR_OD_PF_NR: guestAddress?.houseNumber || "",
      Wohn_PLZ: guestAddress?.zipCode || "",
      Wohn_ORT: guestAddress?.city || "",
      Wohn_Staat_ISO_KENNZ: "",
      Telefon: getValidPhoneNumber(guest.phone, guest.mobilePhone) || "",
      "E-Mail": guest.email || "",
      REISERUECKKEHRER: "",
      TN_R_SYS_REL_BERUF: "",
    });
  });
  return data;
};

const exportXlsx = (rows: Row[], fileName: string) => {
  dataExport.exportXlsx(getHeaders(), rows, { fileName });
};

const exportOctoware = {
  mapData,
  exportXlsx,
};

export default exportOctoware;
