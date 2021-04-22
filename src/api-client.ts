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
  (response) => response,
  (error) => {
    const status = parseError(error)?.status;
    // we do not log out if a 401 error is thrown because the admin user action triggers a 401 when the user is not allowed to manage other users.
    if (status === 403) {
      store.commit(
        "userLogin/setAuthenticationError",
        "Ihre Sitzung ist abgelaufen."
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

const authClient = IrisClientFrontendApiFactory(
  clientConfig,
  undefined,
  authAxiosInstance
);

export default authClient;
