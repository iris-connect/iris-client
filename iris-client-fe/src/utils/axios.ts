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

const parseErrorMessage = (error: unknown): ErrorMessage => {
  if (typeof error === "object") {
    const e = error as Record<string, unknown>;
    return Object.keys(e)
      .map((key) => parseErrorMessage(e[key]))
      .filter((v) => v)
      .join(", ");
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

  if (error.response) {
    const errData = error.response.data;
    return (
      errData.message + " (" + errData.error + " [" + errData.status + "])"
    );
  }

  const parsedError = parseError(error);
  return parseErrorMessage(parsedError.data) || fallback;
};
