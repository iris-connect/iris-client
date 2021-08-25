import _get from "lodash/get";
import dayjs from "./date";
import _transform from "lodash/transform";
import _isEqual from "lodash/isEqual";
import _isObject from "lodash/isObject";
import _isEmpty from "lodash/isEmpty";

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
export const entryNormalizer = <T>(obj?: T) => <K extends keyof T>(
  key: K,
  fallback: T[K],
  type = "string"
) => {
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
  parse?: boolean
): A => {
  if (!parse) return normalized;
  const parsed = parseData(normalized);
  notifyDifference(source, parsed, "UserList");
  return parsed;
};

export const notifyDifference = <A>(a: A, b: A, msg?: string): void => {
  if (process.env.NODE_ENV !== "production") {
    if (a && b) {
      const diffA = difference(a, b);
      const diffB = difference(b, a);
      if (!_isEmpty(diffA) || !_isEmpty(diffB)) {
        console.log(msg, "diff A - B: ", diffA, "diff B - A: ", diffB);
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
      result[key] =
        _isObject(value) && _isObject(base[key])
          ? difference(value, base[key])
          : [value, base[key]];
    }
  });
};
