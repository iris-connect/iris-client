import axios, { AxiosError, AxiosResponse } from "axios";
import dayjs from "@/utils/date";

interface ParsedError {
  data: unknown;
  status: number;
}

export type ErrorMessage = string | null;

export const parseError = (error: AxiosError): ParsedError => {
  if (error.isAxiosError) {
    if (error.response) {
      const response = error.response as AxiosResponse;
      return {
        data: response.data,
        status: response.status,
      };
    }
    if (error.request) {
      return {
        data: error.request?.statusText,
        status: error.request?.status,
      };
    }
  }
  return {
    data: error.message,
    status: -1,
  };
};

const userBlockedRegExp = /User blocked! \((.*)\)/i;

const parseErrorMessage = (error: unknown): ErrorMessage => {
  if (typeof error === "object") {
    const e = error as Record<string, unknown>;
    return Object.keys(e)
      .map((key) => parseErrorMessage(e[key]))
      .filter((v) => v)
      .join(", ");
  }
  if (typeof error === "string") {
    if (userBlockedRegExp.test(error)) {
      const dtString = error.match(userBlockedRegExp)?.[1];
      if (dtString) {
        const diffInMinutes = Math.abs(dayjs().diff(dtString, "minutes"));
        return `Die Anmeldung mit diesem Anmeldenamen wurde aufgrund zu vieler Fehlversuche für die nächsten ${diffInMinutes} Minuten gesperrt.`;
      }
    }
    return error;
  }
  return null;
};

export const getErrorMessage = (
  error: AxiosError | string,
  fallback = "Fehler"
): ErrorMessage => {
  if (!error || axios.isCancel(error)) return "";
  if (typeof error === "string") return error;
  const parsedError = parseError(error);
  const message = parseErrorMessage(parsedError.data) || fallback;
  return `${message} [${parsedError.status}]`;
};
