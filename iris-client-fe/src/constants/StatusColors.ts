import { DataRequestStatus } from "@/api";

const getColor = function (status?: DataRequestStatus): string {
  switch (status) {
    case DataRequestStatus.DataRequested:
      return "blue";
    case DataRequestStatus.DataReceived:
      return "red";
    case DataRequestStatus.Closed:
      return "green";
    case DataRequestStatus.Aborted:
      return "black";
    default:
      return "gray"; // TODO
  }
};

const StatusColors = {
  getColor,
};

export default StatusColors;
