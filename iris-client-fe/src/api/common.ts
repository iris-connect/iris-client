/* eslint-disable @typescript-eslint/no-explicit-any */

import { DataRequestStatus } from "@/api/api";
import globalAxios, {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  Method,
} from "axios";
/**
 *
 * @export
 */
export type DataQuery = {
  size: number;
  page: number;
  sort?: string | null;
  status?: DataRequestStatus | null;
  search?: string | null;
  sortOrderDesc?: boolean;
};

export type RequestQuery = {
  [index: string]: number | string | string[] | null | boolean;
};

export interface RequestOptions<D = any> extends AxiosRequestConfig<D> {
  query?: RequestQuery;
  formData?: boolean;
}

export type ApiResponse<T = any> = Promise<AxiosResponse<T>>;

export type ApiRequestFunction = <T, D>(
  method: Method,
  path: string,
  data?: D,
  options?: RequestOptions<D>
) => ApiResponse<T>;

/**
 *
 * @export
 * @class RequiredError
 * @extends {Error}
 */
export class RequiredError extends Error {
  name: "RequiredError" = "RequiredError";
  constructor(public field: string, msg?: string) {
    super(msg);
  }
}

/**
 * @throws {RequiredError}
 * @export
 */
export const assertParamExists = function (
  functionName: string,
  paramName: string,
  paramValue: unknown
): void {
  if (paramValue === null || paramValue === undefined) {
    throw new RequiredError(
      paramName,
      `Required parameter ${paramName} was null or undefined when calling ${functionName}.`
    );
  }
};

const createSearchParams = function (url: URL, ...objects: RequestQuery[]) {
  const searchParams = new URLSearchParams(url.search);
  for (const object of objects) {
    for (const key in object) {
      const value = object[key];
      if (Array.isArray(value)) {
        searchParams.delete(key);
        for (const item of value) {
          searchParams.append(key, item);
        }
      } else if (value) {
        searchParams.set(key, String(value));
      }
    }
  }
  return searchParams.toString();
};

const toPathString = function (url: URL): string {
  return url.pathname + url.search + url.hash;
};

/**
 *
 * @export
 */
export const getSortAttribute = function (key: string): string {
  const sortAttributes: { [key: string]: string } = {
    extID: "refId",
    name: "name",
    startTime: "requestStart",
    endTime: "requestEnd",
    status: "status",
    lastChange: "metadata.lastModified",
    generatedTime: "metadata.created",
    address: "contactAddressStreet",
    representative: "contactRepresentative",
    email: "contactEmail",
    phone: "contactPhone",
  };
  return sortAttributes[key];
};

export const apiRequestBuilder =
  (axiosInstance: AxiosInstance = globalAxios): ApiRequestFunction =>
  <T = any, D = any>(
    method: Method,
    path: string,
    data?: D,
    options?: RequestOptions<D>
  ): ApiResponse<T> => {
    const url = new URL(path, "https://example.com");
    url.search = createSearchParams(url, options?.query || {});
    const requestData =
      options?.formData && data
        ? createFormData(data as Record<string, any>)
        : data;
    return axiosInstance.request({
      url: toPathString(url),
      data: requestData,
      method,
      ...options,
    });
  };

const createFormData = (
  data: Record<string, string | Blob | File | File[]>
): FormData => {
  const formData = new FormData();
  let key: keyof typeof data;
  for (key in data) {
    const entry = data[key];
    if (Array.isArray(entry)) {
      entry.forEach((item) => {
        formData.append(`${key}`, item);
      });
    } else {
      formData.append(key, entry);
    }
  }
  return formData;
};
