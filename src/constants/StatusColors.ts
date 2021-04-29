import {
  DataRequestCaseDetailsStatusEnum,
  ExistingDataRequestClientWithLocationStatusEnum,
} from "@/api";

const getColor = function (status: string): string {
  switch (status) {
    case DataRequestCaseDetailsStatusEnum.DataRequested:
    case ExistingDataRequestClientWithLocationStatusEnum.DataRequested:
      return "blue";
    case DataRequestCaseDetailsStatusEnum.DataReceived:
    case ExistingDataRequestClientWithLocationStatusEnum.DataReceived:
      return "red";
    case DataRequestCaseDetailsStatusEnum.Closed:
    case ExistingDataRequestClientWithLocationStatusEnum.Closed:
      return "green";
    default:
      return "gray"; // TODO
  }
};

const StatusColors = {
  getColor,
};

export default StatusColors;
