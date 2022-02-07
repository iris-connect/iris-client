/* eslint-disable   @typescript-eslint/no-explicit-any */
import Vue from "vue";
import { parseData } from "@/utils/data";

export enum STATUS {
  IDLE = "idle",
  PENDING = "pending",
  OK = "ok",
  FAILED = "failed",
}

export interface AsyncActionState<R> {
  result: R | null;
  status: STATUS;
  loading: boolean;
  error: any;
}

export const asyncAction = <
  E extends (...args: any[]) => any,
  R = Awaited<ReturnType<E>>
>(
  actionExecute: E
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
      .catch((error) => {
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
      keys?.length && keys.length > 0
        ? keys
        : (Object.keys(state) as Array<keyof typeof state>);
    stateKeys.forEach((key: keyof typeof state) => {
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
