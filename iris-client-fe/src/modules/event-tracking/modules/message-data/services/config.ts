import { DataRequestStatus } from "@/api";

export const exportableStatus = [
  DataRequestStatus.DataReceived,
  DataRequestStatus.Closed,
];

export const importableStatus = [
  DataRequestStatus.DataRequested,
  DataRequestStatus.DataReceived,
];
