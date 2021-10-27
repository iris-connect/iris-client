import globalAxios, { AxiosInstance } from "axios";
import { apiRequestBuilder, ApiRequestFunction } from "@/api/common";

/**
 *
 * @export
 * @class BaseAPI
 */
export class BaseAPI {
  protected axios: AxiosInstance | undefined;
  protected apiRequest: ApiRequestFunction;
  constructor(axios: AxiosInstance = globalAxios) {
    if (axios) {
      this.axios = axios;
    }
    this.apiRequest = apiRequestBuilder(this.axios);
  }
}
