import axios, { AxiosError, AxiosResponse } from "axios";

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

const parseErrorMessage = (error: unknown, keys: string[]): ErrorMessage => {
  if (typeof error === "object") {
    const e = error as Record<string, string>;
    const errorKey = keys.find((k) => {
      return Object.prototype.hasOwnProperty.call(e, k);
    });
    if (errorKey) return e[errorKey];
  }
  if (typeof error === "string") return error;
  return null;
};

export const getErrorMessage = (
  error: AxiosError | string,
  fallback = "Fehler"
): ErrorMessage => {
  if (!error || axios.isCancel(error)) return "";
  if (typeof error === "string") return error;
  const parsedError = parseError(error);
  const message =
    parseErrorMessage(parsedError.data, ["message", "error"]) || fallback;
  return `${message} [${parsedError.status}]`;
};
