import { CheckinApp, CheckinAppStatus, CheckinAppStatusInfo } from "@/api";

export const dummyCheckinApps: CheckinApp[] = [
  {
    name: "checkin.app.test",
    groups: ["checkin-app"],
  },
  {
    name: "test.checkin.app",
    groups: ["checkin-app"],
  },
  {
    name: "App.Checkin.test",
    groups: ["checkin-app"],
  },
  {
    name: "App.Checkin.empty-group",
    groups: [],
  },
  {
    name: "Checkin.no-group",
  },
];

export const dummyCheckinAppStatus: Record<string, CheckinAppStatusInfo> = {
  ["checkin.app.test"]: {
    info: {
      version: "1.0.0",
      serverInfo: {
        name: "server.test",
      },
    },
    status: CheckinAppStatus.OK,
    message: "Die Status Prüfung für diesen App Anbieter war erfolgreich.",
  },
  ["test.checkin.app"]: {
    status: CheckinAppStatus.WARNING,
    message: "Die Status Anfrage für diesen App Anbieter wurde abgelehnt.",
  },
  ["App.Checkin.test"]: {
    status: CheckinAppStatus.ERROR,
    message: "Bei der Prüfung des Status ist ein Fehler aufgetreten.",
  },
  ["App.Checkin.empty-group"]: {
    status: CheckinAppStatus.OK,
    message: "Die Status Prüfung für diesen App Anbieter war erfolgreich.",
  },
  ["Checkin.no-group"]: {
    status: CheckinAppStatus.ERROR,
    message:
      "Die Status Anfrage für diesen App Anbieter ist derzeit nicht möglich.",
  },
};

export const getDummyCheckinAppStatus = (
  appName: string
): CheckinAppStatusInfo | undefined => {
  return dummyCheckinAppStatus[appName];
};
