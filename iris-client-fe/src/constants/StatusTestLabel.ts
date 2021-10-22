import { DataRequestStatus } from "@/api";

const getStatusTestLabel = function (status?: DataRequestStatus): string {
  switch (status) {
    case DataRequestStatus.DataRequested:
      return "requested";
    case DataRequestStatus.DataReceived:
      return "received";
    case DataRequestStatus.Closed:
      return "closed";
    case DataRequestStatus.Aborted:
      return "aborted";
    default:
      return "unknown"; // TODO find better name
  }
};

const StatusTestLabel = {
  getStatusTestLabel,
};

export default StatusTestLabel;
