import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";
import store from "@/store";
import { IrisClientFrontendApi } from "@/api";

import { makeMockAPIServer } from "@/server/mockAPIServer";
import { UserSession } from "@/views/user-login/user-login.store";
import { get as _get } from "lodash";
import { parseError } from "@/utils/axios";
import config from "@/config";
import messages from "@/common/messages";
import _set from "lodash/set";

if (process.env.VUE_APP_ENABLE_MOCK_SERVER === "true") {
  // Not sure whether imported mockAPIServer ends up in bundle for deployment.
  makeMockAPIServer();
}

const { apiBaseURL } = config;

const clientConfig: AxiosRequestConfig = {
  baseURL: apiBaseURL,
  transformRequest: (data, headers) => {
    if (data instanceof FormData) {
      if (headers) headers["Content-Type"] = "multipart/form-data";
      return data;
    }
    return JSON.stringify(data !== undefined ? data : {});
  },
};

const applyDefaultContentHeaders = (axiosInstance: AxiosInstance) => {
  const contentType = "application/json; charset=UTF-8";
  axiosInstance.defaults.headers.post["Content-Type"] = contentType;
  axiosInstance.defaults.headers.put["Content-Type"] = contentType;
  axiosInstance.defaults.headers.patch["Content-Type"] = contentType;
};

const baseAxiosInstance = axios.create(clientConfig);
applyDefaultContentHeaders(baseAxiosInstance);

const authAxiosInstance = axios.create(clientConfig);
applyDefaultContentHeaders(authAxiosInstance);

authAxiosInstance.interceptors.request.use((config) => {
  const token = store.state.userLogin.session?.token;
  if (token) {
    _set(config, "headers.Authorization", `Bearer ${token}`);
  }
  return config;
});

authAxiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = parseError(error)?.status;
    if (status === 401 || status === 403) {
      store.commit(
        "userLogin/setAuthenticationError",
        messages.error.sessionExpired
      );
      store.commit("userLogin/setSession");
    }
    return Promise.reject(error);
  }
);

export const sessionFromResponse = (response: AxiosResponse): UserSession => {
  const headers = response.headers;
  // upper-/lowercase fallback is necessary because the Authentication-Info header is lowercase.
  const authHeader = _get(
    headers,
    "authentication-info",
    _get(headers, "Authentication-Info")
  );
  const token = authHeader?.split(" ")[1];
  return {
    token,
  };
};

export const baseClient = new IrisClientFrontendApi(baseAxiosInstance);

const authClient = new IrisClientFrontendApi(authAxiosInstance);

export default authClient;
