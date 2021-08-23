import _get from "lodash/get";

export const normalize = <T, K extends keyof T>(
  obj: T | unknown,
  key: K,
  type: string,
  fallback: T[K]
): T[K] => {
  const val: T[K] = _get(obj, key);
  if (val && validateType(val, type)) return val;
  return fallback;
};

const validateType = (value: unknown, type: string): boolean => {
  if (type === "array") {
    return Array.isArray(value);
  }
  return typeof value === type;
};
