/* eslint-disable @typescript-eslint/no-explicit-any */

import { DataRequestStatus } from "@/api/api";
import globalAxios, {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  CancelToken,
  CancelTokenSource,
  Method,
} from "axios";
import axios from "axios";
import { serialize } from "object-to-formdata";

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
  multipart?: boolean;
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
      options?.multipart && data
        ? serialize(data, { indices: true, dotsForObjectNotation: true })
        : data;
    return axiosInstance.request({
      url: toPathString(url),
      data: requestData,
      method,
      ...options,
    });
  };

export const cancelTokenProvider = () => {
  let source: CancelTokenSource = axios.CancelToken.source();
  return (): CancelToken => {
    try {
      source.cancel("request canceled");
      source = axios.CancelToken.source();
    } catch (e) {
      // ignored
    }
    return source.token;
  };
};
