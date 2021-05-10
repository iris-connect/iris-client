import { DataRequestStatus } from "@/api";

const getMessage = function (status?: DataRequestStatus): string {
  switch (status) {
    case DataRequestStatus.DataRequested:
      return "Angefragt";
    case DataRequestStatus.DataReceived:
      return "Geliefert";
    case DataRequestStatus.Closed:
      return "Bearbeitet";
    case DataRequestStatus.Aborted:
      return "Abgebrochen";
    default:
      return "Unbekannt"; // TODO find better name
  }
};

const StatusMessages = {
  getMessage,
};

export default StatusMessages;
