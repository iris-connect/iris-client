import {
  DataRequestCaseDetailsStatusEnum,
  ExistingDataRequestClientWithLocationStatusEnum,
} from "@/api";

const getMessage = function (status: string): string {
  switch (status) {
    case DataRequestCaseDetailsStatusEnum.DataRequested:
    case ExistingDataRequestClientWithLocationStatusEnum.DataRequested:
      return "Angefragt";
    case DataRequestCaseDetailsStatusEnum.DataReceived:
    case ExistingDataRequestClientWithLocationStatusEnum.DataReceived:
      return "Geliefert";
    case DataRequestCaseDetailsStatusEnum.Closed:
    case ExistingDataRequestClientWithLocationStatusEnum.Closed:
      return "Abgeschlossen";
    default:
      return "Unbekannt"; // TODO find better name
  }
};

const StatusMessages = {
  getMessage,
};

export default StatusMessages;
