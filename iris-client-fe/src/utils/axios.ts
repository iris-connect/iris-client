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
    const e = error as Record<string, unknown>;
    const message = keys
      .map((k) => e[k])
      .filter((v) => typeof v === "string" && v.length > 0);
    if (message.length > 0) {
      return message.join(", ");
    }
    // return Object.keys(e)
    //   .map((key) => {
    //     return parseErrorMessage(e[key], keys);
    //   })
    //   .filter((v) => !_isNil(v))
    //   .join(", ");
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
