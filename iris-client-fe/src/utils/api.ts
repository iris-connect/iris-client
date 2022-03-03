/* eslint-disable   @typescript-eslint/no-explicit-any */
import _mapValues from "lodash/mapValues";
import { ErrorMessage } from "@/utils/axios";
import { AsyncActionState } from "@/utils/asyncAction";
import { getObjectKeys } from "@/utils/data";
import _some from "lodash/some";
import _pick from "lodash/pick";

type ApiLike = {
  [k: string]: (...args: any) => any;
};

type ApiBundle<T extends ApiLike = ApiLike> = {
  [Property in keyof T]: ReturnType<T[Property]>;
};

export const apiBundleProvider =
  <T extends ApiLike>(api: T) =>
  <K extends keyof T>(keys?: K[]) => {
    return bundleApi(api, keys);
  };

export const bundleApi = <T extends ApiLike, K extends keyof T>(
  api: T,
  keys?: K[]
): ApiBundle<Pick<T, K>> => {
  const bundledKeys =
    keys?.length && keys.length > 0 ? keys : getObjectKeys(api);
  const bundledApi = _pick(
    _mapValues(api, (item) => item()),
    bundledKeys
  );
  return {
    ...bundledApi,
  };
};

type ApiState<T> = {
  [P in keyof T]: { state: AsyncActionState };
};

export const getApiErrorMessages = <T, K extends keyof T>(
  api: ApiState<T>,
  keys?: K[]
): ErrorMessage[] => {
  return getCombinedApiState("error", api, keys).filter((v) => v);
};

export const getApiLoading = <T, K extends keyof T>(
  api: ApiState<T>,
  keys?: K[]
): boolean => {
  return _some(getCombinedApiState("loading", api, keys));
};

export const getCombinedApiState = <
  S extends keyof AsyncActionState,
  T,
  K extends keyof T
>(
  state: S,
  api: ApiState<T>,
  keys?: K[]
): Array<AsyncActionState[S]> => {
  const stateKeys = keys?.length && keys.length > 0 ? keys : getObjectKeys(api);
  return stateKeys.map((key) => {
    return api[key].state[state];
  });
};
