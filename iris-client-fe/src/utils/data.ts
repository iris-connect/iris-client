import _get from "lodash/get";
import dayjs from "./date";
import _transform from "lodash/transform";
import _isEqual from "lodash/isEqual";
import _isObject from "lodash/isObject";
import _isEmpty from "lodash/isEmpty";
import store from "@/store";

export type DataNormalizer<T> = (source?: T, parse?: boolean) => T;

enum DataType {
  string = "string",
  array = "array",
  dateString = "dateString",
  boolean = "boolean",
  number = "number",
  any = "any",
}

type DataTypeArg = keyof typeof DataType;

export const normalizeData = <T>(
  source: T | undefined,
  callback: (n: EntryNormalizer<T>) => Complete<T>,
  parse?: boolean,
  message?: string
): T => {
  if (!isEnabled()) return source as T;
  const normalizer = entryNormalizer(source);
  return finalizeData(callback(normalizer), source, parse, message) as T;
};

export const normalizeValue = <T>(
  source: T | undefined,
  callback: (n: ValueNormalizer<T>) => T,
  parse?: boolean,
  message?: string
): T => {
  if (!isEnabled()) return source as T;
  const normalizer = valueNormalizer(source);
  return finalizeData(callback(normalizer), source, parse, message);
};

export type ValueNormalizer<T> = (fallback: T, type?: DataTypeArg) => T;

export const valueNormalizer =
  <T>(value?: T) =>
  (fallback: T, type: DataTypeArg = "string"): T => {
    return getNormalizedValue(value, fallback, type);
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
  type?: DataTypeArg
) => T[K];

const entryNormalizer =
  <T>(obj?: T) =>
  <K extends keyof T>(
    key: K,
    fallback: T[K],
    type: DataTypeArg = "string"
  ): T[K] => {
    return normalize<T, K>(obj, key, fallback, type);
  };

const normalize = <T, K extends keyof T>(
  obj: T | unknown | undefined,
  key: K,
  fallback: T[K],
  type: DataTypeArg = "string"
): T[K] => {
  const value: T[K] = _get(obj, key);
  return getNormalizedValue(value, fallback, type);
};

export const getNormalizedValue = <T>(
  value: T | undefined,
  fallback: T,
  type: DataTypeArg
): T => {
  if (value !== undefined && validateType(value, type)) return value;
  return fallback;
};

const validateType = (value: unknown, type: DataTypeArg): boolean => {
  if (type === DataType.array) {
    return Array.isArray(value);
  }
  if (type === DataType.dateString) {
    return typeof value === "string" && dayjs(value).isValid();
  }
  if (type === DataType.any) {
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

const finalizeData = <A>(
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

const notifyDifference = <A>(a: A, b: A, msg?: string): void => {
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
const difference = <A extends Record<string, any>, B extends A>(
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

export const getObjectKeys = <T>(obj: T): Array<keyof T> => {
  return Object.keys(obj) as Array<keyof typeof obj>;
};

export const getEnumKeys = <O extends object, K extends keyof O = keyof O>(
  obj: O
): K[] => {
  return Object.keys(obj).filter((k) => Number.isNaN(+k)) as K[];
};
