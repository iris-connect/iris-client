import axios, { AxiosInstance, AxiosRequestConfig } from "axios";
import store from "@/store";
import { IrisClientFrontendApi } from "@/api";

import { makeMockAPIServer } from "@/server/mockAPIServer";
import { parseError } from "@/utils/axios";
import config from "@/config";
import messages from "@/common/messages";

if (process.env.VUE_APP_ENABLE_MOCK_SERVER === "true") {
  // Not sure whether imported mockAPIServer ends up in bundle for deployment.
  makeMockAPIServer();
}

const { apiBaseURL } = config;

const clientConfig: AxiosRequestConfig = {
  baseURL: apiBaseURL,
  withCredentials: true,
  transformRequest: (data) => {
    return JSON.stringify(data !== undefined ? data : {});
  },
};

const applyDefaultContentHeaders = (axiosInstance: AxiosInstance) => {
  const contentType = "application/json; charset=UTF-8";
  axiosInstance.defaults.headers.post["Content-Type"] = contentType;
  axiosInstance.defaults.headers.put["Content-Type"] = contentType;
  axiosInstance.defaults.headers.patch["Content-Type"] = contentType;
};

const applyDefaultResponseInterceptors = (
  axiosInstance: AxiosInstance,
  notify?: boolean
) => {
  axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
      const status = parseError(error)?.status;
      if (status === 401 || status === 403) {
        if (notify) {
          store.commit(
            "userLogin/setAuthenticationError",
            messages.error.sessionExpired
          );
        }
        store.commit("userLogin/setSession");
      }
      return Promise.reject(error);
    }
  );
};

const baseAxiosInstance = axios.create(clientConfig);
applyDefaultContentHeaders(baseAxiosInstance);
applyDefaultResponseInterceptors(baseAxiosInstance, false);

const authAxiosInstance = axios.create(clientConfig);
applyDefaultContentHeaders(authAxiosInstance);
applyDefaultResponseInterceptors(authAxiosInstance, true);

export const baseClient = new IrisClientFrontendApi(baseAxiosInstance);

const authClient = new IrisClientFrontendApi(authAxiosInstance);

export default authClient;
