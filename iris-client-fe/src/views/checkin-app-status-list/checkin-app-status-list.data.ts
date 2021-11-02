import {
  CheckinApp,
  CheckinAppInfo,
  CheckinAppServerInfo,
  CheckinAppStatus,
  CheckinAppStatusInfo,
} from "@/api";
import { Complete, normalizeData } from "@/utils/data";

const normalizeCheckinAppServerInfo = (
  source?: CheckinAppServerInfo,
  parse?: boolean
): CheckinAppServerInfo => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<CheckinAppServerInfo> = {
        name: normalizer("name", undefined),
      };
      return normalized;
    },
    parse,
    "CheckinAppServerInfo"
  );
};

const normalizeCheckinAppInfo = (
  source?: CheckinAppInfo,
  parse?: boolean
): CheckinAppInfo => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<CheckinAppInfo> = {
        version: normalizer("version", undefined),
        serverInfo: source?.serverInfo
          ? normalizeCheckinAppServerInfo(source?.serverInfo)
          : source?.serverInfo,
      };
      return normalized;
    },
    parse,
    "CheckinAppInfo"
  );
};

export const normalizeCheckinAppStatusInfo = (
  source?: CheckinAppStatusInfo,
  parse?: boolean
): CheckinAppStatusInfo => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<CheckinAppStatusInfo> = {
        info: source?.info
          ? normalizeCheckinAppInfo(source?.info)
          : source?.info,
        status: normalizer("status", CheckinAppStatus.UNKNOWN),
        message: normalizer("message", ""),
      };
      return normalized;
    },
    parse,
    "CheckinAppStatusInfo"
  );
};

const normalizeCheckinApp = (
  source?: CheckinApp,
  parse?: boolean
): CheckinApp => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<CheckinApp> = {
        name: normalizer("name", ""),
        groups: normalizer("groups", undefined, "array"),
      };
      return normalized;
    },
    parse,
    "CheckinApp"
  );
};

export const normalizeCheckinAppList = (
  source?: CheckinApp[],
  parse?: boolean
): CheckinApp[] => {
  return normalizeData(
    source,
    () => {
      return (source || []).map((item) => normalizeCheckinApp(item));
    },
    parse,
    "CheckinAppList"
  );
};
