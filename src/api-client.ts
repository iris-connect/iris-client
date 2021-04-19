import axios, { AxiosResponse } from "axios";
import store from "@/store";
import { Configuration, IrisClientFrontendApiFactory } from "@/api";

import { makeMockAPIServer } from "@/server/mockAPIServer";
import { UserSession } from "@/views/user-login/user-login.store";
import { get as _get } from "lodash";
import { parseError } from "@/utils/axios";
import config from "@/config";

if (process.env.VUE_APP_ENABLE_MOCK_SERVER === "true") {
  // Not sure whether imported mockAPIServer ends up in bundle for deployment.
  makeMockAPIServer();
}

const { apiBaseURL } = config;
export const clientConfig = new Configuration({
  basePath: apiBaseURL,
});

const authAxiosInstance = axios.create();

authAxiosInstance.interceptors.request.use((config) => {
  const token = store.state.userLogin.session?.token;
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

authAxiosInstance.interceptors.response.use(
  (response) => {
    const session = sessionFromResponse(response);
    store.commit("userLogin/setSession", session);
    return response;
  },
  (error) => {
    const status = parseError(error)?.status;
    if (status === 401) {
      // @todo: handle redirect to user login view with info message: token expired, etc.
      store.commit("userLogin/setSession");
    }
    return Promise.reject(error);
  }
);

export const sessionFromResponse = (response: AxiosResponse): UserSession => {
  const headers = response.headers;
  // upper-/lowercase fallback is necessary because the Authorization header in the miragejs response is lowercase.
  const authHeader = _get(
    headers,
    "Authorization",
    _get(headers, "authorization")
  );
  const token = authHeader?.split(" ")[1];
  return {
    token,
  };
};

const authClient = IrisClientFrontendApiFactory(
  clientConfig,
  undefined,
  authAxiosInstance
);

export default authClient;
