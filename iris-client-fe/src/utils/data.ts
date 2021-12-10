import _get from "lodash/get";
import dayjs from "./date";
import _transform from "lodash/transform";
import _isEqual from "lodash/isEqual";
import _isObject from "lodash/isObject";
import _isEmpty from "lodash/isEmpty";
import store from "@/store";

export const normalizeData = <T>(
  source: T | undefined,
  callback: (n: EntryNormalizer<T>) => T,
  parse?: boolean,
  message?: string
): T => {
  if (!isEnabled()) return source as T;
  const normalizer = entryNormalizer(source);
  return finalizeData(callback(normalizer), source, parse, message);
};

// utility type to check if all keys of T exist
export type Complete<T> = {
  [P in keyof Required<T>]: Pick<T, P> extends Required<Pick<T, P>>
    ? T[P]
    : T[P] | undefined;
};

export const isEnabled = (): boolean => {
  return store.state.normalizeSettings.enabled;
};

export type EntryNormalizer<T> = <K extends keyof T>(
  key: K,
  fallback: T[K],
  type?: string
) => T[K];

export const entryNormalizer =
  <T>(obj?: T) =>
  <K extends keyof T>(key: K, fallback: T[K], type = "string"): T[K] => {
    return normalize<T, K>(obj, key, type, fallback);
  };

export const normalize = <T, K extends keyof T>(
  obj: T | unknown | undefined,
  key: K,
  type: string,
  fallback: T[K]
): T[K] => {
  const val: T[K] = _get(obj, key);
  if (val !== undefined && validateType(val, type)) return val;
  return fallback;
};

const validateType = (value: unknown, type: string): boolean => {
  if (type === "array") {
    return Array.isArray(value);
  }
  if (type === "dateString") {
    return typeof value === "string" && dayjs(value).isValid();
  }
  if (type === "any") {
    return true;
  }
  return typeof value === type;
};

export const parseData = <T>(data: T): T => {
  try {
    return JSON.parse(JSON.stringify(data));
  } catch {
    return data;
  }
};

export const finalizeData = <A>(
  normalized: A,
  source?: A,
  parse?: boolean,
  message?: string
): A => {
  if (!parse) return normalized;
  const parsed = parseData(normalized);
  notifyDifference(source, parsed, message);
  return parsed;
};

export const notifyDifference = <A>(a: A, b: A, msg?: string): void => {
  if (store.state.normalizeSettings.logEnabled) {
    if (a && b) {
      const diffA = difference(a, b);
      const diffB = difference(b, a);
      if (!_isEmpty(diffA) || !_isEmpty(diffB)) {
        // eslint-disable-next-line no-console
        console.warn(
          `[${msg || "unknown"}]:`,
          "\ndiff source (A) -> normalizer (B):\n",
          diffA,
          "\ndiff normalizer (A) -> source (B):\n",
          diffB
        );
      } else {
        // eslint-disable-next-line no-console
        console.log(`[${msg || "unknown"}]: mapping successful`);
      }
    }
  }
};

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const difference = <A extends Record<string, any>, B extends A>(
  object: A,
  base: B
): Record<string, unknown> => {
  return _transform(object, (result, value, key) => {
    if (!_isEqual(value, base[key])) {
      if (_isObject(value) && _isObject(base[key])) {
        const diff = difference(value, base[key]);
        if (!_isEmpty(diff)) {
          result[key] = diff;
        }
      } else {
        result[key] = { A: value, B: base[key] };
      }
    }
  });
};