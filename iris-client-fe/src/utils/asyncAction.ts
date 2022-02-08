/* eslint-disable   @typescript-eslint/no-explicit-any */
import Vue from "vue";
import { getObjectKeys, parseData } from "@/utils/data";
import { getErrorMessage } from "@/utils/axios";

export enum STATUS {
  IDLE = "idle",
  PENDING = "pending",
  OK = "ok",
  FAILED = "failed",
}

export interface AsyncActionState<R = any> {
  status: STATUS;
  loading: boolean;
  error: any;
  result: R | null;
}

export const asyncAction = <
  E extends (...args: any[]) => any,
  R = Awaited<ReturnType<E>>
>(
  actionExecute: E,
  parseError?: boolean
): {
  state: AsyncActionState<R>;
  reset: (keys?: Array<keyof AsyncActionState<R>>) => void;
  execute: (...args: Parameters<E>) => Promise<R>;
} => {
  const initialState: AsyncActionState<R> = {
    status: STATUS.IDLE,
    loading: false,
    error: null,
    result: null,
  };
  const state = Vue.observable(parseData(initialState));
  const execute = (...args: Parameters<E>) => {
    state.status = STATUS.PENDING;
    state.loading = true;
    state.error = null;
    return Promise.resolve(actionExecute(...args))
      .then((result) => {
        state.status = STATUS.OK;
        state.result = result;
        return result;
      })
      .catch((e) => {
        const error = parseError !== false ? getErrorMessage(e) : e;
        state.status = STATUS.FAILED;
        state.error = error;
        return Promise.reject(error);
      })
      .finally(() => {
        state.loading = false;
      });
  };
  const reset = (keys?: Array<keyof typeof state>) => {
    const stateKeys =
      keys?.length && keys.length > 0 ? keys : getObjectKeys(state);
    stateKeys.forEach((key) => {
      // _set(state, key, parseData(initialState[key]));
      state[key] = parseData(initialState[key]);
    });
  };
  return {
    state,
    execute,
    reset,
  };
};

export default asyncAction;
