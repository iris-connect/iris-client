import { flattenDeep } from "lodash";

export const join = (arr: Array<unknown>, separator: string): string => {
  return flattenDeep(arr)
    .filter((n) => n)
    .join(separator);
};
